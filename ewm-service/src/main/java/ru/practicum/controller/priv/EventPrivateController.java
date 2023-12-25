package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.eventDto.*;
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
    @PostMapping("/test/{userId}/events")
    EventFullDto addEventTest(@PathVariable @Positive Integer userId,
                              @RequestBody @Valid NewEventDto eventDto) {
        return eventService.addEventPrivateTest(userId,eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    EventFullDto getEventForEventId(@PathVariable @Positive Integer userId,
                                    @PathVariable @Positive Integer eventId) {
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
    @PostMapping("/likes/{userId}/{eventId}")
    EventLikeFullDto addLike(@PathVariable @Positive Integer userId,
                             @PathVariable @Positive Integer eventId,
                             @RequestParam Boolean like) {
        return eventService.addLikePrivate(userId,eventId, like);
    }
    @GetMapping("/likes/{userId}/events/{eventId}")
    EventLikeFullDto getEventForUserByEventId(@PathVariable @Positive Integer userId,
                                              @PathVariable @Positive Integer eventId) {
        return eventService.getEventByIdForUserLikePrivate(eventId,userId);
    }
    @GetMapping("/likes/{userId}/events")
    List<EventLikeShotDto>  getEventsByUserLikes(@PathVariable @Positive Integer userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventsByUserLikePrivate(userId,from,size);
    }
    @DeleteMapping("/likes/{userId}/events/{eventId}")
    void deleteLike(@PathVariable @Positive Integer userId,
                    @PathVariable @Positive Integer eventId) {
        eventService.deleteLikePrivate(eventId,userId);
    }
    @PatchMapping("/likes/{userId}/events/{eventId}")
    EventLikeFullDto updateLike(@PathVariable @Positive Integer userId,
                                @PathVariable @Positive Integer eventId,
                                @RequestParam Boolean like) {
        return eventService.updateLikePrivate(userId,eventId, like);
    }
    @GetMapping("/likes/{userId}")
    List<EventLikeShotDto> getLikesByUser(@PathVariable @Positive Integer userId,
                                          @RequestParam(required = false) Boolean like,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getLikesByUserPrivate(userId,like,from,size);
    }


}
