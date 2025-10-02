<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>비밀번호 재설정 - Codular</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css" />
</head>

<body class="page--simple d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<div class="container py-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow-sm">
        <div class="card-body p-4">
          <h3 class="mb-4">비밀번호 재설정</h3>
          <form id="resetForm">
            <input type="hidden" id="email" value="${param.email}" />
            <input type="hidden" id="token" value="${param.token}" />

            <div class="mb-3">
              <label for="password" class="form-label">새 비밀번호</label>
              <input type="password" class="form-control" id="password" required minlength="8" />
              <div id="pwHelp" class="form-text text-muted">영문+숫자 조합, 8자 이상</div>
            </div>

            <div class="mb-3">
              <label for="confirm" class="form-label">비밀번호 확인</label>
              <input type="password" class="form-control" id="confirm" required />
              <div id="confirmHelp" class="form-text text-muted"></div>
            </div>

            <button type="submit" class="btn btn-primary w-100">비밀번호 변경</button>
          </form>
          <div id="msg" class="mt-3"></div>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>

<script>
const pw = document.getElementById('password');
const cf = document.getElementById('confirm');
const confirmHelp = document.getElementById('confirmHelp');

cf.addEventListener('input', () => {
  if (pw.value !== cf.value) {
    confirmHelp.textContent = '비밀번호가 일치하지 않습니다.';
    confirmHelp.className = 'form-text text-danger';
  } else {
    confirmHelp.textContent = '비밀번호가 일치합니다.';
    confirmHelp.className = 'form-text text-success';
  }
});

document.getElementById('resetForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const email = document.getElementById('email').value;
  const token = document.getElementById('token').value;
  const newPassword = pw.value;

  const res = await fetch('${pageContext.request.contextPath}/api/v1/auth/password/reset', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, token, newPassword })
  });

  const msg = document.getElementById('msg');
  if (res.ok) {
    msg.innerHTML = '<div class="alert alert-success">비밀번호가 성공적으로 변경되었습니다. 로그인 페이지로 이동하세요.</div>';
    setTimeout(() => location.href='${pageContext.request.contextPath}/auth/sign-in', 2000);
  } else {
    msg.innerHTML = '<div class="alert alert-danger">재설정에 실패했습니다. 다시 시도해주세요.</div>';
  }
});
</script>
</body>
</html>