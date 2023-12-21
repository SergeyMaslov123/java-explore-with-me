package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.requestDto.ParticipationRequestDto;
import ru.practicum.service.requestService.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    List<ParticipationRequestDto> getRequestForUser(@PathVariable Integer userId) {
        return requestService.getRequestFroUserId(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto addRequest(@PathVariable Integer userId,
                                       @RequestParam Integer eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto cancelRequest(@PathVariable Integer userId,
                                          @PathVariable Integer requestId) {
        return requestService.cancelRequest(userId, requestId);

    }

}
