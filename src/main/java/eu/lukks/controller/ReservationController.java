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
import eu.lukks.service.IReservationSingleService;

@Controller
public class ReservationController {

private IReservationService iReservationService;
private IReservationSingleService iReservationSingleService;


@Autowired
public ReservationController(IReservationService iReservationService,
		IReservationSingleService iReservationSingleService) {
	super();
	this.iReservationService = iReservationService;
	this.iReservationSingleService = iReservationSingleService;
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
	iReservationSingleService.saveReservationSingleDay(reservation.getDateFrom(), reservation.getDateTo());
	List<Reservation> reservations = iReservationService.findAllReservations();
	model.addAttribute("reservations", reservations);
	return "index";
}
	
@GetMapping("/reservation/delete/{reservationId}")
public String deleteReservationById(@PathVariable("reservationId")Long reservationId,
													Model model) {
	Reservation reservationById = iReservationService.readReservationById(reservationId);
	iReservationSingleService.deleteReservationSingleDay(reservationById.getDateFrom(), reservationById.getDateTo());
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
reservationById.setDateFrom(reservation.getDateFrom());
reservationById.setDateTo(reservation.getDateTo());
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
