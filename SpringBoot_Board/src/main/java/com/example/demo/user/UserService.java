package com.example.demo.user;


import com.example.demo.core.error.exception.Exception400;
import com.example.demo.core.error.exception.Exception401;
import com.example.demo.core.error.exception.Exception500;
import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.security.JwtTokenProvider;
import com.example.demo.kakao.KakaoService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        checkEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        requestDTO.setPassword(encodedPassword);

        userRepository.save(requestDTO.toEntity());
    }

    public void checkEmail(String email){
        Optional<User> byEmail = userRepository.findByEmail(email);

        if(byEmail.isPresent()){
            throw new Exception400("이미 존재하는 email 입니다" + email);
        }
    }

    @Transactional
    public String authenticateAndCreateToken(UserRequest.JoinDTO joinDto) {
        // ** 인증 작업
        try{
            // 사용자로부터 받은 이메일과 비밀번호를 가지고 토큰을 생성.
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(
                    joinDto.getEmail(), joinDto.getPassword());
            // 토큰을 이용해 인증을 시도.
            Authentication authentication
                    = authenticationManager.authenticate(token);
            // ** 인증 완료 값을 받아온다.
            // 인증키
            // 인증된 사용자의 정보를 가져옵니다.
            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
            // JWT 토큰을 생성.
            String prefixJwt = JwtTokenProvider.create(customUserDetails.getUser());
            // "Bearer "를 제거해서 순수한 토큰만을 가져옵니다.
            String access_token = prefixJwt.replace(JwtTokenProvider.TOKEN_PREFIX, "");
            // JWT 리프레시 토큰을 생성.
            String refreshToken = JwtTokenProvider.createRefresh(customUserDetails.getUser());

            User user = customUserDetails.getUser();
            // 생성된 토큰들을 사용자 정보에 저장.
            user.setAccess_token(access_token);
            user.setRefresh_token(refreshToken);

            // 사용자 정보를 저장.
            userRepository.save(user);

            return prefixJwt;
        }catch (Exception e){
            throw new Exception401("인증되지 않음.");
        }
    }

    public void login(UserRequest.JoinDTO requestDTO, HttpSession session) {
        try {
            final String oauthUrl = "http://localhost:8080/user/oauth";
            ResponseEntity<JsonNode> response = userPost(oauthUrl,null, requestDTO);
            String access_token = response.getHeaders().getFirst(JwtTokenProvider.HEADER);
            session.setAttribute("access_token", access_token);
            session.setAttribute("platform", "user");

            setUserInfoInSession(session);
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    public User setUserInfoInSession(HttpSession session) {
        // 세션에서 액세스 토큰을 가져옵니다.
        String access_token = (String) session.getAttribute("access_token");

        if (session.getAttribute("platform").equals("kakao")) {
            String email = kakaoService.getUserFromKakao(access_token).getEmail();
            return userRepository.findByEmail(email).orElseThrow(
                    () -> new Exception401("인증되지 않았습니다."));
        }
        // 사용자 정보를 가져오기 위한 URL을 설정.
        final String infoUrl = "http://localhost:8080/user/user_id";
        // 새로운 HTTP 헤더를 생성.
        HttpHeaders headers = new HttpHeaders();
        // 헤더의 컨텐츠 타입을 JSON으로 설정.
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 액세스 토큰을 헤더에 추가.
        headers.set("Authorization", access_token);
        // HTTP POST 요청을 보내고, 응답을 받아옵니다. 이때, 응답 본문에서 "response" 필드를 long 타입으로 변환하여 사용자 ID를 가져옵니다.
        Long user_id = userPost(infoUrl, headers, null).getBody().get("response").asLong();
        // 사용자 ID를 이용해 사용자를 찾아 반환.
        return userRepository.findById(user_id).get();
    }

    public <T> ResponseEntity<JsonNode> userPost(String requestUrl, HttpHeaders headers, T body){
        try{
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

            return restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, JsonNode.class);
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    @Transactional
    public String logout(HttpSession session) {
        try {
            if (session.getAttribute("platform").equals("kakao")) {
                kakaoService.KakaoLogout(session);
            }else {
                User user = setUserInfoInSession(session);
                userRepository.save(clearTokens(user));
                session.invalidate();
            }
            return "/";
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }
    public User clearTokens(User user){
        user.setAccess_token(null);
        user.setRefresh_token(null);
        return user;
    }
}
