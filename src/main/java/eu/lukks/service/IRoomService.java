package eu.lukks.service;

import java.util.List;

import eu.lukks.domain.Room;

public interface IRoomService {

	List<Room> getAllRooms();

	void addNewRoom(Room room);

	Room getRoomById(Long id);

}
