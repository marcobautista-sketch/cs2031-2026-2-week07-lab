package pe.edu.utec.flyaway.flight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record CreateFlightRequest(

        @NotBlank
        @Pattern(regexp = "^[A-Z0-9]{1,6}$",
                message = "must contain only A-Z and 0-9, with a maximum of 6 characters")
        String flightNumber,

        @NotBlank
        String airline,

        @NotNull
        LocalDateTime departureTime,

        @NotNull
        LocalDateTime arrivalTime,

        @NotNull
        @Positive
        Integer availableSeats) {
}
