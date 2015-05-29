package io.autoscaling.eventapp.repository;

import io.autoscaling.eventapp.domain.Participant;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Participant entity.
 */
public interface ParticipantRepository extends JpaRepository<Participant,Long> {

}
