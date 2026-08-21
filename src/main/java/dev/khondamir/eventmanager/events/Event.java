package dev.khondamir.eventmanager.events;

import java.time.OffsetDateTime;

public record Event(
        Long id,
        String name,
        OffsetDateTime date,
        Integer duration,
        Long cost,
        Integer maxPlaces,
        Long locationId,
        Long ownerId,
        EventStatus status
) {}