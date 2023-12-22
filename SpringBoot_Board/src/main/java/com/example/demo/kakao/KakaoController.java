package com.example.demo.kakao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping("/kakao")
public class KakaoController {
    private final KakaoService kakaoService;

    @GetMapping("/oauth")
    public String KakaoConnect(){
        String link = kakaoService.KakaoConnect();
        return "redirect:" + link;
    }

    @GetMapping("/relogin")
    public String KakaoAutoConnect(Error error){
        String link = kakaoService.KakaoAutoConnect();

        return "redirect:"+link;
    }

    @GetMapping(value = "/login", produces = "application/json")
    public String kakaoLogin(@RequestParam("code")String code, Error error, HttpServletRequest request, HttpServletResponse res) {
        // 로그인은 크롬 화면에서 하고 여기서 실제로는 토큰, 사용자 정보 얻기를 함
        String link = kakaoService.KakaoLogin(code,request.getSession());

        // 다시 로그인 화면으로 돌아옴
        return "redirect:" + link;
    }

    @GetMapping("/logout")
    public String KakaoLogout(HttpServletRequest request){
        kakaoService.KakaoLogout(request.getSession());
        return "index";
    }

    @GetMapping("/flogout")
    public String KakaoFLogout(HttpServletRequest request){
        String lnk=kakaoService.KakaoFLogout(request.getSession());

        return "redirect:" + lnk;
    }

}
