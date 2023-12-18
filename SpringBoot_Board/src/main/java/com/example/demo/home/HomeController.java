package com.example.demo.home;

import com.example.demo.core.security.CustomUserDetails;
import org.springframework.boot.Banner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("access_token") != null)
            return "kakao";
        return "index";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }
}
