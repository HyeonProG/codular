package com.codular.domain.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/password/forgot")
    public String forgotPassword() {
        return "auth/password-forgot";
    }

    @GetMapping("/password/reset")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("token")String token, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("token", token);
        return "auth/password-reset";
    }

}
