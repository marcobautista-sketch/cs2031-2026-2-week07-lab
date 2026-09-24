package pe.edu.utec.flyaway.booking.dto;

import jakarta.validation.constraints.NotNull;

public record CreateBookingRequest(

        @NotNull
        Long flightId) {
}
