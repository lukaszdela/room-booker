package eu.lukks.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
	
	@GetMapping("/admin/list/update/{reservationId}")
	public List<ReservationSingle> reservationsListUpdateId(@PathVariable("reservationId")Long reservationId) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		LocalDate actualDate = reservationById.getDateFrom();
		LocalDate firstDayActualDate = actualDate.withDayOfMonth(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String text = firstDayActualDate.format(formatter);
		LocalDate parsedDate = LocalDate.parse(text, formatter);
		
		List<ReservationSingle> reservationSingles = new ArrayList<ReservationSingle>();
		List<ReservationSingle> reservationSinglesAll = new ArrayList<ReservationSingle>();
		reservationSinglesAll.addAll(iReservationSingleService.findReservationSingleStartMonth(parsedDate));
		reservationSingles.addAll(reservationSinglesAll);
		List<LocalDate> reservationByIdDates = new ArrayList<LocalDate>();
		reservationByIdDates.addAll(iReservationService.datesListFromDateToDate(reservationById.getDateFrom(), reservationById.getDateTo()));
		
		for(ReservationSingle reservationSingle: reservationSinglesAll) {
			for(LocalDate date: reservationByIdDates) {
				if(reservationSingle.getDate().equals(date)) {
					reservationSingles.remove(reservationSingle);
				}
			}
		}
		return reservationSingles;
	}
	
	@GetMapping("/admin/list/show/{reservationId}")
	public List<ReservationSingle> reservationsListShowId(@PathVariable("reservationId")Long reservationId) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		LocalDate actualDate = reservationById.getDateFrom();
		LocalDate firstDayActualDate = actualDate.withDayOfMonth(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String text = firstDayActualDate.format(formatter);
		LocalDate parsedDate = LocalDate.parse(text, formatter);
		List<ReservationSingle> reservationSinglesNew = new ArrayList<ReservationSingle>();
		List<ReservationSingle> reservationSinglesAll = new ArrayList<ReservationSingle>();
		reservationSinglesAll.addAll(iReservationSingleService.findReservationSingleStartMonth(parsedDate));
		List<LocalDate> reservationByIdDates = new ArrayList<LocalDate>();
		reservationByIdDates.addAll(iReservationService.datesListFromDateToDate(reservationById.getDateFrom(), reservationById.getDateTo()));
		
		for(ReservationSingle reservationSingle: reservationSinglesAll) {
			for(LocalDate date: reservationByIdDates) {
				if(reservationSingle.getDate().equals(date)) {
					reservationSinglesNew.add(reservationSingle);
				}
			}
		}
		return reservationSinglesNew;
	}


}
