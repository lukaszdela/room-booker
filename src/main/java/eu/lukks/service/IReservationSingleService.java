package eu.lukks.service;

import java.time.LocalDate;
import java.util.List;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationSingle;
import eu.lukks.domain.Room;

public interface IReservationSingleService {

	void saveReservationSingleDay(Room room, Reservation reservation);

	void deleteReservationSingleDay(Room room, Reservation reservation);

	Boolean checkByDate(Room room, Reservation reservation);

	List<ReservationSingle> getAllSingleReservations();

	String daysReservedForNewReservation(Room room, Reservation reservation);

	LocalDate parsedDayToday();

	LocalDate parsedDayDate(LocalDate date);

	List<ReservationSingle> getSimpleReservationsForRoom(Room room);

	List<ReservationSingle> findReservationSingleStartMonth(LocalDate date);

	void saveReservationSingleDayByDates(Room room, Reservation reservation, LocalDate dateFrom, LocalDate dateTo);

	void deleteReservationSingleDayByDates(Room room, Reservation reservation, LocalDate dateFrom, LocalDate dateTo);

}
