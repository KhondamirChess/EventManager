package dev.khondamir.eventmanager.location;

import org.springframework.stereotype.Component;

@Component
public class LocationDtoConverter {
    public LocationDto toDto(Location location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity()
        );
    }

    public Location toDomain(LocationDto locationDto) {
        return new Location(
                locationDto.id(),
                locationDto.name(),
                locationDto.address(),
                locationDto.capacity()
        );
    }
}

