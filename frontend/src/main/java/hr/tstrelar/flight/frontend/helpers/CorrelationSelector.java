package hr.tstrelar.flight.frontend.helpers;

public final class CorrelationSelector {
    public static String select(final String id) {
        return "JMSCorrelationID = '" + id + "'";
    }
}
