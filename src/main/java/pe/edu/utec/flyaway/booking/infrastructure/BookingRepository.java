package pe.edu.utec.flyaway.booking.infrastructure;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utec.flyaway.booking.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.customer.id = :customerId
              AND b.flight.departureTime < :arrivalTime
              AND b.flight.arrivalTime > :departureTime
            """)
    boolean existsOverlappingBooking(@Param("customerId") Long customerId,
                                     @Param("departureTime") LocalDateTime departureTime,
                                     @Param("arrivalTime") LocalDateTime arrivalTime);
}
