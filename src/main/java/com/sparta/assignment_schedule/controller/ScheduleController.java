package com.sparta.assignment_schedule.controller;

import com.sparta.assignment_schedule.dto.ScheduleCreateRequestDto;
import com.sparta.assignment_schedule.dto.ScheduleResponseDto;
import com.sparta.assignment_schedule.dto.ScheduleUpdateRequestDto;
import com.sparta.assignment_schedule.entity.Schedule;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final Map<Long, Schedule> scheduleList = new HashMap<>();

    @PostMapping("")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleCreateRequestDto requestDto) {
        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (title, username, password, contents, date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, requestDto.getTitle());
            preparedStatement.setString(2, requestDto.getUsername());
            preparedStatement.setLong(3, requestDto.getPassword());
            preparedStatement.setString(4, requestDto.getContents());
            preparedStatement.setString(5, requestDto.getDate());
            return preparedStatement;
        }, keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        schedule.setId(id);

        // Entity -> ResponseDto
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);
        return scheduleResponseDto;
    }

    @GetMapping("")
    public List<ScheduleResponseDto> getAllSchedules() {
        // DB에서 모든 schedule 조회
        String sql = "SELECT * FROM schedule";

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
                    @Override
                    public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        // SQL 결과를 ScheduleResponseDto로 변환
                        Long id = rs.getLong("id");
                        String title = rs.getString("title");
                        String username = rs.getString("username");
                        String contents = rs.getString("contents");
                        LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                        Schedule schedule = new Schedule();
                        schedule.setId(id);
                        schedule.setTitle(title);
                        schedule.setUsername(username);
                        schedule.setContents(contents);
                        schedule.setDate(date);

                        return new ScheduleResponseDto(schedule);
                    }
                }).stream()
                // 날짜를 기준으로 내림차순 정렬
                .sorted(Comparator.comparing(ScheduleResponseDto::getDate).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ScheduleResponseDto getScheduleById(@PathVariable Long id) {
        // DB에서 schedule 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";

        try {
            Schedule schedule = jdbcTemplate.queryForObject(sql, new RowMapper<Schedule>() {
                @Override
                public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Schedule schedule = new Schedule();
                    schedule.setId(rs.getLong("id"));
                    schedule.setTitle(rs.getString("title"));
                    schedule.setUsername(rs.getString("username"));
                    schedule.setContents(rs.getString("contents"));
                    schedule.setDate(rs.getTimestamp("date").toLocalDateTime());
                    return schedule;
                }
            }, id);
            return new ScheduleResponseDto(schedule);
        } catch (EmptyResultDataAccessException e) {
            // 해당 ID의 데이터가 없을 경우 예외 처리
            throw new IllegalArgumentException("존재하지 않는 일정입니다.");
        }
    }

    @PutMapping("/{id}/{password}")
    public ScheduleResponseDto updateSchedule(@PathVariable Long id, @PathVariable Long password,
                                              @RequestBody ScheduleUpdateRequestDto requestDto) {
        // DB에서 해당 id로 일정 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";
        try {
            Schedule schedule = jdbcTemplate.queryForObject(sql, new RowMapper<Schedule>() {
                @Override
                public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Schedule schedule = new Schedule();
                    schedule.setId(rs.getLong("id"));
                    schedule.setTitle(rs.getString("title"));
                    schedule.setUsername(rs.getString("username"));
                    schedule.setPassword(rs.getLong("password"));
                    schedule.setContents(rs.getString("contents"));
                    schedule.setDate(rs.getTimestamp("date").toLocalDateTime());
                    schedule.setModifiedDate(rs.getTimestamp("modifiedDate").toLocalDateTime());
                    return schedule;
                }
            }, id);

            // 비밀번호 확인
            if (!schedule.getPassword().equals(password)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // 일정 업데이트
            String updatesql = "UPDATE schedule SET title = ?, contents = ?, username = ?, modified_date = ? WHERE id = ?";
            jdbcTemplate.update(updatesql, schedule.getTitle(), schedule.getContents(), schedule.getUsername(), LocalDateTime.now(), id);
            return new ScheduleResponseDto(schedule);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 일정입니다.");
        }
    }

    @DeleteMapping("/{id}/{password}")
    public String deleteSchedule(@PathVariable Long id, @PathVariable Long password) {
        // DB에서 해당 id로 일정 조회
        String sql = "DELETE FROM schedule WHERE id = ?";
        try {
            Schedule schedule = jdbcTemplate.queryForObject(sql, new RowMapper<Schedule>() {
                @Override
                public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Schedule schedule = new Schedule();
                    schedule.setId(rs.getLong("id"));
                    schedule.setPassword(rs.getLong("password"));
                    return schedule;
                }
            }, id);

            // 비밀번호 확인
            if (!schedule.getPassword().equals(password)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // 일정 삭제
            String deletesql = "DELETE FROM schedule WHERE id = ?";
            jdbcTemplate.update(deletesql, id);
            return "삭제되었습니다.";
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 일정입니다.");
        }
    }
}


//    private final ScheduleService scheduleService;
//
//    @Autowired
//    public ScheduleController(ScheduleService scheduleService) {
//        this.scheduleService = scheduleService;
//    }
//
//    // 일정 등록
//    @PostMapping("/schedules")
//    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
//        return scheduleService.createSchedule(requestDto);
//    }
//
//
//    // 전체 일정 목록 조회
//    @GetMapping("/schedules")
//    public List<ScheduleResponseDto> getScheduleList(
//            @RequestParam(required = false) String date,
//            @RequestParam(required = false) String name) {
//        return scheduleService.getScheduleList(date, name);
//    }
//
//    // 선택 일정 조회
//    @GetMapping("/schedules/{id}")
//    public ScheduleResponseDto getSchedule(@PathVariable Long id) {
//        return scheduleService.getSchedule(id);
//    }
//
//    // 선택 일정 수정
//    @PutMapping("/schedules/{id}")
//    public Long updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
//        return scheduleService.updateSchedule(id, requestDto);
//    }
//
//    // 선택 일정 삭제
//    @DeleteMapping("/schedules/{id}")
//    public Long deleteSchedule(@PathVariable Long id, @RequestParam String password) {
//        return scheduleService.deleteSchedule(id, password);
//    }

