package eu.lukks.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationSingle;
import eu.lukks.domain.ReservationSingleDto;
import eu.lukks.domain.Room;
import eu.lukks.service.IReservationService;
import eu.lukks.service.IReservationSingleService;
import eu.lukks.service.IRoomService;

@RestController
public class DatesZabutoCalendarController {
	
	private IReservationService iReservationService;
	private IReservationSingleService iReservationSingleService;
	private IRoomService iRoomService;
	
	@Autowired
	public DatesZabutoCalendarController(IReservationService iReservationService,
			IReservationSingleService iReservationSingleService, IRoomService iRoomService) {
		super();
		this.iReservationService = iReservationService;
		this.iReservationSingleService = iReservationSingleService;
		this.iRoomService = iRoomService;
	}
	
	

	@GetMapping("/admin/list/update/{reservationId}")
	public List<ReservationSingleDto> reservationsListUpdateId(@PathVariable("reservationId")Long reservationId) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		Room roomReservationById = reservationById.getRoom();
		List<ReservationSingle> reservationSinglesRoom = new ArrayList<ReservationSingle>(roomReservationById.getReservationSingles());
		List<ReservationSingle> reservationSinglesReservation = new ArrayList<ReservationSingle>(reservationById.getReservationSingles());
		List<ReservationSingleDto> reservationSingleDtos = new ArrayList<ReservationSingleDto>();
		
		reservationSinglesRoom.removeAll(reservationSinglesReservation);
//		for(ReservationSingle reservationSingleRoom: reservationSinglesRoom) {
//			for(ReservationSingle reservationSingleReservation: reservationSinglesReservation) {
//				if(reservationSingleRoom.getDate().isEqual(reservationSingleReservation.getDate())) {
//					reservationSinglesRoom.remove(reservationSingleRoom);
//				}
//			}
//		}
		
		for(ReservationSingle reservation: reservationSinglesRoom) {
			ReservationSingleDto dto = new ReservationSingleDto();
			dto.setDate(reservation.getDate());
			dto.setBadge(reservation.getBadge());
			dto.setClassname(reservation.getClassname());
			reservationSingleDtos.add(dto);
		}
		
		return reservationSingleDtos;
	}
	
	@GetMapping("/admin/list/show/{reservationId}")
	public List<ReservationSingleDto> reservationsListShowId(@PathVariable("reservationId")Long reservationId) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		List<ReservationSingle> reservationSingles = reservationById.getReservationSingles();
		List<ReservationSingleDto> reservationSingleDtos = new ArrayList<ReservationSingleDto>();
		for(ReservationSingle reservation: reservationSingles) {
			ReservationSingleDto dto = new ReservationSingleDto();
			dto.setDate(reservation.getDate());
			dto.setBadge(reservation.getBadge());
			dto.setClassname(reservation.getClassname());
			reservationSingleDtos.add(dto);
		}
		return reservationSingleDtos;
	}
	
	@GetMapping("/list/{roomId}")
	public List<ReservationSingleDto> reservationsSimpleForRoom(@PathVariable("roomId")Long roomId){
		Room roomById = iRoomService.getRoomById(roomId);
		List<ReservationSingle> reservationSinglesFromStartThisMonth = new ArrayList<ReservationSingle>(iReservationSingleService.findReservationSingleStartMonth(iReservationSingleService.parsedDayToday()));
		List<ReservationSingle> reservationSinglesFromStartThisMonthForRoom = new ArrayList<ReservationSingle>();
		for(ReservationSingle reservation: reservationSinglesFromStartThisMonth) {
			if(reservation.getRoom().equals(roomById)) {
				reservationSinglesFromStartThisMonthForRoom.add(reservation);
			}
		}
				
		List<ReservationSingleDto> reservationSingleDtos = new ArrayList<ReservationSingleDto>();
		for(ReservationSingle reservation: reservationSinglesFromStartThisMonthForRoom) {
			ReservationSingleDto dto = new ReservationSingleDto();
			dto.setDate(reservation.getDate());
			dto.setBadge(reservation.getBadge());
			dto.setClassname(reservation.getClassname());
			reservationSingleDtos.add(dto);
		}
		return reservationSingleDtos;
	}

}
