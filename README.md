# [Spring 3기] Ch 3. 일정 관리 서버 만들기

![images.jpg](images.jpg)

## 📁 과제 소개 및 목표

Spring 강의를 듣고 구현하고자 하는 서비스의 전체적인 흐름을 파악하고 필요한 기능을 설계할 수 있다.
<br>API 명세서, ERD, SQL 데이터 베이스를 작성할 수 있다.
<br>Spring Boot를 기반으로 CRUD 기능이 포함된 REST API를 만들 수 있다.


## 🌏 개발 환경

- Java (JDK) 17.0.12
- IDE: IntelliJ IDEA Ultimate
- Spring Boot: 3.3.4
    - Dependencies
        - Spring Web
        - Thymeleaf
        - Lombok
        - MySQL driver
        - JDBC API


## ⏲ 개발 기간

2024.09.26 ~ 2024.10.04

### Task Process

|        Date         | Progress                        |
|:-------------------:|---------------------------------|
| 24.09.26 ~ 24.10.01 | 과제 발제 이후 개인 과제 요구 사항 이해<br>Spring 강의 수강 |
| 24.10.02 ~ 24.10.04 | 코드 작성, README 작성, 트러블 슈팅        |
|     24.10.04 ~      | 과제 제출                           |

## 📋 목차

- 1️⃣ 기획편 - 요구 사항 정의 및 설계

- 2️⃣ 개발편 - 단계별 필수 기능 구현을 위한 코딩

- 3️⃣ 고찰 및 회고 편 - 힘들거나 어려웠던 부분 및 소감

<br>

### 1️⃣ 기획편
#### 요구사항 숙지
**Lv 2. 일정 생성 및 조회**

1. 일정 생성(일정 작성하기)
    - 할일, 작성자명, 비밀번호, 작성/수정일 저장
    - 작성/수정일은 날짜와 시간을 모두 포함한 상태
    - 각 일정의 고유 식별자(id)를 자동으로 생성하여 관리
    - 최초 입력 시 수정일은 작성일과 동일
2. 전체 일정 조회(등록된 일정 불러오기)
    - 수정일: YYYY-MM-DD
    - 작성자명
    - 위 두 조건 중 한 가지만을 충족하거나, 둘 다 충족하지 않거나, 둘 다 충족할 수도 있음
    - 수정일 기준 내림차순으로 정렬하여 조회
3. 선택 일정 조회(선택한 일정 정보 불러오기)
    - 단건 정보 조회
    - 고유 식별자(id)를 사용하여 조회

**Lv 3. 일정 수정 및 삭제**

1. 선택한 일정 수정
    - 일정 내용 중 할일, 작성자명만 수정 가능
        - 이때 비밀번호와 함께 전달 > 비밀번호가 일치하지 않을 시 에러 메시지 출력
        - 작성일은 변경할 수 없음. 수정일은 수정 완료 시 수정한 시점으로 변경
2. 선택한 일정 삭제

###  2️⃣ 개발편

#### Step 1. 필수 기능 가이드

**Lv 1. API 명세 및 ERD 작성**

#### 일정 앱 API 명세서

|         기능         |  Method  |         URL         | RequestBody                                                                                           | Response                                                                                  |  상태 코드   |
|:-------------------:|:--------:|:-------------------:|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|:-------------:|
|  **일정 생성**   |   POST   |   /api/schedules    | {<br>"todo": "할일",<br>"name": "작성자명",<br>"password": "비밀번호",<br>"date": "2024-10-01 12:00:00"<br>}    | {<br>"id": 1,<br>"todo": "할일",<br>"name": "작성자명",<br>"date": "2024-10-01 12:00:00"<br>}   |   201: 정상등록   |
| **전체 일정 조회** |   GET    |   /api/schedules    | -                                                                                                     | [{<br>"id": 1,<br>"todo": "할일",<br>"name": "작성자명",<br>"date": "2024-09-30 09:00:00"<br>}] |  200: 정상 조회   |
| **선택 일정 조회** |   GET    | /api/schedules/{id} | -                                                                                                     | {<br>"id": 1,<br>"todo": "할일",<br>"name": "작성자명",<br>"date": "2024-10-01 12:00:00"<br>}   |  200: 정상 조회   |
| **선택 일정 수정** |   PUT    | /api/schedules/{id} | {<br>"todo": "수정된 할일",<br>"name": "작성자명",<br>"password": "비밀번호"<br>"date": "2024-10-01 12:00:00"<br>} | {<br>"id": 1,<br>"todo": "할일",<br>"name": "작성자명",<br>"date": "2024-10-01 12:00:00"<br>}   |  200: 정상 수정   |
| **선택 일정 삭제** |  DELETE  | /api/schedules/{id} | {<br>"password": "비밀번호"<br>}                                                                          | {<br>"message": "삭제 성공"<br>}                                                              |  204: 정상 삭제   |

