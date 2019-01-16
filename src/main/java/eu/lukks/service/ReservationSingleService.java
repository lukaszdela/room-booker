package eu.lukks.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.lukks.domain.ReservationSingle;
import eu.lukks.repository.ReservationSingleRepository;

@Service
public class ReservationSingleService implements IReservationSingleService{
	
	private ReservationSingleRepository reservationSingleRepository;

	@Autowired
	public ReservationSingleService(ReservationSingleRepository reservationSingleRepository) {
		super();
		this.reservationSingleRepository = reservationSingleRepository;
	}
	
	@Override
	public void saveReservationSingleDay(LocalDate dateFrom, LocalDate dateTo) {
		LocalDate counterDate = dateFrom;
		
		while (counterDate.isBefore(dateTo.plusDays(1))) {
			ReservationSingle reservationSingle = new ReservationSingle();
			reservationSingle.setBadge(true);
			reservationSingle.setClassname("purple-event");
			reservationSingle.setDate(counterDate);
			reservationSingleRepository.save(reservationSingle);
			counterDate = counterDate.plusDays(1);
		}
	}
	
	@Override
	public void deleteReservationSingleDay(LocalDate dateFrom, LocalDate dateTo) {
		LocalDate counterDate = dateFrom;
		while (counterDate.isBefore(dateTo.plusDays(1))) {
			reservationSingleRepository.deleteById(reservationSingleRepository.getIdByDate(counterDate));
			counterDate = counterDate.plusDays(1);
		}
	}
	
	@Override
	public List<ReservationSingle> findReservationSingleStartMonth(LocalDate date){
		return reservationSingleRepository.searchSingleReservationStartMonth(date);
	}
	
	@Override
	public Boolean checkByDate(LocalDate dateFrom, LocalDate dateTo) {
		LocalDate counterDate = dateFrom;
		Boolean trigger = false;
		while (counterDate.isBefore(dateTo.plusDays(1))) {
			if (reservationSingleRepository.getIdByDate(counterDate) != null) {
				trigger = true;
			}
			counterDate = counterDate.plusDays(1);
		}
		return trigger;
	}
	

	
}
