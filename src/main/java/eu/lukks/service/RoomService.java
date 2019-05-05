package eu.lukks.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.lukks.domain.Room;
import eu.lukks.domain.RoomDto;
import eu.lukks.repository.RoomRepository;

@Service
public class RoomService implements IRoomService {

	private RoomRepository roomRepository;
	private static final Logger LOGGER = Logger.getLogger(RoomService.class.getName());

	@Autowired
	public RoomService(RoomRepository roomRepository) {
		super();
		this.roomRepository = roomRepository;
	}

	@Override
	public List<Room> getAllRooms() {
		LOGGER.info("Get all rooms list.");
		return roomRepository.findAll();
	}

	@Override
	public void addNewRoom(Room room) {
		LOGGER.info("New room created: " + room.getTitle());
		roomRepository.save(room);
	}

	@Override
	public Room getRoomById(Long id) {
		LOGGER.info("Get room by id: " + id.toString());
		return roomRepository.findById(id).orElse(null);
	}

	@Override
	public void saveRoom(Room room) {
		LOGGER.info("Save room " + room.getTitle());
		roomRepository.save(room);
	}

	@Override
	public void deleteRoomFiles(Room room) {
		String uploadDirectory = System.getProperty("user.dir") + "/files";
		File folder = new File(uploadDirectory + "/" + room.getId().toString());
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				listOfFiles[i].delete();
			}
		}
		try {
			FileUtils.deleteDirectory(folder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Delete files fo room id: " + room.getId().toString());
	}

	@Override
	public void deleteRoom(Room room) {
		roomRepository.delete(room);
		LOGGER.info("Delete room id: " + room.getId().toString());
	}

	@Override
	public List<Room> getAllActiveRooms() {
		LOGGER.info("Active rooms list get.");
		return roomRepository.getAllActiveRooms();
	}

	@Override
	public void createDirectoryForRoomFiles(Long id) {
		String roomDirectory = System.getProperty("user.dir") + "/files/" + id.toString();
		new File(roomDirectory).mkdir();
		LOGGER.info("Create directory for room files, room id: " + id.toString());
	}

	@Override
	public List<RoomDto> roomsToRoomsDto(List<Room> rooms) {
		List<RoomDto> roomsDto = new ArrayList<RoomDto>();
		for (Room room : rooms) {
			RoomDto roomDto = new RoomDto();
			roomDto.setId(room.getId());
			roomDto.setTitle(room.getTitle());
			roomDto.setShortDescription(room.getShortDescription());
			roomDto.setDescription(room.getDescription());
			roomDto.setPersonNumber(room.getPersonNumber());
			roomDto.setDayPrice(room.getDayPrice());
			roomDto.setMainPhotoThumb(room.getMainPhotoThumb());
			roomsDto.add(roomDto);
		}
		LOGGER.info("Room list to RoomDto list.");
		return roomsDto;
	}

	@Override
	public List<RoomDto> roomsToRoomsDtoAdmin(List<Room> rooms) {
		List<RoomDto> roomsDto = new ArrayList<RoomDto>();
		for (Room room : rooms) {
			RoomDto roomDto = new RoomDto();
			roomDto.setId(room.getId());
			roomDto.setTitle(room.getTitle());
			roomDto.setShortDescription(room.getShortDescription());
			roomDto.setDescription(room.getDescription());
			roomDto.setPersonNumber(room.getPersonNumber());
			roomDto.setDayPrice(room.getDayPrice());
			roomDto.setStatus(room.getStatus());
			roomsDto.add(roomDto);
		}
		LOGGER.info("Room list to RoomDto list.");
		return roomsDto;
	}

	@Override
	public RoomDto roomToRoomDto(Room room) {
		RoomDto roomDto = new RoomDto();
		roomDto.setId(room.getId());
		roomDto.setTitle(room.getTitle());
		roomDto.setShortDescription(room.getShortDescription());
		roomDto.setDescription(room.getDescription());
		roomDto.setPersonNumber(room.getPersonNumber());
		roomDto.setDayPrice(room.getDayPrice());
		roomDto.setStatus(room.getStatus());
		LOGGER.info("Room to RoomDto.");
		return roomDto;
	}

	@Override
	public Room updateRoomFromRoomDto(Room updatedRoom, RoomDto updateRoomDto) {
		updatedRoom.setTitle(updateRoomDto.getTitle());
		updatedRoom.setShortDescription(updateRoomDto.getShortDescription());
		updatedRoom.setDescription(updateRoomDto.getDescription());
		updatedRoom.setPersonNumber(updateRoomDto.getPersonNumber());
		updatedRoom.setDayPrice(updateRoomDto.getDayPrice());
		LOGGER.info("Update room id: " + updatedRoom.getId().toString());
		return updatedRoom;
	}
}
