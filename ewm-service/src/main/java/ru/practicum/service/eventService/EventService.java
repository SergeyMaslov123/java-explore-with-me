package ru.practicum.service.eventService;

import ru.practicum.dto.eventDto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventDto> getEventForPublicAll(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        Integer from,
                                        Integer size,
                                        HttpServletRequest request);

    EventFullDto getEventForIdPublic(Integer id, HttpServletRequest request);

    List<EventShotDto> getEventByUserIdPrivate(Integer id, Integer from, Integer size);

    EventFullDto addEventPrivate(Integer id, NewEventDto newEventDto);

    EventFullDto getEventByUserForEventIdPrivate(Integer userId, Integer eventId);

    EventFullDto updateEventByUserPrivate(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getEventAdmin(List<Integer> users,
                                     List<String> states,
                                     List<Integer> categories,
                                     String rangeStart,
                                     String rangeEnd,
                                     Integer from,
                                     Integer size);

    EventFullDto updateEventAdmin(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

}
