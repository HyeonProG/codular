package com.codular.domain.user.mapper;

import com.codular.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void signUp(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    int countByEmail(String email);

    default boolean existsByEmail(String email) {
        return countByEmail(email) > 0;
    }

    int countByNickname(String nickname);

    default boolean existsByNickname(String nickname) {
        return countByNickname(nickname) > 0;
    }

    String findNicknameById(Long id);

}
