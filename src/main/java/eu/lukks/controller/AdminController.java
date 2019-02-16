package eu.lukks.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationSingle;
import eu.lukks.domain.Room;
import eu.lukks.domain.RoomDto;
import eu.lukks.service.IReservationService;
import eu.lukks.service.IReservationSingleService;
import eu.lukks.service.IRoomService;

@Controller
public class AdminController {
	
	private IReservationService iReservationService;
	private IReservationSingleService iReservationSingleService;
	private IRoomService iRoomSrevice;
	
	@Autowired
	public AdminController(IReservationService iReservationService, IReservationSingleService iReservationSingleService,
			IRoomService iRoomSrevice) {
		super();
		this.iReservationService = iReservationService;
		this.iReservationSingleService = iReservationSingleService;
		this.iRoomSrevice = iRoomSrevice;
	}
	
	
	@GetMapping("/admin/reservations")
	public String getAllReservationsAdmin(Model model) {
		List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0,20));
		model.addAttribute("reservations", reservations);
		return "admin";
	}
	

	@GetMapping("/admin/rooms")
	public String getAllRooms(Model model) {
		List<Room> rooms = iRoomSrevice.getAllRooms();
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
		return "rooms";
	}
	
	@PostMapping("/admin/room/add/new")
	public String createNewRoom(@ModelAttribute Room room, Model model) {
		iRoomSrevice.addNewRoom(room);
		List<Room> roomsget = iRoomSrevice.getAllRooms();
		List<RoomDto> roomsDto = new ArrayList<RoomDto>();
		for(Room room1: roomsget) {
			RoomDto roomDto = new RoomDto();
			roomDto.setId(room1.getId());
			roomDto.setTitle(room1.getTitle());
			roomDto.setShortDescription(room1.getShortDescription());
			roomDto.setDescription(room1.getDescription());
			roomDto.setPersonNumber(room1.getPersonNumber());
			roomDto.setDayPrice(room1.getDayPrice());
			roomsDto.add(roomDto);
		}
		model.addAttribute("rooms", roomsDto);
		String msg = String.format("New room has been created.");
        model.addAttribute("msgStatus", msg);
        return "rooms";
	}
	
	@GetMapping("/admin/reservation/delete/{reservationId}/{roomId}")
	public String deleteReservationById(@PathVariable("reservationId")Long reservationId,
										@PathVariable("roomId")Long roomId,
										Model model) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		Room roomById = iRoomSrevice.getRoomById(roomId);
		iReservationSingleService.deleteReservationSingleDay(roomById, reservationById);
		String reservationIdString = reservationId.toString();
		iReservationService.deleteReservationById(reservationId);
		List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0,20));
		String msg = String.format("Reservation number: " + reservationIdString + " has been deleted");
	    model.addAttribute("msgStatus", msg);
		model.addAttribute("reservations", reservations);
		return "admin";
	}

	@PostMapping("/admin/reservation/update/save/{reservationId}/{roomId}")
	public String updateReservation(@ModelAttribute Reservation updatedReservation,
									@PathVariable("reservationId")Long reservationId,
									@PathVariable("roomId")Long roomId,
									Model model) {
	Reservation reservationById = iReservationService.readReservationById(reservationId);
	LocalDate reservationSingleFromReservationByIdDateFrom = reservationById.getDateFrom();
	LocalDate reservationSingleFromReservationByIdDateTo = reservationById.getDateTo();
	LocalDate reservationSingleFromUpdatedReservationDateFrom = updatedReservation.getDateFrom();
	LocalDate reservationSingleFromUpdatedReservationDateTo = updatedReservation.getDateTo();
	Room roomById = iRoomSrevice.getRoomById(roomId);
		if (!iReservationService.checkDateFromAfterDateTo(updatedReservation.getDateFrom(), updatedReservation.getDateTo())
					) {
								if(!iReservationService.checkByDateSingleInReservations(roomById, reservationById, updatedReservation.getDateFrom(), updatedReservation.getDateTo())) {
									iReservationService.saveReservation(iReservationService.updateReservation(reservationById, updatedReservation));
									iReservationSingleService.deleteReservationSingleDayByDates(roomById, reservationById, reservationSingleFromReservationByIdDateFrom, reservationSingleFromReservationByIdDateTo);
									iReservationSingleService.saveReservationSingleDayByDates(roomById, reservationById, reservationSingleFromUpdatedReservationDateFrom, reservationSingleFromUpdatedReservationDateTo);
//									iReservationService.deleteReservationById(reservationId);
//									iReservationService.saveReservation(reservation);
									String msg = String.format("Reservation number: " + reservationById.getId().toString() + " has been updated");
								    model.addAttribute("msgStatus", msg);
								}else {
									String differentDays = iReservationService.checkByDateSingleInReservationsBookedDays(roomById, reservationById, updatedReservation.getDateFrom(), updatedReservation.getDateTo());
									String msg = String.format("Dates: " + differentDays + " are reserved. Please choose another.");
								    model.addAttribute("msgStatus", msg);
								}
							
							}else {
								String msg = String.format("Reservation dates are incorrect. Please change.");
							    model.addAttribute("msgStatus", msg);
							}


	List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0,20));
	model.addAttribute("reservations", reservations);
	return "admin";
	}

	@GetMapping("/admin/reservation/update/{reservationId}")
	public String updateReservationSite(@PathVariable("reservationId")Long reservationId,
										Model model) {
		Reservation reservation = iReservationService.readReservationById(reservationId);
		model.addAttribute("reservation", reservation);
		return "update";
	}

	@GetMapping("/admin/reservation/show/{reservationId}")
	public String showReservationById(@PathVariable("reservationId")Long reservationId,
										Model model) {
		Reservation reservation = iReservationService.readReservationById(reservationId);
		model.addAttribute("reservation", reservation);
		return "show";
	}

	@PostMapping("/admin/search/date")
	public String searchReservationsByDate(@RequestParam("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
									@RequestParam("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
									Model model) {
		List<Reservation> reservations = iReservationService.searchReservationsByDate(dateFrom, dateTo);
		model.addAttribute("reservations", reservations);
		return "admin";
	}

	@PostMapping("/admin/search/number")
	public String searchReservationsByNumber(@RequestParam("lastReservations") Integer number, Model model) {
		if(number>0) {
			List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0,number));
			model.addAttribute("reservations", reservations);	
		}else {
			String msg = String.format("Number latest reservations must be above 0");
	        model.addAttribute("msg", msg);
		}
		return "admin";
	}
	

}
