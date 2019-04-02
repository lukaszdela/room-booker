package eu.lukks.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.Room;
import eu.lukks.domain.RoomDto;
import eu.lukks.service.IReservationService;
import eu.lukks.service.IReservationSingleService;
import eu.lukks.service.IRoomService;

@Controller
public class PublicController {
	
	private IReservationService iReservationService;
	private IReservationSingleService iReservationSingleService;
	private IRoomService iRoomService;
	
	@Autowired
	public PublicController(IReservationService iReservationService,
			IReservationSingleService iReservationSingleService, IRoomService iRoomService) {
		super();
		this.iReservationService = iReservationService;
		this.iReservationSingleService = iReservationSingleService;
		this.iRoomService = iRoomService;
	}
	
	@PostMapping("/reservation/add/{roomId}")
	public String newReservation(@ModelAttribute Reservation reservation,
									@PathVariable("roomId")Long roomId,
									Model model) {
		Room roomById = iRoomService.getRoomById(roomId);
		if(roomById.getStatus().equals(true)) {
		if (!iReservationService.checkDateFromAfterDateTo(reservation.getDateFrom(), reservation.getDateTo()) &&
			iReservationService.checkDateFromAfterDateTo(reservation.getDateFrom(), LocalDate.now().minusDays(1))	
				) {
			if (!iReservationSingleService.checkByDate(roomById, reservation)) {
				reservation.setPrice(iReservationService.calculatePriceForNewReservation(reservation));
				reservation.setRoom(roomById);
				reservation.setRoomTitle(roomById.getTitle());
				iReservationService.saveReservation(reservation);
				iReservationSingleService.saveReservationSingleDay(roomById, reservation);
				String msg = String.format("Room is reserved from " + reservation.getDateFrom() + " to " + reservation.getDateTo());
		        model.addAttribute("msg", msg);
			}else {
				String msg = String.format("Dates: " + iReservationSingleService.daysReservedForNewReservation(roomById, reservation) + " are reserved. Please choose another reservation dates.");
		        model.addAttribute("msg", msg);
			}
			
		}else {
			String msg = String.format("Selected dates are incorrect. Please try again.");
	        model.addAttribute("msg", msg);
		}
		
		List<Reservation> reservations = iReservationService.findAllReservations();
		model.addAttribute("room", roomById);
		model.addAttribute("reservations", reservations);
		return "reservation";
		}else {
			return "index";
		}
	}
	
	@GetMapping("/")
	public String getAllEnabledRooms(Model model) {
		List<Room> rooms = iRoomService.getAllActiveRooms();
		List<RoomDto> roomsDto = new ArrayList<RoomDto>();
		for(Room room: rooms) {
			RoomDto roomDto = new RoomDto();
			roomDto.setId(room.getId());
			roomDto.setTitle(room.getTitle());
			roomDto.setShortDescription(room.getShortDescription());
			roomDto.setDescription(room.getDescription());
			roomDto.setPersonNumber(room.getPersonNumber());
			roomDto.setDayPrice(room.getDayPrice());
			roomsDto.add(roomDto);
		}
		model.addAttribute("rooms", roomsDto);
		return "index";
	}
	
	@GetMapping("/reservation/{roomId}")
	public String reservationPageForRoom(@PathVariable("roomId")Long roomId,
									Model model) {
		Room room = iRoomService.getRoomById(roomId);
		if(room.getStatus().equals(true)) {			
		model.addAttribute("room", room);		
		return "reservation";
		}else {
			return "index";
		}
	}
	
	@ExceptionHandler(Exception.class)
	public String handleException(final Exception e) {
	    return "forward:/";
	}
	
	

}
