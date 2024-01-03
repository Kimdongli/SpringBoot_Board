## 스프링부트 이용하여 게시판 만들기
---
## Board V2.4.0

### ※ 개발환경
\- __IDE : InteliJ IDEA Community__  
\- __Java 11,HTML5__  
\- __Spring Boot 2.7.6__  
\- __JDK 11__  
\- __MySQL 8.0.35__  
\- __Lombok__
\- __Spring Web__  
\- __Spring Data JPA__  
\- __Thymleaf__  
\- __aplication.yml__  

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
1. __회원가입 및 로그인 기능(구현완료)__  
2. __게시글 검색 기능__  
3. __추천 버튼(추천 많이받을시 인기 게시물 등록)__

---


## ※ 기능 구현 설명 CURD

### 게시물
1. 메인화면(/) [localhost:8080](http://localhost:8080/)  
<div class="centered-image">
  <img src="./image/1.png" with="350" height="350">
</div><br>

2. 게시판 페이지(/board, /board/paging)  
\-한 페이지 게시글 5개  
\-최대 3페이지씩
<div class="centered-image">
  <img src="./image/11.png" >
</div><br>


3. 게시물 작성(/board/save)  
  \- 파일첨부(단일/다중)
<div class="centered-image">
  <img src="./image/2.png" >
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

## ※ 회원가입 및 로그인 기능 구현 설명(kakao RestAPI)
1. 메인 화면 이동(/)  
  \- 로그인을하면 로그인한것을 확인후 로그인 후 화면으로 이동  
  <div class="centered-image">
    <img src="./image/kakao3.png">
  </div><br>

### 일반 회원
1. 로그인(/user/login)
<div class="centered-image">
  <img src="./image/kakao1.png">
</div><br>

2. 회원가입(/user/join)
<div class="centered-image">
  <img src="./image/kakao2.png">
</div><br>

3. 로그아웃(/user/logout)  
 \- 로그아웃 버튼을누르면 로그아웃

4. 이메일 중복확인(/user/check)

5. 로그인 인증 토큰 발급(/user/oauth)

6. 토큰 갱신(/user/refresh)

7. (세션 기반)연결된 사용자 Id를 반환(/user/send_userid)

8. (Spring Security 기반)인증된 사용자 Id를 반환(/user/user_id)

### 카카오 회원
1. 카카오톡 인증 토큰 발급(/kakao/oauth)

2. 카카오톡 재로그인(/kakao/relogin)

3. 카카오톡 로그인(/kakao/login)  
\- 카카오 디벨로퍼에서 설정
<div class="centered-image">
  <img src="./image/kakao4.png">
</div><br>

4. 카카오톡 로그아웃(/kakao/logout)

5. 전체 로그아웃(/kakao/flogout)


---

### ※ 버전 업데이트

### V1.0.0 (2023.11.21)
1. __[기능 기능 추가] 게시물 작성__  
\- 게시글 작성 버튼과 게시물 제목, 내용 입력 필드를 구현하였습니다. (엔드포인트: /board/save)
2. __[기능 기능 추가] 메인 페이지 이동__  
\- 메인 페이지로 이동하는 기능을 기능 추가하였습니다. (엔드포인트: /)

### V1.1.0 (2023.11.24)

1. __[기능 추가] 게시물 페이지__  
\- 게시물 페이지를 구현하였습니다. (엔드포인트: /board/paging)
2. __[기능 추가] 게시물 작성 화면 이동__  
\- 게시물 작성 화면으로 이동하는 기능을 추가하였습니다. (엔드포인트: /board/create)
3. __[기능 추가] 게시물 조회__  
\- 게시물 번호, 제목, 작성일, 내용을 조회하는 기능을 추가하였습니다. (엔드포인트: /board/{id})
4. __[기능 추가] 게시물 수정__  
\- 게시물을 수정하는 기능을 추가하였습니다. (엔드포인트: /board/update, /board/update/{id})
5. __[기능 추가] 게시물 삭제__  
\- 게시물을 삭제하는 기능을 추가하였습니다. (엔드포인트: /board/delete/{id})

### V1.2.0 (2023.11.25)
1. __[기능 추가] 게시물 댓글 연동__  
\- 게시물과 댓글을 연동하는 기능을 추가하였습니다. (엔드포인트: /getComments/{boardId})
2. __[기능 추가] 댓글 작성__  
\- 댓글 작성 버튼을 추가하였습니다. (엔드포인트: /comment/save)

### V1.3.0 (2023.11.26)

1. __[기능 추가] 댓글 삭제__  
\- 댓글을 삭제하는 기능을 추가하였습니다. (엔드포인트: /comment/delete/{id})

### V1.4.0 (2023.11.28)

1. __[기능 추가] 게시물 파일 다운로드__  
\- 게시물 작성 시 파일을 다운로드할 수 있는 버튼을 추가하였습니다. (엔드포인트: /download/{uuid}/{fileName})

### V1.4.1 (2023.11.29)
1. __[보안 개선] 새로고침 후 목록 사라짐 방지__
2. __[보안 개선] 게시물 등록 시 파일 미등록 허용__
3. __[디자인 개선] 화면 디자인 수정__

### V1.5.0 (2023.12.12)
1. __[기능 추가] 댓글 카테고리 구현__
2. __[기능 추가] 댓글 수정__   
\- 댓글 수정 후에도 카테고리가 유지되도록 기능을 추가하였습니다. (엔드포인트: /comment/update/{id})

---
### V2.0.0(2023.12.13)
1. __[보안 기능 추가] 사용자 인증 기능 구현__
2. __[유틸리티 추가] API 및 응답 필터 유틸리티 구현__
3. __[기능 추가] 유저 로그인,회원가입__  
\- 유저 로그인 기능을 추가하였습니다. (엔드포인트: /user/login)  
\- 유저 회원가입 기능을 추가하였습니다. (엔드포인트: /user/join)

### V2.1.0(2023.12.14)
1. __[기능 추가] 유저 로그아웃__  
\- 유저 로그아웃 기능을 추가하였습니다. (엔드포인트: /user/logout)
2. __[기능 추가] 사용자 인증 및 JWT 생성 처리__  
\- 사용자 인증 JWT 토큰을 생성 반환하는 기능을 추가하였습니다.(엔드포인트: /user/oauth)
3. __[기능 추가] 유저 ID값 반환(세션기반)__  
\- 로그인된 유저 Id값을 세션 기반 반환하는 기능 추가하였다. (엔드포인트: /user/user_id)
4. __[기능 추가] 토큰 갱신__  
\- access_token유효기간 만료시 대체할 refresh_token 추가하였습니다.(/user/refresh)

### V2.2.0(2023.12.18)
1. __[기능 추가] 카카오 회원가입 및 로그인__  
\- 카카오 로그인 및 회원가입 기능을 추가하였습니다.(엔드포인트:/kakao/login)  
2. __[기능 추가] 카카오 로그아웃__  
\- 카카오 유저 로그아웃 기능을 추가하였습니다.(엔드포인트:/kakao/flogout)

### V2.3.0(2023.12.19)
1. __[기능 수정] 카카오 로그인 오류 수정__  
\- 카카오 로그인 기능에서 발생하던 오류를 수정하였습니다. (엔드포인트: /kakao/login)  
2. __[기능 추가] UserService에 refresh_token 추가__   
\- UserService에 사용자 인증을 유지하기 위한 refresh_token 기능을 추가하였습니다.

### V2.3.1(2023.12.20)
1. __[기능 수정] 파일 이름 뷰 수정__  
\- 파일 이름이 뷰에 정확하게 표시되도록 수정하였습니다.
2. __[기능 수정] 게시판 HTML, JavaScript 수정__  
\- 게시판의 HTML 및 JavaScript 코드를 전체적으로 수정하였습니다.

### V2.4.0(2023.12.22)
1. __[기능 추가] 유저 ID값 반환(Security기반)__  
\- 로그인된 유저 Id값을 Security기반을 반환하는 기능 추가하였다.(엔드포인트: /user/send_userid)
2. __[기능 수정] 페이징 HTML 수정__  
\- 페이징 처리를 위한 HTML 코드를 수정하였습니다.
3. __[기능 수정] 로그인, 회원가입 디자인 수정__  
\- 로그인 및 회원가입 페이지의 디자인을 수정하였습니다.
4. __[기능 수정] DTO에 updateTime, createTime 수정__  
\- DTO에서 사용하는 updateTime, createTime 필드의 처리 방식을 수정하였습니다.
