package pe.edu.utec.flyaway.flight.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utec.flyaway.flight.domain.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumber(String flightNumber);

    List<Flight> findByFlightNumberContainingIgnoreCaseAndAirlineContainingIgnoreCaseAndDepartureTimeBetween(
            String flightNumber, String airline, LocalDateTime departureFrom, LocalDateTime departureTo);
}
