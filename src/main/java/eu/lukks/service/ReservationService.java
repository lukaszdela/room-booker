package eu.lukks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.lukks.domain.Reservation;
import eu.lukks.repository.ReservationRepository;

@Service
public class ReservationService implements IReservationService{
	
	private ReservationRepository reservationRepository;

	@Autowired
	public ReservationService(ReservationRepository reservationRepository) {
		super();
		this.reservationRepository = reservationRepository;
	}
	
	@Override
	public Reservation readReservationById(Long id) {
		return reservationRepository.findById(id).orElse(null);
	}
	
	@Override
	public void saveReservation(Reservation reservation) {
		reservationRepository.save(reservation);
	}
	
	@Override
	public void deleteReservationById(Long id) {
		reservationRepository.deleteById(id);
	}
	
	@Override
	public List<Reservation> findAllReservations(){
		return reservationRepository.findAll();
	}

}
