package ru.practicum.dto.userDto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class UserDto {
    @Email
    @NotNull
    @Size(min = 6, max = 254)
    String email;
    Integer id;
    @NotBlank
    @Size(min = 2, max = 250)
    String name;

}
