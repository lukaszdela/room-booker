package eu.lukks.controller;

import java.awt.print.Pageable;
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
		return iReservationSingleService.findAllReservationSingle(); 
	}
	
	@GetMapping("/admin/list/default")
	public List<Reservation> listDefaultAdminReservations(){
		return iReservationService.listDefaultAdminReservations(PageRequest.of(0,2));
	}

}
