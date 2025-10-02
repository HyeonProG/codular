<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>비밀번호 찾기 - Codular</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css" />
</head>

<body class="page--simple d-flex flex-column min-vh-100">
  <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="container py-5 mt-auto">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5 shadow-sm p-4 rounded bg-white">
          <h3 class="mb-4">비밀번호 찾기</h3>
          <form id="forgotForm">
            <div class="mb-3">
              <label for="email" class="form-label">가입한 이메일</label>
              <input type="email" class="form-control" id="email" name="email" required/>
            </div>
            <button type="submit" class="btn btn-primary">비밀번호 재설정 메일 보내기</button>
          </form>

          <div id="msg" class="mt-3"></div>
        </div>
      </div>
    </div>


  <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>


<script>
document.getElementById('forgotForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const email = document.getElementById('email').value;
  try {
    const res = await fetch('${pageContext.request.contextPath}/api/v1/auth/password/forgot', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email })
    });
    if (res.ok) {
      document.getElementById('msg').innerHTML =
        '<div class="alert alert-success">입력하신 이메일로 재설정 링크를 보냈습니다.</div>';
    } else {
      document.getElementById('msg').innerHTML =
        '<div class="alert alert-danger">메일 발송에 실패했습니다. 다시 시도해주세요.</div>';
    }
  } catch (err) {
    document.getElementById('msg').innerHTML =
      '<div class="alert alert-danger">오류가 발생했습니다.</div>';
  }
});
</script>