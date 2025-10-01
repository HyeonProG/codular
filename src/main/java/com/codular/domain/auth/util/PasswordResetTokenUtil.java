package com.codular.domain.auth.util;

import com.codular.common.exception.BaseException;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordResetTokenUtil {

    private static final int DEFAULT_BYTES = 32;
    private final SecureRandom secureRandom = new SecureRandom();

    public String newToken() {
        return newToken(DEFAULT_BYTES);
    }

    public String newToken(int numBytes) {
        if (numBytes <= 0) {
            throw new IllegalArgumentException("numBytes must be positive");
        }
        byte[] buf = new byte[numBytes];
        secureRandom.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

}
