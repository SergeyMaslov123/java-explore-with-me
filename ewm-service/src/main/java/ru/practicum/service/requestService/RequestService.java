package ru.practicum.service.requestService;

import ru.practicum.dto.requestDto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.dto.requestDto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequestFroUserId(Integer userId);

    ParticipationRequestDto addRequest(Integer userId, Integer eventId);

    ParticipationRequestDto cancelRequest(Integer userId, Integer requestId);

    List<ParticipationRequestDto> getRequestForUserIdEventId(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateRequest(Integer userId, Integer eventId, EventRequestStatusUpdateRequest eventUpdate);
}
