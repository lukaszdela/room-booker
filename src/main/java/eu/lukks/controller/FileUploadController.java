package eu.lukks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import eu.lukks.domain.Room;
import eu.lukks.service.IPhotoService;
import eu.lukks.service.IRoomService;

@Controller
public class FileUploadController {

	private IRoomService iRoomService;
	private IPhotoService iPhotoService;

	@Autowired
	public FileUploadController(IRoomService iRoomService, IPhotoService iPhotoService) {
		super();
		this.iRoomService = iRoomService;
		this.iPhotoService = iPhotoService;
	}

	public static String uploadDirectory = System.getProperty("user.dir") + "/files";

	@GetMapping("/admin/photos/{roomId}")
	public String uploadPage(Model model, @PathVariable("roomId") Long roomId) {
		Room room = iRoomService.getRoomById(roomId);
		iPhotoService.roomDirectoryCheckAndCreate(room.getId().toString());

		model.addAttribute("mainphoto", room.getMainPhoto());
		model.addAttribute("mainthumb", room.getMainPhotoThumb());
		model.addAttribute("roomId", roomId);
		model.addAttribute("files", iPhotoService.fileListRoomDirectory(room, roomId.toString(), uploadDirectory));
		return "photos";
	}

	@RequestMapping("/admin/file/upload/")
	public String uploadFile(Model model, @RequestParam(name = "roomId") Long roomId,
			@RequestParam("file") MultipartFile[] files) {
		Room room = iRoomService.getRoomById(roomId);
		iPhotoService.uploadFiles(roomId.toString(), files);

		model.addAttribute("mainphoto", room.getMainPhoto());
		model.addAttribute("mainthumb", room.getMainPhotoThumb());
		model.addAttribute("roomId", roomId.toString());
		model.addAttribute("files", iPhotoService.fileListRoomDirectory(room, roomId.toString(), uploadDirectory));
		return "photos";
	}

	@GetMapping("/admin/file/delete/{roomId}/{filename:.+}")
	public String deleteFile(@PathVariable("roomId") Long roomId, @PathVariable("filename") String filename,
			Model model) {
		Room room = iRoomService.getRoomById(roomId);
		iPhotoService.deleteFile(filename, roomId.toString());

		model.addAttribute("mainphoto", room.getMainPhoto());
		model.addAttribute("mainthumb", room.getMainPhotoThumb());
		model.addAttribute("roomId", roomId);
		model.addAttribute("files", iPhotoService.fileListRoomDirectory(room, roomId.toString(), uploadDirectory));
		return "photos";
	}

	@GetMapping("/admin/file/addthumb/{roomId}/{filename:.+}")
	public String addMainPhotoThumb(@PathVariable("roomId") Long roomId, @PathVariable("filename") String filename,
			Model model) {
		Room room = iRoomService.getRoomById(roomId);
		String path = "/upload/" + roomId.toString() + "/" + filename;
		room.setMainPhotoThumb(path);
		iRoomService.saveRoom(room);

		model.addAttribute("mainphoto", room.getMainPhoto());
		model.addAttribute("mainthumb", room.getMainPhotoThumb());
		model.addAttribute("roomId", roomId);
		model.addAttribute("files", iPhotoService.fileListRoomDirectory(room, roomId.toString(), uploadDirectory));
		return "photos";
	}

	@GetMapping("/admin/file/mainphoto/{roomId}/{filename:.+}")
	public String setMainPhoto(@PathVariable("roomId") Long roomId, @PathVariable("filename") String filename,
			Model model) {
		Room room = iRoomService.getRoomById(roomId);
		String path = "/upload/" + roomId.toString() + "/" + filename;
		room.setMainPhoto(path);
		iRoomService.saveRoom(room);

		model.addAttribute("mainphoto", room.getMainPhoto());
		model.addAttribute("mainthumb", room.getMainPhotoThumb());
		model.addAttribute("roomId", roomId);
		model.addAttribute("files", iPhotoService.fileListRoomDirectory(room, roomId.toString(), uploadDirectory));
		return "photos";
	}

	@GetMapping("/admin/file/photo/set/{roomId}/{filename:.+}")
	public String setPhoto(@PathVariable("roomId") Long roomId, @PathVariable("filename") String filename,
			Model model) {
		Room room = iRoomService.getRoomById(roomId);
		String path = "/upload/" + roomId.toString() + "/" + filename;
		iPhotoService.setPhoto(room, filename, path);

		model.addAttribute("mainphoto", room.getMainPhoto());
		model.addAttribute("mainthumb", room.getMainPhotoThumb());
		model.addAttribute("roomId", roomId);
		model.addAttribute("files", iPhotoService.fileListRoomDirectory(room, roomId.toString(), uploadDirectory));
		return "photos";
	}

	@GetMapping("/admin/file/photo/unset/{roomId}/{filename:.+}")
	public String unsetPhoto(@PathVariable("roomId") Long roomId, @PathVariable("filename") String filename,
			Model model) {
		Room room = iRoomService.getRoomById(roomId);
		String path = "/upload/" + roomId.toString() + "/" + filename;
		iPhotoService.unsetPhoto(room, path);

		model.addAttribute("mainphoto", room.getMainPhoto());
		model.addAttribute("mainthumb", room.getMainPhotoThumb());
		model.addAttribute("roomId", roomId);
		model.addAttribute("files", iPhotoService.fileListRoomDirectory(room, roomId.toString(), uploadDirectory));
		return "photos";
	}

	@ExceptionHandler(Exception.class)
	public void handleException(final Exception e) {

	}

}
