package com.sparta.assignment_schedule.domain.service;

import com.sparta.assignment_schedule.domain.dto.SchedulesRequestDto;
import com.sparta.assignment_schedule.domain.dto.SchedulesResponseDto;
import com.sparta.assignment_schedule.domain.repository.SchedulesRepository;
import com.sparta.assignment_schedule.entity.Schedules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SchedulesService {

    private final SchedulesRepository schedulesRepository;

    /*
     * schedulesRequestDto -> DB
     * DTO <-> DAO Entity(객체) <-> DB
     */
    // request -> Schedules
    // Schedules -> DB
    public SchedulesResponseDto createSchedules(SchedulesRequestDto schedulesRequestDto) {
        Schedules schedules = schedulesRepository.save(Schedules.from(schedulesRequestDto));
        return schedules.to();
    }

    public List<SchedulesResponseDto> getSchedulesList(String userName) {
        return schedulesRepository.findAll(userName);
    }

    public SchedulesResponseDto getSchedules(Long id) {
        Schedules schedules = schedulesRepository.findById(id);
        return schedules.to();
    }

    public void updateSchedules(Long id, SchedulesRequestDto requestDto) {
        Schedules schedules = schedulesRepository.findById(id);
        // 선택한 일정이 존재하는지?
        if(schedules == null) {
            throw new IllegalArgumentException("해당 일정을 찾을 수 없습니다.");
        }
        // 비밀번호가 일치하는지?
        if (!Objects.equals(schedules.getPassword(), requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        schedulesRepository.update(id, requestDto);
    }

    public void deleteSchedules(Long id, SchedulesRequestDto requestDto) {
        Schedules schedules = schedulesRepository.findById(id);
        // 선택한 일정이 존재하는지?
        if(schedules == null) {
            throw new IllegalArgumentException("해당 일정을 찾을 수 없습니다.");
        }
        // 비밀번호가 일치하는지?
        if (!Objects.equals(schedules.getPassword(), requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        schedulesRepository.delete(id) {

        }
    }
}