#### ERD
![img.png](img.png)

#### SQL 작성하기
schedule.sql에 작성함.

**Lv 2. 일정 생성 및 조회**
**Lv 3. 일정 수정 및 삭제**

부분마다 보여주면 코드가 빠지는 부분이 있을 것 같아 전체 코드 한번에 볼 수 있도록 함.

**entity package**
[Schedule.java]
<details>
<summary> 코드 전체 확인하기 </summary>

```java
package com.sparta.assignment_schedule.entity;

import com.sparta.assignment_schedule.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    private Long id;
    private String name;
    private String password;
    private String todo;
    private String date;

    public Schedule(ScheduleRequestDto scheduleRequestDto) {
        this.name = scheduleRequestDto.getName();
        this.password = scheduleRequestDto.getPassword();
        this.todo = scheduleRequestDto.getTodo();
        this.date = scheduleRequestDto.getDate();
    }
}

```

</details>

✔ About Schedule.java

1. Entity 패키지로, 일정 데이터를 저장하고 관리하는 역할을 수행하며, 데이터베이스와의 직접적인 매핑을 담당하는 객체를 정의
2. Annotation
   - @Getter / @Setter: 필드에 대한 getter 메소드, setter 메소드를 자동 생성
   - @NoArgsConstructor: 기본 생성자를 자동 생성
3. 필드 속성
   - Long id: 데이터 베이스에서 일정의 고유 식별자(PK) 역할
   - String name: 일정 생성자의 이름 저장
   - String password: 일정 생성 및 수정 시 사용되는 비밀번호를 저장
   - String todo: 일정 내용 저장
   - String date: 일정의 작성일 또는 수정일을 날짜 및 시간 형식으로 저장(문자열로 표현함)

**controller package**
[ScheduleController.java]
<details>
<summary> 코드 전체 확인하기 </summary>

```java
package com.sparta.assignment_schedule.controller;

import com.sparta.assignment_schedule.dto.ScheduleRequestDto;
import com.sparta.assignment_schedule.dto.ScheduleResponseDto;
import com.sparta.assignment_schedule.service.ScheduleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //일정 등록
    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.createSchedule(requestDto);
    }

    //선택한 일정 조회
    @GetMapping("/schedules/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Long id) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.getSchedule(id);
    }

    //전체 일정 목록 조회
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getScheduleList() {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.getScheduleList();
    }

    //선택한 일정 수정
    @PutMapping("/schedules/{id}")
    public Long updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.updateSchedule(id, scheduleRequestDto);
    }

    //선택한 일정 삭제
    @DeleteMapping("/schedules/{id}")
    public Long deleteSchedule(@PathVariable Long id) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.deleteSchedule(id);
    }
}


```

</details>

✔ About ScheduleController.java

1. 사용자의 요청을 처리하고, 비즈니스 로직을 수행하는 서비스 계층과 상호작용하여 데이터를 반환하는 역할을 담당. HTTP 요청을 처리하는 컨트롤러.
2. Annotation
   - @RestController: RESTful 웹 서비스의 컨트롤러임을 선언. 모든 메소드의 반환값이 JSON 형태로 처리됨.
   - @RequestMapping("/api"): 이 컨트롤러에 속한 모든 메소드의 기본 경로를 /api로 설정.
3. 필드 및 생성자: ScheduleController(JdbcTemplate jdbcTemplate)
4. 메소드:
   - @PostMapping("/schedules"): 일정 등록
   - @GetMapping("/schedules"): 전체 일정 목록 조회
   - @GetMapping("/schedules/{id}"): 선택 일정 조회
   - @PutMapping("/schedules/{id}"): 선택한 일정 수정
   - @DeleteMapping("/schedules/{id}"): 선택한 일정 삭제
5. 요약
   - 일정 생성, 조회, 수정, 삭제와 같은 CRUD 작업을 처리하는 역할을 수행.
   - 서비스 계층(ScheduleService)과 상호작용하여 실제 비즈니스 로직이 처리된 결과를 반환.


**dto Packages**
[ScheduleRequestDto.java]
<details>
<summary> 코드 전체 확인하기 </summary>

