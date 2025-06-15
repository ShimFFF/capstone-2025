

### 네이밍

- 클래스 : **PascalCase,** and·or와 같은 접속사를 사용하지 않고 25자 내외로 작성
- 함수 : **camelCase**
- 변수: **camelCase**
- DB 테이블: **snake_case**
- ENUM, 상수: **PascalCase**
- 컬렉션(Collection): **복수형**을 사용합니다. (Ex. users, userList)
- LocalDateTime: 접미사에 **Date 혹은 At**를 붙입니다.

<br>

### 클래스명

- 각 패키지명을 접미사에 명시합니다.
- 구현체의 경우 ~ Impl를 접미사에 추가합니다. (ex. UserServiceImpl, UserRepositoryImpl)

```java
controller: UserController
service: UserService
repository: UserRepository
dto/request: UserRequest
dto/response: UserResponse
entity: User
config: WebConfig
exception: UserNotFoundException
```

<br>

### 메소드명

- 메소드 작성 순서는 public 밑에 private입니다. 모든 public 메소드 밑에서부터 모든 private 메소드가 정의됩니다.
- event, design과 같은 **이중적인 단어를 가지는 단어는 지양합니다**.
- 메소드의 부수효과를 구체적으로 설명합니다.

    ```java
    void getTemp() {
    	Object temp = findTemp();
    	if (temp == null) {
    		temp = new Temp();
    	}
    	return temp;
    }
    ```

  해당 예시에서, 단순히 Temp를 조회하는 것이 아니고 비어있으면 새롭게 생성하는 역할을 하고 있음.  
  따라서 `getTemp` 보다 `getOrCreateTemp()` 가 적절함  
  단, 위는 예시일 뿐 **한 개의 메소드는 한 개의 역할만 하는 것을 지향**

<br>

- 의도가 전달되도록 최대한 간결하게 표현
- 메소드의 목적을 동사로 표현해 메소드의 앞에 붙임
    - `find~`, `add~`, `update~`, `remove~`, `create~`

<br>

### URL
> URL은 RESTful API 설계 가이드에 따라 작성

- HTTP Method로 구분할 수 있는 get, put 등의 행위는 url에 표현하지 않음
- 마지막에 `/` 를 포함하지 않음
- `_` 대신 `-`를 사용
- uri에는 camelCase를 사용하지 않음
    - **Bad**: `/api/communityPosts`
    - **Good**: `/api/community-posts`
    - 단, query parameter와 path variable에는 camelCase를 허용
        - ex) `/api/community-posts/{commentId}` , `/api/community-posts?postType="BLOG"`
- 소문자를 사용
- 확장자는 포함하지 않음
- 복수형을 사용

```
/api/auth
/api/users
/api/posts
/api/books
```

[REST API Naming가이드](https://velog.io/@caesars000/RESTful-API-가이드)

[REST란? REST API 디자인 가이드](https://covenant.tistory.com/241#google_vignette)



## Entity

Entity 생성 시 @Table, @Column 등 어노테이션을 모두 사용.  
이유는 코드 상의 변경이 발생해도 테이블 상과 매핑을 유지하기 위해서

또한 엔티티에 필드를 작성할 때는

1. default (db의 super key 개념에 해당하는 애들)
2. information (db의 일반 행)
3. relations (다른 테이블과의 매핑)

순으로 필드를 작성

<br>

## Pacakge

```

member
	⎿ entity
	⎿ enums
	⎿ web
		⎿ controller
		⎿ dto
	⎿ service
	⎿ repository 

```

패키지 구조는 위와 같이 도메인형 구조로 구축

[아키텍처 패키지 구조 : 계층형 vs 도메인형](https://velog.io/@syb0228/아키텍처-패키지-구조-계층형-vs-도메인형)