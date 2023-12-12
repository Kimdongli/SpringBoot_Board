## 스프링부트 이용하여 게시판 만들기
---
## Board V1.4.1(11/20 ~ 12/1)

### ※ 개발환경
\- __aplication.yml__  
\- __IDE : IDE : InteliJ IDEA Community__  
\- __Spring Boot 2.7.6__  
\- __JDK 11__  
\- __MySQL 8.0.35__  
\- __Lombok__
\- __Spring Web__  
\- __Spring Data JPA__  
\- __Thymleaf__

---

### ※ 게시판 주요기능(CURD)

1. __데이터 베이스__  
\- __A. 게시물 DB 저장__  
\- __B. 댓글 DB 저장__  

2. __게시물 기능__  
\- __A. 게시물 목록(/board)__  
\- __B. 게시물 등록(/board/save)__  
\- __C. 게시물 수정(/board/update/{id})__  
\- __D. 게시물 조회(/board/{id})__  
\- __E. 게시물 삭제(/board/delete/{id})__

3. __댓글 기능__  
\- __A. 댓글 작성(/comment/save)__  
\- __B. 댓글 삭제(/comment/delete/{id})__  
\- __C. 댓글 수정(/comment/update/{id})__

---

### ※ 수정 처리
1. __글삭제(/board/delete/{id})__  
2. __댓글 삭제(/comment/delete/{id})__  
2. __페이징 처리(/board/paging)__  
\- /board/paging?page=1  
\- /board/paging/1
3. __게시글__  
\- 한 페이지에  게시글 5개씩 ==> 최대 3개 페이지씩  
4. __파일 첨부하기__  
5. __단일 파일 첨부__  
6. __댓글 수정(/comment/update/{id})__

---

### ※ 향후 업데이트 예정 기능
1. __추천 버튼(추천 많이받을시 인기 게시물 등록)__  
2. __게시글 검색 기능__  
3. __회원가입 및 로그인 기능__

---


## ※ 기능 구현 설명 CURD
### 게시물
1. 메인화면(/) [localhost:8080](http://localhost:8080/)  
<div class="centered-image">
  <img src="./image/1.png">
</div><br>


2. 게시판 페이지(/board, /board/paging)  
\-한 페이지 게시글 5개
\-최대 3페이지씩
<div class="centered-image">
  <img src="./image/11.png">
</div><br>


3. 게시물 작성(/board/save)  
  \- 파일첨부(단일/다중)
<div class="centered-image">
  <img src="./image/2.png">
</div><br>

4. 게시물 조회(/board/{id})
<div class="centered-image">
  <img src="./image/4.png">
</div><br>

5. 게시물 수정(/board/update/{id})  
\- 상세화면에서 수정 버튼 클릭  
\- 서버에서 해당 게시글 정보를 찾아서 가지고 수정 화면 출력
<div class="centered-image">
  <img src="./image/8.png">
</div><br>

6. 게시물 수정 적용(/board/update)
<div class="centered-image">
  <img src="./image/9.png">
</div><br>

7. 게시물 삭제(/board/delete/{id})  
\- 상세 화면에서 삭제 버튼 클릭하면 삭제가능

### 댓글
1. 게시물 댓글 작성(/comment/save)
<div class="centered-image">
  <img src="./image/14.png">
</div><br>

2. 게시물 댓글 확인(/comment/getComments/{boardId})
<div class="centered-image">
  <img src="./image/12.png">
</div><br>

3. 게시물 댓글 삭제(/comment/delete/{id})  
\- 게시물 화면에서 삭제버튼 클릭하면 삭제가능

4. 게시물 댓글 수정(/comment/update/{id})
<div class="centered-image">
  <img src="./image/15.png">
</div><br>
<div class="centered-image">
  <img src="./image/16.png">
</div><br>



### 파일 다운
1. 게시물에 첨부한 파일(이미지) 다운로드(/download/{uuid},{filename})
<div class="centered-image">
  <img src="./image/10.png">
</div><br>

---

### ※ 버전 업데이트

### V1.0.0 (2023.11.21)

1. __[추가] 게시물 작성 구현(/board/save)__  
\- 게시글 작성 버튼 구현(Html)  
\- 게시글 작성 구현 게시물 제목,게시물 내용(Html)
2. __[추가] 메인 페이지 이동 구현(/)__  

### V1.1.0 (2023.11.24)

1. __[추가] 게시물 페이지 구현(/board/paging)__  
2. __[추가] 게시물 작성 화면으로 이동(/board/create)__  
3. __[추가] 게시물 조회(/board/{id})__  
\- 게시물 번호, 게시물 제목,게시글 작성일, 게시글 내용  
\- 목록,수정,삭제 버튼(Html, JavaScript)
4. __[추가] 게시물 수정(/board/update,/board/update/{id})__  
5. __[추가] 게시물 삭제(/board/delete/{id})__

### V1.2.0 (2023.11.25)
1. __[추가] 게시물 댓글 연동(/getComments/{boardId})__
2. __[추가] 댓글 작성(/comment/save)__  
\- 댓글 버튼 작성(Html, JavaScript)

### V1.3.0 (2023.11.26)

1. __[추가] 댓글 삭제(/comment/delete/{id})__
\- 댓글 삭제 버튼(Html, JavaScript)

### V1.4.0 (2023.11.28)

1. __[추가] 게시물 파일 다운로드(/download/{uuid}/{fileName})__  
\- 게시물 작성시 다운로드 버튼(Html, JavaScript)

### V1.4.1 (2023.11.29)

1. __새로고침후 목록 사라지는거 보안__
2. __게시물 등록시 파일을 등록을 안하여도 등록되도록 보안__
3. __화면 디자인 수정__

### V1.5.0 (2023.12.12)
1. __[추가]게시물 댓글 카테고리__
2. __[추가] 댓글 수정 기능(/comment/update/{id})__  
\- 댓글 수정 후에도 카테고리가 유지되도록 기능을 보완.
