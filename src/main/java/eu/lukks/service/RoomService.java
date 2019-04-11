package eu.lukks.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.lukks.domain.Photo;
import eu.lukks.domain.PhotoFile;
import eu.lukks.domain.Room;
import eu.lukks.domain.RoomDto;
import eu.lukks.repository.RoomRepository;

@Service
public class RoomService implements IRoomService{
	
	private RoomRepository roomRepository;

	@Autowired
	public RoomService(RoomRepository roomRepository) {
		super();
		this.roomRepository = roomRepository;
	}
	
	@Override
	public List<Room> getAllRooms(){
		return roomRepository.findAll();
	}
	
	@Override
	public void addNewRoom(Room room) {
		roomRepository.save(room);
	}
	
	@Override
	public Room getRoomById(Long id) {
		return roomRepository.findById(id).orElse(null);
	}
	
	@Override
	public void saveRoom(Room room) {
		roomRepository.save(room);
	}
	
	@Override
	public void deleteRoomFiles(Room room) {
		String uploadDirectory = System.getProperty("user.dir")+"/files";
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
	}
	@Override
	public void deleteRoom(Room room) {
		roomRepository.delete(room);
	}
	
	@Override
	public List<Room> getAllActiveRooms(){
		return roomRepository.getAllActiveRooms();
	}
	
	@Override
	public void createDirectoryForRoomFiles(Long id) {
		String roomDirectory = System.getProperty("user.dir")+"/files/"+id.toString();
		new File(roomDirectory).mkdir();
	}
	
	@Override
	public List<RoomDto> roomsToRoomsDto(List<Room> rooms){
		List<RoomDto> roomsDto = new ArrayList<RoomDto>();
		for(Room room: rooms) {
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
		return roomsDto;
	}
	
	@Override
	public List<RoomDto> roomsToRoomsDtoAdmin(List<Room> rooms){
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
		return roomDto;
	}
	
	@Override
	public Room updateRoomFromRoomDto(Room updatedRoom, RoomDto updateRoomDto) {
		updatedRoom.setTitle(updateRoomDto.getTitle());
		updatedRoom.setShortDescription(updateRoomDto.getShortDescription());
		updatedRoom.setDescription(updateRoomDto.getDescription());
		updatedRoom.setPersonNumber(updateRoomDto.getPersonNumber());
		updatedRoom.setDayPrice(updateRoomDto.getDayPrice());
		return updatedRoom;
	}
}
