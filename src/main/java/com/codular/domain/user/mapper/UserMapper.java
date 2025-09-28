package com.codular.domain.user.mapper;

import com.codular.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    // 회원가입
    void signUp(User user);

    // 이메일로 유저 조회
    Optional<User> findByEmail(@Param("email") String email);

    // ID로 유저 조회
    Optional<User> findById(@Param("id") Long id);

    // 닉네임으로 유저 조회
    Optional<User> findByNickname(@Param("nickname") String nickname);

}
