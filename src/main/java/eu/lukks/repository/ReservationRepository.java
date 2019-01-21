package eu.lukks.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eu.lukks.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	
	@Query("select e from Reservation e where (e.dateFrom >= :dateFrom) and (e.dateTo <= :dateTo)")
    List<Reservation> searchReservationsByDate(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);
	
	@Query("select e from Reservation e order by e.id desc")
	List<Reservation> listNumberedAdminReservations(Pageable pageable);

}
