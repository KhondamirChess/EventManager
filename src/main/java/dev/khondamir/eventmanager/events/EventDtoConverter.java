package dev.khondamir.eventmanager.events;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class EventDtoConverter {
    public EventDto toDto(Event event, Long occupiedPlaces){
        return new EventDto(
                event.id(),
                event.name(),
                event.date(),
                event.duration(),
                event.cost(),
                event.maxPlaces(),
                event.locationId(),
                event.ownerId(),
                event.status(),
                occupiedPlaces
        );
    }

    public Event toDomain(EventDto eventDto){
        return new Event(
                eventDto.id(),
                eventDto.name(),
                eventDto.date(),
                eventDto.duration(),
                eventDto.cost(),
                eventDto.maxPlaces(),
                eventDto.locationId(),
                eventDto.ownerId(),
                eventDto.status()
        );
    }
}
