package pe.edu.utec.flyaway.auth.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.utec.flyaway.auth.dto.LoginRequest;
import pe.edu.utec.flyaway.auth.dto.TokenResponse;
import pe.edu.utec.flyaway.security.JwtService;
import pe.edu.utec.flyaway.user.domain.User;
import pe.edu.utec.flyaway.user.infrastructure.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return new TokenResponse(jwtService.generateToken(user.getEmail()));
    }
}
