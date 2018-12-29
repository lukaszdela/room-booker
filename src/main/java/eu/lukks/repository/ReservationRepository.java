package eu.lukks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.lukks.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
