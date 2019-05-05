package eu.lukks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.lukks.domain.User;
import eu.lukks.service.IUserService;

@Controller
public class UserController {

	private IUserService iUserService;

	@Autowired
	public UserController(IUserService iUserService) {
		super();
		this.iUserService = iUserService;
	}

	@PostMapping("/admin/user/password/save")
	public String adminPasswordPage(@RequestParam("passwd1") String password1,
			@RequestParam("passwd2") String password2, Model model) {
		if (password1.equals(password2)) {
			User user = iUserService.getUserByUsername("admin");
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String password = encoder.encode(password1);
			user.setPassword(password);
			iUserService.saveUser(user);
			String msg = String.format("Password has been changed.");
			model.addAttribute("msgStatus", msg);
		} else {
			String msg = String.format("The entered passwords don't match. Try again");
			model.addAttribute("msgStatus", msg);
		}
		return "reservations";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(final Exception e) {
		return "forward:/admin/reservations";
	}

}
