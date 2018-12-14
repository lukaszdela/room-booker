package eu.lukks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.lukks.domain.ReservationSingle;

@Repository
public interface ReservationSingleRepository extends JpaRepository<ReservationSingle, Long>{
}
