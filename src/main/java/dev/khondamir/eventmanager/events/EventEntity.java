package dev.khondamir.eventmanager.events;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventName;
    private OffsetDateTime eventdate;
    private Integer duration;
    private Long cost;
    private Integer maxPlaces;
    private Long locationId;
    private Long ownerId;
    private EventStatus status;

    public EventEntity(
            Long id,
            String eventName,
            OffsetDateTime eventdate,
            Integer duration,
            Long cost,
            Integer maxPlaces,
            Long locationId,
            Long ownerId,
            EventStatus status
            ) {
        this.id = id;
        this.eventName = eventName;
        this.eventdate = eventdate;
        this.duration = duration;
        this.cost = cost;
        this.maxPlaces = maxPlaces;
        this.locationId = locationId;
        this.ownerId = ownerId;
        this.status = status;
    }
    public EventEntity() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public OffsetDateTime getEventdate() {
        return eventdate;
    }
    public void setEventdate(OffsetDateTime eventdate) {
        this.eventdate = eventdate;
    }
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    public Long getCost() {
        return cost;
    }
    public void setCost(Long cost) {
        this.cost = cost;
    }
    public Integer getMaxPlaces() {
        return maxPlaces;
    }
    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }
    public Long getLocationId() {
        return locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public EventStatus getStatus() {
        return status;
    }
    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
