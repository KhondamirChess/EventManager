package dev.khondamir.eventmanager.events;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {
    @Transactional
    @Modifying
    @Query("""
                        UPDATE EventEntity e
                        SET e.eventName = :name,
                        e.eventdate = :date,
                        e.duration = :duration,
                        e.cost = :cost,
                        e.maxPlaces = :maxPlaces,
                        e.locationId = :locationId
                        WHERE e.id = :id
            """)
    void updateEvent(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("date") OffsetDateTime date,
            @Param("duration") Integer duration,
            @Param("cost") Long cost,
            @Param("maxPlaces") Integer maxPlaces,
            @Param("locationId") Long locationId
    );

    List<EventEntity> findAll();
    List<EventEntity> findByOwnerId(Long id);
}