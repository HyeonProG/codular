package com.codular.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenReissueRequestDto {

    @NotNull(message = "userId는 필수 값입니다.")
    private Long userId;

    @NotBlank(message = "refreshToken은 필수 값입니다.")
    private String refreshToken;

}
