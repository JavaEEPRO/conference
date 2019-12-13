package si.inspirited;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import si.inspirited.persistence.model.Participant;
import si.inspirited.persistence.repo.ParticipantRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ParticipantRepositoryIntegrationTests {

    @Autowired
    ParticipantRepository participantRepository;

    @Test
    public void requestingForInitialParticipantFromDataStorage_whenCouldBeQueriedFromStorage_thenCorrect() {
        Participant participant = null;
        if (participantRepository.findByLogin("test").isPresent()) {
            participant = participantRepository.findByLogin("test").get();
        }
        assertNotNull(participant);
        assertEquals("One Test Participant", participant.getFullName());
    }
}
