<%@ page contentType="text/html; charset=UTF-8" %>

<section class="py-5 text-center">
  <h1 class="display-4 fw-bold mb-3">Codular</h1>
  <p class="lead text-muted mb-4">개발자가 자주 쓰는 Boilerplate 코드를 모아 공유하는 커뮤니티 플랫폼</p>
  <div class="d-flex gap-3 justify-content-center">
    <a href="/posts" class="btn btn-primary btn-lg px-4">보일러플레이트 보러가기</a>
    <a href="/auth/sign-in" class="btn btn-outline-secondary btn-lg px-4">로그인</a>
  </div>
</section>
<section class="container py-5">
  <div class="row g-4 text-center">
    <div class="col-12 col-md-4">
      <div class="p-4 border rounded-3 h-100">
        <div class="fs-2 mb-2">🧩</div>
        <h5 class="fw-semibold mb-2">모듈형 보일러플레이트</h5>
        <p class="text-muted mb-0">Auth, 파일업로드, 검색 등 재사용 가능한 코드 조각을 한곳에.</p>
      </div>
    </div>
    <div class="col-12 col-md-4">
      <div class="p-4 border rounded-3 h-100">
        <div class="fs-2 mb-2">⚡️</div>
        <h5 class="fw-semibold mb-2">빠른 시작</h5>
        <p class="text-muted mb-0">ZIP/스니펫로 바로 가져가 프로젝트에 붙여쓰기.</p>
      </div>
    </div>
    <div class="col-12 col-md-4">
      <div class="p-4 border rounded-3 h-100">
        <div class="fs-2 mb-2">🤝</div>
        <h5 class="fw-semibold mb-2">커뮤니티 검증</h5>
        <p class="text-muted mb-0">좋아요/다운로드로 품질 신뢰도 확인.</p>
      </div>
    </div>
  </div>
</section>
<section class="container py-5">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0">최근 추가된 보일러플레이트</h4>
    <a href="/posts" class="link-primary">모두 보기 →</a>
  </div>
  <div class="row g-4">
    <div class="col-12 col-md-4">
      <div class="card h-100 shadow-sm">
        <div class="card-body">
          <h5 class="card-title mb-2">Spring Boot JWT 로그인</h5>
          <p class="card-text text-muted small">MyBatis · JWT · Refresh · Redis</p>
          <div class="d-flex gap-2">
            <a class="btn btn-outline-primary btn-sm" href="#">자세히</a>
            <a class="btn btn-light btn-sm" href="#">다운로드</a>
          </div>
        </div>
      </div>
    </div>
    <!-- 필요 카드 더 복제 -->
  </div>
</section>
<section class="py-5 my-4 bg-light">
  <div class="container d-md-flex align-items-center justify-content-between text-center text-md-start">
    <div class="mb-3 mb-md-0">
      <h4 class="fw-semibold mb-1">당신의 보일러플레이트를 공유하세요</h4>
      <p class="text-muted mb-0">ZIP 또는 스니펫으로 업로드하고 커뮤니티와 함께 성장하세요.</p>
    </div>
    <a href="/posts/new" class="btn btn-primary px-4 py-2 mt-3 mt-md-0">보일러플레이트 업로드</a>
  </div>
</section>