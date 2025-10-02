package com.codular.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateNicknameRequestDto {

    @NotBlank
    @Size(min = 2, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9가-힣_]+$", message = "닉네임은 2~20자(한글/영문/숫자/_ )로 입력하세요.")
    private String nickname;

}
