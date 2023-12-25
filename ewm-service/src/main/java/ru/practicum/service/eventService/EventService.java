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
    EventFullDto addEventPrivateTest(Integer id, NewEventDto newEventDto);

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
    EventFullDto updateEventAdminTest(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

    EventLikeFullDto addLikePrivate(Integer userId, Integer eventId, Boolean like);

    EventLikeFullDto getEventByIdForUserLikePrivate(Integer eventId, Integer userId);
    List<EventLikeShotDto> getEventsByUserLikePrivate(Integer userId, Integer from, Integer size);
    void deleteLikePrivate(Integer eventId, Integer userId);
    List<EventLikeShotDto> getLikesByUserPrivate(Integer userId, Boolean like,Integer from, Integer size);
    EventLikeFullDto updateLikePrivate(Integer userId, Integer eventId, Boolean like);
    EventLikeFullDto getEventLikePublic(Integer eventId,HttpServletRequest request);
    List<EventLikeShotDto> getEventsLikePublic(String text,
                                         List<Integer> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         Integer from,
                                         Integer size,
                                         HttpServletRequest request);



}
