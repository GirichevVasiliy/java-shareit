package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private Long id;
    @NotNull
    @Email(regexp = "^[a-zA-Z0-9.]+[^._]@[^.\\-_]+[a-zA-Z0-9.]+[a-zA-Z0-9]$", message = "Email введен некорректно")
    private String email;
    @NotNull
    @NotBlank(message = "Поле Name не должно быть пустым")
    private String name;

    public UserDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}