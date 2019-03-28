package eu.lukks.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import eu.lukks.domain.Photo;
import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationDto;
import eu.lukks.domain.ReservationSingle;
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
	
	@GetMapping("/admin/room/reservations/{roomId}")
	public String roomReservations(@PathVariable("roomId")Long roomId,
									Model model) {
		Room roomById = iRoomSrevice.getRoomById(roomId);
		List<Reservation> reservations = roomById.getReservations();
		List<ReservationDto> reservationDtos = new ArrayList<ReservationDto>();
		for(Reservation reservation: reservations) {
			ReservationDto reservationDto = new ReservationDto();
			reservationDto.setId(reservation.getId());
			reservationDto.setName(reservation.getName());
			reservationDto.setSurname(reservation.getSurname());
			reservationDto.setAddress(reservation.getAddress());
			reservationDto.setZip(reservation.getZip());
			reservationDto.setCity(reservation.getCity());
			reservationDto.setPhone(reservation.getPhone());
			reservationDto.setMail(reservation.getMail());
			reservationDto.setPrice(reservation.getPrice());
			reservationDto.setRoomTitle(reservation.getRoomTitle());
			reservationDto.setBreakfast(reservation.getBreakfast());
			reservationDto.setParking(reservation.getParking());
			reservationDto.setDateFrom(reservation.getDateFrom());
			reservationDto.setDateTo(reservation.getDateTo());
			reservationDto.setRoomId(reservation.getRoom().getId());
			reservationDtos.add(reservationDto);
		}
		model.addAttribute("reservations", reservationDtos);
		return "admin";
	}
	
	@GetMapping("/admin/room/show/{roomId}")
	public String showRoom(@PathVariable("roomId")Long roomId,
							Model model) {
		Room roomById = iRoomSrevice.getRoomById(roomId);
		RoomDto roomDto = new RoomDto();
		
			roomDto.setId(roomById.getId());
			roomDto.setTitle(roomById.getTitle());
			roomDto.setShortDescription(roomById.getShortDescription());
			roomDto.setDescription(roomById.getDescription());
			roomDto.setPersonNumber(roomById.getPersonNumber());
			roomDto.setDayPrice(roomById.getDayPrice());
		
		model.addAttribute("room", roomDto);
		return "showroom";
	}
	
	@GetMapping("/admin/room/update/{roomId}")
	public String updateRoom(@PathVariable("roomId")Long roomId,
								Model model) {
		Room roomById = iRoomSrevice.getRoomById(roomId);
		RoomDto roomDto = new RoomDto();
		
		roomDto.setId(roomById.getId());
		roomDto.setTitle(roomById.getTitle());
		roomDto.setShortDescription(roomById.getShortDescription());
		roomDto.setDescription(roomById.getDescription());
		roomDto.setPersonNumber(roomById.getPersonNumber());
		roomDto.setDayPrice(roomById.getDayPrice());
		
		model.addAttribute("room", roomDto);
		return "updateroom";
	}
	
	@PostMapping("/admin/room/update/save/{roomId}")
	public String updateRoomSave(@PathVariable("roomId")Long roomId,
									@ModelAttribute RoomDto updateRoomDto,
									Model model) {
		Room updatedRoom = iRoomSrevice.getRoomById(roomId);
		updatedRoom.setTitle(updateRoomDto.getTitle());
		updatedRoom.setShortDescription(updateRoomDto.getShortDescription());
		updatedRoom.setDescription(updateRoomDto.getDescription());
		updatedRoom.setPersonNumber(updateRoomDto.getPersonNumber());
		updatedRoom.setDayPrice(updateRoomDto.getDayPrice());
		iRoomSrevice.saveRoom(updatedRoom);
		
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
	
	@GetMapping("/admin/room/delete/{roomId}")
	public String deleteRoom(@PathVariable("roomId")Long roomId,
								Model model){
	
		Room roomById = iRoomSrevice.getRoomById(roomId);
		List<Reservation> reservationsRoomById = roomById.getReservations();
		List<Photo> photosRoomById = roomById.getPhotos();
		
		if(reservationsRoomById != null) {
					for(Reservation reservation: reservationsRoomById) {
						iReservationSingleService.deleteReservationSingleDay(reservation.getRoom(), reservation);
						iReservationService.deleteReservationById(reservation.getId());
					}
			}
		if(photosRoomById != null) {
			for(Photo photo: photosRoomById) {
				iPhotoService.deletePhoto(photo);
			}
		}
		
		iRoomSrevice.deleteRoom(roomById);
		
		
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
	
	
}
