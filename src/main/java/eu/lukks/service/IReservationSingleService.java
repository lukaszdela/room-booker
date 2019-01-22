package eu.lukks.service;

import java.time.LocalDate;
import java.util.List;

import eu.lukks.domain.ReservationSingle;

public interface IReservationSingleService {

	void saveReservationSingleDay(LocalDate datefrom, LocalDate dateto);

	void deleteReservationSingleDay(LocalDate dateFrom, LocalDate dateTo);

	Boolean checkByDate(LocalDate dateFrom, LocalDate dateTo);

	List<ReservationSingle> findReservationSingleStartMonth(LocalDate date);

	List<ReservationSingle> getAllSingleReservations();

	String daysReservedForNewReservation(LocalDate dateFrom, LocalDate dateTo);

	LocalDate parsedDayToday();

	LocalDate parsedDayDate(LocalDate date);

}
