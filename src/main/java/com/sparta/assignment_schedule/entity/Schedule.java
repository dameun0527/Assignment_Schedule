package com.sparta.assignment_schedule.entity;

import com.sparta.assignment_schedule.domain.dto.ScheduleRequestDto;
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
