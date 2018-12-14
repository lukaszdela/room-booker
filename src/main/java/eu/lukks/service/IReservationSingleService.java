package eu.lukks.service;

import java.time.LocalDate;
import java.util.List;

import eu.lukks.domain.ReservationSingle;

public interface IReservationSingleService {

	void saveReservationSingleDay(LocalDate datefrom, LocalDate dateto);

	List<ReservationSingle> findAllReservationSingle();

}
