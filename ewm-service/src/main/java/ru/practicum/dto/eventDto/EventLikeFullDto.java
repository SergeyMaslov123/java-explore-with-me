package ru.practicum.dto.eventDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.dto.categoriesDto.CategoriesDto;
import ru.practicum.dto.userDto.UserShotDto;
import ru.practicum.model.Location;
@Data
@AllArgsConstructor
public class EventLikeFullDto {
    private String annotation;
    private CategoriesDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Integer id;
    private UserShotDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Integer views;
    private Double rate;
}
