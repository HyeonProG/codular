(function () {
  'use strict';

  const BASE = typeof window.APP_CTX === 'string' ? window.APP_CTX : '';
  const log  = (...a) => console.debug('[header]', ...a);

  const $auth = document.getElementById('authArea');

  // 로그인 영역 기본 UI (비로그인)
  function renderLoggedOut() {
    if (!$auth) return;
    $auth.innerHTML = `
      <a class="btn btn-outline-dark" href="${BASE}/auth/sign-in">로그인</a>
      <a class="btn btn-primary" href="${BASE}/auth/sign-up">회원가입</a>
    `;
  }

  // nickname 안전 삽입: innerHTML로 직접 넣지 않고 textContent 사용
  function renderDropdown(nickname) {
    if (!$auth) return;
    $auth.innerHTML = `
      <div class="dropdown">
        <button class="btn btn-outline-secondary dropdown-toggle" type="button"
                data-bs-toggle="dropdown" aria-expanded="false"></button>
        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" href="${BASE}/mypage">마이페이지</a></li>
          <li><hr class="dropdown-divider"></li>
          <li><button class="dropdown-item" type="button" data-action="logout">로그아웃</button></li>
        </ul>
      </div>
    `;
    // XSS 방지: 버튼 텍스트는 textContent로 주입
    const btn = $auth.querySelector('.dropdown-toggle');
    if (btn) btn.textContent = nickname || '회원';
  }

  async function getMe() {
    const res = await fetch(`${BASE}/api/v1/user/me`, {
      method: 'GET',
      credentials: 'include',
      cache: 'no-store',
      headers: { 'X-Requested-With': 'XMLHttpRequest' }
    });
    log('GET /user/me ->', res.status);

    if (res.status === 401) return null;   // 비로그인
    if (!res.ok) return null;

    const json = await res.json().catch(() => null);
    return (json && json.isSuccess && json.result) ? json.result : null; // {nickname}
  }

  let loggingOut = false; // 중복 요청 방지

  // 이벤트 위임: authArea 내부에서만 처리 (전역 캡처 제거)
  if ($auth) {
    $auth.addEventListener('click', async (e) => {
      const el = e.target.closest('[data-action="logout"]');
      if (!el) return;

      e.preventDefault();
      if (loggingOut) return;
      loggingOut = true;

      el.disabled = true;
      el.textContent = '로그아웃 중…';

      try {
        const res = await fetch(`${BASE}/api/v1/auth/logout`, {
          method: 'POST',
          credentials: 'include',
          headers: { 'X-Requested-With': 'XMLHttpRequest' }
        });
        log('POST /auth/logout ->', res.status);
      } catch (err) {
        log('logout error', err);
      } finally {
        // 쿠키 무효화/Redis 삭제 후 헤더 갱신
        location.reload();
      }
    });
  }

  async function boot() {
    if (!$auth) return;
    try {
      const me = await getMe();
      if (me) renderDropdown(me.nickname);
      else    renderLoggedOut();
    } catch (e) {
      log('init error', e);
      renderLoggedOut();
    }
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', boot);
  } else {
    boot();
  }
})();