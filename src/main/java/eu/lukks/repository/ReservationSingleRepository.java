package eu.lukks.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.lukks.domain.ReservationSingle;
import eu.lukks.domain.Room;

@Repository
public interface ReservationSingleRepository extends JpaRepository<ReservationSingle, Long>{
	
	@Query("select e.id from ReservationSingle e where e.date = :date")
    Long getIdByDate(@Param("date") LocalDate date);
	
	@Query("select e from ReservationSingle e where e.date = :date")
    ReservationSingle getReservationSingleByDate(@Param("date") LocalDate date);
	
	@Query("select e from ReservationSingle e")
	List<ReservationSingle> getAllSingleReservations();
	
	@Query("select e.id from ReservationSingle e where e.room = :room")
    List<ReservationSingle> getAllByRoom(@Param("room") Room room);
	
	@Query("select e from ReservationSingle e where e.date >= :dateStartMonth")
	List<ReservationSingle> searchSingleReservationStartMonth(@Param("dateStartMonth") LocalDate date);
}
