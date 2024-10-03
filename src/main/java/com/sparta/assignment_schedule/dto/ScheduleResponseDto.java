package com.sparta.assignment_schedule.dto;

import com.sparta.assignment_schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String name;
    private String password;
    private String todo;
    private String date;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.name = schedule.getName();
        this.password = schedule.getPassword();
        this.todo = schedule.getTodo();
        this.date = schedule.getDate();
    }
}
