package com.example.demo.kakao;


import com.example.demo.core.error.exception.Exception401;
import com.example.demo.core.error.exception.Exception500;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jdk.jshell.spi.ExecutionControlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Transactional
@Service
public class KakaoService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String restApi = "dd2a883a2188ccb6208badd587ca6c40";
    private final String adminKey = "025652b3be3f918b5196a259d63425dd";

    public String KakaoConnect(){
        try {
            StringBuffer url = new StringBuffer();
            url.append("https://kauth.kakao.com/oauth/authorize?");
            url.append("client_id=").append(restApi);
            url.append("&redirect_uri=").append("http://localhost:8080/oauth/kakao");
            url.append("&response_type=" + "code");
            return url.toString();
        }catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    public String KakaoAutoConnect(){
        try{
            StringBuffer url = new StringBuffer();
            url.append(KakaoConnect());
            url.append("&prompt=" + "login");

            return url.toString();
        }catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    // 카카오 액세스 토큰을 얻는 메소드
    public JsonNode getKakaoAccessToken(String code){
        final String requestUrl = "https://kauth.kakao.com/oauth/token";

        // 요청에 필요한 파라미터를 담는 맵
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code"); // 요청타입
        parameters.add("client_id",restApi); // 클라이언트 ID
        parameters.add("redirect_uri", "http://localhost:8080/oauth/kakao"); // 리다이렉트 URI
        parameters.add("code",code); // 인증 코드

        final ResponseEntity<JsonNode> response = KakaoPost(requestUrl, null,parameters);
        return response.getBody();
    }

    @Transactional
    public String KakaoLogin(String code, HttpSession session){
        try {
            JsonNode token = getKakaoAccessToken(code);
            String access_token = token.get("access_token").asText();
            String refresh_token = token.get("refresh_token").asText();

            session.setAttribute("access_token", access_token);
            session.setAttribute("platform", "kakao");

            User user = KakaoJoin(access_token);
            user.setAccess_token(access_token);
            user.setRefresh_token(refresh_token);
            userRepository.save(user);
            return "http://localhost:8080/";
        }catch (Exception e){
            throw new Exception401("인증안됨");
        }
    }

    public User KakaoJoin(String access_token){
        try {
            User user = getUserFromKakao(access_token);
            String email = user.getEmail();
            if(!userRepository.existsByEmail(email)){
                return userRepository.save(user);
            }
            return userRepository.findByEmail(email).orElseThrow(
                    ()->new Exception401("인증되지 않습니다."));
        }catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    public User getUserFromKakao(String access_token){
        JsonNode userInfo = getKakaoUserInfo(access_token);
        JsonNode kakao_account = userInfo.path("kakao_account");
        String encodePassword = passwordEncoder.encode(access_token);
        JsonNode properties = userInfo.path("properties");

        User user = User.builder()
                .email(kakao_account.path("email").asText())
                .password(encodePassword)
                .name(properties.path("nickname").asText())
                .roles(Collections.singletonList("ROLE_USER"))
                .platform("kakao")
                .build();
        return user;
    }

    public JsonNode getKakaoUserInfo(String access_token){
        final String requestUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + access_token);
        ResponseEntity<JsonNode> response = KakaoPost(requestUrl, headers, null);

        return response.getBody();
    }

    @Transactional
    public void KakaoLogout(HttpSession session){
        final String requestUrl = "https://kapi.kakao.com/v1/user/logout";
        String access_token = (String) session.getAttribute("access_token");

        try {
            String email = getUserFromKakao(access_token).getEmail();
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new Exception401("사용자를 찾을 수 없습니다."));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            headers.set("Authorization", "Bearer " + access_token);
            KakaoPost(requestUrl, headers, null);
            user.setAccess_token(null);
            user.setRefresh_token(null);
            session.invalidate();
        }catch (Exception500 e){
            throw new Exception500("로그아웃 실행중 오류 발생");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public String KakaoFLogout(HttpSession session){
        try {
            String access_token =(String)session.getAttribute("access_token");
            String email = getUserFromKakao(access_token).getEmail();
            User user = userRepository.findByEmail(email).orElseThrow(
                    ()->new Exception401("사용자를 찾을 수 없습니다."));
            user.setAccess_token(null);
            user.setRefresh_token(null);
            session.invalidate();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("https://kauth.kakao.com/oauth/logout?");
            stringBuffer.append("client_id=").append(restApi);
            stringBuffer.append("&logout_redirect_url=" + "http://localhost:8080/");

            return stringBuffer.toString();

        }catch (Exception500 e){
            throw new Exception500("로그아웃 도중 오류 발생");
        }


    }

    // HTTP POST 요청을 보내는 메소드
    public <T> ResponseEntity<JsonNode> KakaoPost(String requestUrl, HttpHeaders headers, T body){
        try {
            // Spring에서 제공하는 HTTP 연결을 담당하는 클래스
            RestTemplate restTemplate = new RestTemplate();
            // HTTP 요청의 본문(body)과 헤더(headers)를 포함하는 클래스
            HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

            // 제공된 URL로 HTTP 요청을 보내고, 응답을 JsonNode 클래스로 매핑하는 메소드 호출
            return restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, JsonNode.class);
        }catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

}
