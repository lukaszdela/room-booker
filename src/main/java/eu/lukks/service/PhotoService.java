package eu.lukks.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eu.lukks.domain.Photo;
import eu.lukks.domain.PhotoFile;
import eu.lukks.domain.Room;
import eu.lukks.repository.PhotoRepository;

@Service
public class PhotoService implements IPhotoService {

	private PhotoRepository photoRepository;
	private IRoomService iRoomService;
	private static final Logger LOGGER = Logger.getLogger(PhotoService.class.getName());

	@Autowired
	public PhotoService(PhotoRepository photoRepository, IRoomService iRoomService) {
		super();
		this.photoRepository = photoRepository;
		this.iRoomService = iRoomService;
	}

	@Override
	public void unsetPhoto(Room room, String filename) {
		List<Photo> roomPhotos = new ArrayList<Photo>(room.getPhotos());
		for (Photo photo : roomPhotos) {
			if (photo.getPathAndFilename().equals(filename)) {
				photoRepository.deleteById(photo.getId());
			}
		}
		LOGGER.info("Unset photo for room id: " + room.getId().toString());
	}

	@Override
	public void deleteRoomPhotos(Room room) {
		List<Photo> roomPhotos = new ArrayList<Photo>(room.getPhotos());
		for (Photo photo : roomPhotos) {
			photoRepository.deleteById(photo.getId());
		}
		LOGGER.info("Delete photos for room id :" + room.getId().toString());
	}

	@Override
	public void setPhoto(Room room, String filename, String filenameAndPath) {
		List<Photo> roomPhotos = room.getPhotos();
		Photo photo = new Photo();
		photo.setFilename(filename);
		photo.setPathAndFilename(filenameAndPath);
		photo.setRoom(room);
		photoRepository.save(photo);
		roomPhotos.add(photo);
		iRoomService.saveRoom(room);
		LOGGER.info("Set photo for room id: " + room.getId().toString());
	}

	@Override
	public List<PhotoFile> fileListRoomDirectory(Room room, String roomId, String uploadDirectory) {
		List<PhotoFile> filelist = new ArrayList<PhotoFile>();
		File folder = new File(uploadDirectory + "/" + roomId);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				PhotoFile photoFile = new PhotoFile();
				photoFile.setFilename(listOfFiles[i].getName());
				photoFile.setFullPath("/upload/" + roomId + "/" + listOfFiles[i].getName());
				photoFile.setChecked(false);
				for (Photo photo : room.getPhotos()) {
					if (listOfFiles[i].getName().equals(photo.getFilename())) {
						photoFile.setChecked(true);
					}
				}
				filelist.add(photoFile);

			}
		}
		LOGGER.info("File list for room id: " + roomId);
		return filelist;
	}

	@Override
	public void roomDirectoryCheckAndCreate(String roomId) {
		String uploadDirectory = System.getProperty("user.dir") + "/files";
		String roomPath = uploadDirectory + "/" + roomId;
		File roomDirectory = new File(roomPath);
		if (!roomDirectory.exists()) {
			roomDirectory.mkdir();
		}
		LOGGER.info("Check if directory for room exists, if not create for room id: " + roomId);
	}

	@Override
	public void uploadFiles(String roomId, MultipartFile[] files) {
		String uploadDirectory = System.getProperty("user.dir") + "/files";
		StringBuilder fileNames = new StringBuilder();
		String roomPath = uploadDirectory + "/" + roomId;
		File roomDirectory = new File(roomPath);

		if (roomDirectory.exists() && roomDirectory.isDirectory()) {
			for (MultipartFile file : files) {
				Path fileNameAndPath = Paths.get(roomPath, file.getOriginalFilename());
				fileNames.append(file.getOriginalFilename());
				try {
					Files.write(fileNameAndPath, file.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			roomDirectory.mkdir();
			for (MultipartFile file : files) {
				Path fileNameAndPath = Paths.get(roomPath, file.getOriginalFilename());
				fileNames.append(file.getOriginalFilename());
				try {
					Files.write(fileNameAndPath, file.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		LOGGER.info("Upload files for room id: " + roomId);
	}

	@Override
	public void deleteFile(String filename, String roomId) {
		String uploadDirectory = System.getProperty("user.dir") + "/files";
		try {
			Files.deleteIfExists(Paths.get(uploadDirectory + "/" + roomId + "/", filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Delete file " + filename + " for room id: " + roomId);
	}

}
