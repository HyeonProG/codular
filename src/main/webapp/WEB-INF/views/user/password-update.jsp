<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>비밀번호 변경 - Codular</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css" />
  <style>
    .page--simple main { padding-top: 2rem; padding-bottom: 2rem; }
    .avatar-xl { width: 96px; height: 96px; border-radius: 50%; object-fit: cover; }
    .sidebar .list-group-item { border: 0; padding-left: 0; padding-right: 0; }
    .sidebar .list-group-item.active { font-weight: 600; color: #0d6efd; background: transparent; }
  </style>
</head>

<body class="page--simple d-flex flex-column min-vh-100">
  <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

  <main class="container flex-grow-1">
    <div class="row g-4">
      <aside class="col-12 col-md-3">
        <jsp:include page="/WEB-INF/views/layout/mypage-sidebar.jsp">
          <jsp:param name="active" value="password"/>
        </jsp:include>
      </aside>

      <!-- 우측 콘텐츠 -->
      <section class="col-12 col-md-9">
        <div class="card shadow-sm">
          <div class="card-body">
            <h1 class="h5 mb-4">비밀번호 변경</h1>

            <form id="pwForm" class="needs-validation" novalidate>
              <div class="mb-3">
                <label for="currentPassword" class="form-label">현재 비밀번호</label>
                <input id="currentPassword" name="currentPassword" type="password" class="form-control" required autocomplete="current-password" placeholder="현재 비밀번호">
                <div class="invalid-feedback">현재 비밀번호를 입력해주세요.</div>
              </div>

              <div class="mb-3">
                <label for="newPassword" class="form-label">새 비밀번호</label>
                <input id="newPassword" name="newPassword" type="password" class="form-control" required minlength="8" maxlength="64" placeholder="새 비밀번호 (영문+숫자 조합)">
                <div class="form-text" id="pwHint">영문 + 숫자 조합 8~64자</div>
                <div class="invalid-feedback">새 비밀번호를 확인해주세요.</div>
              </div>

              <div class="mb-4">
                <label for="confirmPassword" class="form-label">새 비밀번호 확인</label>
                <input id="confirmPassword" name="confirmPassword" type="password" class="form-control" required placeholder="새 비밀번호 다시 입력">
                <div class="invalid-feedback">새 비밀번호가 일치하지 않습니다.</div>
              </div>

              <button id="btnSubmit" class="btn btn-primary w-100 py-2" type="submit">변경하기</button>
            </form>
          </div>
        </div>
      </section>
    </div>
  </main>

  <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <script>
  (() => {
    const ctx = '${pageContext.request.contextPath}';

    // CSRF
    function getCookie(name) {
      const m = document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)');
      return m ? decodeURIComponent(m.pop()) : '';
    }
    const CSRF_HEADER = 'X-XSRF-TOKEN';
    const CSRF_TOKEN  = getCookie('XSRF-TOKEN');

    const form = document.getElementById('pwForm');
    const btn  = document.getElementById('btnSubmit');
    const cur  = document.getElementById('currentPassword');
    const pw   = document.getElementById('newPassword');
    const pw2  = document.getElementById('confirmPassword');

    function validPolicy(v) {
      if (!v || v.length < 8 || v.length > 64) return false;
      const hasAlpha = /[A-Za-z]/.test(v);
      const hasDigit = /\d/.test(v);
      return hasAlpha && hasDigit;
    }

    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      // 1) 클라이언트 측 검증
      if (!cur.value.trim()) {
        await Swal.fire({ icon: 'warning', title: '입력 오류', text: '현재 비밀번호를 입력해주세요.' });
        return;
      }
      if (!validPolicy(pw.value)) {
        await Swal.fire({ icon: 'warning', title: '입력 오류', text: '비밀번호는 영문+숫자 조합 8~64자로 입력해주세요.' });
        return;
      }
      if (pw.value !== pw2.value) {
        await Swal.fire({ icon: 'warning', title: '입력 오류', text: '새 비밀번호가 일치하지 않습니다.' });
        return;
      }

      // 2) 서버 요청
      btn.disabled = true; btn.textContent = '변경 중…';
      try {
        const res = await fetch(ctx + '/api/v1/user/password', {
          method: 'PATCH',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest',
            [CSRF_HEADER]: CSRF_TOKEN
          },
          body: JSON.stringify({
            currentPassword: cur.value,
            newPassword: pw.value,
            confirmPassword: pw2.value
          })
        });

        const data = await res.json().catch(() => ({}));

        if (!res.ok || data.isSuccess === false) {
          await Swal.fire({
            icon: 'error',
            title: '변경 실패',
            text: data.message || '비밀번호 변경에 실패했습니다.'
          });
          return;
        }

        await Swal.fire({
          icon: 'success',
          title: '변경 완료',
          text: '비밀번호가 성공적으로 변경되었습니다.'
        });
        location.reload();

      } catch (err) {
        await Swal.fire({
          icon: 'error',
          title: '오류',
          text: '네트워크 오류가 발생했습니다.'
        });
      } finally {
        btn.disabled = false; btn.textContent = '변경하기';
      }
    });
  })();
  </script>
</body>
</html>