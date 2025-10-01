<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>페이지를 찾을 수 없습니다 · Codular</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="icon" href="<c:url value='/favicon.ico'/>">
  <link rel="stylesheet" href="<c:url value='/css/app.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/error.css'/>">
</head>
<body class="bg-light page--simple">

  <main class="container page-main--center" style="min-height:55vh;">
    <div class="text-center">
      <div class="display-1 fw-bold mb-2">404</div>
      <h1 class="h3 fw-semibold mb-3">페이지를 찾을 수 없습니다</h1>
      <p class="text-muted mb-4">요청하신 페이지가 삭제되었거나, 주소가 변경되었을 수 있어요.</p>
      <div class="d-flex gap-3 justify-content-center">
        <button type="button" class="btn btn-outline-secondary" onclick="history.back()">← 뒤로가기</button>
        <a class="btn btn-primary" href="<c:url value='/'/>">홈으로 가기</a>
      </div>
    </div>
  </main>

  <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>