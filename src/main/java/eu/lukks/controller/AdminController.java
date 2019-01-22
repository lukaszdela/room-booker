package eu.lukks.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.lukks.domain.Reservation;
import eu.lukks.service.IReservationService;
import eu.lukks.service.IReservationSingleService;

@Controller
public class AdminController {
	
	private IReservationService iReservationService;
	private IReservationSingleService iReservationSingleService;
	
	@Autowired
	public AdminController(IReservationService iReservationService,
			IReservationSingleService iReservationSingleService) {
		super();
		this.iReservationService = iReservationService;
		this.iReservationSingleService = iReservationSingleService;
	}
	
	@GetMapping("/admin")
	public String getAllReservationsAdmin(Model model) {
		List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0,20));
		model.addAttribute("reservations", reservations);
		return "admin";
	}
	
	@GetMapping("/admin/reservation/delete/{reservationId}")
	public String deleteReservationById(@PathVariable("reservationId")Long reservationId, Model model) {
		Reservation reservation = iReservationService.readReservationById(reservationId);
		iReservationSingleService.deleteReservationSingleDay(reservation.getDateFrom(), reservation.getDateTo());
		iReservationService.deleteReservationById(reservationId);
		List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0,20));
		String msg = String.format("Reservation number: " + reservationId.toString() + " has been deleted");
	    model.addAttribute("msgStatus", msg);
		model.addAttribute("reservations", reservations);
		return "admin";
	}

	@PostMapping("/admin/reservation/update/save/{reservationId}")
	public String updateReservation(@ModelAttribute Reservation updatedReservation,
									@PathVariable("reservationId")Long reservationId,
									Model model) {
	Reservation reservationById = iReservationService.readReservationById(reservationId);
		if (!iReservationService.checkDateFromAfterDateTo(updatedReservation.getDateFrom(), updatedReservation.getDateTo())	 &&
				iReservationService.checkDateFromAfterDateTo(updatedReservation.getDateFrom(), LocalDate.now().minusDays(1))
					) {
								if(!iReservationService.checkByDateSingleInReservations(updatedReservation.getDateFrom(), updatedReservation.getDateTo(), reservationById.getDateFrom(), reservationById.getDateTo())) {
									iReservationSingleService.deleteReservationSingleDay(reservationById.getDateFrom(), reservationById.getDateTo());
									iReservationService.saveReservation(iReservationService.updateReservation(reservationById, updatedReservation));
									iReservationSingleService.saveReservationSingleDay(reservationById.getDateFrom(), reservationById.getDateTo());
									String msg = String.format("Reservation number: " + reservationById.getId().toString() + " has been updated");
								    model.addAttribute("msgStatus", msg);
								}else {
									String differentDays = iReservationService.checkByDateSingleInReservationsBookedDays(updatedReservation.getDateFrom(), updatedReservation.getDateTo(), reservationById.getDateFrom(), reservationById.getDateTo());
									String msg = String.format("Dates: " + differentDays + " are reserved. Please choose another.");
								    model.addAttribute("msgStatus", msg);
								}
							
							}else {
								String msg = String.format("Reservation dates are incorrect. Please change.");
							    model.addAttribute("msgStatus", msg);
							}


	List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0,20));
	model.addAttribute("reservations", reservations);
	return "admin";
	}

	@GetMapping("/admin/reservation/update/{reservationId}")
	public String updateReservationSite(@PathVariable("reservationId")Long reservationId,
										Model model) {
		Reservation reservation = iReservationService.readReservationById(reservationId);
		model.addAttribute("reservation", reservation);
		return "update";
	}

	@GetMapping("/admin/reservation/show/{reservationId}")
	public String showReservationById(@PathVariable("reservationId")Long reservationId,
										Model model) {
		Reservation reservation = iReservationService.readReservationById(reservationId);
		model.addAttribute("reservation", reservation);
		return "show";
	}

	@PostMapping("/admin/search/date")
	public String searchReservationsByDate(@RequestParam("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
									@RequestParam("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
									Model model) {
		List<Reservation> reservations = iReservationService.searchReservationsByDate(dateFrom, dateTo);
		model.addAttribute("reservations", reservations);
		return "admin";
	}

	@PostMapping("/admin/search/number")
	public String searchReservationsByNumber(@RequestParam("lastReservations") Integer number, Model model) {
		if(number>0) {
			List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0,number));
			model.addAttribute("reservations", reservations);	
		}else {
			String msg = String.format("Number latest reservations must be above 0");
	        model.addAttribute("msg", msg);
		}
		return "admin";
	}
	

}
