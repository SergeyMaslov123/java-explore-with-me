package ru.practicum.dto.requestDto;

import lombok.Value;

import java.util.List;

@Value
public class EventRequestStatusUpdateRequest {
    List<Integer> requestIds;
    String status;
}
