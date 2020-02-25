package hr.tstrelar.backend.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "FLIGHTINFO_COMPANIES")
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "NAME", length = 100)
    private String name;

    @OneToMany(mappedBy = "company")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Flight> flights;
}
