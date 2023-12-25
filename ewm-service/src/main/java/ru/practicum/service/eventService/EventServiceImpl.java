package ru.practicum.service.eventService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatClient;
import ru.practicum.dto.eventDto.*;
import ru.practicum.exception.ConflictEx;
import ru.practicum.exception.EntityNotFoundEx;
import ru.practicum.exception.ValidationEx;
import ru.practicum.model.*;
import ru.practicum.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final StatClient statClient;
    private final RequestRepository requestRepository;
    private final LikeRepository likeRepository;

    @Override
    public List<EventDto> getEventForPublicAll(
            String text,
            List<Integer> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size,
            HttpServletRequest request
    ) {
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        Sort sort1;
        Pageable pageable;
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                sort1 = Sort.by(Sort.Direction.ASC, "eventDate");
                pageable = PageRequest.of(from > 0 ? from / size : 0, size, sort1);
            } else if (sort.equals("VIEWS")) {
                sort1 = Sort.by(Sort.Direction.ASC, "views");
                pageable = PageRequest.of(from > 0 ? from / size : 0, size, sort1);
            } else {
                throw new ValidationEx("sort not");
            }
        } else {
            pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        }
        HitDto hitDto = new HitDto(
                "ewm service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        statClient.addHit(hitDto);
        if (onlyAvailable) {
            if (rangeStart != null && rangeEnd != null) {
                LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (start.isAfter(end)) {
                    throw new ValidationEx("start after end");
                }
                return eventRepository.findAllEventOnlyAvailableWithDate(
                                text,
                                paid,
                                categories,
                                start,
                                end,
                                pageable
                        )
                        .stream()
                        .map(EventMapper::eventDtoFromEvent)
                        .collect(Collectors.toList());
            } else {
                return eventRepository.findAllEventOnlyAvailableNotDate(
                                text,
                                paid,
                                categories,
                                LocalDateTime.now(),
                                pageable
                        )
                        .stream()
                        .map(EventMapper::eventDtoFromEvent)
                        .collect(Collectors.toList());
            }
        } else {
            if (rangeStart != null && rangeEnd != null) {
                LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (start.isAfter(end)) {
                    throw new ValidationEx("start after end");
                }
                return eventRepository.findAllEventWithDate(
                                text,
                                paid,
                                categories,
                                start,
                                end,
                                pageable
                        )
                        .stream()
                        .map(EventMapper::eventDtoFromEvent)
                        .collect(Collectors.toList());
            } else {
                return eventRepository.findAllEventNotDate(
                                text,
                                paid,
                                categories,
                                LocalDateTime.now(),
                                pageable
                        )
                        .stream()
                        .map(EventMapper::eventDtoFromEvent)
                        .collect(Collectors.toList());
            }
        }
    }

    @Override
    public EventFullDto getEventForIdPublic(Integer id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundEx("event not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EntityNotFoundEx("event not published");
        }
        event.setViews(event.getViews() + 1);
        HitDto hitDto = new HitDto(
                "ewm service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        statClient.addHit(hitDto);
        return EventMapper.toEventFullDtoFromEvent(event);

    }

    @Override
    public List<EventShotDto> getEventByUserIdPrivate(Integer userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("user not found"));
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        return eventRepository.findAllByInitiator_id(userId, pageable).stream()
                .map(EventMapper::eventShotDtoFromEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEventPrivate(Integer userId, NewEventDto newEventDto) {
        if (LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .isBefore(LocalDateTime.now().plusMinutes(120))) {
            throw new ValidationEx("time! < 2");// 409 ex
        }
        Event event = EventMapper.toEventForNewEventDto(newEventDto);
        Categories categories = categoriesRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundEx("category not found"));
        event.setCategory(categories);
        event.setCreatedOn(LocalDateTime.now());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundEx("user not found"));
        event.setInitiator(user);
        event.setState(State.PENDING);
        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        locationRepository.save(newEventDto.getLocation());

        return EventMapper.toEventFullDtoFromEvent(eventRepository.save(event));
    }

    @Override
    public EventFullDto addEventPrivateTest(Integer userId, NewEventDto newEventDto) {
        Event event = EventMapper.toEventForNewEventDto(newEventDto);
        Categories categories = categoriesRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundEx("category not found"));
        event.setCategory(categories);
        event.setCreatedOn(LocalDateTime.now().minusDays(5));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundEx("user not found"));
        event.setInitiator(user);
        event.setState(State.PENDING);
        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        locationRepository.save(newEventDto.getLocation());

        return EventMapper.toEventFullDtoFromEvent(eventRepository.save(event));

    }

    @Override
    public EventFullDto getEventByUserForEventIdPrivate(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundEx("user not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("not found event"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundEx("user not initiator");
        }
        return EventMapper.toEventFullDtoFromEvent(event);
    }


    @Override
    public EventFullDto updateEventByUserPrivate(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundEx("user not found"));
        Event oldEvent = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("event not found"));
        if (oldEvent.getState().equals(State.PUBLISHED)) {
            throw new ConflictEx("status publish");
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            oldEvent.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            Categories categories = categoriesRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new EntityNotFoundEx("category not found"));
            oldEvent.setCategory(categories);
        }
        if (updateEventUserRequest.getDescription() != null) {
            oldEvent.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .isBefore(LocalDateTime.now().plusMinutes(120))) {
                throw new ValidationEx("date not valid");
            }
            oldEvent.setEventDate(LocalDateTime.parse(
                    updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            ));
        }
        if (updateEventUserRequest.getLocation() != null) {
            oldEvent.setLocation(updateEventUserRequest.getLocation());
            locationRepository.save(updateEventUserRequest.getLocation());
        }
        if (updateEventUserRequest.getPaid() != null) {
            oldEvent.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            StateAction newState = StateAction.valueOf(updateEventUserRequest.getStateAction());
            if (newState.equals(StateAction.SEND_TO_REVIEW)) {
                oldEvent.setState(State.PENDING);
            }
            if (newState.equals(StateAction.CANCEL_REVIEW)) {

                oldEvent.setState(State.CANCELED);
            }
        }
        if (updateEventUserRequest.getTitle() != null) {
            oldEvent.setTitle(updateEventUserRequest.getTitle());
        }
        return EventMapper.toEventFullDtoFromEvent(eventRepository.save(oldEvent));
    }

    @Override
    public List<EventFullDto> getEventAdmin(List<Integer> users, List<String> states, List<Integer> categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        List<State> statesList = null;
        if (states != null) {
            statesList = states.stream().map(State::valueOf).collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        if (rangeStart != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return eventRepository.findAllByAdmin(users, statesList, categories, start, end, pageable).stream()
                    .map(EventMapper::toEventFullDtoFromEvent)
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAllByAdminNotDate(users, statesList, categories, LocalDateTime.now(), pageable).stream()
                    .map(EventMapper::toEventFullDtoFromEvent)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto updateEventAdmin(Integer eventId, UpdateEventAdminRequest updateEvent) {
        Event oldEvent = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("event not found"));
        if (updateEvent.getEventDate() != null) {
            if (oldEvent.getPublishedOn() != null) {
                if (LocalDateTime.parse(updateEvent.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        .isBefore(oldEvent.getPublishedOn().minusMinutes(60))) {
                    throw new ValidationEx("409 time error ");
                }
            }
            if (LocalDateTime.parse(updateEvent.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .isBefore(LocalDateTime.now().plusMinutes(120))) {
                throw new ValidationEx("409 time error ");
            }
            oldEvent.setEventDate(LocalDateTime.parse(updateEvent.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (updateEvent.getAnnotation() != null) {
            oldEvent.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Categories categories = categoriesRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new EntityNotFoundEx("category not found"));
            oldEvent.setCategory(categories);
        }
        if (updateEvent.getLocation() != null) {
            locationRepository.save(updateEvent.getLocation());
            oldEvent.setLocation(updateEvent.getLocation());

        }
        if (updateEvent.getPaid() != null) {
            oldEvent.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(updateEvent.getStateAction());
            if (oldEvent.getState().equals(State.PENDING) && stateAction.equals(StateAction.PUBLISH_EVENT)) {
                oldEvent.setState(State.PUBLISHED);
                oldEvent.setPublishedOn(LocalDateTime.now());
            } else if (oldEvent.getState().equals(State.PENDING) && stateAction.equals(StateAction.REJECT_EVENT)) {
                oldEvent.setState(State.CANCELED);
            } else {
                throw new ConflictEx("event publish");
            }
        }
        if (updateEvent.getTitle() != null) {
            oldEvent.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getDescription() != null) {
            oldEvent.setDescription(updateEvent.getDescription());
        }
        return EventMapper.toEventFullDtoFromEvent(eventRepository.save(oldEvent));
    }

    @Override
    public EventFullDto updateEventAdminTest(Integer eventId, UpdateEventAdminRequest updateEvent) {
        Event oldEvent = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("event not found"));
        if (updateEvent.getEventDate() != null) {

            oldEvent.setEventDate(LocalDateTime.parse(updateEvent.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (updateEvent.getAnnotation() != null) {
            oldEvent.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Categories categories = categoriesRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new EntityNotFoundEx("category not found"));
            oldEvent.setCategory(categories);
        }
        if (updateEvent.getLocation() != null) {
            locationRepository.save(updateEvent.getLocation());
            oldEvent.setLocation(updateEvent.getLocation());

        }
        if (updateEvent.getPaid() != null) {
            oldEvent.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(updateEvent.getStateAction());
            if (oldEvent.getState().equals(State.PENDING) && stateAction.equals(StateAction.PUBLISH_EVENT)) {
                oldEvent.setState(State.PUBLISHED);
                oldEvent.setPublishedOn(LocalDateTime.now());
            } else if (oldEvent.getState().equals(State.PENDING) && stateAction.equals(StateAction.REJECT_EVENT)) {
                oldEvent.setState(State.CANCELED);
            } else {
                throw new ConflictEx("event publish");
            }
        }
        if (updateEvent.getTitle() != null) {
            oldEvent.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getDescription() != null) {
            oldEvent.setDescription(updateEvent.getDescription());
        }
        return EventMapper.toEventFullDtoFromEvent(eventRepository.save(oldEvent));
    }

    @Override
    public EventLikeFullDto addLikePrivate(Integer userId, Integer eventId, Boolean like) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("user not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("event not found"));
        Request request = requestRepository.findByRequester_idAndEvent_id(userId, eventId).orElseThrow(() ->
                new EntityNotFoundEx("User not have request fo event"));
        if (event.getInitiator().equals(user) || event.getEventDate().isAfter(LocalDateTime.now()) ||
                !request.getStatusRequest().equals(StatusRequest.CONFIRMED)
        ) {
            throw new ValidationEx("validate ex");
        }
        Like newLike = new Like(new LikePk(userId, eventId), like);
        likeRepository.save(newLike);
        updateRate(event);
        eventRepository.save(event);
        return EventMapper.toEventLikeFullDtoFromEvent(event);
    }

    @Override
    public EventLikeFullDto getEventByIdForUserLikePrivate(Integer eventId, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundEx("user not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("not found event"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundEx("user not initiator");
        }
        return EventMapper.toEventLikeFullDtoFromEvent(event);
    }

    @Override
    public List<EventLikeShotDto> getEventsByUserLikePrivate(Integer userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("user not found"));
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        return eventRepository.findAllByInitiator_id(userId, pageable).stream()
                .map(EventMapper::toEventLikeShotDtoFromEvent)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLikePrivate(Integer eventId, Integer userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("not event"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("not user"));
        LikePk likePk = new LikePk(userId, eventId);
        Like like = likeRepository.findById(likePk).orElseThrow(() -> new EntityNotFoundEx("not like"));
        likeRepository.deleteById(likePk);
        updateRate(event);
        eventRepository.save(event);
    }

    @Override
    public List<EventLikeShotDto> getLikesByUserPrivate(Integer userId, Boolean like, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("not user"));
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Like> likes = likeRepository.findLikeByUser(userId, like, pageable).toList();
        List<Integer> idsEvent = likes.stream()
                .map(like1 -> like1.getId().getEvent_id())
                .collect(Collectors.toList());
        return eventRepository.findAllByIdInOrderByRateDesc(idsEvent).stream()
                .map(EventMapper::toEventLikeShotDtoFromEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventLikeFullDto updateLikePrivate(Integer userId, Integer eventId, Boolean like) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("not event"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("not user"));
        Like likeEvent = likeRepository.findById(new LikePk(userId, eventId)).orElseThrow(() -> new EntityNotFoundEx("not like"));
        likeEvent.setLikeEvent(like);
        likeRepository.save(likeEvent);
        updateRate(event);
        return EventMapper.toEventLikeFullDtoFromEvent(event);
    }

    @Override
    public EventLikeFullDto getEventLikePublic(Integer eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("event not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EntityNotFoundEx("event not published");
        }
        event.setViews(event.getViews() + 1);
        HitDto hitDto = new HitDto(
                "ewm service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        statClient.addHit(hitDto);
        return EventMapper.toEventLikeFullDtoFromEvent(event);
    }

    @Override
    public List<EventLikeShotDto> getEventsLikePublic(String text,
                                                      List<Integer> categories,
                                                      Boolean paid,
                                                      String rangeStart,
                                                      String rangeEnd,
                                                      Boolean onlyAvailable,
                                                      String sort,
                                                      Integer from,
                                                      Integer size,
                                                      HttpServletRequest request) {
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        Sort sort1;
        Pageable pageable;
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                sort1 = Sort.by(Sort.Direction.ASC, "eventDate");
                pageable = PageRequest.of(from > 0 ? from / size : 0, size, sort1);
            } else if (sort.equals("VIEWS")) {
                sort1 = Sort.by(Sort.Direction.ASC, "views");
                pageable = PageRequest.of(from > 0 ? from / size : 0, size, sort1);
            } else {
                throw new ValidationEx("sort not");
            }
        } else {
            pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        }
        HitDto hitDto = new HitDto(
                "ewm service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        statClient.addHit(hitDto);
        if (onlyAvailable) {
            if (rangeStart != null && rangeEnd != null) {
                LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (start.isAfter(end)) {
                    throw new ValidationEx("start after end");
                }
                return eventRepository.findAllEventOnlyAvailableWithDateLike(
                                text,
                                paid,
                                categories,
                                start,
                                end,
                                pageable
                        )
                        .stream()
                        .map(EventMapper::toEventLikeShotDtoFromEvent)
                        .collect(Collectors.toList());
            } else {
                return eventRepository.findAllEventOnlyAvailableNotDateLike(
                                text,
                                paid,
                                categories,
                                LocalDateTime.now(),
                                pageable
                        )
                        .stream()
                        .map(EventMapper::toEventLikeShotDtoFromEvent)
                        .collect(Collectors.toList());
            }
        } else {
            if (rangeStart != null && rangeEnd != null) {
                LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (start.isAfter(end)) {
                    throw new ValidationEx("start after end");
                }
                return eventRepository.findAllEventWithDateLike(
                                text,
                                paid,
                                categories,
                                start,
                                end,
                                pageable
                        )
                        .stream()
                        .map(EventMapper::toEventLikeShotDtoFromEvent)
                        .collect(Collectors.toList());
            } else {
                return eventRepository.findAllEventNotDateLike(
                                text,
                                paid,
                                categories,
                                LocalDateTime.now(),
                                pageable
                        )
                        .stream()
                        .map(EventMapper::toEventLikeShotDtoFromEvent)
                        .collect(Collectors.toList());
            }
        }
    }

    private void updateRate(Event event) {
        Double rate = (likeRepository.countByLikePk_event_idAndLikeEvent(event.getId(), true) /
                likeRepository.countByLikePk_event_id(event.getId())) * 10;
        event.setRate(rate);
        eventRepository.save(event);
    }

}
