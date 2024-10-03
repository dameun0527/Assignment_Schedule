package com.sparta.assignment_schedule.dto;

import com.sparta.assignment_schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String username;
    private String date;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.username = schedule.getUsername();
        this.date = schedule.getDate();
    }

//    public ScheduleResponseDto(Long id, String name, String todo, String date) {
//        this.id = id;
//        this.name = name;
//        this.todo = todo;
//        this.date = date;
//    }
}
