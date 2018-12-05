package eu.lukks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
public String newReservation(@ModelAttribute Reservation reservation,
								Model model) {
	iReservationService.saveReservation(reservation);
	List<Reservation> reservations = iReservationService.findAllReservations();
	model.addAttribute("reservations", reservations);
	return "index";
}
	
@GetMapping("/reservation/delete/{reservationId}")
public String deleteReservationById(@PathVariable("reservationId")Long reservationId,
													Model model) {
	iReservationService.deleteReservationById(reservationId);
	List<Reservation> reservationList = iReservationService.findAllReservations();
	model.addAttribute("reservations", reservationList);
	return "index";
}

@PostMapping("/reservation/update/save/{reservationId}")
public String updateReservation(@ModelAttribute Reservation reservation,
								@PathVariable("reservationId")Long reservationId,
								Model model) {
Reservation reservationById = iReservationService.readReservationById(reservationId);
reservationById.setName(reservation.getName());
reservationById.setDate(reservation.getDate());
iReservationService.saveReservation(reservationById);
List<Reservation> reservations = iReservationService.findAllReservations();
model.addAttribute("reservations", reservations);
return "index";
}

@PostMapping("/reservation/update/{reservationId}")
public String updateReservationSite(@PathVariable("reservationId")Long reservationId,
									Model model) {
	Reservation reservation = iReservationService.readReservationById(reservationId);
	model.addAttribute("reservation", reservation);
	return "update";
}


}
