package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.eventDto.EventFullDto;
import ru.practicum.dto.eventDto.EventShotDto;
import ru.practicum.dto.eventDto.NewEventDto;
import ru.practicum.dto.eventDto.UpdateEventUserRequest;
import ru.practicum.dto.requestDto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.dto.requestDto.ParticipationRequestDto;
import ru.practicum.service.eventService.EventService;
import ru.practicum.service.requestService.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping("/{userId}/events")
    List<EventShotDto> getEventsByUserId(@PathVariable @Positive Integer userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventByUserIdPrivate(userId, from, size);

    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto addEvent(@PathVariable @Positive Integer userId,
                          @RequestBody @Valid NewEventDto eventDto) {
        return eventService.addEventPrivate(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    EventFullDto getEventForEventId(@PathVariable @Positive Integer userId,
                                    @PathVariable Integer eventId) {
        return eventService.getEventByUserForEventIdPrivate(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    EventFullDto updateEventForEventId(@PathVariable @Positive Integer userId,
                                       @PathVariable @Positive Integer eventId,
                                       @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateEventByUserPrivate(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getRequestForUserId(@PathVariable @Positive Integer userId,
                                                      @PathVariable @Positive Integer eventId) {
        return requestService.getRequestForUserIdEventId(userId, eventId);

    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    EventRequestStatusUpdateResult updateRequest(@PathVariable @Positive Integer userId,
                                                 @PathVariable @Positive Integer eventId,
                                                 @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return requestService.updateRequest(userId, eventId, eventRequestStatusUpdateRequest);

    }


}
