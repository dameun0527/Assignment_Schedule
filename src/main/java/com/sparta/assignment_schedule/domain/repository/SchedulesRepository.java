package com.sparta.assignment_schedule.domain.repository;

import com.sparta.assignment_schedule.domain.dto.SchedulesRequestDto;
import com.sparta.assignment_schedule.domain.dto.SchedulesResponseDto;
import com.sparta.assignment_schedule.entity.Schedules;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SchedulesRepository {

    private final JdbcTemplate jdbcTemplate;

    public Schedules save(Schedules schedules) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO schedules(username, title, password, contents, createdAt) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, schedules.getUsername());
            preparedStatement.setString(2, schedules.getTitle());
            preparedStatement.setString(3, schedules.getPassword());
            preparedStatement.setString(4, schedules.getContents());
            preparedStatement.setString(4, schedules.getCreatedAt());
            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        schedules.setId(id);
        return schedules;
    }

    public List<SchedulesResponseDto> findAll(String userName) {
        String sql = "SELECT * FROM schedules WHERE username = ?";
        return jdbcTemplate.query(sql,(rs, rowNum) -> {

            Long id = rs.getLong("id");
            String username = rs.getString("username");
            String title = rs.getString("title");
            String contents = rs.getString("contents");
            String createdAt = rs.getString("created_at");
            String updatedAt = rs.getString("updated_at");
            return new SchedulesResponseDto(id, username, title, contents, createdAt, updatedAt);
        }, userName);
    }

    public Schedules findById(Long id) {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        return jdbcTemplate.query(sql, rs -> {
            if(rs.next()) {
                return Schedules.from(rs);
            } else {
                return null;
            }
        }, id);
    }

    public void update(Long id, SchedulesRequestDto requestDto) {
        String sql = "UPDATE todo SET contents =?, username = ?, updated at = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getContents(), requestDto.getUsername(), requestDto.getUpdatedAt(), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM schedules WHERE id = ?";
        jdbcTemplate.update(sql,id);
    }
}

