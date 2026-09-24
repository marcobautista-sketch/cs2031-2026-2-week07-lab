package pe.edu.utec.flyaway.booking.dto;

import java.time.LocalDateTime;
import pe.edu.utec.flyaway.booking.domain.Booking;

public record BookingResponse(
        Long id,
        Long customerId,
        String customerFirstName,
        String customerLastName,
        Long flightId,
        String flightNumber,
        String airline,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        LocalDateTime bookingDate) {

    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getCustomer().getId(),
                booking.getCustomer().getFirstName(),
                booking.getCustomer().getLastName(),
                booking.getFlight().getId(),
                booking.getFlight().getFlightNumber(),
                booking.getFlight().getAirline(),
                booking.getFlight().getDepartureTime(),
                booking.getFlight().getArrivalTime(),
                booking.getBookingDate());
    }
}
