<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Codular</title>

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="<%=ctx%>/css/bootstrap.min.css"/>
  <!-- 앱 공용 CSS -->
  <link rel="stylesheet" href="<%=ctx%>/css/app.css"/>

  <script>window.APP_CTX = '<%=ctx%>';</script>
</head>
<body class="page--simple d-flex flex-column min-vh-100">

<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom py-0">
  <div class="container">
    <a class="navbar-brand fw-bold d-flex align-items-center" href="<%=ctx%>/">
      <img src="<%=ctx%>/images/logo.png" alt="Codular Logo" class="brand-logo me-2"/>
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nv"
            aria-controls="nv" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div id="nv" class="collapse navbar-collapse">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/posts">보일러플레이트</a></li>
      </ul>

      <div id="authArea" class="d-flex gap-2 align-items-center">
        <a class="btn btn-outline-dark" href="<%=ctx%>/auth/sign-in">로그인</a>
        <a class="btn btn-primary" href="<%=ctx%>/auth/sign-up">회원가입</a>
      </div>
    </div>
  </div>
</nav>

<!-- 헤더가 스크립트를 필요로 하면 여기에 -->
<script defer src="<%=ctx%>/js/header-auth.js"></script>