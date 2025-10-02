<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="sidebar">
  <div class="d-flex align-items-center gap-3 mb-3">
    <c:choose>
      <c:when test="${not empty view.profileImageUrl}">
        <img src="${view.profileImageUrl}" alt="프로필" class="avatar-xl border" />
      </c:when>
      <c:otherwise>
        <img src="${pageContext.request.contextPath}/images/default-profile.jpeg" alt="프로필" class="avatar-xl border" />
      </c:otherwise>
    </c:choose>
    <div>
      <div class="fw-semibold">${view.nickname}</div>
      <div class="text-muted small">${view.email}</div>
    </div>
  </div>

  <div class="list-group">
    <a href="${pageContext.request.contextPath}/mypage"
       class="list-group-item ${page eq 'mypage' ? 'active' : ''}">내 정보</a>
    <a href="${pageContext.request.contextPath}/mypage/posts"
       class="list-group-item ${page eq 'posts' ? 'active' : ''}">내가 올린 게시글</a>
    <a href="${pageContext.request.contextPath}/mypage/password"
       class="list-group-item ${page eq 'password' ? 'active' : ''}">비밀번호 변경</a>
  </div>
</div>