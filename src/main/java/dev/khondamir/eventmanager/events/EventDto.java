package dev.khondamir.eventmanager.events;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record EventDto(
        Long id,
        @NotBlank(message = "Event name must not be blank")
        String name,
        @NotNull
        OffsetDateTime date,
        @NotNull
        Integer duration,
        @NotNull
        @Min(1)
        Long cost,
        @NotNull
        @Min(value = 1)
        Integer maxPlaces,
        @NotNull
        Long locationId,
        Long ownerId,
        EventStatus status,
        Long occupiedPlaces
) {
}

