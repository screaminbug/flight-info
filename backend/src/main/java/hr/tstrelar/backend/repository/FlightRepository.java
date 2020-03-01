package hr.tstrelar.backend.repository;

import hr.tstrelar.backend.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, UUID>, FlightSearchRepository {}

