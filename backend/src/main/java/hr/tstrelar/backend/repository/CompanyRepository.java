package hr.tstrelar.backend.repository;

import hr.tstrelar.backend.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findFirstByName(String name);
}

