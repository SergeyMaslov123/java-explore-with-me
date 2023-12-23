package ru.practicum.dto.compilationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.dto.eventDto.EventShotDto;

import java.util.Set;

@Data
@AllArgsConstructor
public class CompilationDto {
    private Set<EventShotDto> events;
    private Integer id;
    private Boolean pinned;
    private String title;
}
