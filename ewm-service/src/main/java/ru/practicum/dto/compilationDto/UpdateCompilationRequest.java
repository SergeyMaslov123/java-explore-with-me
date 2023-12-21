package ru.practicum.dto.compilationDto;

import lombok.Value;

import javax.validation.constraints.Size;
import java.util.Set;

@Value
public class UpdateCompilationRequest {
    Set<Integer> events;
    Boolean pinned;
    @Size(min = 1, max = 50)
    String title;
}
