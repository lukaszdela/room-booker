package eu.lukks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import eu.lukks.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	@Query("select e from Room e where e.status = true")
	List<Room> getAllActiveRooms();

}
