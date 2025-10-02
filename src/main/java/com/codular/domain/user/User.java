package com.codular.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profileUrl;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    @Builder
    public User(Long id, String email, String password, String nickname, String profileUrl, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.role = role;
    }

}
