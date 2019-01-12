package eu.lukks.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.lukks.domain.Reservation;
import eu.lukks.repository.ReservationRepository;
@Service
public class ReservationService implements IReservationService{
	
	private ReservationRepository reservartionRepository;

	@Autowired
	public ReservationService(ReservationRepository reservartionRepository) {
		super();
		this.reservartionRepository = reservartionRepository;
	}

	@Override
	public Reservation readReservationById(Long id) {
		return reservartionRepository.findById(id).orElse(null);
	}
	
	@Override
	public void saveReservation(Reservation reservation) {
		reservartionRepository.save(reservation);
	}
	
	@Override
	public void deleteReservationById(Long id) {
		reservartionRepository.deleteById(id);
	}
	
	@Override
	public List<Reservation> findAllReservations(){
		return reservartionRepository.findAll();
	}

	@Override
	public Boolean checkDateFromAfterDateTo(LocalDate dateFrom, LocalDate dateTo) {
		return dateFrom.isAfter(dateTo);
	}
	
	@Override
	public Integer howManyDays(LocalDate dateFrom, LocalDate dateTo) {
		LocalDate counterDate = dateFrom;
		Integer counter = 0;
		while (counterDate.isBefore(dateTo.plusDays(1))) {
			counterDate = counterDate.plusDays(1);
			counter++;
		}
		return counter;
	}
	
	@Override
	public List<Reservation> searchReservations(LocalDate dateFrom, LocalDate dateTo, String name, String surname){
		return reservartionRepository.searchReservations(dateFrom, dateTo, name, surname);
	}
	
	@Override
	public List<Reservation> searchReservationsByName(String name, String surname){
		return reservartionRepository.searchReservationsByName(name, surname);
	}
	
	@Override
	public List<Reservation> listDefaultAdminReservations(Pageable pageable){
		return reservartionRepository.listDefaultAdminReservations(pageable);
	}
	
	
	
}
