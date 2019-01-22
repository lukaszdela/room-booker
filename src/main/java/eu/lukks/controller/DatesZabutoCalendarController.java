package eu.lukks.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.ReservationSingle;
import eu.lukks.service.IReservationService;
import eu.lukks.service.IReservationSingleService;

@RestController
public class DatesZabutoCalendarController {
	
	private IReservationService iReservationService;
	private IReservationSingleService iReservationSingleService;
	
	@Autowired
	public DatesZabutoCalendarController(IReservationService iReservationService,
			IReservationSingleService iReservationSingleService) {
		super();
		this.iReservationService = iReservationService;
		this.iReservationSingleService = iReservationSingleService;
	}
	
	@GetMapping("/list")
	public List<ReservationSingle> reservationsList() {
		return iReservationSingleService.findReservationSingleStartMonth(iReservationSingleService.parsedDayToday());
	}
	
	@GetMapping("/admin/list/update/{reservationId}")
	public List<ReservationSingle> reservationsListUpdateId(@PathVariable("reservationId")Long reservationId) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		List<ReservationSingle> reservationSinglesAll = new ArrayList<ReservationSingle>(iReservationSingleService.findReservationSingleStartMonth(iReservationSingleService.parsedDayToday()));
		List<ReservationSingle> reservationSingles = new ArrayList<ReservationSingle>(reservationSinglesAll);
		List<LocalDate> reservationByIdDates = new ArrayList<LocalDate>(iReservationService.datesListFromDateToDate(reservationById.getDateFrom(), reservationById.getDateTo()));
		for(ReservationSingle reservationSingle: reservationSinglesAll) {
			for(LocalDate date: reservationByIdDates) {
				if(reservationSingle.getDate().isEqual(date)) {
					reservationSingles.remove(reservationSingle);
				}
			}
		}
		return reservationSingles;
	}
	
	@GetMapping("/admin/list/show/{reservationId}")
	public List<ReservationSingle> reservationsListShowId(@PathVariable("reservationId")Long reservationId) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		List<ReservationSingle> reservationSinglesAll = new ArrayList<ReservationSingle>(iReservationSingleService.findReservationSingleStartMonth(iReservationSingleService.parsedDayDate(reservationById.getDateFrom())));
		List<ReservationSingle> reservationSinglesNew = new ArrayList<ReservationSingle>();
		List<LocalDate> reservationByIdDates = new ArrayList<LocalDate>(iReservationService.datesListFromDateToDate(reservationById.getDateFrom(), reservationById.getDateTo()));
		
		for(ReservationSingle reservationSingle: reservationSinglesAll) {
			for(LocalDate date: reservationByIdDates) {
				if(reservationSingle.getDate().isEqual(date)) {
					reservationSinglesNew.add(reservationSingle);
				}
			}
		}
		return reservationSinglesNew;
	}

}
