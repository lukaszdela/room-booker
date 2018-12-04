package eu.lukks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.lukks.domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
