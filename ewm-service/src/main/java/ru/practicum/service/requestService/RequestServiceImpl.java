package ru.practicum.service.requestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.requestDto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.dto.requestDto.ParticipationRequestDto;
import ru.practicum.dto.requestDto.RequestMapper;
import ru.practicum.exception.ConflictEx;
import ru.practicum.exception.EntityNotFoundEx;
import ru.practicum.exception.ValidationEx;
import ru.practicum.model.*;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequestFroUserId(Integer userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addRequest(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("users not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("users not found"));
        if (event.getInitiator().equals(user) ||
                !event.getState().equals(State.PUBLISHED) ||
                (event.getParticipantLimit() <= event.getConfirmedRequests() && event.getParticipantLimit() != 0)) {
            throw new ConflictEx("user event ");
        }
        if (requestRepository.existsByRequester_idAndEvent_id(userId, eventId)) {
            throw new ConflictEx("request ");
        }
        Request request = new Request(null, LocalDateTime.now(), event, user, StatusRequest.PENDING);
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            request.setStatusRequest(StatusRequest.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("users not found"));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundEx("users not found"));
        if (request.getRequester().equals(user)) {
            request.setStatusRequest(StatusRequest.CANCELED);
            return RequestMapper.toRequestDto(requestRepository.save(request));
        } else {
            throw new ValidationEx("user not creator");
        }
    }

    @Override
    public List<ParticipationRequestDto> getRequestForUserIdEventId(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("users not found "));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("users not found "));
        if (event.getInitiator().equals(user)) {
            return requestRepository.findAllByEvent_id(eventId)
                    .stream()
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else {
            throw new ValidationEx("user not event");
        }
    }

    @Override
    public EventRequestStatusUpdateResult updateRequest(Integer userId, Integer eventId, EventRequestStatusUpdateRequest requestUpdate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("users not found "));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundEx("users not found "));
        List<Request> requests = requestRepository.findAllByIdIn(requestUpdate.getRequestIds());
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (event.getParticipantLimit() == 0 || event.getRequestModeration().equals(false)) {
            requests.forEach(request -> {
                request.setStatusRequest(StatusRequest.CONFIRMED);
                requestRepository.save(request);
                confirmedRequests.add(RequestMapper.toRequestDto(request));
                event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());
                eventRepository.save(event);
            });
        } else {
            if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                throw new ConflictEx("status conflict");
            } else {
                for (Request request : requests) {
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        request.setStatusRequest(StatusRequest.valueOf(requestUpdate.getStatus()));
                        requestRepository.save(request);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        eventRepository.save(event);
                        if (requestUpdate.getStatus().equals("CONFIRMED")) {
                            confirmedRequests.add(RequestMapper.toRequestDto(request));
                        } else {
                            rejectedRequests.add(RequestMapper.toRequestDto(request));
                        }
                    } else {
                        request.setStatusRequest(StatusRequest.REJECTED);
                        requestRepository.save(request);
                        rejectedRequests.add(RequestMapper.toRequestDto(request));
                    }
                }
            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
