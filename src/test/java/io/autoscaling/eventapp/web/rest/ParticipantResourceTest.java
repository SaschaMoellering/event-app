package io.autoscaling.eventapp.web.rest;

import io.autoscaling.eventapp.Application;
import io.autoscaling.eventapp.domain.Participant;
import io.autoscaling.eventapp.repository.ParticipantRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParticipantResource REST controller.
 *
 * @see ParticipantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParticipantResourceTest {

    private static final String DEFAULT_FIRSTNAME = "SAMPLE_TEXT";
    private static final String UPDATED_FIRSTNAME = "UPDATED_TEXT";
    private static final String DEFAULT_LASTNAME = "SAMPLE_TEXT";
    private static final String UPDATED_LASTNAME = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_BIRTHDAY = new LocalDate(0L);
    private static final LocalDate UPDATED_BIRTHDAY = new LocalDate();

    @Inject
    private ParticipantRepository participantRepository;

    private MockMvc restParticipantMockMvc;

    private Participant participant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParticipantResource participantResource = new ParticipantResource();
        ReflectionTestUtils.setField(participantResource, "participantRepository", participantRepository);
        this.restParticipantMockMvc = MockMvcBuilders.standaloneSetup(participantResource).build();
    }

    @Before
    public void initTest() {
        participant = new Participant();
        participant.setFirstname(DEFAULT_FIRSTNAME);
        participant.setLastname(DEFAULT_LASTNAME);
        participant.setBirthday(DEFAULT_BIRTHDAY);
    }

    @Test
    @Transactional
    public void createParticipant() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();

        // Create the Participant
        restParticipantMockMvc.perform(post("/api/participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(participant)))
                .andExpect(status().isCreated());

        // Validate the Participant in the database
        List<Participant> participants = participantRepository.findAll();
        assertThat(participants).hasSize(databaseSizeBeforeCreate + 1);
        Participant testParticipant = participants.get(participants.size() - 1);
        assertThat(testParticipant.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testParticipant.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testParticipant.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllParticipants() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participants
        restParticipantMockMvc.perform(get("/api/participants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(participant.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
                .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
                .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())));
    }

    @Test
    @Transactional
    public void getParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", participant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(participant.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParticipant() throws Exception {
        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

		int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Update the participant
        participant.setFirstname(UPDATED_FIRSTNAME);
        participant.setLastname(UPDATED_LASTNAME);
        participant.setBirthday(UPDATED_BIRTHDAY);
        restParticipantMockMvc.perform(put("/api/participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(participant)))
                .andExpect(status().isOk());

        // Validate the Participant in the database
        List<Participant> participants = participantRepository.findAll();
        assertThat(participants).hasSize(databaseSizeBeforeUpdate);
        Participant testParticipant = participants.get(participants.size() - 1);
        assertThat(testParticipant.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testParticipant.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testParticipant.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void deleteParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

		int databaseSizeBeforeDelete = participantRepository.findAll().size();

        // Get the participant
        restParticipantMockMvc.perform(delete("/api/participants/{id}", participant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Participant> participants = participantRepository.findAll();
        assertThat(participants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
