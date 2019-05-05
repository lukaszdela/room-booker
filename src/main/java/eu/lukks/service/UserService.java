package eu.lukks.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.lukks.domain.User;
import eu.lukks.repository.UserRepository;

@Service
public class UserService implements IUserService{
	
	private UserRepository userRepository;
	private static final Logger LOGGER = Logger.getLogger(RoomService.class.getName());

	@Autowired
	public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@Override
	public User getUserByUsername(String username) {
		LOGGER.info("Get user: " + username);
		return userRepository.getUserByUsername(username);
	}
	
	@Override
	public void saveUser(User user) {
		LOGGER.info("Save user " + user.getUsername());
		userRepository.save(user);
		}

}
