package com.codular.domain.auth.repository;

public interface MailSenderRepository {

    void send(String to, String subject, String body);

    default void sendHtml(String to, String subject, String htmlBody) {
        send(to, subject, htmlBody);
    }

}
