package eu.lukks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.lukks.domain.Room;
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
	
	

}
