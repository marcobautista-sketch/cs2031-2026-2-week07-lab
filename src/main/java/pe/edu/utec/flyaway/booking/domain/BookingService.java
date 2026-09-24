package pe.edu.utec.flyaway.booking.domain;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.utec.flyaway.booking.dto.BookingResponse;
import pe.edu.utec.flyaway.booking.dto.CreateBookingRequest;
import pe.edu.utec.flyaway.booking.infrastructure.BookingEmailWriter;
import pe.edu.utec.flyaway.booking.infrastructure.BookingRepository;
import pe.edu.utec.flyaway.flight.domain.Flight;
import pe.edu.utec.flyaway.flight.infrastructure.FlightRepository;
import pe.edu.utec.flyaway.user.domain.User;
import pe.edu.utec.flyaway.user.infrastructure.UserRepository;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final BookingEmailWriter emailWriter;

    @Transactional
    public BookingResponse book(CreateBookingRequest request) {
        User customer = currentUser();

        Flight flight = flightRepository.findById(request.flightId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));

        if (!flight.getDepartureTime().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot book a flight that already departed or is in transit");
        }

        if (flight.getAvailableSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No seats available on this flight");
        }

        if (bookingRepository.existsOverlappingBooking(
                customer.getId(), flight.getDepartureTime(), flight.getArrivalTime())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You already have a booking that overlaps with this flight");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setFlight(flight);
        booking.setBookingDate(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);

        emailWriter.write(saved);

        return BookingResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public BookingResponse findById(Long id) {
        return bookingRepository.findById(id)
                .map(BookingResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    private User currentUser() {
        String email = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown authenticated user"));
    }
}
