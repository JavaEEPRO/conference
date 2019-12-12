package si.inspirited.persistence.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import si.inspirited.persistence.model.Conference;
import java.util.Optional;

@Repository
public interface ConferenceRepository extends PagingAndSortingRepository<Conference, Long> {

    default Optional<Conference> findByName(String name) {
        return Optional.empty();
    }
}
