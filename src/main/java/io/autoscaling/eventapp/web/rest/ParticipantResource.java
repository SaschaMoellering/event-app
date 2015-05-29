package io.autoscaling.eventapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.autoscaling.eventapp.domain.Participant;
import io.autoscaling.eventapp.repository.ParticipantRepository;
import io.autoscaling.eventapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Participant.
 */
@RestController
@RequestMapping("/api")
public class ParticipantResource {

    private final Logger log = LoggerFactory.getLogger(ParticipantResource.class);

    @Inject
    private ParticipantRepository participantRepository;

    /**
     * POST  /participants -> Create a new participant.
     */
    @RequestMapping(value = "/participants",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Participant participant) throws URISyntaxException {
        log.debug("REST request to save Participant : {}", participant);
        if (participant.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new participant cannot already have an ID").build();
        }
        participantRepository.save(participant);
        return ResponseEntity.created(new URI("/api/participants/" + participant.getId())).build();
    }

    /**
     * PUT  /participants -> Updates an existing participant.
     */
    @RequestMapping(value = "/participants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Participant participant) throws URISyntaxException {
        log.debug("REST request to update Participant : {}", participant);
        if (participant.getId() == null) {
            return create(participant);
        }
        participantRepository.save(participant);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /participants -> get all the participants.
     */
    @RequestMapping(value = "/participants",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Participant>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Participant> page = participantRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/participants", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /participants/:id -> get the "id" participant.
     */
    @RequestMapping(value = "/participants/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Participant> get(@PathVariable Long id) {
        log.debug("REST request to get Participant : {}", id);
        return Optional.ofNullable(participantRepository.findOne(id))
            .map(participant -> new ResponseEntity<>(
                participant,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /participants/:id -> delete the "id" participant.
     */
    @RequestMapping(value = "/participants/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Participant : {}", id);
        participantRepository.delete(id);
    }
}
