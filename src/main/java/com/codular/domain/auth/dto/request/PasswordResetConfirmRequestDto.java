package com.codular.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordResetConfirmRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String token;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+\\-={}\\[\\]:;\"'<>?,./]{8,64}$",
            message = "비밀번호는 영문과 숫자를 포함하여 8자리 이상이어야 합니다.")
    private String newPassword;

}
