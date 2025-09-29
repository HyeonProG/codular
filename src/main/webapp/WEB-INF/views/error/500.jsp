<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>서버 오류가 발생했습니다 · Codular</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="icon" href="<c:url value='/favicon.ico'/>">
  <link rel="stylesheet" href="<c:url value='/css/app.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/error.css'/>">
</head>
<body class="bg-light page--simple">

  <main class="container page-main--center" style="min-height:55vh;">
    <div class="text-center">
      <div class="display-1 fw-bold mb-2">500</div>
      <h1 class="h3 fw-semibold mb-3">서버 내부 오류가 발생했습니다</h1>
      <p class="text-muted mb-4">
        잠시 후 다시 시도해주세요. 문제가 지속되면 관리자에게 문의해주세요.
      </p>

      <!-- 개발환경에서만 노출하고 싶다면 profile 체크 후 표시 -->
      <c:if test="${not empty exception}">
        <details class="text-start mx-auto mb-4" style="max-width:720px;">
          <summary class="text-muted">오류 상세 보기</summary>
          <pre class="mt-2 small bg-white border rounded p-3"><c:out value="${exception}"/></pre>
        </details>
      </c:if>

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