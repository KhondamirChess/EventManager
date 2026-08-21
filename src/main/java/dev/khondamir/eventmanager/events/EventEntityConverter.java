package dev.khondamir.eventmanager.events;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class EventEntityConverter {
    public EventEntity toEntity(Event event) {
        return new EventEntity(
                event.id(),
                event.name(),
                event.date(),
                event.duration(),
                event.cost(),
                event.maxPlaces(),
                event.locationId(),
                event.ownerId(),
                event.status()
        );
    }

    public Event toDomain(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getEventName(),
                eventEntity.getEventdate(),
                eventEntity.getDuration(),
                eventEntity.getCost(),
                eventEntity.getMaxPlaces(),
                eventEntity.getLocationId(),
                eventEntity.getOwnerId(),
                eventEntity.getStatus()
        );
    }
}
