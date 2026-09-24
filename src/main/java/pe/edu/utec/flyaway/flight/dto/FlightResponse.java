package pe.edu.utec.flyaway.flight.dto;

import java.time.LocalDateTime;
import pe.edu.utec.flyaway.flight.domain.Flight;

public record FlightResponse(
        Long id,
        String flightNumber,
        String airline,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Integer availableSeats) {

    public static FlightResponse from(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAvailableSeats());
    }
}
