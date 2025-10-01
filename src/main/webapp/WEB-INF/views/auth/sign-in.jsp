<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>로그인 - Codular</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css" />
</head>

<body class="page--simple d-flex flex-column min-vh-100">
  <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

  <main class="container flex-grow-1 d-flex align-items-center py-4">
    <section class="mx-auto w-100" style="max-width: 480px;">
      <h1 class="h3 mb-4 text-center">로그인</h1>

      <form id="signInForm" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
          <label for="email" class="form-label">이메일</label>
          <input id="email" name="email" type="email" class="form-control" required
                 placeholder="email@example.com" autocomplete="username" />
          <div class="invalid-feedback">이메일을 입력해주세요.</div>
        </div>

        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input id="password" name="password" type="password" class="form-control" required
                 placeholder="••••••••" autocomplete="current-password" />
          <div class="invalid-feedback">비밀번호를 입력해주세요.</div>
        </div>

        <div class="d-flex justify-content-end mb-3">
          <a href="/auth/reset-password" class="small text-muted">비밀번호 찾기</a>
        </div>

        <button class="btn btn-primary w-100 py-2" type="submit">로그인</button>
      </form>

      <p class="text-center mt-4 mb-0">
        계정이 없으신가요? <a href="/auth/sign-up">회원가입</a>
      </p>
    </section>
  </main>

  <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <script>
    (() => {
        const form = document.getElementById('signInForm');
        form.addEventListener('submit', async e => {
          e.preventDefault();
          if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
          }
          form.classList.add('was-validated');

          const email = form.email.value.trim();
          const password = form.password.value;

          try {
            const response = await fetch('/api/v1/auth/sign-in', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({ email, password })
            });

            if (response.status === 401) {
              Swal.fire({
                icon: 'error',
                title: '로그인 실패',
                text: '이메일 또는 비밀번호가 올바르지 않습니다.',
              }).then(() => {
                window.location.reload(); // 새로고침해서 입력칸 초기화
              });
              return;
            }

            const data = await response.json();
            if (!data.isSuccess) {
              Swal.fire({
                icon: 'error',
                title: '로그인 실패',
                text: '이메일 또는 비밀번호가 올바르지 않습니다.',
              }).then(() => {
                window.location.reload();
              });
              return;
            }

            const urlParams = new URLSearchParams(window.location.search);
            const redirect = urlParams.get('redirect') || '/';
            window.location.href = redirect;

          } catch (error) {
            Swal.fire({
              icon: 'error',
              title: '오류',
              text: '로그인 중 오류가 발생했습니다. 다시 시도해주세요.',
              confirmButtonColor: '#0d6efd',
              confirmButtonText: '확인'
            }).then(() => {
              window.location.reload();
            });
          }
        });
    })();
  </script>
</body>
</html>