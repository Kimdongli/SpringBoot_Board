## REST API 설계

1.회원가입 요청

### 유저
\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|POST|/user/join|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|

\- 요청 본체

|변수명|타입|설명|필수 여부|
|-|-|-|-|
|email|String|사용자 이메일 주소,로그인 아이디로 사용됨|O|
|password |String|사용자 비밀번호,영문자(최소한 하나의 대문자),특수문자,숫자를 	각각 하나 이	상 포함하는 8~20자의 조합|O|
|name|String|사용자 이름|O|

2.로그인 요청

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|POST|/user/login|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|

\- 요청 본체

|변수명|타입|설명|필수 여부|
|-|-|-|-|
|email|String|사용자 이메일 주소,로그인 아이디로 사용됨|O|
|password |String|사용자 비밀번호,영문자(최소한 하나의 대문자),특수문자,숫자를 	각각 하나 이	상 포함하는 8~20자의 조합|O|

3.로그아웃 요청

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|POST|/user/logout|엑세스 토큰|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|
|Authorization|Authorization: Bearer ${access_token}(사용자 인증)|

### 게시판

4.게시판 작성

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|POST|/board/save|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|

\- 요청 본체

|변수명|타입|설명|필수 여부|
|-|-|-|-|
|userId|Long|유저 아이디(게시물 작성시 유저 정보 저장용도)|O|
|title|String|게시물 제목|O|
|contents|String|게시물 내용|O|
|createTime|LocalDateTime|게시물 최초 작성 시간|O|
|updateTime|LocalDateTime|게시물 최근 수정 시간|X|
|files|List<BoardFile>|첨부 파일|X|
|comment|List<Comment>|댓글|X|

5.게시판 수정

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|POST|/board/update|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|
|Authorization|Authorization: Bearer ${access_token}(사용자 인증)|
\- 요청 본체

|변수명|타입|설명|필수 여부|
|-|-|-|-|
|id|Long|게시판 아이디|O|
|title|String|게시물 제목|O|
|contents|String|게시물 내용|O|
|files|List<BoardFile>|첨부 파일|X|


6.게시판 목록

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|GET|/board/paging|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|

\- 요청 본체

|변수명|타입|설명|필수 여부|
|-|-|-|-|
|id|Long|게시판 아이디|O|
|userId|Long|유저 아이디(게시물 작성시 유저 정보 저장용도)|O|
|title|String|게시물 제목|O|
|contents|String|게시물 내용|O|
|createTime|LocalDateTime|게시물 최초 작성 시간|O|
|updateTime|LocalDateTime|게시물 최근 수정 시간|X|

7.게시판 삭제

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|GET|/board/delete|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|
|Authorization|Authorization: Bearer ${access_token}(사용자 인증)|

### 댓글

8.댓글 작성

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|POST|/comment/save|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|

\- 요청 본체

|변수명|타입|설명|필수 여부|
|-|-|-|-|
|id|Long|댓글 아이디|O|
|boadrId|Long|댓글 작성시 댓글달 게시판 연결|O|
|userId|Long|댓글이 특정 사용자에 의해 작성되었음을 위함|O|
|contents|String|댓글 작성 내용|O|
|writer|String|댓글 작성자 이름|O|

9.댓글 수정

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|GET|/comment/update|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|
|Authorization|Authorization: Bearer ${access_token}(사용자 인증)|

\- 요청 본체

|변수명|타입|설명|필수 여부|
|-|-|-|-|
|id|Long|댓글 아이디|O|
|userId|Long|댓글이 특정 사용자에 의해 작성되었음을 위함|O|
|contents|String|댓글 작성 내용|O|

10.댓글 목록

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|GET|/comment/getComments|-|

\- 헤더

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|


\- 요청 본체

|변수명|타입|설명|필수 여부|
|-|-|-|-|
|id|Long|댓글 아이디|O|
|userId|Long|댓글이 특정 사용자에 의해 작성되었음을 위함|O|
|contents|String|댓글 작성 내용|O|

11.댓글 삭제

\- 정보

|메서드|URL|인증 방식|
|-|-|-|
|GET|/comment/delte/{id}|-|

|이름|타입|
|-|-|
|ContentType|application/json(데이터 타입)|
|Authorization|Authorization: Bearer ${access_token}(사용자 인증)|
