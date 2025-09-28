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

    // 유저 정보 저장
    public void save(User user) {
        userMapper.signUp(user);
    }

    // 이메일로 유저 정보 조회
    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    // ID로 유저 정보 조회
    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }

    // 닉네임으로 유저 정보 조회
    public Optional<User> findByNickname(String nickname) {
        return userMapper.findByNickname(nickname);
    }

}
