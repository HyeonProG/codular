package com.codular.domain.user.controller;

import com.codular.domain.user.dto.response.UserMyPageResponseDto;
import com.codular.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    @GetMapping("/mypage")
    public String myPage(Model model, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return "redirect:/auth/sign-in";
        }

        Long userId = (Long) authentication.getPrincipal();
        UserMyPageResponseDto view = userService.getUserMyPage(userId);

        model.addAttribute("view", view);
        return "user/mypage";
    }

}
