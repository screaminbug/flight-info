package hr.tstrelar.backend.repository;

import hr.tstrelar.backend.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {}

