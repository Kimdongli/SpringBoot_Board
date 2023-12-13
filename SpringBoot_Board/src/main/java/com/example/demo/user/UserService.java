package com.example.demo.user;


import com.example.demo.core.error.exception.Exception400;
import com.example.demo.core.error.exception.Exception401;
import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        Optional<User> byEmail = userRepository.findByEmail(requestDTO.getEmail());

        if(byEmail.isPresent()){
            throw new Exception400("이미 존재하는 email 입니다" + requestDTO.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        requestDTO.setPassword(encodedPassword);

        userRepository.save(requestDTO.toEntity());
    }

    @Transactional
    public String login(UserRequest.JoinDTO requestDTO) {
        String jwt = "";

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());

            Authentication authentication =  authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken
            );

            if (authentication.getPrincipal() == null){
                throw new Exception401("Principal is null");
            }

            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();

            jwt  = JwtTokenProvider.create(customUserDetails.getUser());

        } catch (Exception e){
            throw new Exception401("인증되지 않음.");
        }

        return jwt;
    }

    @Transactional
    public String connect(UserRequest.JoinDTO joinDto) {
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


}
