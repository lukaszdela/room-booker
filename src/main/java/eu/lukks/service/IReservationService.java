package eu.lukks.service;

import java.time.LocalDate;
import java.util.List;

import eu.lukks.domain.Reservation;

public interface IReservationService {
	
	Reservation readReservationById(Long id);

	void saveReservation(Reservation reservation);

	void deleteReservationById(Long id);

	List<Reservation> findAllReservations();

	Boolean checkDateFromAfterDateTo(LocalDate dateFrom, LocalDate dateTo);

	Integer howManyDays(LocalDate dateFrom, LocalDate dateTo);



}
