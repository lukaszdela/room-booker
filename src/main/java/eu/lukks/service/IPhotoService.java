package eu.lukks.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import eu.lukks.domain.Photo;
import eu.lukks.domain.PhotoFile;
import eu.lukks.domain.Room;

public interface IPhotoService {

	void unsetPhoto(Room room, String filename);

	void setPhoto(Room room, String filename, String filenameAndPath);

	List<PhotoFile> fileListRoomDirectory(Room room, String roomId, String uploadDirectory);

	void uploadFiles(String roomId, MultipartFile[] files);

	void deleteFile(String filename, String roomId);

	void deleteRoomPhotos(Room room);

	void roomDirectoryCheckAndCreate(String roomId);

}
