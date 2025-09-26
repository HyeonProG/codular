

# Codular

개발자들이 자주 사용하는 **보일러플레이트 코드**를 한 곳에 모아 공유하고 검색할 수 있는 커뮤니티 플랫폼입니다.  
MVP는 Java 17 / Spring Boot / MyBatis / JSP / MySQL 기반으로 구현합니다.

---

## 프로젝트 목표

- 흩어진 보일러플레이트 코드를 한 곳에서 쉽게 찾아 활용 가능
- 카테고리와 기술 스택 기반의 검색/필터 제공
- ZIP 파일 업로드 또는 코드 스니펫 공유 지원
- 좋아요, 댓글을 통한 커뮤니티 기능 제공

---

## 기술 스택

- **Backend**: Java 17, Spring Boot, Spring MVC, MyBatis
- **DB**: MySQL 8
- **View**: JSP (Jakarta JSTL)
- **Build/Tools**: Gradle, Lombok
- **Infra**: AWS S3 (파일 저장)

---

## 프로젝트 구조

```
src
 └─ main
     ├─ java/com/codular
     │   ├─ CodularApplication.java
     │   ├─ config/                # 설정 (MyBatis, WebMvc 등)
     │   └─ domain/
     │       ├─ user/
     │       ├─ post/
     │       ├─ category/
     │       ├─ stack/
     │       └─ comment/
     ├─ resources/
     │   ├─ application.yml
     │   ├─ schema.sql             # DB 스키마
     │   └─ data.sql               # 샘플 데이터 (선택)
     └─ webapp/WEB-INF/view/
         ├─ index.jsp
         └─ ... (도메인별 JSP)
```

---

## MVP 기능

- 회원가입 / 로그인
- 보일러플레이트 업로드 (ZIP / Snippet)
- 게시글 목록 / 상세 조회
- 카테고리 및 스택 기반 검색 / 필터
- 좋아요 (Reaction) 기능
- 댓글 / 대댓글 기능