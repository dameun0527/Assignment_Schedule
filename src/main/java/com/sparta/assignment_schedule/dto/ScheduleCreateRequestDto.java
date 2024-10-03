package com.sparta.assignment_schedule.dto;

import lombok.Getter;

@Getter
public class ScheduleCreateRequestDto {
    private String title;
    private String contents;
    private String username;
    private String date;
    private Long password;
}
