<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인 - Codular</title>
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
                 placeholder="닉네임" autocomplete="nickname" />
          <div class="invalid-feedback">닉네임을 입력해주세요.</div>
        </div>

        <!-- 비밀번호 -->
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input id="password" name="password" type="password" class="form-control" required
                 placeholder="8자 이상" minlength="8" autocomplete="new-password" />
          <div class="invalid-feedback">비밀번호(8자 이상)를 입력해주세요.</div>
        </div>

        <!-- 비밀번호 확인 -->
        <div class="mb-3">
          <label for="passwordConfirm" class="form-label">비밀번호 확인</label>
          <input id="passwordConfirm" name="passwordConfirm" type="password" class="form-control" required
                 placeholder="비밀번호 재입력" minlength="8" autocomplete="new-password" />
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
        form.addEventListener('submit', async e => {
          e.preventDefault();
          if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
          }
          form.classList.add('was-validated');

          const email = form.email.value.trim();
          const nickname = form.nickname.value.trim();
          const password = form.password.value;
          const confirmPassword = form.confirmPassword.value;

          if (password !== confirmPassword) {
            Swal.fire({ icon: 'error', title: '오류', text: '비밀번호가 일치하지 않습니다.' });
            return;
          }

          try {
            const response = await fetch('/api/v1/auth/sign-up', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({ email, nickname, password })
            });

            const data = await response.json();
            if (!data.isSuccess) {
              Swal.fire({ icon: 'error', title: '회원가입 실패', text: data.message || '회원가입에 실패했습니다.' });
              return;
            }

            Swal.fire({ icon: 'success', title: '회원가입 완료', text: '로그인 페이지로 이동합니다.' })
                .then(() => window.location.href = '/auth/sign-in');

          } catch (error) {
            Swal.fire({ icon: 'error', title: '오류', text: '회원가입 중 문제가 발생했습니다. 다시 시도해주세요.' });
          }
        });
      })();
    </script>

</body>
</html>