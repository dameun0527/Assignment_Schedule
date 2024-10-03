package com.sparta.assignment_schedule.entity;

import com.sparta.assignment_schedule.dto.ScheduleCreateRequestDto;
import com.sparta.assignment_schedule.dto.ScheduleUpdateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    private Long id;
    private String username;
    private Long password;
    private String title;
    private String contents;
    private LocalDateTime date;
    private LocalDateTime modifiedDate;

    public Schedule(ScheduleCreateRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.password = requestDto.getPassword();
        LocalDateTime now = LocalDateTime.now();
        this.date = now;
        this.modifiedDate = now;
    }

    public void update(ScheduleUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = requestDto.getUsername();
        this.modifiedDate = LocalDateTime.now();
    }
}
