package dev.khondamir.eventmanager.location;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationEntityConverter locationEntityConverter;

    public LocationService(LocationRepository locationRepository, LocationEntityConverter locationEntityConverter) {
        this.locationRepository = locationRepository;
        this.locationEntityConverter = locationEntityConverter;
    }

    public Location createLocation(Location locationToCreate) {
        var locationToSave = locationEntityConverter.toEntity(locationToCreate);
        return locationEntityConverter.toDomain(
                locationRepository.save(locationToSave));
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll().
                stream()
                .map(locationEntityConverter::toDomain)
                .toList();
    }

    @Transactional
    public Location updateLocation(
            Long id,
            Location locationToUpdate
    ) {
        LocationEntity entity = locationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No location with id" + id));
        entity.setName(locationToUpdate.name());
        entity.setAddress(locationToUpdate.address());
        entity.setCapacity(locationToUpdate.capacity());

        return locationEntityConverter.toDomain(entity);
    }

    public Location getLocationById(Long id) {
        var foundLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No location with id" + id));
        return locationEntityConverter.toDomain(foundLocation);
    }

    public void deleteLocation(Long id) {
        LocationEntity entity = locationRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("No location with id" + id));
        locationRepository.delete(entity);
    }

    public boolean isLocationExistsById(Long id){
        return locationRepository.existsById(id);
    }
}
