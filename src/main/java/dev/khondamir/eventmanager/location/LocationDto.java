package dev.khondamir.eventmanager.location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto (
        Long id,
        @NotBlank
        String name,
        @NotBlank
        String address,
        @Min(1)
        Integer capacity
){

}
