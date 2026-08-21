package dev.khondamir.eventmanager.events;

import dev.khondamir.eventmanager.registrations.EventRegistrationRepository;
import dev.khondamir.eventmanager.users.User;
import dev.khondamir.eventmanager.location.LocationService;
import dev.khondamir.eventmanager.users.UserRole;
import jakarta.persistence.EntityNotFoundException;
import dev.khondamir.eventmanager.web.ForbiddenException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final EventEntityConverter eventEntityConverter;
    private final EventRegistrationRepository eventRegistrationRepository;

    public EventService(
            EventRepository eventRepository,
            LocationService locationService,
            EventEntityConverter eventEntityConverter,
            EventRegistrationRepository eventRegistrationRepository
    ) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.eventEntityConverter = eventEntityConverter;
        this.eventRegistrationRepository = eventRegistrationRepository;
    }

    public Event createEvent(Event eventToCreate, User currentUser) {
        checkLocationExistence(eventToCreate.locationId());

        var eventWithOwner = new Event(
                eventToCreate.id(),
                eventToCreate.name(),
                eventToCreate.date(),
                eventToCreate.duration(),
                eventToCreate.cost(),
                eventToCreate.maxPlaces(),
                eventToCreate.locationId(),
                currentUser.id(),
                EventStatus.WAIT_START
        );

        var eventToSave = eventEntityConverter.toEntity(eventWithOwner);
        var savedEvent = eventEntityConverter.toDomain(
                eventRepository.save(eventToSave)
        );

        return savedEvent;
    }

    public Event findById(Long id) {
        var foundEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Event with id " + id + " not found"
                ));
        return eventEntityConverter.toDomain(foundEvent);
    }

    public void deleteEvent(Long id, User currentUser) {
        var existingEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"));

        checkOwnershipOrAdmin(existingEntity.getOwnerId(), currentUser);

        if (existingEntity.getStatus() != EventStatus.WAIT_START) {
            throw new ForbiddenException("Only events that have not started can be cancelled");
        }
        existingEntity.setStatus(EventStatus.CANCELLED);
        eventRepository.save(existingEntity);
    }

    public Event updateEvent(
            Long id,
            Event eventToUpdate,
            User currentUser
    ) {
        var existingEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"));

        checkOwnershipOrAdmin(existingEntity.getOwnerId(), currentUser);

        checkLocationExistence(eventToUpdate.locationId());

        eventRepository.updateEvent(
                id,
                eventToUpdate.name(),
                eventToUpdate.date(),
                eventToUpdate.duration(),
                eventToUpdate.cost(),
                eventToUpdate.maxPlaces(),
                eventToUpdate.locationId()
        );
        var updatedEvent = eventEntityConverter.toDomain(
                eventRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"))
        );
        return updatedEvent;
    }

    public List<Event> findAll() {
        return eventRepository.findAll()
                .stream()
                .map(eventEntityConverter::toDomain)
                .toList();
    }

    public List<Event> findMyEvents(User currentUser) {
        return eventRepository.findByOwnerId(currentUser.id())
                .stream()
                .map(eventEntityConverter::toDomain)
                .toList();
    }

    public List<Event> searchEvents(EventSearchRequestDto filter) {
        var spec = EventSpecifications.withFilters(filter);
        return eventRepository.findAll(spec)
                .stream()
                .map(eventEntityConverter::toDomain)
                .toList();
    }

    public Long getOccupiedPlaces(Long eventId) {
        return eventRegistrationRepository.countByEventId(eventId);
    }

    private void checkLocationExistence(Long locationId) {
        if (!locationService.isLocationExistsById(locationId)) {
            throw new EntityNotFoundException("Location does not exist with id=%s".
                    formatted(locationId));
        }
    }

    private void checkOwnershipOrAdmin(Long ownerId, User currentUser) {
        boolean isOwner = ownerId.equals(currentUser.id());
        boolean isAdmin = currentUser.role() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("Only the event owner or an admin can perform this action");
        }
    }
}
