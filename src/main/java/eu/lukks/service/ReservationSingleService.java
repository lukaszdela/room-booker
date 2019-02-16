package eu.lukks.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationSingle;
import eu.lukks.domain.Room;
import eu.lukks.repository.ReservationRepository;
import eu.lukks.repository.ReservationSingleRepository;
import eu.lukks.repository.RoomRepository;

@Service
public class ReservationSingleService implements IReservationSingleService{
	
	private ReservationSingleRepository reservationSingleRepository;
	private ReservationRepository reservationRepository;
	private RoomRepository roomRepository;
	
	@Autowired
	public ReservationSingleService(ReservationSingleRepository reservationSingleRepository,
			ReservationRepository reservationRepository, RoomRepository roomRepository) {
		super();
		this.reservationSingleRepository = reservationSingleRepository;
		this.reservationRepository = reservationRepository;
		this.roomRepository = roomRepository;
	}

	@Override
	public List<ReservationSingle> getAllSingleReservations(){
		return reservationSingleRepository.findAll();
	}
	
	@Override
	public void saveReservationSingleDay(Room room, Reservation reservation) {
		LocalDate counterDate = reservation.getDateFrom();
		
		while (counterDate.isBefore(reservation.getDateTo().plusDays(1))) {
			ReservationSingle reservationSingle = new ReservationSingle();
			reservationSingle.setBadge(true);
			reservationSingle.setClassname("purple-event");
			reservationSingle.setDate(counterDate);
			reservationSingle.setReservation(reservation);
			reservationSingle.setRoom(room);
			reservationSingleRepository.save(reservationSingle);
			counterDate = counterDate.plusDays(1);
		}
	}
	
	@Override
	public void saveReservationSingleDayByDates(Room room, Reservation reservation, LocalDate dateFrom, LocalDate dateTo) {
		LocalDate counterDate = dateFrom;
		
		while (counterDate.isBefore(dateTo.plusDays(1))) {
			ReservationSingle reservationSingle = new ReservationSingle();
			reservationSingle.setBadge(true);
			reservationSingle.setClassname("purple-event");
			reservationSingle.setDate(counterDate);
			reservationSingle.setReservation(reservation);
			reservationSingle.setRoom(room);
			reservationSingleRepository.save(reservationSingle);
			counterDate = counterDate.plusDays(1);
		}
	}
	
	@Override
	public void deleteReservationSingleDay(Room room, Reservation reservation) {
		
		LocalDate counterDate = reservation.getDateFrom();
		while (counterDate.isBefore(reservation.getDateTo().plusDays(1))) {

			List<ReservationSingle> reservationSinglesReservation = new ArrayList<ReservationSingle>(reservation.getReservationSingles());
			for(ReservationSingle single: reservationSinglesReservation) {
				if(single.getDate().equals(counterDate)){	
					reservationSingleRepository.delete(single);
				}
			}
			
			List<ReservationSingle> reservationSinglesRoom = new ArrayList<ReservationSingle>(room.getReservationSingles());
			for(ReservationSingle single: reservationSinglesRoom) {
				if(single.getDate().equals(counterDate)){
					reservationSingleRepository.delete(single);
				}
			}					
			
			counterDate = counterDate.plusDays(1);
		}
	}

	@Override
public void deleteReservationSingleDayByDates(Room room, Reservation reservation, LocalDate dateFrom, LocalDate dateTo) {
		
		LocalDate counterDate = dateFrom;
		while (counterDate.isBefore(dateTo.plusDays(1))) {

			List<ReservationSingle> reservationSinglesReservation = new ArrayList<ReservationSingle>(reservation.getReservationSingles());
			for(ReservationSingle single: reservationSinglesReservation) {
				if(single.getDate().equals(counterDate)){	
					reservationSingleRepository.delete(single);
				}
			}
			
			List<ReservationSingle> reservationSinglesRoom = new ArrayList<ReservationSingle>(room.getReservationSingles());
			for(ReservationSingle single: reservationSinglesRoom) {
				if(single.getDate().equals(counterDate)){
					reservationSingleRepository.delete(single);
				}
			}					
			
			counterDate = counterDate.plusDays(1);
		}
	}
	
	@Override
	public Boolean checkByDate(Room room, Reservation reservation) {
		List<ReservationSingle> reservationSinglesRoom = room.getReservationSingles();
		LocalDate counterDate = reservation.getDateFrom();
		LocalDate dateTo = reservation.getDateTo().plusDays(1);
		Boolean trigger = false;
		
		List<LocalDate> roomReservations = new ArrayList<LocalDate>();
		for(ReservationSingle reservationSingleRoom: reservationSinglesRoom) {
			roomReservations.add(reservationSingleRoom.getDate());
		}
		
		List<LocalDate> reservationDates = new ArrayList<LocalDate>();
		while (counterDate.isBefore(dateTo)) {
			reservationDates.add(counterDate);
			counterDate = counterDate.plusDays(1);
		}
		
		List<LocalDate> equalDates = new ArrayList<LocalDate>();
		for(LocalDate roomReservation: roomReservations) {
			for(LocalDate reservationDate: reservationDates) {
				if(roomReservation.equals(reservationDate)) {
					equalDates.add(reservationDate);
				}
			}
		}
		
		if(equalDates.size()>0) {
        	trigger = true;
        }
		
		return trigger;
	}
	
	@Override
	public String daysReservedForNewReservation(Room room, Reservation reservation) {
		List<ReservationSingle> reservationSinglesRoom = room.getReservationSingles();
		LocalDate counterDate = reservation.getDateFrom();
		LocalDate dateTo = reservation.getDateTo().plusDays(1);
		StringBuilder stringBuilder = new StringBuilder();
		
		List<LocalDate> roomReservations = new ArrayList<LocalDate>();
		for(ReservationSingle reservationSingleRoom: reservationSinglesRoom) {
			roomReservations.add(reservationSingleRoom.getDate());
		}
		
		List<LocalDate> reservationDates = new ArrayList<LocalDate>();
		while (counterDate.isBefore(dateTo)) {
			reservationDates.add(counterDate);
			counterDate = counterDate.plusDays(1);
		}
		
		List<LocalDate> equalDates = new ArrayList<LocalDate>();
		for(LocalDate roomReservation: roomReservations) {
			for(LocalDate reservationDate: reservationDates) {
				if(roomReservation.equals(reservationDate)) {
					equalDates.add(reservationDate);
				}
			}
		}
		
		if(equalDates.size()>0) {
			for(LocalDate equalDate: equalDates) {
				stringBuilder.append(equalDate);
				stringBuilder.append(", ");	
			}
			stringBuilder.deleteCharAt(stringBuilder.length() - 2);
			}
		
		return stringBuilder.toString();
	}
	
	@Override
	public LocalDate parsedDayToday() {
		LocalDate actualDate = LocalDate.now();
		LocalDate firstDayActualDate = actualDate.withDayOfMonth(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String text = firstDayActualDate.format(formatter);
		return LocalDate.parse(text, formatter);
	}
	
	@Override
	public LocalDate parsedDayDate(LocalDate date) {
		LocalDate actualDate = date;
		LocalDate firstDayActualDate = actualDate.withDayOfMonth(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String text = firstDayActualDate.format(formatter);
		return LocalDate.parse(text, formatter);
	}
	
	@Override
	public List<ReservationSingle> getSimpleReservationsForRoom(Room room){
		return reservationSingleRepository.getAllByRoom(room);
	}
	
	@Override
	public List<ReservationSingle> findReservationSingleStartMonth(LocalDate date){
		return reservationSingleRepository.searchSingleReservationStartMonth(date);
}

	
}
