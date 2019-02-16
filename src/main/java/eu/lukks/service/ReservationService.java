package eu.lukks.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.activation.DataContentHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationSingle;
import eu.lukks.domain.Room;
import eu.lukks.repository.ReservationRepository;
import eu.lukks.repository.ReservationSingleRepository;
@Service
public class ReservationService implements IReservationService{
	
	private ReservationRepository reservartionRepository;
	private ReservationSingleRepository reservationSingleRepository;

	@Autowired
	public ReservationService(ReservationRepository reservartionRepository,
			ReservationSingleRepository reservationSingleRepository) {
		super();
		this.reservartionRepository = reservartionRepository;
		this.reservationSingleRepository = reservationSingleRepository;
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
	public List<Reservation> searchReservationsByDate(LocalDate dateFrom, LocalDate dateTo){
		return reservartionRepository.searchReservationsByDate(dateFrom, dateTo);
	}
	
	@Override
	public List<Reservation> listNumberedAdminReservations(Pageable pageable){
		return reservartionRepository.listNumberedAdminReservations(pageable);
	}
	
	@Override
	public Boolean checkByDateSingleInReservations(Room room, Reservation reservation, LocalDate newDateFrom, LocalDate newDateTo) {
		List<ReservationSingle> reservationSinglesRoom = room.getReservationSingles(); 
		List<ReservationSingle> reservationSinglesReservation = reservation.getReservationSingles();
		
		List<LocalDate> reservationSinglesReservationsDates = new ArrayList<LocalDate>();
		for(ReservationSingle reservationSingle: reservationSinglesReservation) {
			reservationSinglesReservationsDates.add(reservationSingle.getDate());
		}
		
		List<LocalDate> reservationSinglesRoomDates = new ArrayList<LocalDate>();
		for(ReservationSingle reservationSingle: reservationSinglesRoom) {
			reservationSinglesRoomDates.add(reservationSingle.getDate());
		}
		
		Collection<LocalDate> similarDays = new HashSet<LocalDate>(reservationSinglesReservationsDates);
		Collection<LocalDate> differentDays = new HashSet<LocalDate>();
		
		differentDays.addAll(reservationSinglesReservationsDates);
		differentDays.addAll(reservationSinglesRoomDates);
		
		similarDays.retainAll(reservationSinglesRoomDates);
		differentDays.removeAll(similarDays);
			
		LocalDate counterDate = newDateFrom;
		Boolean trigger = false;
		
		List<LocalDate> updateDates = new ArrayList<LocalDate>();
		while (counterDate.isBefore(newDateTo.plusDays(1))) {
			updateDates.add(counterDate);
			counterDate = counterDate.plusDays(1);
		}
	
        List<LocalDate> equalDays = new ArrayList<LocalDate>();
        for(LocalDate roomDate: differentDays) {
        	for(LocalDate updateDate: updateDates) {
        		if(roomDate.equals(updateDate)) {
        			equalDays.add(roomDate);
        		}
        	}
        }

        
        if(equalDays.size()>0) {
        	trigger = true;
        }
		
		return trigger;
	}
	
	@Override
	public String checkByDateSingleInReservationsBookedDays(Room room, Reservation reservation, LocalDate newDateFrom, LocalDate newDateTo) {
		List<ReservationSingle> reservationSinglesRoom = room.getReservationSingles(); 
		List<ReservationSingle> reservationSinglesReservation = reservation.getReservationSingles();
		
		List<LocalDate> reservationSinglesReservationsDates = new ArrayList<LocalDate>();
		for(ReservationSingle reservationSingle: reservationSinglesReservation) {
			reservationSinglesReservationsDates.add(reservationSingle.getDate());
		}
		
		List<LocalDate> reservationSinglesRoomDates = new ArrayList<LocalDate>();
		for(ReservationSingle reservationSingle: reservationSinglesRoom) {
			reservationSinglesRoomDates.add(reservationSingle.getDate());
		}
		
		Collection<LocalDate> similarDays = new HashSet<LocalDate>(reservationSinglesReservationsDates);
		Collection<LocalDate> differentDays = new HashSet<LocalDate>();
		
		differentDays.addAll(reservationSinglesReservationsDates);
		differentDays.addAll(reservationSinglesRoomDates);
		
		similarDays.retainAll(reservationSinglesRoomDates);
		differentDays.removeAll(similarDays);
			
		LocalDate counterDate = newDateFrom;
		
		List<LocalDate> updateDates = new ArrayList<LocalDate>();
		while (counterDate.isBefore(newDateTo.plusDays(1))) {
			updateDates.add(counterDate);
			counterDate = counterDate.plusDays(1);
		}
	
        List<LocalDate> equalDays = new ArrayList<LocalDate>();
        for(LocalDate roomDate: differentDays) {
        	for(LocalDate updateDate: updateDates) {
        		if(roomDate.equals(updateDate)) {
        			equalDays.add(roomDate);
        		}
        	}
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        
        for(LocalDate date: equalDays) {
        	stringBuilder.append(date);
        	stringBuilder.append(", ");
        }
		stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        
		return stringBuilder.toString();
	}
	
	@Override
	public List<LocalDate> datesListFromDateToDate(LocalDate dateFrom, LocalDate dateTo){
		List<LocalDate> datesList = new ArrayList<LocalDate>();
		if (dateFrom.isBefore(dateTo) || dateFrom.equals(dateTo)) {
			while (dateFrom.isBefore(dateTo.plusDays(1))) {
				datesList.add(dateFrom);
				dateFrom = dateFrom.plusDays(1);
			}			
		}
		return datesList;
	}
	
	@Override
	public Integer calculatePriceForNewReservation(Reservation reservation) {
		Integer priceOneDay = 120;
		Integer priceBreakfast = 5;
		Integer priceParking = 5;
		Integer reservationCost = 0;
		Integer daysCounter = howManyDays(reservation.getDateFrom(), reservation.getDateTo());
		reservationCost = daysCounter * priceOneDay;
		if(reservation.getBreakfast() == true) {
			reservationCost = reservationCost + (daysCounter * priceBreakfast);
		}
		if(reservation.getParking() == true) {
			reservationCost = reservationCost + (daysCounter * priceParking);
		}
		return reservationCost;
	}
	
	@Override
	public Reservation updateReservation(Reservation reservation, Reservation updatedReservation) {
		reservation.setDateFrom(updatedReservation.getDateFrom());
		reservation.setDateTo(updatedReservation.getDateTo());
		reservation.setName(updatedReservation.getName());
		reservation.setSurname(updatedReservation.getSurname());
		reservation.setAddress(updatedReservation.getAddress());
		reservation.setZip(updatedReservation.getZip());
		reservation.setCity(updatedReservation.getCity());
		reservation.setPhone(updatedReservation.getPhone());
		reservation.setMail(updatedReservation.getMail());
		reservation.setPrice(updatedReservation.getPrice());
		if(updatedReservation.getBreakfast() == true) {
			reservation.setBreakfast(true);
		}else {
			reservation.setBreakfast(false);
		}
		if(updatedReservation.getParking() == true) {
			reservation.setParking(true);
		}else {
			reservation.setParking(false);
		}
		return reservation;
	}
	
}
