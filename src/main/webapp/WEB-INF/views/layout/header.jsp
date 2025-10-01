<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom py-0">
  <div class="container">
    <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/">
      <img src="${pageContext.request.contextPath}/images/logo.png" alt="Codular Logo" class="brand-logo me-2"/>
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nv"
            aria-controls="nv" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div id="nv" class="collapse navbar-collapse">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item"><a class="nav-link text-dark" href="${pageContext.request.contextPath}/posts">보일러플레이트</a></li>
      </ul>

      <div id="authArea" class="d-flex gap-2 align-items-center">
        <a class="btn btn-outline-dark" href="${pageContext.request.contextPath}/auth/sign-in">로그인</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/auth/sign-up">회원가입</a>
      </div>
    </div>
  </div>
</nav>

<script>
  window.APP_CTX = '${pageContext.request.contextPath}';
</script>
<script defer src="${pageContext.request.contextPath}/js/header-auth.js"></script>