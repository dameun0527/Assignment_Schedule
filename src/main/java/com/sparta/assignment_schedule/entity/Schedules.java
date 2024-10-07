package com.sparta.assignment_schedule.entity;

import com.sparta.assignment_schedule.domain.dto.SchedulesRequestDto;
import com.sparta.assignment_schedule.domain.dto.SchedulesResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class Schedules {
    private Long id;
    private String username;
    private String title;
    private String password;
    private String contents;
    private String createdAt;
    private String updatedAt;

    public static Schedules from(SchedulesRequestDto requestDto) {
        Schedules schedules = new Schedules();
        schedules.init(requestDto);
        return schedules;
    }

    // 오버로딩!!!: 같은 이름의 메서드를 사용하고 싶을 때
    public static Schedules from(ResultSet rs) throws SQLException {
        Schedules schedules = new Schedules();
        schedules.init(rs);
        return schedules;

    }

    private void init(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.username = rs.getString("username");
        this.title = rs.getString("title");
        this.password = rs.getString("password");
        this.contents = rs.getString("contents");
        this.createdAt = rs.getString("created_at");
        this.updatedAt = rs.getString("updated_at");

    }

    public void init(SchedulesRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.title = requestDto.getTitle();
        this.password = requestDto.getPassword();
        this.contents = requestDto.getContents();
        this.createdAt = requestDto.getCreatedAt();
        this.updatedAt = requestDto.getUpdatedAt();
    }

    public SchedulesResponseDto to() {
        return new SchedulesResponseDto(this.id, this.username, this.title, this.contents, this.createdAt, this.updatedAt);
    }
}
