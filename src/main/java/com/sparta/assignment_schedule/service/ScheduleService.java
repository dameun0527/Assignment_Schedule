package com.sparta.assignment_schedule.service;

import com.sparta.assignment_schedule.dto.ScheduleRequestDto;
import com.sparta.assignment_schedule.dto.ScheduleResponseDto;
import com.sparta.assignment_schedule.entity.Schedule;
import com.sparta.assignment_schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 일정 등록
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);

        //DB 저장
        Schedule saveSchedule = scheduleRepository.save(schedule);

        // Entity -> ResponseDto
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);
        return scheduleResponseDto;
    }

    // 전체 일정 조회
    public List<ScheduleResponseDto> getScheduleList(String date, String name) {
        return scheduleRepository.findAll(date, name);
    }

    // 선택한 일정 조회
    public ScheduleResponseDto getSchedule(Long id) {
        // 해당 일정이 DB에 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(id);
        if (schedule != null) {
            return scheduleRepository.find(id);
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    // 일정 수정
    public Long updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
        // 해당 일정이 DB에 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(id); // 일정 조회

        // 일정이 없으면 예외처리
        if (schedule == null) {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }

        // 비밀번호가 null인 경우를 고려하여 비밀번호 확인
        if (schedule.getPassword() == null || !schedule.getPassword().equals(scheduleRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 수정일 갱신 (수정일만 업데이트)
        schedule.setTodo(scheduleRequestDto.getTodo());  // 할 일(todo)만 수정
        schedule.setModifiedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 업데이트된 일정 저장
        scheduleRepository.updateSchedule(id,scheduleRequestDto);  // 수정된 schedule 객체 저장
        return schedule.getId();
    }


    //일정 삭제
    public Long deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id);
        // 해당 일정이 DB에 존재하는지 확인
        if (schedule == null) {
            throw new IllegalArgumentException("해당 일정은 존재하지 않습니다.");
        }
        // 비밀번호 확인
        if (!schedule.getPassword().equals(inputPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 일정 삭제
        scheduleRepository.delete(id);
        return id;
    }
}
