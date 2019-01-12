package eu.lukks.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

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
	if (!iReservationService.checkDateFromAfterDateTo(reservation.getDateFrom(), reservation.getDateTo()) &&
		iReservationService.checkDateFromAfterDateTo(reservation.getDateFrom(), LocalDate.now().minusDays(1))	
			) {
		if (!iReservationSingleService.checkByDate(reservation.getDateFrom(), reservation.getDateTo())) {
			reservation.setPrice(iReservationService.howManyDays(reservation.getDateFrom(), reservation.getDateTo()) * 120);
			iReservationService.saveReservation(reservation);
			iReservationSingleService.saveReservationSingleDay(reservation.getDateFrom(), reservation.getDateTo());
		}else {
			String msg = String.format("The selected date is reserved. Please choose another one.");
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

@PostMapping("/admin/search")
public String searchReservations(@RequestParam("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
								@RequestParam("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
								@RequestParam("name")String name,
								@RequestParam("surname")String surname,
								Model model) {
	List<Reservation> reservations = iReservationService.searchReservations(dateFrom, dateTo, name, surname);
	model.addAttribute("reservations", reservations);
	return "admin";
}

//@PostMapping("/admin/search")
//public String searchReservations(@RequestParam("name")String name, @RequestParam("surname")String surname,
//								Model model) {
//	List<Reservation> reservations = iReservationService.searchReservationsByName(name, surname);
//	model.addAttribute("reservations", reservations);
//	return "admin";
//}


}
