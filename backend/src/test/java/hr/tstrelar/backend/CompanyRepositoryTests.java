package hr.tstrelar.backend;

import hr.tstrelar.backend.domain.Company;
import hr.tstrelar.backend.repository.CompanyRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Log4j2
public class CompanyRepositoryTests {
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void testSaveCompany() {
        final String NAME = "Lufthansa";
        Company company = new Company();
        company.setName(NAME);
        companyRepository.save(company);
        Company company1 = companyRepository.findFirstByName(NAME);
        log.info("Found saved company: {}", company1);
        assertNotNull(company1);
        assertEquals(company1.getName(), company.getName());
        assertNotNull(company1.getId());
    }
    @Test
    public void testDeleteCompany() {
        final String NAME = "Lufthansa";
        Company company = new Company();
        company.setName(NAME);
        companyRepository.save(company);
        assertNotNull(company);
        companyRepository.delete(company);
        Company company1 = companyRepository.findFirstByName(NAME);
        assertNull(company1);
    }
}
