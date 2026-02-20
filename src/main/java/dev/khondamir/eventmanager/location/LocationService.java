package dev.khondamir.eventmanager.location;

import jakarta.persistence.EntityNotFoundException;
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

    public Location updateLocation(
            Long id,
            Location locationToUpdate
    ) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("No location with id" + id);
        }
        locationRepository.updateLocation(
                id,
                locationToUpdate.name(),
                locationToUpdate.address(),
                locationToUpdate.capacity()
        );
        var updatedLocation = locationEntityConverter.toDomain(
                locationRepository.findById(id).orElseThrow());

        return updatedLocation;
    }

    public Location getLocationById(Long id) {
        var foundLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No location with id" + id));
        return locationEntityConverter.toDomain(foundLocation);


    }

    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("No location with id" + id);
        }
        locationRepository.deleteById(id);
    }
}
