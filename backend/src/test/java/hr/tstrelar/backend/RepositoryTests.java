package hr.tstrelar.backend;

import hr.tstrelar.backend.repository.CompanyRepository;
import hr.tstrelar.backend.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class RepositoryTests {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void injectedComponentsAreNotNull() {
        final String mess = "Couldn't inject ";
        assertNotNull(dataSource, mess + "DataSource");
        assertNotNull(jdbcTemplate, mess + "JdbcTemplate");
        assertNotNull(entityManager, mess + "EntityManager");
        assertNotNull(companyRepository, mess + "CompanyRepository");
        assertNotNull(flightRepository, mess + "FlightRepository");
    }
}
