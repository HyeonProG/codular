<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${empty pageTitle ? 'Codular' : pageTitle}"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css"/>
</head>
<body>
    <jsp:include page="/WEB-INF/views/layout/_header.jsp"/>
    <main class="container py-4">
        <jsp:include page="${param.content}"/>
    </main>

    <script>
        // 토큰 유틸, 로그아웃
        function getAccess() {
            return localStorage.getItem('accessToken');
        }
        function getRefresh() {
            return localStorage.getItem('refreshToken');
        }
        function setTokens(access, refresh) {
            if (access) {
                localStorage.setItem('accessToken', access);
            }
            if (refresh) {
                localStorage.setItem("refreshToken", refresh);
            }
        }
        function clearTokens() {
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
        }
        async function logout() {
            const access = getAccess();
            if (!access) {
                clearTokens();
                location.href='/';
                return;
            }
            await fetch('/api/v1/auth/logout', {method:'POST', headers:{ 'Authorization':'Bearer ' + access}});
            clearTokens();
            location.href='/';
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>