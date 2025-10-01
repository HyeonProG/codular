package com.codular.domain.auth.service;

public interface PasswordResetService {

    void requestResetPassword(String email, String resetLinkBase);

    void confirmResetPassword(String email, String token, String newPassword);

}
