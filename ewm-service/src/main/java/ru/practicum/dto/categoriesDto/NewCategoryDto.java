package ru.practicum.dto.categoriesDto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class NewCategoryDto {
    Integer id;
    @NotBlank
    @Size(min = 1, max = 50)
    String name;
}
