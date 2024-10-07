package com.sparta.assignment_schedule.domain.controller;

import com.sparta.assignment_schedule.domain.dto.SchedulesRequestDto;
import com.sparta.assignment_schedule.domain.dto.SchedulesResponseDto;
import com.sparta.assignment_schedule.domain.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class SchedulesController {
    // Service와 Controller 연결
    private final SchedulesService schedulesService;

//    public SchedulesController(SchedulesService schedulesService) {
//        this.schedulesService = schedulesService;
//    }

    // 일정 생성
    /* json {"title": "할일",
             "name": "작성자명",
             "password": "비밀번호",
             "contents": "내용",
             "date": "2024-10-01"} */

    @PostMapping()
    public ResponseEntity<SchedulesResponseDto> createSchedules(@RequestBody SchedulesRequestDto schedulesRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(schedulesService.createSchedules(schedulesRequestDto));
    }

    // 전체 일정 조회
    @GetMapping()
    public ResponseEntity<List<SchedulesResponseDto>> getSchedulesList(@RequestParam String userName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(schedulesService.getSchedulesList(userName));
    }

    // 선택 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<SchedulesResponseDto> getSchedules(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(schedulesService.getSchedules(id));
    }

    // 선택 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSchedules(
            @PathVariable Long id,
            @RequestBody SchedulesRequestDto requestDto
    ) {
        schedulesService.updateSchedules(id, requestDto);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedules(
            @PathVariable Long id,
            @RequestBody SchedulesRequestDto requestDto
    ) {
        schedulesService.deleteSchedules(id, requestDto);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}