package hr.tstrelar.backend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.backend.domain.QFlight;
import hr.tstrelar.backend.helper.NullableBooleanExpression;
import hr.tstrelar.flight.model.SearchDto;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Component
public class FlightSearchRepositoryImpl implements FlightSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final NullableBooleanExpression<List<String>> strings = new NullableBooleanExpression<>();
    private final NullableBooleanExpression<List<Integer>> integers = new NullableBooleanExpression<>();
    private final NullableBooleanExpression<Date> date = new NullableBooleanExpression<>();

    @Override
    public List<Flight> searchFlights(SearchDto searchParams) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        final QFlight flight = QFlight.flight;
        BooleanBuilder builder = new BooleanBuilder();
        return queryFactory.selectFrom(flight)
                .where(builder
                        .and(strings.find(flight.airportArrival::in, searchParams.getArrivalAirports()))
                        .and(strings.find(flight.airportDeparture::in, searchParams.getDepartureAirports()))
                        .and(date.find(flight.dateArrival::after, searchParams.getDateArrivalFrom()))
                        .and(date.find(flight.dateArrival::before, searchParams.getDateArrivalTo()))
                        .and(date.find(flight.dateDeparture::after, searchParams.getDateDepartureFrom()))
                        .and(date.find(flight.dateDeparture::before, searchParams.getDateDepartureTo()))
                        .and(strings.find(flight.company.name::in, searchParams.getCompanies()))
                        .and(integers.find(flight.numberOfPassengers::in, searchParams.getNumbersOfPassengers()))
                        .and(integers.find(flight.numberOfTransfers::in, searchParams.getNumbersOfTransfers()))
                        .and(strings.find(flight.flightId::in, searchParams.getFlightIds())))
                .fetch();
    }
}
