package si.inspirited.persistence.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import si.inspirited.persistence.model.Participant;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends PagingAndSortingRepository<Participant, Long> {

    Optional<Participant> findByLogin(String login);
}
