package dev.khondamir.eventmanager.location;

import org.springframework.stereotype.Component;

@Component
public class LocationEntityConverter {
    public LocationEntity toEntity(Location location) {
        if (location.id() == null){
            return new LocationEntity(
                    location.name(),
                    location.address(),
                    location.capacity()
            );
        }else {
            var entity = new LocationEntity();
            entity.setId(location.id());
            entity.setName(location.name());
            entity.setAddress(location.address());
            entity.setCapacity(location.capacity());
            return entity;
        }
    }

    public Location toDomain(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity()
        );
    }
}
