package pe.edu.utec.flyaway.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest(

        @NotBlank
        @Pattern(regexp = ".*[A-Z].*", message = "must contain at least one uppercase letter")
        String firstName,

        @NotBlank
        @Pattern(regexp = ".*[A-Z].*", message = "must contain at least one uppercase letter")
        String lastName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "must be at least 8 characters long and contain at least one letter and one digit")
        String password) {
}
