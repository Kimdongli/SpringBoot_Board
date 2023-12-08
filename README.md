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

---

### ※ 수정 처리
1. __글삭제(/board/delete/{id})__  
2. __댓글 삭제(/comment/delete/{id})__  
2. __페이징 처리(/board/paging)__  
\- /board/paging?page=1  
\- /board/paging/1
3. __게시글__  
\- __한 페이지에  게시글 5개씩 ==> 최대 3개 페이지씩__  
\- __한페이지에  3개씩 ==>5개__

4. __파일 첨부하기__  
5. __단일 파일 첨부__  
6. __다중 파일 첨부__  

---

### ※ 향후 업데이트 예정 기능
1. __추천 버튼(추천 많이받을시 인기 게시물 등록)__  
2. __게시글 검색 기능__  
3. __댓글 수정 기능__  
4. __회원가입 및 로그인 기능__

---

### ※ 버전 업데이트

### V1.0.0 (2023.11.21)

1. [추가] 게시물 작성 구현(/board/save)  
\- 게시글 작성 버튼 구현(Html)  
\- 게시글 작성 구현 게시물 제목,게시물 내용(Html)
2. [추가] 메인 페이지 이동 구현(/)  

### V1.1.0 (2023.11.24)

1. [추가] 게시물 페이지 구현(/board/paging)  
2. [추가] 게시물 작성 화면으로 이동(/board/create)  
3. [추가] 게시물 조회(/board/{id})  
\- 게시물 번호, 게시물 제목,게시글 작성일, 게시글 내용  
\- 목록,수정,삭제 버튼(Html, JavaScript)
4. [추가] 게시물 수정(/board/update,/board/update/{id})  
5. [추가] 게시물 삭제(/board/delete/{id})

### V1.2.0 (2023.11.25)
1. [추가] 게시물 게시글 연동(/getComments/{boardId})
1. [추가] 게시글 작성(/comment/save)  
\- 게시글 버튼 작성(Html, JavaScript)

### V1.3.0 (2023.11.26)

1. [추가] 게시글 삭제(/comment/delete/{id})
\- 게시글 삭제 버튼(Html, JavaScript)

### V1.4.0 (2023.11.28)

1. [추가] 게시물 파일 다운로드(/download/{uuid}/{fileName})

\- 게시글 작성시 다운로드 버튼(Html, JavaScript)
### V1.4.1 (2023.11.28)

1. 새로고침후 목록 사라지는거 보안
2. 게시물 등록시 파일을 등록을 안하여도 등록되도록 보안
3. 화면 디자 수정

---

### ※ 기능 구현 설명 CURD

#### __1.InteliJ 실행후 [localhost:8080](http://localhost:8080/) 으로 들어가서 글쓰기를 누릅니다.__

<div class="centered-image">
  <img src="./image/1.png">
</div><br>

##### MySQL

<div class="centered-image">
  <img src="./image/1-1.png">
</div><br>

#### __2. Title,Contents작성후 File까지 첨부하고(File 첨부는 null값이여도 상관없습니다.)__

<div class="centered-image">
  <img src="./image/2.png">
</div><br>

- __게시물 작성후 데이터 베이스에 Board,File 정보가 저장된것을 확인할수 있습니다.__

##### MySQL(Board)

<div class="centered-image">
  <img src="./image/1-2.png">
</div><br>

##### MySQL(File)

<div class="centered-image">
  <img src="./image/1-3.png">
</div><br>

#### __3.게시판으로 이동후 게시판 내부로 들어가면__

<div class="centered-image">
  <img src="./image/3.png">
</div><br>

#### __4.게시판 내부 뷰__

<div class="centered-image">
  <img src="./image/4.png">
</div><br>

#### __5.게시판 목록 기능__
- __게시판 목록버튼을 클릭하면 게시판으로 이동합니다.__

<div class="centered-image">
  <img src="./image/3.png">
</div><br>

#### __6.게시판 수정기능__

<div class="centered-image">
  <img src="./image/8.png">
</div><br>

- __수정버튼을 누른후 적용 되어있는걸 확인할수 있습니다.__

<div class="centered-image">
  <img src="./image/9.png">
</div><br>

##### MySQL

- __SQL에도 잘 저장 되어있는것을 확인할수 있습니다.__

<div class="centered-image">
  <img src="./image/1-4.png">
</div><br>

#### __7. 게시판 파일 다운로드__
- __파일 다운로드를 누르면 제가등록한 파일이 다운로드 되는것을 확인할수 있습니다.__

<div class="centered-image">
  <img src="./image/10.png">
</div><br>

#### __8.게시판 삭제기능__

- __삭제버튼을 누르면 게시판으로 넘어가면서 삭제된걸 확인할수 있습니다.__

<div class="centered-image">
  <img src="./image/11.png">
</div><br>



##### MySQL(Board)

- __뷰에서 삭제버튼을 누른후 SQL에도 File,Board가 삭제된것을 확인할수있습니다.__

<div class="centered-image">
  <img src="./image/1-5.png">
</div><br>

##### MySQL(File)
<div class="centered-image">
  <img src="./image/1-6.png">
</div><br>

#### __9.게시판 댓글기능__
- __댓글 작성자와 내용을 입력후 댓글작성 버튼을 누르면 현재화면처럼 댓글이 등록되어있는것을 확인할수 있습니다.__

<div class="centered-image">
  <img src="./image/12.png">
</div><br>

##### MySQL

- __댓글 작성한것이 SQL에 잘 저장되어있는것을 확인할수 있습니다.__

<div class="centered-image">
  <img src="./image/1-7.png">
</div><br>

#### __10.게시판 댓글삭제__
- __댓글 작성이 되어있고 그 댓글을 삭제버튼을 누를시 즉시 삭제됩니다.__

<div class="centered-image">
  <img src="./image/13.png">
</div><br>

##### MySQL

- __댓글 삭제후 SQL에도 사라진것을 확인할수 있습니다.__

<div class="centered-image">
  <img src="./image/1-8.png">
</div><br>
