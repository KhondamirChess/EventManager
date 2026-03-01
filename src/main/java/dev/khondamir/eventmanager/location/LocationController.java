package dev.khondamir.eventmanager.location;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;
    private final LocationDtoConverter locationDtoConverter;

    public LocationController(LocationService locationService, LocationDtoConverter locationDtoConverter) {
        this.locationService = locationService;
        this.locationDtoConverter = locationDtoConverter;
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @Valid @RequestBody LocationDto locationDtoToCreate
    ) {
        log.info("Post request to create location: {}", locationDtoToCreate);
        var createdLocation = locationService.createLocation(
                locationDtoConverter.toDomain(locationDtoToCreate)
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationDtoConverter.toDto(createdLocation));
    }

    @GetMapping
    public List<LocationDto> getAllLocations() {
        log.info("Get request to get all locations");
        return locationService.getAllLocations()
                .stream()
                .map(locationDtoConverter::toDto)
                .toList();
    }

    @PutMapping("/{id}")
    public LocationDto updateLocation(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody LocationDto locationDtoToUpdate
    ){
        log.info("Put request to update location: {}", locationDtoToUpdate);
        var updatedLocation = locationService.updateLocation(
                id,
                locationDtoConverter.toDomain(locationDtoToUpdate)
        );
        return locationDtoConverter.toDto(updatedLocation);
    }

    @GetMapping("/{id}")
    public LocationDto getLocationById(
            @PathVariable("id") Long id
    ){
        log.info("Get request to get location by Id: id={}", id);
        var foundLocation = locationService.getLocationById(id);
        return locationDtoConverter.toDto(foundLocation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(
            @PathVariable("id") Long id
    ){
        log.info("Delete request to delete location: {}", id);
        locationService.deleteLocation(id);
    }

}
