package eu.lukks.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.lukks.domain.Reservation;
import eu.lukks.service.IReservationService;

@Controller
public class SearchController {

	private IReservationService iReservationService;

	@Autowired
	public SearchController(IReservationService iReservationService) {
		super();
		this.iReservationService = iReservationService;
	}

	@PostMapping("/admin/search/date")
	public String searchReservationsByDate(
			@RequestParam("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
			@RequestParam("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo, Model model) {
		List<Reservation> reservations = iReservationService.searchReservationsByDate(dateFrom, dateTo);
		model.addAttribute("reservations", iReservationService.reservationToReservationDto(reservations));
		return "reservations";
	}

	@PostMapping("/admin/search/number")
	public String searchReservationsByNumber(@RequestParam("lastReservations") Integer number, Model model) {
		if (number > 0) {
			List<Reservation> reservations = iReservationService
					.listNumberedAdminReservations(PageRequest.of(0, number));
			model.addAttribute("reservations", iReservationService.reservationToReservationDto(reservations));
		} else {
			String msg = String.format("Number latest reservations must be above 0");
			model.addAttribute("msg", msg);
		}
		return "reservations";
	}
	
	@ExceptionHandler(Exception.class)
	public String handleException(final Exception e) {
		return "forward:/admin/reservations";
	}

}
