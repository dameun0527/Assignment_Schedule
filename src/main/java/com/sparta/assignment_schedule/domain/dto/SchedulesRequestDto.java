package com.sparta.assignment_schedule.domain.dto;

import lombok.Getter;

@Getter
public class SchedulesRequestDto {
    private String username;
    private String title;
    private String password;
    private String contents;
    private String createdAt;
    private String updatedAt;
}
