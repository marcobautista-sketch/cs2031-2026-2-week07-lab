package pe.edu.utec.flyaway.booking.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.edu.utec.flyaway.booking.domain.Booking;

@Component
@Slf4j
public class BookingEmailWriter {

    private static final DateTimeFormatter ISO_8601 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final Path outputDir;

    public BookingEmailWriter(@Value("${app.email.output-dir}") String outputDir) {
        this.outputDir = Path.of(outputDir);
    }

    public void write(Booking booking) {
        Path file = outputDir.resolve("flight_booking_email_%d.txt".formatted(booking.getId()));
        try {
            Files.createDirectories(outputDir);
            Files.writeString(file, buildContent(booking));
            log.info("Booking confirmation email written to {}", file.toAbsolutePath());
        } catch (IOException exception) {
            log.error("Could not write booking confirmation email to {}", file.toAbsolutePath(), exception);
        }
    }

    private String buildContent(Booking booking) {
        return """
                To: %s
                Subject: Booking confirmation %d - Fly Away Travel

                Dear %s %s,

                Your flight booking has been confirmed.

                Booking id: %d
                Booking date: %s
                Passenger: %s %s
                Flight number: %s
                Airline: %s
                Departure: %s
                Arrival: %s

                Thank you for flying with Fly Away Travel.
                """.formatted(
                booking.getCustomer().getEmail(),
                booking.getId(),
                booking.getCustomer().getFirstName(),
                booking.getCustomer().getLastName(),
                booking.getId(),
                format(booking.getBookingDate()),
                booking.getCustomer().getFirstName(),
                booking.getCustomer().getLastName(),
                booking.getFlight().getFlightNumber(),
                booking.getFlight().getAirline(),
                format(booking.getFlight().getDepartureTime()),
                format(booking.getFlight().getArrivalTime()));
    }

    private String format(LocalDateTime dateTime) {
        return dateTime.format(ISO_8601);
    }
}
