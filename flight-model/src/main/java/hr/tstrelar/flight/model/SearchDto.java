package hr.tstrelar.flight.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchDto implements Serializable {
    private Date dateDepartureFrom;
    private Date dateDepartureTo;
    private Date dateArrivalFrom;
    private Date dateArrivalTo;
    private List<String> departureAirports;
    private List<String> arrivalAirports;
    private List<Integer> numbersOfPassengers;
    private List<Integer> numbersOfTransfers;
    private List<String> companies;
    private List<String> flightIds;
}
