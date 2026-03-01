package dev.khondamir.eventmanager.location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto (
        Long id,
        @NotBlank(message = "Location name must not be blank")
        String name,
        @NotBlank(message = "Location address must not be blank")
        String address,
        @NotNull(message = "Location capacity must not be null")
        @Min(value = 1, message = "Location capacity must be at least 1")
        Integer capacity
){

}
