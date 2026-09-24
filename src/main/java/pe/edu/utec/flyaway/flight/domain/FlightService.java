package pe.edu.utec.flyaway.flight.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.utec.flyaway.flight.dto.CreateFlightRequest;
import pe.edu.utec.flyaway.flight.dto.FlightResponse;
import pe.edu.utec.flyaway.flight.infrastructure.FlightRepository;

@Service
@RequiredArgsConstructor
public class FlightService {

    private static final LocalDateTime EARLIEST_DEPARTURE = LocalDateTime.of(1900, 1, 1, 0, 0);
    private static final LocalDateTime LATEST_DEPARTURE = LocalDateTime.of(2999, 12, 31, 23, 59, 59);

    private final FlightRepository flightRepository;

    public FlightResponse create(CreateFlightRequest request) {
        if (!request.departureTime().isBefore(request.arrivalTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Departure time must be before arrival time");
        }

        if (flightRepository.existsByFlightNumber(request.flightNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Flight number already exists");
        }

        Flight flight = new Flight();
        flight.setFlightNumber(request.flightNumber());
        flight.setAirline(request.airline());
        flight.setDepartureTime(request.departureTime());
        flight.setArrivalTime(request.arrivalTime());
        flight.setAvailableSeats(request.availableSeats());

        return FlightResponse.from(flightRepository.save(flight));
    }

    public List<FlightResponse> search(String flightNumber, String airline,
                                       LocalDateTime departureFrom, LocalDateTime departureTo) {
        return flightRepository
                .findByFlightNumberContainingIgnoreCaseAndAirlineContainingIgnoreCaseAndDepartureTimeBetween(
                        flightNumber == null ? "" : flightNumber,
                        airline == null ? "" : airline,
                        departureFrom == null ? EARLIEST_DEPARTURE : departureFrom,
                        departureTo == null ? LATEST_DEPARTURE : departureTo)
                .stream()
                .map(FlightResponse::from)
                .toList();
    }
}
