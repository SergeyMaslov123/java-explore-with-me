package ru.practicum.dto.categoriesDto;

import lombok.Value;

import javax.validation.constraints.Size;

@Value
public class CategoriesDto {
    Integer id;
    @Size(min = 1, max = 50)
    String name;
}
