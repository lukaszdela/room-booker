package eu.lukks.controller;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationSingle;
import eu.lukks.service.IReservationService;
import eu.lukks.service.IReservationSingleService;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	private IReservationService iReservationService;
	private IReservationSingleService iReservationSingleService;
	
	@Autowired
	public RestController(IReservationService iReservationService,
			IReservationSingleService iReservationSingleService) {
		super();
		this.iReservationService = iReservationService;
		this.iReservationSingleService = iReservationSingleService;
	}


	@GetMapping("/list")
	public List<ReservationSingle> reservationsList() {
		LocalDate actualDate = LocalDate.now();
		LocalDate firstDayActualDate = actualDate.withDayOfMonth(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String text = firstDayActualDate.format(formatter);
		LocalDate parsedDate = LocalDate.parse(text, formatter);
		return iReservationSingleService.findReservationSingleStartMonth(parsedDate);
	}


}
