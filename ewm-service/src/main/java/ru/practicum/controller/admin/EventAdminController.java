package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.eventDto.EventFullDto;
import ru.practicum.dto.eventDto.UpdateEventAdminRequest;
import ru.practicum.service.eventService.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                 @RequestParam(required = false) List<String> states,
                                 @RequestParam(required = false) List<Integer> categories,
                                 @RequestParam(required = false) String rangeStart,
                                 @RequestParam(required = false) String rangeEnd,
                                 @RequestParam(defaultValue = "0") Integer from,
                                 @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    EventFullDto updateEvent(@PathVariable Integer eventId,
                             @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEventAdmin(eventId, updateEventAdminRequest);
    }

    @PatchMapping("/test/{eventId}")
    EventFullDto updateEventTest(@PathVariable Integer eventId,
                                 @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEventAdminTest(eventId, updateEventAdminRequest);
    }
}
