package com.codular.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpResponseDto {

    private String email;
    private String nickname;

    @Builder
    public UserSignUpResponseDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

}
