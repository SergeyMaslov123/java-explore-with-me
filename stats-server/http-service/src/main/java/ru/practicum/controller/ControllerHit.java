package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ViewStat;
import ru.practicum.HitDto;
import ru.practicum.service.ServiceHit;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ControllerHit {
    private final ServiceHit serviceHit;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto addHit(@RequestBody HitDto hitDto) {
        return serviceHit.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStat> getHit(@RequestParam String start,
                                 @RequestParam String end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(defaultValue = "false") Boolean unique) {
        return serviceHit.getHit(start, end, uris, unique);
    }
}