```java
package com.sparta.assignment_schedule.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private String name;
    private String password;
    private String todo;
    private String date;
}
```

</details>

✔ About ScheduleRequestDto.java

1. 클라이언트에서 전송된 데이터를 서버 측에서 손쉽게 받아 처리하기 위한 DTO로 사용됨.
2. 주로 일정 생성 및 수정 작업에서 요청 데이터를 전달하는 역할을 수행.

[ScheduleResponseDto.java]
<details>
<summary> 코드 전체 확인하기 </summary>

```java
package com.sparta.assignment_schedule.dto;

import com.sparta.assignment_schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String name;
    private String todo;
    private String date;

    public ScheduleResponseDto(Schedule schedule){
        this.id=schedule.getId();
        this.name=schedule.getName();
        this.todo=schedule.getTodo();
        this.date=schedule.getDate();
    }
    public ScheduleResponseDto(Long id,String name,String todo,String date){
        this.id=id;
        this.name=name;
        this.todo=todo;
        this.date=date;
    }
}
```

</details>

✔ About ScheduleResponseDto.java

1. 서버가 클라이언트 요청에 대해 일정 데이터를 전달할 때 사용하는 DTO로 활용
2. Schedule entity에서 필요한 정보를 추출해, 클라이언트에게 반환할 데이터를 준비하는 역할을 수행.


**service package**
[ScheduleService.java]
<details>
<summary> 코드 전체 확인하기 </summary>

```java
package com.sparta.assignment_schedule.service;

import com.sparta.assignment_schedule.dto.ScheduleRequestDto;
import com.sparta.assignment_schedule.dto.ScheduleResponseDto;
import com.sparta.assignment_schedule.entity.Schedule;
import com.sparta.assignment_schedule.repository.ScheduleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //일정 등록
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        //RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);

        //DB 저장
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule saveSchedule = scheduleRepository.save(schedule);

        //Entity -> ResponseDto
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);
        return scheduleResponseDto;
    }

    //선택한 일정 조회
    public ScheduleResponseDto getSchedule(Long id) {
        //해당 일정이 존재하는 지 확인
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule schedule = scheduleRepository.findById(id);
        if (schedule != null) {
            ScheduleResponseDto scheduleResponseDto = scheduleRepository.find(id);
            return scheduleResponseDto;
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다");
        }
    }

    //일정 수정
    public Long updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
        //해당 일정이 DB에 존재하는 지 확인
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule schedule = scheduleRepository.findById(id);
        if (schedule != null) {
            scheduleRepository.updateSchedule(id, scheduleRequestDto);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다");
        }
    }

    //전체 일정 조회
    public List<ScheduleResponseDto> getScheduleList() {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        return scheduleRepository.findAll();
    }

    //일정 삭제
    public Long deleteSchedule(Long id) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);

        Schedule schedule = scheduleRepository.findById(id);
        //해당 일정이 DB에 존재하는 지 확인
        if (schedule != null) {
            scheduleRepository.delete(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다");
        }
    }

    public Schedule findById(Long id) {
        String sql = "SELECT * FROM schedule WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setName(resultSet.getString("name"));
                schedule.setTodo(resultSet.getString("todo"));
                return schedule;
            } else {
                return null;
            }
        }, id);
    }
}
```

</details>

✔ About ScheduleService.java

1. Controller와 Repository 사이에서 비즈니스 로직을 수행하며, 데이터를 가공하고 Repository에 저장하거나 조회하는 역할을 담당.
2. Annotation
   - @Service: spring에서 이 클래스가 서비스 계층을 담당하는 빈(Bean)으로 관리되도록 함.
3. 필드 및 생성자: ScheduleService(JdbcTemplate jdbcTemplate)
4. 메소드:
    - createSchedule(): 일정 등록
    - getScheduleList(): 전체 일정 목록 조회
    - getSchedule(Long id): 선택 일정 조회
    - updateSchedule(): 선택한 일정 수정
    - deleteSchedule(): 선택한 일정 삭제
5. 요약
    - 일정 등록, 조회, 수정, 삭제 등 모든 핵심 비즈니스 로직 처리
    - JdbcTemplate을 사용해 SQL 쿼리를 직접 실행하고, 데이터 베이스와 상호작용하며 데이터를 처리
    - 데이터의 CRUD(생성, 조회, 수정, 삭제) 작업을 수행하며, 각 작업에 대해 예외 처리도 포함.


**repository package**
[ScheduleRepository.java]
<details>
<summary> 코드 전체 확인하기 </summary>

