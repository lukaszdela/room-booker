package eu.lukks.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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


@PostMapping("/reservation/add")
public String newReservation(@ModelAttribute Reservation reservation,
								Model model) {
	if (!iReservationService.checkDateFromAfterDateTo(reservation.getDateFrom(), reservation.getDateTo()) &&
		iReservationService.checkDateFromAfterDateTo(reservation.getDateFrom(), LocalDate.now().minusDays(1))	
			) {
		if (!iReservationSingleService.checkByDate(reservation.getDateFrom(), reservation.getDateTo())) {
			reservation.setPrice(iReservationService.calculatePriceForNewReservation(reservation));
			iReservationService.saveReservation(reservation);
			iReservationSingleService.saveReservationSingleDay(reservation.getDateFrom(), reservation.getDateTo());
			String msg = String.format("Room is reserved from " + reservation.getDateFrom() + " to " + reservation.getDateTo());
	        model.addAttribute("msg", msg);
		}else {
			String msg = String.format("Dates: " + iReservationSingleService.daysReservedForNewReservation(reservation.getDateFrom(), reservation.getDateTo()) + " are reserved. Please choose another reservation dates.");
	        model.addAttribute("msg", msg);
		}
		
	}else {
		String msg = String.format("Selected dates are incorrect. Please try again.");
        model.addAttribute("msg", msg);
	}
	
	List<Reservation> reservations = iReservationService.findAllReservations();
	model.addAttribute("reservations", reservations);
	return "index";
}


}
