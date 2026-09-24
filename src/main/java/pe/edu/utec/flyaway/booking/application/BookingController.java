package pe.edu.utec.flyaway.booking.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utec.flyaway.booking.domain.BookingService;
import pe.edu.utec.flyaway.booking.dto.BookingResponse;
import pe.edu.utec.flyaway.booking.dto.CreateBookingRequest;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/flights/book")
    public ResponseEntity<BookingResponse> book(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(request));
    }

    @GetMapping("/flight/book/{id}")
    public BookingResponse findById(@PathVariable Long id) {
        return bookingService.findById(id);
    }
}
