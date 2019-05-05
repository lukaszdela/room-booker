package eu.lukks.service;

import eu.lukks.domain.User;

public interface IUserService {

	User getUserByUsername(String username);

	void saveUser(User user);

}
