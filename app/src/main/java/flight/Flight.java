/**
 *
 */
package flight;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import system.Database;

/**
 * A class to represent a Flight object.
 */
public class Flight implements Serializable{

    /** The flight number that makes this flight unique */
    private String flightNum;

    /** The date and time that the flight is expected to leave on. */
    private String departureDateTime;

    /** The date and time that the flight is expected to arrive on. */
    private String arrivalDateTime;

    /** The Airline that the flight is registered with. */
    private String airline;

    /** The city that the flight is leaving from. */
    private String origin;

    /** The city that the flight is arriving at. */
    private String destination;

    /** The total cost of the flight. */
    private double cost;

    /** The number of seats on the flight */
    private int numSeats;


    public Flight(String flightNum, String departureDateTime,
                  String arrivalDateTime, String airline,
                  String origin, String destination, 
                  double cost, int numOfSeats) {
        super();
        this.flightNum = flightNum;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.cost = cost;
        this.numSeats = numOfSeats;
    }

    /**
     * Returns the cost of the flight.
     * @return the cost of this Flight.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Returns the flight number of the flight.
     * @return the unique flight number of this Flight.
     */
    public String getFlightNum() {
        return flightNum;
    }

    /**
     * Returns the airline of the flight.
     * @return the airline that this Flight is registered to.
     */
    public String getAirline() {
        return airline;
    }

    /**
     * Returns the departure date and time of the flight.
     * @return departure date and time for this Flight.
     */
    public String getDepartureDateTime() {
        return departureDateTime;
    }

    /**
     * Returns the arrival date and time of the flight.
     * @return arrival date and time for this Flight.
     */
    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    /**
     * Returns the origin of the flight.
     * @return the origin of this Flight.
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Returns the destination of the flight. 
     * @return the destination of this Flight.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the number of seats available for the flight.
     * @return the number of seats for this Flight.
     */
    public int getNumSeats() { return numSeats; }

    /**
     * Decreases the number of seats by 1.
     */
    public void bookedSeat() {
		this.numSeats = getNumSeats() - 1;
	}

	/**
     * Return the total time in minutes that a user is
     * stopped over in an itinerary.
     * @param difference is the total length of time that a
     * flight is stopped over in a city.
     * @return the total time in minutes of the stop over.
     */
    public int calculateTime(long difference) {

        long minutes = (difference / (1000*60) % 60);
        long hours = ((difference / (1000*60*60)) % 24);
        long days = (difference / (1000*60*60*24) % 365);

        return (int) ((days * 24 * 60) + hours * 60 + minutes);
    }

    /**
     * Calculates the number of hours of travel time for this single Flight 
     * using two Date objects from a Flights arrival and departure.
     * @return the number of hours of travel time for a single Flight.
     */
    public int getTravelTime() {

        long departure = getDateTime(getDepartureDateTime()).getTime();
        long arrival = getDateTime(getArrivalDateTime()).getTime();
        long difference = arrival - departure;

        return calculateTime(difference);
    }

    /**
     * Returns if flight arrival year is the same as depart 
     * year of connecting flight
     * @return true if flight arrival year is the same as depart 
     * year of connecting flight
     */
    public boolean sameYear(Flight connection){
    	String flightArrivalYear = this.getArrivalDateTime().substring(0, 4);
    	String connectDepartYear = connection.
    			getDepartureDateTime().substring(0, 4);
        return flightArrivalYear.equals(connectDepartYear);
    }
    
    /**
     * Gives a Date object in String format.
     * @param dateTime is the String format of the flights arrival or
     * departure date.
     * @return date object representing the flights arrival or departure date.
     */
    public Date getDateTime(String dateTime) {
        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(dateTime);
        }
        catch (ParseException ex) {
            System.out.println("Exception " + ex);
        }
        return date;
    }

    /**
     * Calculates the number of minutes between these two flights.
     * Returns the number of minutes in the stopover between flights.
     * @return number of minutes between arrival and departure time.
     */
    public int getStopOverTime(Flight other) {

        long arrival = getDateTime(getArrivalDateTime()).getTime();
        long departure = other.getDateTime(
                other.getDepartureDateTime()).getTime();
        long difference = departure - arrival;

        return calculateTime(difference);
    }

    /**
     * Returns true if the connecting flights stopover is not greater than 6h.
     * @return true if the difference of arrival and departure time
     * is greater than 0 hours and not greater than 6 hours
     */
    public boolean getStopOver(Flight other) {
        long total = getStopOverTime(other);

        return (0 <= total && total <= 360);
    }

    /**
     * Edits the information of a Flight object and stores in the database.
     * @param flight is the new flight information that will overwrite the
     * old flight information.
     */
    public void editFlight(String flight) {
        String[] flightString = flight.split(",");

        this.departureDateTime = flightString[0];
        this.arrivalDateTime = flightString[1];
        this.airline = flightString[2];
        this.origin = flightString[3];
        this.destination = flightString[4];
        this.cost = Double.parseDouble(flightString[5]);
        this.numSeats = Integer.parseInt(flightString[6]);

        Database.getInstance().addFlight(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Flight other = (Flight) obj;
        if (flightNum == null) {
            if (other.flightNum != null)
                return false;
        } else if (!flightNum.equals(other.flightNum))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return  flightNum + "," +
                departureDateTime + "," +
                arrivalDateTime + "," +
                airline + "," +
                origin + "," +
                destination + "," +
                String.format("%.2f", cost);
    }
}