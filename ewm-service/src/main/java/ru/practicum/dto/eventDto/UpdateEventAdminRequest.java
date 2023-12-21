package ru.practicum.dto.eventDto;

import lombok.Value;
import ru.practicum.model.Location;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Value
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    @Positive
    Integer category;
    @Size(min = 20, max = 7000)
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    @Size(min = 3, max = 120)
    String title;
}
