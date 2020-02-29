package hr.tstrelar.backend.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "FLIGHTINFO_FLIGHTS")
@Data
public class Flight {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Column (name = "FLIGHT_ID", length = 15)
    private String flightId;

    @Column (name = "AIRPORT_DEPARTURE", length = 3)
    private String airportDeparture;

    @Column (name = "AIRPORT_ARRIVAL", length = 3)
    private String airportArrival;

    @Column (name = "DATE_DEPARTURE")
    private Date dateDeparture;

    @Column (name = "DATE_ARRIVAL")
    private Date dateArrival;

    @Column (name = "NUMBER_OF_TRANSFERS")
    private int numberOfTransfers;

    @Column (name = "NUMBER_OF_PASSENGERS")
    private int numberOfPassengers;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID", nullable = false)
    private Company company;
}

