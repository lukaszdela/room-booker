package eu.lukks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import eu.lukks.domain.ReservationSingle;
import eu.lukks.service.IReservationSingleService;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	private IReservationSingleService iReservationSingleService;
	
	
	@Autowired
	public RestController(IReservationSingleService iReservationSingleService) {
		super();
		this.iReservationSingleService = iReservationSingleService;
	}


	@GetMapping("/list")
	public List<ReservationSingle> reservationsList() {
		return iReservationSingleService.findAllReservationSingle(); 
	}

}
