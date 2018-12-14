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
	
	public void deleteReservationSingleDay(LocalDate dateFrom, LocalDate dateTo) {
		
	}
	
	@Override
	public List<ReservationSingle> findAllReservationSingle(){
		return reservationSingleRepository.findAll();
	}
	
}
