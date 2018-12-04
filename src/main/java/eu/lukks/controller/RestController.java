package eu.lukks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import eu.lukks.domain.Reservation;
import eu.lukks.service.IReservationService;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	private IReservationService iReservationService;


	@Autowired
	public RestController(IReservationService iReservationService) {
		super();
		this.iReservationService = iReservationService;
	}

	@GetMapping("/list")
	public List<Reservation> reservationsList() {
		return iReservationService.findAllReservations(); 
	}

}
