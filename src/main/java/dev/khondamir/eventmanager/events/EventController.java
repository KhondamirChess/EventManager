package dev.khondamir.eventmanager.events;

import dev.khondamir.eventmanager.users.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final EventDtoConverter eventDtoConverter;

    public EventController(EventService eventService, EventDtoConverter eventDtoConverter) {
        this.eventService = eventService;
        this.eventDtoConverter = eventDtoConverter;
    }
    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventDto eventToCreate,
            @AuthenticationPrincipal User currentUser
    ) {
        log.info("Creating event: {}", eventToCreate);
        var createdEvent = eventService.createEvent(
                eventDtoConverter.toDomain(eventToCreate),
                currentUser
        );
        var occupiedPlaces = eventService.getOccupiedPlaces(createdEvent.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventDtoConverter.toDto(createdEvent, occupiedPlaces));
    }
    @GetMapping("/{id}")
    public EventDto findById(
            @PathVariable("id") Long id
    ){
        log.info("Finding event: {}", id);
        var foundEvent = eventService.findById(id);
        var occupiedPlaces = eventService.getOccupiedPlaces(foundEvent.id());
        return eventDtoConverter.toDto(foundEvent, occupiedPlaces);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ){
        log.info("Deleting event: {}", id);
        eventService.deleteEvent(id, currentUser);

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public EventDto updateEvent(
            @PathVariable("id") Long id,
            @RequestBody @Valid EventDto eventToUpdate,
            @AuthenticationPrincipal User currentUser
    ){
        log.info("Updating event: id={}, eventToUpdate={}",id, eventToUpdate);
        var updatedEvent = eventService.updateEvent(
                id,
                eventDtoConverter.toDomain(eventToUpdate),
                currentUser
        );
        var occupiedPlaces = eventService.getOccupiedPlaces(updatedEvent.id());
        return eventDtoConverter.toDto(updatedEvent, occupiedPlaces);
    }
    @GetMapping
    public List<EventDto> findAll() {
        log.info("Finding all events");
        return eventService.findAll()
                .stream()
                .map(event -> eventDtoConverter.toDto(event, eventService.getOccupiedPlaces(event.id())))
                .toList();
    }

    @GetMapping("/my")
    public List<EventDto> findMyEvents(
            @AuthenticationPrincipal User currentUser
    ) {
        log.info("Finding events for user: {}", currentUser);
        return eventService.findMyEvents(currentUser)
                .stream()
                .map(event -> eventDtoConverter.toDto(event, eventService.getOccupiedPlaces(event.id())))
                .toList();
    }

    @PostMapping("/search")
    public List<EventDto> searchEvent(
            @RequestBody EventSearchRequestDto searchRequest
    ){
        log.info("Searching for event: {}", searchRequest);
        return eventService.searchEvents(searchRequest)
                .stream()
                .map(event -> eventDtoConverter.toDto(event, eventService.getOccupiedPlaces(event.id())))
                .toList();
    }
}
