package com.example.demo.kakao;


import com.example.demo.core.error.exception.Exception401;
import com.example.demo.core.error.exception.Exception500;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
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

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
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
            url.append("&redirect_uri=").append("http://localhost:8080/kakao/login");
            url.append("&response_type=" + "code");

            return url.toString();
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    public String KakaoAutoConnect(){
        try {
            StringBuffer url = new StringBuffer();
            url.append(KakaoConnect());
            url.append("&prompt=" + "login");

            return url.toString();
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    public JsonNode getKakaoAccessToken(String code) {
        // 요청 보낼 링크(토큰 얻기)
        final String requestUrl = "https://kauth.kakao.com/oauth/token";
        // 매개변수와 값의 리스트
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code"); // 인증 타입 (고정값임)
        parameters.add("client_id", restApi); // REST API KEY
        parameters.add("redirect_uri", "http://localhost:8080/kakao/login"); // 리다이렉트 URI
        parameters.add("code", code); // 인가 코드

        final ResponseEntity<JsonNode> response = kakaoPost(requestUrl, null, parameters);

        return response.getBody();
    }

    @Transactional
    // kakaoConnect의 결과값(인가코드)가 아래의 매개변수 code로 들어감
    public String KakaoLogin(String code,HttpSession session){
        try {
            // 인카코드에 있는 토큰을 추출
            JsonNode token = getKakaoAccessToken(code);
            String access_token = token.get("access_token").asText();
            // Bear 넣어야 할지도?
            String refresh_token = token.get("refresh_token").asText();

            session.setAttribute("access_token", access_token);
            session.setAttribute("platform", "kakao");

            // 로그인한 클라이언트의 사용자 정보를 json 타입으로 획득
            User user = kakaoJoin(access_token);
            user.setAccess_token(access_token);
            user.setRefresh_token(refresh_token);
            userRepository.save(user);
            return "http://localhost:8080/";
        } catch (Exception e){
            throw new Exception401("인증되지 않음.");
        }
    }

    public boolean checkEmail(String email){
        // 동일한 이메일이 있는지 확인.
        Optional<User> users = userRepository.findByEmail(email);
        return users.isPresent();
    }

    public User kakaoJoin(String access_token) {
        try {
            User userInfo = getUserFromKakao(access_token);
            String email = userInfo.getEmail();
            if (!checkEmail(email)) {
                return userRepository.save(userInfo);
            }
            return userRepository.findByEmail(email).orElseThrow(
                    () -> new Exception401("인증되지 않았습니다."));
        } catch (Exception e) {
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

    public JsonNode getKakaoUserInfo(String access_token) {
        final String requestUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + access_token);
        ResponseEntity<JsonNode> response = kakaoPost(requestUrl, headers, null);

        return response.getBody();
    }

    @Transactional
    public void KakaoLogout(HttpSession session){
        final String requestUrl = "https://kapi.kakao.com/v1/user/logout";
        String access_token = (String) session.getAttribute("access_token");

        try{
            String email = getUserFromKakao(access_token).getEmail();
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new Exception401("로그인된 사용자를 찾을 수 없습니다."));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            headers.set("Authorization", "Bearer " + access_token);
            kakaoPost(requestUrl, headers, null);
            user.setAccess_token(null);
            user.setRefresh_token(null);
            session.invalidate();
        }
        catch (Exception500 e){
            throw new Exception500("로그아웃 도중 오류 발생");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public String KakaoFLogout(HttpSession session) {
        try{
            String access_token = (String) session.getAttribute("access_token");
            String email = getUserFromKakao(access_token).getEmail();
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new Exception401("로그인된 사용자를 찾을 수 없습니다."));
            user.setAccess_token(null);
            user.setRefresh_token(null);
            session.invalidate();
            StringBuffer url = new StringBuffer();
            url.append("https://kauth.kakao.com/oauth/logout?");
            url.append("client_id=").append(restApi);
            url.append("&logout_redirect_uri=" + "http://localhost:8080/");

            return url.toString();
        }
        catch (Exception500 e){
            throw new Exception500("로그아웃 도중 오류 발생");
        }
    }
    public <T> ResponseEntity<JsonNode> kakaoPost(String requestUrl, HttpHeaders headers, T body){
        try{
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

            return restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, JsonNode.class);
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

}
