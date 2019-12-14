package si.inspirited;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import si.inspirited.persistence.model.Participant;
import si.inspirited.persistence.model.Role;
import si.inspirited.persistence.repo.ParticipantRepository;
import si.inspirited.persistence.repo.RoleRepository;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ParticipantRepositoryIntegrationTests {

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void requestingForInitialParticipantFromDataStorage_whenCouldBeQueriedFromStorage_thenCorrect() {
        Participant participant = null;
        if (participantRepository.findByLogin("test").isPresent()) {
            participant = participantRepository.findByLogin("test").get();
        }
        assertNotNull(participant);
        assertNotNull(participant.getRoles());
        assertEquals(2, participant.getRoles().size());
        Role roleUser = null;
        Role roleAdmin = null;
        if (roleRepository.findByName("ROLE_USER").isPresent()) {
            roleUser = roleRepository.findByName("ROLE_USER").get();
        }
        if (roleRepository.findByName("ROLE_ADMIN").isPresent()) {
            roleAdmin = roleRepository.findByName("ROLE_ADMIN").get();
        }
        assertNotNull(roleUser);
        assertNotNull(roleAdmin);
        assertTrue(participant.getRoles().contains(roleUser));
        assertTrue(participant.getRoles().contains(roleAdmin));
        assertTrue(participant.isEnabled());
        assertEquals("One Test Participant", participant.getFullName());
    }
}
