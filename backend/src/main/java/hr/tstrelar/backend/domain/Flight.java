package hr.tstrelar.backend.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FLIGHTINFO_FLIGHTS")
@Data
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column (name = "DEPARTURE_AIRPORT", length = 5)
    private String departureAirport;

    @Column (name = "ARRIVAL_AIRPORT", length = 5)
    private String arrivalAirport;

    @Column (name = "DATE_RETURN")
    private Date returnDate;

    @Column (name = "DATE_DEPARTURE")
    private Date dateDeparture;

    @Column (name = "NUMBER_OF_TRANSFERS", length = 2)
    private int numberOfTransfers;

    @Column (name = "NUMBER_OF_PASSENGERS", length = 10)
    private int numberOfPassengers;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID", nullable = false)
    private Company company;

}

