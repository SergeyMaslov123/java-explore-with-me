package ru.practicum.dto.requestDto;

import lombok.Value;


@Value
public class ParticipationRequestDto {
    String created;
    Integer event;
    Integer id;
    Integer requester;
    String status;
}
