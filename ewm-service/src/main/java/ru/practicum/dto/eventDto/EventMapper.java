package ru.practicum.dto.eventDto;

import ru.practicum.dto.categoriesDto.CategoriesMapper;
import ru.practicum.dto.userDto.UserMapper;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    public static EventShotDto eventShotDtoFromEvent(Event event) {
        return new EventShotDto(
                event.getAnnotation(),
                CategoriesMapper.toCategoriesDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getId(),
                UserMapper.userShotDtoFromUser(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static Event toEventForNewEventDto(NewEventDto newEventDto) {
        return new Event(
                null,
                newEventDto.getAnnotation(),
                null,
                0,
                null,
                newEventDto.getDescription(),
                LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                null,
                newEventDto.getLocation(),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(),
                null,
                newEventDto.getTitle(),
                0,
                null);
    }

    public static EventFullDto toEventFullDtoFromEvent(Event event) {
        if (event.getPublishedOn() == null) {
            return new EventFullDto(
                    event.getAnnotation(),
                    CategoriesMapper.toCategoriesDto(event.getCategory()),
                    event.getConfirmedRequests(),
                    event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getDescription(),
                    event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getId(),
                    UserMapper.userShotDtoFromUser(event.getInitiator()),
                    event.getLocation(),
                    event.getPaid(),
                    event.getParticipantLimit(),
                    null,
                    event.getRequestModeration(),
                    event.getState().toString(),
                    event.getTitle(),
                    event.getViews()
            );
        } else {
            return new EventFullDto(
                    event.getAnnotation(),
                    CategoriesMapper.toCategoriesDto(event.getCategory()),
                    event.getConfirmedRequests(),
                    event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getDescription(),
                    event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getId(),
                    UserMapper.userShotDtoFromUser(event.getInitiator()),
                    event.getLocation(),
                    event.getPaid(),
                    event.getParticipantLimit(),
                    event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getRequestModeration(),
                    event.getState().toString(),
                    event.getTitle(),
                    event.getViews()
            );
        }
    }

    public static EventDto eventDtoFromEvent(Event event) {
        return new EventDto(
                event.getAnnotation(),
                CategoriesMapper.toCategoriesDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getId(),
                UserMapper.userShotDtoFromUser(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static EventLikeShotDto toEventLikeShotDtoFromEvent(Event event) {
        return new EventLikeShotDto(
                event.getAnnotation(),
                CategoriesMapper.toCategoriesDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getId(),
                UserMapper.userShotDtoFromUser(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews(),
                event.getRate()
        );
    }

    public static EventLikeFullDto toEventLikeFullDtoFromEvent(Event event) {
        if (event.getPublishedOn() == null) {
            return new EventLikeFullDto(
                    event.getAnnotation(),
                    CategoriesMapper.toCategoriesDto(event.getCategory()),
                    event.getConfirmedRequests(),
                    event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getDescription(),
                    event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getId(),
                    UserMapper.userShotDtoFromUser(event.getInitiator()),
                    event.getLocation(),
                    event.getPaid(),
                    event.getParticipantLimit(),
                    null,
                    event.getRequestModeration(),
                    event.getState().toString(),
                    event.getTitle(),
                    event.getViews(),
                    event.getRate()
            );
        } else {
            return new EventLikeFullDto(
                    event.getAnnotation(),
                    CategoriesMapper.toCategoriesDto(event.getCategory()),
                    event.getConfirmedRequests(),
                    event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getDescription(),
                    event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getId(),
                    UserMapper.userShotDtoFromUser(event.getInitiator()),
                    event.getLocation(),
                    event.getPaid(),
                    event.getParticipantLimit(),
                    event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    event.getRequestModeration(),
                    event.getState().toString(),
                    event.getTitle(),
                    event.getViews(),
                    event.getRate()
            );
        }
    }
}
