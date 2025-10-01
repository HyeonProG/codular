<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입 - Codular</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
      <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css" />
</head>
<body class="page--simple d-flex flex-column min-vh-100">
  <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

  <main class="container flex-grow-1 d-flex align-items-center py-4">
    <section class="mx-auto w-100" style="max-width: 560px;">
      <h1 class="h3 mb-4 text-center">회원가입</h1>

      <form id="signUpForm" class="needs-validation" novalidate>
        <!-- 이메일 -->
        <div class="mb-3">
          <label for="email" class="form-label">이메일</label>
          <input id="email" name="email" type="email" class="form-control" required
                 placeholder="email@example.com" autocomplete="username" />
          <div class="invalid-feedback">올바른 이메일을 입력해주세요.</div>
        </div>

        <!-- 닉네임 -->
        <div class="mb-3">
          <label for="nickname" class="form-label">닉네임</label>
          <input id="nickname" name="nickname" type="text" class="form-control" required
                 minlength="2" maxlength="20"
                 pattern="[A-Za-z0-9가-힣_]{2,20}"
                 placeholder="닉네임" autocomplete="nickname" />
          <div class="invalid-feedback">닉네임은 2~20자(한글/영문/숫자/_ )로 입력하세요.</div>
        </div>

        <!-- 비밀번호 -->
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input id="password" name="password" type="password" class="form-control" required
                 minlength="8" maxlength="64"
                 pattern="[A-Za-z0-9!@#$%^&amp;*()_\-+=]{8,64}"
                 placeholder="8자 이상 (영문+숫자)" autocomplete="new-password" />
          <div class="invalid-feedback">비밀번호는 8~64자이며 영문과 숫자를 모두 포함해야 합니다.</div>
        </div>

        <!-- 비밀번호 확인 -->
        <div class="mb-3">
          <label for="passwordConfirm" class="form-label">비밀번호 확인</label>
          <input id="passwordConfirm" name="passwordConfirm" type="password" class="form-control" required
                 minlength="8" maxlength="64" placeholder="비밀번호 재입력" autocomplete="new-password" />
          <div class="invalid-feedback">비밀번호가 일치하지 않습니다.</div>
        </div>

        <button class="btn btn-primary w-100 py-2" type="submit">회원가입</button>

        <p class="text-center mt-4 mb-0">
          이미 계정이 있으신가요? <a href="/auth/sign-in">로그인</a>
        </p>
      </form>
    </section>
  </main>

  <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <script>
    (() => {
      const form = document.getElementById('signUpForm');
      const emailEl = form.email;
      const nickEl = form.nickname;
      const pwEl = form.password;
      const pwcEl = form.passwordConfirm;

      // 입력 중 커스텀 에러 초기화
      [emailEl, nickEl, pwEl, pwcEl].forEach(el => {
        el.addEventListener('input', () => el.setCustomValidity(''));
      });

      // 비밀번호 일치 실시간 확인
      const validatePwMatch = () => {
        if (pwEl.value && pwcEl.value && pwEl.value !== pwcEl.value) {
          pwcEl.setCustomValidity('비밀번호가 일치하지 않습니다.');
        } else {
          pwcEl.setCustomValidity('');
        }
      };

      const validatePwRules = () => {
        const v = pwEl.value || '';
        const hasLetter = /[A-Za-z]/.test(v);
        const hasDigit = /\d/.test(v);
        // 패턴/길이는 브라우저가 검증하므로 '영문+숫자 조합'만 커스텀으로 강제
        if (v.length >= 8 && (!hasLetter || !hasDigit)) {
          pwEl.setCustomValidity('영문과 숫자를 모두 포함해야 합니다.');
        } else {
          // 다른 커스텀 오류가 없도록 초기화
          if (pwEl.validity.customError) pwEl.setCustomValidity('');
        }
      };

      pwEl.addEventListener('input', () => { validatePwRules(); validatePwMatch(); });
      pwcEl.addEventListener('input', validatePwMatch);

      form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // 제출 전 검증(영문+숫자 포함, 비밀번호 일치)
        validatePwRules();
        validatePwMatch();

        // 브라우저 내장/패턴 검증
        if (!form.checkValidity()) {
          form.classList.add('was-validated');
          return;
        }
        form.classList.add('was-validated');

        const payload = {
          email: emailEl.value.trim(),
          nickname: nickEl.value.trim(),
          password: pwEl.value
        };

        try {
          const res = await fetch('/api/v1/auth/sign-up', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
          });
          const data = await res.json();

          if (!res.ok || !data.isSuccess) {
            Swal.fire({ icon: 'error', title: '회원가입 실패', text: data.message || '회원가입에 실패했습니다.' });
            return;
          }

          Swal.fire({ icon: 'success', title: '회원가입 완료', text: '로그인 페이지로 이동합니다.' })
            .then(() => window.location.href = '/auth/sign-in');

        } catch (err) {
          Swal.fire({ icon: 'error', title: '오류', text: '회원가입 중 문제가 발생했습니다. 다시 시도해주세요.' });
        }
      });
    })();
  </script>

</body>
</html>