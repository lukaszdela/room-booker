package eu.lukks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import eu.lukks.domain.Photo;
import eu.lukks.domain.Reservation;
import eu.lukks.domain.Room;
import eu.lukks.domain.RoomDto;
import eu.lukks.service.IPhotoService;
import eu.lukks.service.IReservationService;
import eu.lukks.service.IReservationSingleService;
import eu.lukks.service.IRoomService;

@Controller
public class RoomController {

	private IReservationService iReservationService;
	private IReservationSingleService iReservationSingleService;
	private IRoomService iRoomSrevice;
	private IPhotoService iPhotoService;

	@Autowired
	public RoomController(IReservationService iReservationService, IReservationSingleService iReservationSingleService,
			IRoomService iRoomSrevice, IPhotoService iPhotoService) {
		super();
		this.iReservationService = iReservationService;
		this.iReservationSingleService = iReservationSingleService;
		this.iRoomSrevice = iRoomSrevice;
		this.iPhotoService = iPhotoService;
	}

	@GetMapping("/admin/rooms")
	public String getAllRooms(Model model) {
		List<Room> rooms = iRoomSrevice.getAllRooms();
		model.addAttribute("rooms", iRoomSrevice.roomsToRoomsDtoAdmin(rooms));
		return "rooms";
	}

	@PostMapping("/admin/room/add/new")
	public String createNewRoom(@ModelAttribute Room room, Model model) {
		room.setStatus(false);
		iRoomSrevice.addNewRoom(room);
		iRoomSrevice.createDirectoryForRoomFiles(room.getId());
		List<Room> rooms = iRoomSrevice.getAllRooms();
		model.addAttribute("rooms", iRoomSrevice.roomsToRoomsDtoAdmin(rooms));
		String msg = String.format("New room has been created.");
		model.addAttribute("msgStatus", msg);
		return "rooms";
	}

	@GetMapping("/admin/room/reservations/{roomId}")
	public String roomReservations(@PathVariable("roomId") Long roomId, Model model) {
		Room roomById = iRoomSrevice.getRoomById(roomId);
		List<Reservation> reservations = roomById.getReservations();

		String msg = String.format("Reservations for room " + roomById.getTitle() + ".");
		model.addAttribute("msgStatus", msg);
		model.addAttribute("reservations", iReservationService.reservationToReservationDto(reservations));
		return "reservations";
	}

	@GetMapping("/admin/room/update/{roomId}")
	public String updateRoom(@PathVariable("roomId") Long roomId, Model model) {
		Room room = iRoomSrevice.getRoomById(roomId);
		model.addAttribute("room", iRoomSrevice.roomToRoomDto(room));
		return "updateroom";
	}

	@PostMapping("/admin/room/update/save/{roomId}")
	public String updateRoomSave(@PathVariable("roomId") Long roomId, @ModelAttribute RoomDto updateRoomDto,
			Model model) {
		Room updatedRoom = iRoomSrevice.getRoomById(roomId);
		iRoomSrevice.saveRoom(iRoomSrevice.updateRoomFromRoomDto(updatedRoom, updateRoomDto));
		List<Room> rooms = iRoomSrevice.getAllRooms();
		String msg = String.format("Room " + updatedRoom.getTitle() + " has been updated.");
		model.addAttribute("msgStatus", msg);
		model.addAttribute("rooms", iRoomSrevice.roomsToRoomsDtoAdmin(rooms));
		return "rooms";
	}

	@GetMapping("/admin/room/delete/{roomId}")
	public String deleteRoom(@PathVariable("roomId") Long roomId, Model model) {
		Room roomById = iRoomSrevice.getRoomById(roomId);
		String roomTitle = roomById.getTitle();
		List<Reservation> reservationsRoomById = roomById.getReservations();
		List<Photo> photosRoomById = roomById.getPhotos();

		if (reservationsRoomById != null) {
			for (Reservation reservation : reservationsRoomById) {
				iReservationSingleService.deleteReservationSingleDay(reservation.getRoom(), reservation);
				iReservationService.deleteReservationById(reservation.getId());
			}
		}
		if (photosRoomById != null) {
			iPhotoService.deleteRoomPhotos(roomById);
		}
		iRoomSrevice.deleteRoomFiles(roomById);
		iRoomSrevice.deleteRoom(roomById);

		List<Room> rooms = iRoomSrevice.getAllRooms();
		String msg = String.format("Room " + roomTitle + " has been deleted.");
		model.addAttribute("msgStatus", msg);
		model.addAttribute("rooms", iRoomSrevice.roomsToRoomsDtoAdmin(rooms));
		return "rooms";
	}

	@GetMapping("/admin/room/enable/{roomId}")
	public String enableRoom(@PathVariable("roomId") Long roomId, Model model) {
		Room roomById = iRoomSrevice.getRoomById(roomId);
		roomById.setStatus(true);
		iRoomSrevice.saveRoom(roomById);
		List<Room> rooms = iRoomSrevice.getAllRooms();
		String msg = String.format("Room " + roomById.getTitle() + " has been enabled.");
		model.addAttribute("msgStatus", msg);
		model.addAttribute("rooms", iRoomSrevice.roomsToRoomsDtoAdmin(rooms));
		return "rooms";
	}

	@GetMapping("/admin/room/disable/{roomId}")
	public String disableRoom(@PathVariable("roomId") Long roomId, Model model) {
		Room roomById = iRoomSrevice.getRoomById(roomId);
		roomById.setStatus(false);
		iRoomSrevice.saveRoom(roomById);
		List<Room> rooms = iRoomSrevice.getAllRooms();
		String msg = String.format("Room " + roomById.getTitle() + " has been disabled.");
		model.addAttribute("msgStatus", msg);
		model.addAttribute("rooms", iRoomSrevice.roomsToRoomsDtoAdmin(rooms));
		return "rooms";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(final Exception e) {
		return "forward:/admin/rooms";
	}

}
