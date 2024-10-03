package com.sparta.assignment_schedule.repository;

import com.sparta.assignment_schedule.dto.ScheduleRequestDto;
import com.sparta.assignment_schedule.dto.ScheduleResponseDto;
import com.sparta.assignment_schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleRepository {


//    private final JdbcTemplate jdbcTemplate;
//
//    public ScheduleRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    // DB 저장
//    public Schedule save(Schedule schedule) {
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        String sql = "INSERT INTO schedule (name, password, todo, date) VALUES (?, ?, ?, ?)";
//        jdbcTemplate.update(con -> {
//            PreparedStatement preparedStatement = con.prepareStatement(sql,
//                    PreparedStatement.RETURN_GENERATED_KEYS);
//            preparedStatement.setString(1, schedule.getName());
//            preparedStatement.setString(2, schedule.getPassword());
//            preparedStatement.setString(3, schedule.getTodo());
//            preparedStatement.setString(4, schedule.getDate());
//            return preparedStatement;
//        }, keyHolder);
//
//        // DB Insert 후 받아온 기본키 확인
//        Long id = keyHolder.getKey().longValue();
//        schedule.setId(id);
//        return schedule;
//    }
//
//    // 전체 일정 조회
//    public List<ScheduleResponseDto> findAll(String date, String name) {
//        // DB 조회
//        String sql = "SELECT * FROM schedule WHERE 1=1";
//
//        if (date != null && !date.isEmpty()) {
//            sql += " AND date = ?";
//        }
//        if (name != null && !name.isEmpty()) {
//            sql += " AND name = ?";
//        }
//
//        sql += " ORDER BY id DESC";
//
//        List<Object> params = new ArrayList<>();
//        if (date != null && !date.isEmpty()) {
//            params.add(date);
//        }
//        if (name != null && !name.isEmpty()) {
//            params.add(name);
//        }
//
//        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
//            @Override
//            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return new ScheduleResponseDto(
//                        rs.getLong("id"),
//                        rs.getString("name"),
//                        rs.getString("todo"),
//                        rs.getString("date")
//                );
//            }
//        }, params.toArray());
//    }
//
//    // 선택 일정 조회
//    public Schedule findById(Long id) {
//        String sql = "SELECT * FROM schedule WHERE id = ?";
//
//        return jdbcTemplate.query(sql, resultSet -> {
//            if(resultSet.next()) {
//                Schedule schedule = new Schedule();
//                schedule.setName(resultSet.getString("name"));
//                schedule.setTodo(resultSet.getString("todo"));
//                return schedule;
//            } else {
//                return null;
//            }
//        }, id);
//    }
//
//    // 일정 수정
//    public void updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
//        String query = "UPDATE schedule Set todo = ?, modified_date = ? WHERE id = ?";
//        jdbcTemplate.update(query, scheduleRequestDto.getTodo(), LocalDateTime.now(), id);
//    }
//    public ScheduleResponseDto find(Long id) {
//        String sql = "SELECT * FROM schedule WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, new RowMapper<ScheduleResponseDto>() {
//            @Override
//            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return new ScheduleResponseDto(
//                        rs.getLong("id"),
//                        rs.getString("name"),
//                        rs.getString("todo"),
//                        rs.getString("date")
//                );
//            }
//        }, id);
//    }
//
//    // 일정 삭제
//    public void delete(Long id) {
//        String sql = "DELETE FROM schedule WHERE id = ?";
//        jdbcTemplate.update(sql, id);
//    }
}
