package eu.lukks.service;

import java.util.List;

import eu.lukks.domain.Room;
import eu.lukks.domain.RoomDto;

public interface IRoomService {

	List<Room> getAllRooms();

	void addNewRoom(Room room);

	Room getRoomById(Long id);

	void saveRoom(Room room);

	void deleteRoom(Room room);

	List<Room> getAllActiveRooms();

	void createDirectoryForRoomFiles(Long id);

	void deleteRoomFiles(Room room);

	List<RoomDto> roomsToRoomsDto(List<Room> rooms);

	List<RoomDto> roomsToRoomsDtoAdmin(List<Room> rooms);

	RoomDto roomToRoomDto(Room room);

	Room updateRoomFromRoomDto(Room updatedRoom, RoomDto updateRoomDto);

}
