package eu.lukks.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import eu.lukks.domain.Reservation;
import eu.lukks.domain.Room;
import eu.lukks.service.IReservationService;
import eu.lukks.service.IReservationSingleService;
import eu.lukks.service.IRoomService;

@Controller
public class ReservationController {

	private IReservationService iReservationService;
	private IReservationSingleService iReservationSingleService;
	private IRoomService iRoomSrevice;

	@Autowired
	public ReservationController(IReservationService iReservationService,
			IReservationSingleService iReservationSingleService, IRoomService iRoomSrevice) {
		super();
		this.iReservationService = iReservationService;
		this.iReservationSingleService = iReservationSingleService;
		this.iRoomSrevice = iRoomSrevice;
	}

	@GetMapping("/admin/reservations")
	public String getAllReservationsAdmin(Model model) {
		List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0, 20));
		model.addAttribute("reservations", iReservationService.reservationToReservationDto(reservations));
		return "reservations";
	}

	@GetMapping("/admin/reservation/delete/{reservationId}/{roomId}")
	public String deleteReservationById(@PathVariable("reservationId") Long reservationId,
			@PathVariable("roomId") Long roomId, Model model) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		Room roomById = iRoomSrevice.getRoomById(roomId);
		iReservationSingleService.deleteReservationSingleDay(roomById, reservationById);
		String reservationIdString = reservationId.toString();
		iReservationService.deleteReservationById(reservationId);
		List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0, 20));
		String msg = String.format("Reservation number: " + reservationIdString + " has been deleted");
		model.addAttribute("msgStatus", msg);
		model.addAttribute("reservations", reservations);
		return "reservations";
	}

	@PostMapping("/admin/reservation/update/save/{reservationId}/{roomId}")
	public String updateReservation(@ModelAttribute Reservation updatedReservation,
			@PathVariable("reservationId") Long reservationId, @PathVariable("roomId") Long roomId, Model model) {
		Reservation reservationById = iReservationService.readReservationById(reservationId);
		LocalDate reservationSingleFromReservationByIdDateFrom = reservationById.getDateFrom();
		LocalDate reservationSingleFromReservationByIdDateTo = reservationById.getDateTo();
		LocalDate reservationSingleFromUpdatedReservationDateFrom = updatedReservation.getDateFrom();
		LocalDate reservationSingleFromUpdatedReservationDateTo = updatedReservation.getDateTo();
		Room roomById = iRoomSrevice.getRoomById(roomId);
		if (!iReservationService.checkDateFromAfterDateTo(updatedReservation.getDateFrom(),
				updatedReservation.getDateTo())) {
			if (!iReservationService.checkByDateSingleInReservations(roomById, reservationById,
					updatedReservation.getDateFrom(), updatedReservation.getDateTo())) {
				iReservationService
						.saveReservation(iReservationService.updateReservation(reservationById, updatedReservation));
				iReservationSingleService.deleteReservationSingleDayByDates(roomById, reservationById,
						reservationSingleFromReservationByIdDateFrom, reservationSingleFromReservationByIdDateTo);
				iReservationSingleService.saveReservationSingleDayByDates(roomById, reservationById,
						reservationSingleFromUpdatedReservationDateFrom, reservationSingleFromUpdatedReservationDateTo);
				String msg = String
						.format("Reservation number: " + reservationById.getId().toString() + " has been updated");
				model.addAttribute("msgStatus", msg);
			} else {
				String differentDays = iReservationService.checkByDateSingleInReservationsBookedDays(roomById,
						reservationById, updatedReservation.getDateFrom(), updatedReservation.getDateTo());
				String msg = String.format("Dates: " + differentDays + " are reserved. Please choose another.");
				model.addAttribute("msgStatus", msg);
			}

		} else {
			String msg = String.format("Reservation dates are incorrect. Please change.");
			model.addAttribute("msgStatus", msg);
		}

		List<Reservation> reservations = iReservationService.listNumberedAdminReservations(PageRequest.of(0, 20));
		model.addAttribute("reservations", iReservationService.reservationToReservationDto(reservations));
		return "reservations";
	}

	@GetMapping("/admin/reservation/update/{reservationId}")
	public String updateReservationSite(@PathVariable("reservationId") Long reservationId, Model model) {
		Reservation reservation = iReservationService.readReservationById(reservationId);
		model.addAttribute("reservation", reservation);
		return "update";
	}

	@GetMapping("/admin/reservation/show/{reservationId}")
	public String showReservationById(@PathVariable("reservationId") Long reservationId, Model model) {
		Reservation reservation = iReservationService.readReservationById(reservationId);
		model.addAttribute("reservation", iReservationService.reservationToReservationDtoSingle(reservation));
		return "show";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(final Exception e) {
		return "forward:/admin/reservations";
	}

}
