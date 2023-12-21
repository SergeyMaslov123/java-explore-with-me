package ru.practicum.dto.requestDto;

import ru.practicum.model.Request;

import java.time.format.DateTimeFormatter;

public class RequestMapper {
    public static ParticipationRequestDto toRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatusRequest().toString()
        );
    }
}
