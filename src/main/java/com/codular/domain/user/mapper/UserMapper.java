package com.codular.domain.user.mapper;

import com.codular.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void signUp(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

}
