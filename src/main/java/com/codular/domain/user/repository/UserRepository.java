package com.codular.domain.user.repository;

import com.codular.domain.user.User;
import com.codular.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserMapper userMapper;

    public void save(User user) {
        userMapper.signUp(user);
    }

    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    public boolean existsByNickname(String nickname) {
        return userMapper.existsByNickname(nickname);
    }

    public String findNicknameById(Long userId) {
        return userMapper.findNicknameById(userId);
    }

}
