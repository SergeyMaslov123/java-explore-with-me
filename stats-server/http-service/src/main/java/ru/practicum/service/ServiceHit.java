package ru.practicum.service;

import ru.practicum.ViewStat;
import ru.practicum.HitDto;

import java.util.List;

public interface ServiceHit {
    HitDto addHit(HitDto hitDto);

    List<ViewStat> getHit(String start, String end, List<String> uris, Boolean unique);
}
