package si.inspirited.persistence.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import si.inspirited.persistence.model.Room;

@Repository
interface RoomRepository extends PagingAndSortingRepository<Room, Long> {
}