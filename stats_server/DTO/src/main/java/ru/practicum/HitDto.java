package ru.practicum;

import lombok.Value;

@Value
public class HitDto {
    String app;
    String uri;
    String ip;
    String timestamp;
}
