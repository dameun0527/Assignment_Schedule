package com.sparta.assignment_schedule.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchedulesResponseDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
    private String createdAt;
    private String updatedAt;
}
