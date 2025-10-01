package com.codular.domain.auth.util;

import org.springframework.stereotype.Component;

@Component
public class MailSenderUtil {

    public String buildBody(String nickname, String resetLink) {
        return String.format(
                "안녕하세요 %s님,\n\n비밀번호 재설정을 요청하셨습니다.\n" +
                        "아래 링크를 눌러 새로운 비밀번호를 설정해주세요.\n\n%s\n\n" +
                        "만약 요청하지 않으셨다면 이 메일을 무시하셔도 됩니다.",
                nickname, resetLink
        );
    }

}
