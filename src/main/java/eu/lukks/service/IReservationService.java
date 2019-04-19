package eu.lukks.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationDto;
import eu.lukks.domain.Room;

public interface IReservationService {

	Reservation readReservationById(Long id);

	void saveReservation(Reservation reservation);

	void deleteReservationById(Long id);

	List<Reservation> findAllReservations();

	Boolean checkDateFromAfterDateTo(LocalDate dateFrom, LocalDate dateTo);

	Integer howManyDays(LocalDate dateFrom, LocalDate dateTo);

	List<Reservation> listNumberedAdminReservations(Pageable pageable);

	List<Reservation> searchReservationsByDate(LocalDate dateFrom, LocalDate dateTo);

	List<LocalDate> datesListFromDateToDate(LocalDate dateFrom, LocalDate dateTo);

	Double calculatePriceForNewReservation(Reservation reservation, Double priceOneDay);

	Reservation updateReservation(Reservation reservation, Reservation updatedReservation);

	Boolean checkByDateSingleInReservations(Room room, Reservation reservation, LocalDate newDateFrom,
			LocalDate newDateTo);

	String checkByDateSingleInReservationsBookedDays(Room room, Reservation reservation, LocalDate newDateFrom,
			LocalDate newDateTo);

	List<ReservationDto> reservationToReservationDto(List<Reservation> reservations);

	ReservationDto reservationToReservationDtoSingle(Reservation reservation);

}
