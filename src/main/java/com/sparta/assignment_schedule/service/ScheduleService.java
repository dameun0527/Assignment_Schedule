package com.sparta.assignment_schedule.service;

import com.sparta.assignment_schedule.dto.ScheduleRequestDto;
import com.sparta.assignment_schedule.dto.ScheduleResponseDto;
import com.sparta.assignment_schedule.entity.Schedule;
import com.sparta.assignment_schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Component;

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
    public List<ScheduleResponseDto> getScheduleList() {
        return scheduleRepository.findAll();
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
        Schedule schedule = scheduleRepository.findById(id);
        if (schedule != null) {
            scheduleRepository.updateSchedule(id, scheduleRequestDto);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    public Long deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id);
        // 해당 일정이 DB에 존재하는지 확인
        if (schedule != null) {
            // 해당 일정 삭제하기
            scheduleRepository.delete(id);
            return id;
        } else {
            throw new IllegalArgumentException("해당 일정은 존재하지 않습니다.");
        }
    }
}
