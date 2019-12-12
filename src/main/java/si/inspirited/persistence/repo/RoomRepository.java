package si.inspirited.persistence.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import si.inspirited.persistence.model.Room;
import java.util.Optional;

@Repository
public interface RoomRepository extends PagingAndSortingRepository<Room, Long> {

    default Optional<Room> findByName(String name) {
        return Optional.empty();
    }
}