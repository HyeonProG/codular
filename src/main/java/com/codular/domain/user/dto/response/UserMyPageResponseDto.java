package com.codular.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserMyPageResponseDto {

    private final String nickname;
    private final String email;
    private final String profileImageUrl;
    private final String createdAt;

}
