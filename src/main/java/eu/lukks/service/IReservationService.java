package eu.lukks.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import eu.lukks.domain.Reservation;

public interface IReservationService {
	
	Reservation readReservationById(Long id);

	void saveReservation(Reservation reservation);

	void deleteReservationById(Long id);

	List<Reservation> findAllReservations();

	Boolean checkDateFromAfterDateTo(LocalDate dateFrom, LocalDate dateTo);

	Integer howManyDays(LocalDate dateFrom, LocalDate dateTo);

	List<Reservation> listNumberedAdminReservations(Pageable pageable);

	List<Reservation> searchReservationsByDate(LocalDate dateFrom, LocalDate dateTo);

	Boolean checkByDateSingleInReservations(LocalDate newDateFrom, LocalDate newDateTo, LocalDate reservationDateFrom,
			LocalDate reservationDateTo);

	String checkByDateSingleInReservationsBookedDays(LocalDate newDateFrom, LocalDate newDateTo,
			LocalDate reservationDateFrom, LocalDate reservationDateTo);

	List<LocalDate> datesListFromDateToDate(LocalDate dateFrom, LocalDate dateTo);

	Integer calculatePriceForNewReservation(Reservation reservation);



}
