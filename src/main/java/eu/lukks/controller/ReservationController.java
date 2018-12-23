package eu.lukks.controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.message.Message;
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


@GetMapping("/admin")
public String getAllReservationsAdmin(Model model) {
	List<Reservation> reservations = iReservationService.findAllReservations();
	model.addAttribute("reservations", reservations);
	return "admin";
}


@PostMapping("/reservation/add")
public String newReservation(@ModelAttribute Reservation reservation,
								Model model) {
	if (!iReservationSingleService.checkByDate(reservation.getDateFrom(), reservation.getDateTo()) &&
		!iReservationService.checkDateFromAfterDateTo(reservation.getDateFrom(), reservation.getDateTo()) &&
		iReservationService.checkDateFromAfterDateTo(reservation.getDateFrom(), LocalDate.now().minusDays(1))	
			) {
		iReservationService.saveReservation(reservation);
		iReservationSingleService.saveReservationSingleDay(reservation.getDateFrom(), reservation.getDateTo());
	}
	
//	else Message o niepowodzeniu
	
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
	return "admin";
}

@PostMapping("/reservation/update/save/{reservationId}")
public String updateReservation(@ModelAttribute Reservation reservation,
								@PathVariable("reservationId")Long reservationId,
								Model model) {
Reservation reservationById = iReservationService.readReservationById(reservationId);
iReservationSingleService.deleteReservationSingleDay(reservationById.getDateFrom(), reservationById.getDateTo());
reservationById.setDateFrom(reservation.getDateFrom());
reservationById.setDateTo(reservation.getDateTo());
iReservationService.saveReservation(reservationById);
iReservationSingleService.saveReservationSingleDay(reservation.getDateFrom(), reservation.getDateTo());
List<Reservation> reservations = iReservationService.findAllReservations();
model.addAttribute("reservations", reservations);
return "admin";
}

@PostMapping("/reservation/update/{reservationId}")
public String updateReservationSite(@PathVariable("reservationId")Long reservationId,
									Model model) {
	Reservation reservation = iReservationService.readReservationById(reservationId);
	model.addAttribute("reservation", reservation);
	return "update";
}


}