```java
package com.sparta.assignment_schedule.repository;

import com.sparta.assignment_schedule.dto.ScheduleRequestDto;
import com.sparta.assignment_schedule.dto.ScheduleResponseDto;
import com.sparta.assignment_schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;
    public ScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }
    //DB저장
    public Schedule save(Schedule schedule) {
        KeyHolder keyHolder=new GeneratedKeyHolder(); //기본 키를 반환받기 위한 객체

        String sql="INSERT INTO schedule (name, password, todo, date) VALUES (?,?,?,?)";

        jdbcTemplate.update(con ->{
            PreparedStatement preparedStatement=con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,schedule.getName());
            preparedStatement.setString(2,schedule.getPassword());
            preparedStatement.setString(3,schedule.getTodo());
            preparedStatement.setString(4,schedule.getDate());

            return preparedStatement;
        },keyHolder);

        Long id=keyHolder.getKey().longValue();
        schedule.setId(id);
        return schedule;
    }
    //선택한 id 조회
    public Schedule findById(Long id){
        String sql="SELECT * FROM schedule WHERE id = ?";

        return jdbcTemplate.query(sql,resultSet ->{
            if(resultSet.next()){
                Schedule schedule=new Schedule();
                schedule.setName(resultSet.getString("name"));
                schedule.setTodo(resultSet.getString("todo"));
                return schedule;
            }else{
                return null;
            }
        },id);
    }
    //전체 조회
    public List<ScheduleResponseDto> findAll() {
        //DB 조회
        String sql="SELECT * FROM schedule";

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id=rs.getLong("id");
                String name=rs.getString("name");
                String todo=rs.getString("todo");
                String date=rs.getString("date");
                return new ScheduleResponseDto(id,name,todo,date);
            }
        });
    }
    //삭제
    public void delete(Long id) {
        String sql="DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql,id);
    }
    //수정
    public void updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
        String sql="UPDATE schedule SET name = ?, todo = ? WHERE id = ?";
        jdbcTemplate.update(sql,scheduleRequestDto.getName(),scheduleRequestDto.getTodo(),id);
    }
    public ScheduleResponseDto find(Long id) {
        String sql="SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("todo"),
                        rs.getString("date")
                );
            }
        },id);
    }
}


```

</details>

✔ About ScheduleRepository.java

1. 데이터베이스와 직접 상호작용하는 계층. Service 계층에서 호출되며 데이터의 CRUD를 처리
2. Annotation
   - @Repository: spring에서 이 클래스가 데이터 접근 계층을 담당하는 빈(Bean)으로 관리되도록 함.
3. 필드 및 생성자: ScheduleRepository(JdbcTemplate jdbcTemplate)
4. 메소드
   - save(Schedule schedule): 일정 데이터 저장. keyholder로 자동 생성된 id를 저장 > Schedule entity에 설정 후 반환
   - findById(Long id): ID로 특정 일정 조회
   - findAll(): 모든 일정 조회
   - delete(Long id): 특정 일정 삭제
   - updateSchedule(): 일정 수정
   - find(Long id): ID로 일정 조회 후 ScheduleResponseDto로 반환
5. 요약
   - 일정과 관련된 데이터베이스 작업 처리(CRUD)
   - JdbcTemplate을 활용해 SQL 쿼리 실행, 결과 매핑 후 엔티티나 dto로 변환하여 반환
   - CRUD 기능을 위한 다양한 메소드 제공. 각 메소드는 SQL 쿼리를 통해 직접 데이터베이스와 상호작용.


###  3️⃣ 고찰 및 회고편
#### 회고 및 후기

[[자세한 이야기는 블로그에]](https://jisuryu0527.tistory.com/54)

강의를 듣는 데에 시간이 너무 오래 걸렸다. 그런데도 사실 강의 내용을 다 내 것으로 만들지 못했고, 어쩔 수 없이 강의를 보면서 슬쩍슬쩍
코드만 바꾸는 식으로 초안을 만들었다. 이번엔 구글링도 딱히 소용이 없었던 게, 열심히 검색해도 관련된 내용은 거의 없었기 때문이다. 또는 비슷한 내용의 
글을 찾았다 싶어서 보면 JPA로 만든 코드였다. 정말 JDBC를 사용하는 사람이 거의 없단 사실을 덤으로 알게된 것이다. 그만큼 많이 불편하다는 의미
겠지, 싶었다.

원래는 강의때 본 메모 프로젝트처럼 html에서 실제로 보이는 것까지 해보고 싶었는데 이번에도 내 욕심이 좀 크지 않았나 싶었다.