package ru.practicum.dto.eventDto;

import lombok.Value;
import ru.practicum.dto.categoriesDto.CategoriesDto;
import ru.practicum.dto.userDto.UserShotDto;


@Value
public class EventDto {
    String annotation;
    CategoriesDto category;

    Integer confirmedRequests;
    String eventDate;
    Integer id;
    UserShotDto initiator;
    Boolean paid;
    String title;
    Integer views;

}
