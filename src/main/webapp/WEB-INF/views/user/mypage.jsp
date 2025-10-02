<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>마이페이지 - Codular</title>
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
      <!-- 좌측 사이드 -->
      <aside class="col-12 col-md-3">
        <jsp:include page="/WEB-INF/views/layout/mypage-sidebar.jsp"/>
      </aside>

      <!-- 우측 콘텐츠 -->
      <section class="col-12 col-md-9">
        <div class="card shadow-sm">
          <div class="card-body">
            <h1 class="h5 mb-4">내 정보</h1>

            <div class="row g-4 align-items-center">
              <div class="col-auto">
                <c:choose>
                  <c:when test="${not empty view.profileImageUrl}">
                    <img src="${view.profileImageUrl}" alt="프로필" class="avatar-xl border" />
                  </c:when>
                  <c:otherwise>
                    <img src="${pageContext.request.contextPath}/images/default-profile.jpeg" alt="프로필" class="avatar-xl border" />
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="col">
                <div class="mb-2">
                  <div class="text-muted small">닉네임</div>
                  <div class="fs-5 fw-semibold">${view.nickname}</div>
                </div>
                <div class="mb-2">
                  <div class="text-muted small">이메일</div>
                  <div>${view.email}</div>
                </div>
                <div>
                  <div class="text-muted small">가입일</div>
                  <div>${view.createdAt}</div>
                </div>
                <div class="mt-3 d-flex gap-2">
                  <input id="profileImageInput" type="file" accept="image/*" class="d-none">
                  <button id="btnChangeAvatar" type="button" class="btn btn-outline-secondary btn-sm">프로필 이미지 변경</button>
                  <button id="btnChangeNickname" type="button" class="btn btn-primary btn-sm">닉네임 변경</button>
                </div>
              </div>
            </div>

          </div>
        </div>
      </section>
    </div>

    <!-- 닉네임 변경 모달 -->
    <div class="modal fade" id="nicknameModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">닉네임 변경</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form id="nicknameForm" class="needs-validation" novalidate>
              <div class="mb-3">
                <label for="nickname" class="form-label">새 닉네임</label>
                <input id="nickname" name="nickname" type="text" class="form-control" minlength="2" maxlength="30" required
                       placeholder="새 닉네임을 입력하세요" value="${view.nickname}">
                <div class="invalid-feedback">닉네임은 2~30자로 입력해주세요.</div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">취소</button>
            <button id="btnSubmitNickname" type="button" class="btn btn-primary">변경하기</button>
          </div>
        </div>
      </div>
    </div>
  </main>

  <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <script>
    (() => {
      const ctx = '${pageContext.request.contextPath}';

      // ===== 프로필 이미지 변경 =====
      const btnAvatar = document.getElementById('btnChangeAvatar');
      const fileInput = document.getElementById('profileImageInput');

      if (btnAvatar && fileInput) {
        btnAvatar.addEventListener('click', () => fileInput.click());
        fileInput.addEventListener('change', async (e) => {
          const file = e.target.files && e.target.files[0];
          if (!file) return;

          const fd = new FormData();
          fd.append('image', file);

          btnAvatar.disabled = true;
          btnAvatar.textContent = '업로드 중…';

          try {
            const res = await fetch(ctx + '/api/v1/user/profile-image', {
              method: 'PATCH',
              body: fd,
              credentials: 'include',
              headers: { 'X-Requested-With': 'XMLHttpRequest' }
            });

            if (!res.ok) {
              Swal.fire({
                icon: 'error',
                title: '실패',
                text: '이미지 업로드에 실패했습니다.',
                confirmButtonColor: '#0d6efd',
                confirmButtonText: '확인'
              });
            } else {
              // 성공 시 새로고침하여 최신 이미지를 반영
              location.reload();
            }
          } catch (err) {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: '실패',
              text: '네트워크 오류가 발생했습니다.',
              confirmButtonColor: '#0d6efd',
              confirmButtonText: '확인'
            });
          } finally {
            btnAvatar.disabled = false;
            btnAvatar.textContent = '프로필 이미지 변경';
            fileInput.value = '';
          }
        });
      }

      // ===== 닉네임 변경 =====
      const nicknameModalEl = document.getElementById('nicknameModal');
      const nicknameModal = nicknameModalEl ? new bootstrap.Modal(nicknameModalEl) : null;
      const btnOpenNickname = document.getElementById('btnChangeNickname');
      const btnSubmitNickname = document.getElementById('btnSubmitNickname');
      const nicknameForm = document.getElementById('nicknameForm');
      const nicknameInput = document.getElementById('nickname');

      if (btnOpenNickname && nicknameModal) {
        btnOpenNickname.addEventListener('click', () => {
          nicknameForm.classList.remove('was-validated');
          nicknameModal.show();
          setTimeout(() => nicknameInput && nicknameInput.focus(), 50);
        });
      }

      if (btnSubmitNickname && nicknameForm) {
        btnSubmitNickname.addEventListener('click', async () => {
          if (!nicknameForm.checkValidity()) {
            nicknameForm.classList.add('was-validated');
            return;
          }
          const nickname = nicknameInput.value.trim();

          btnSubmitNickname.disabled = true;
          btnSubmitNickname.textContent = '변경 중…';

          try {
            const res = await fetch(ctx + '/api/v1/user/nickname', {
              method: 'PATCH',
              credentials: 'include',
              headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
              },
              body: JSON.stringify({ nickname })
            });

            const data = await res.json().catch(() => ({}));
            if (!res.ok || data.isSuccess === false) {
              Swal.fire({
                icon: 'error',
                title: '실패',
                text: (data && data.message) || '닉네임 변경에 실패했습니다.',
                confirmButtonColor: '#0d6efd',
                confirmButtonText: '확인'
              });
              return;
            }

            Swal.fire({
              icon: 'success',
              title: '닉네임 변경 완료',
              text: '닉네임이 성공적으로 변경되었습니다.',
              confirmButtonColor: '#0d6efd',
              confirmButtonText: '확인'
            }).then(() => {
              // DOM 즉시 반영
              document.querySelectorAll('.fw-semibold').forEach((el) => {
                if (el.textContent.trim() === '${view.nickname}') el.textContent = nickname;
              });
              const sideName = document.querySelector('.sidebar .fw-semibold');
              if (sideName) sideName.textContent = nickname;
              nicknameModal.hide();
            });
          } catch (err) {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: '실패',
              text: '네트워크 오류가 발생했습니다.',
              confirmButtonColor: '#0d6efd',
              confirmButtonText: '확인'
            });
          } finally {
            btnSubmitNickname.disabled = false;
            btnSubmitNickname.textContent = '변경하기';
          }
        });
      }
    })();
  </script>
</body>
</html>