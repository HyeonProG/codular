package com.codular.domain.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthViewController {

    @GetMapping("/sign-up")
    public String signUp() {
        return "auth/sign-up";
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "auth/sign-in";
    }

}
