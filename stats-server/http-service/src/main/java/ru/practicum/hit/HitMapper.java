package ru.practicum.hit;

import ru.practicum.ViewStat;
import ru.practicum.HitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    public static Hit toHitFromDto(HitDto hitDto) {
        return new Hit(null,
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                LocalDateTime.parse(hitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    }

    public static HitDto toHitDtoFromHit(Hit hit) {
        return new HitDto(hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public static ViewStat toDtoAnswerFromHit(Hit hit) {
        return new ViewStat(hit.getApp(), hit.getUri(), null);
    }
}