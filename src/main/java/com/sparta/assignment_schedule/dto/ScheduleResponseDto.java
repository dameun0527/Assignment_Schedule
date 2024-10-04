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
