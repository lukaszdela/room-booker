package eu.lukks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.lukks.domain.Photo;
import eu.lukks.repository.PhotoRepository;

@Service
public class PhotoService implements IPhotoService{
	
	private PhotoRepository photoRepository;

	@Autowired
	public PhotoService(PhotoRepository photoRepository) {
		super();
		this.photoRepository = photoRepository;
	}
	
	@Override
	public void deletePhoto(Photo photo){
		photoRepository.delete(photo);
	}

}
