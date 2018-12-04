package eu.lukks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import eu.lukks.domain.Reservation;
import eu.lukks.service.IReservationService;

@Controller
public class ReservationController {

private IReservationService iReservationService;

@Autowired
public ReservationController(IReservationService iReservationService) {
	super();
	this.iReservationService = iReservationService;
}

@GetMapping("/")
public String getAllReservations(Model model) {
	List<Reservation> reservations = iReservationService.findAllReservations();
	model.addAttribute("reservations", reservations);
	return "index";
}

@PostMapping("/reservation/add")
public String newReservation(@ModelAttribute Reservation reservation) {
	iReservationService.saveReservation(reservation);
	return "index";
}
	
	
	
	
}
