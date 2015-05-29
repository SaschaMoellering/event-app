package io.autoscaling.eventapp.repository;

import io.autoscaling.eventapp.domain.Event;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("select event from Event event left join fetch event.participants where event.id =:id")
    Event findOneWithEagerRelationships(@Param("id") Long id);

}
