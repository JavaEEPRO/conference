package si.inspirited.persistence.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import si.inspirited.persistence.model.Role;
import java.util.Optional;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    default Optional<Role> findByName(String name) {
        return Optional.empty();
    }
}
