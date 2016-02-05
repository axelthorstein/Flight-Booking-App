/**
 *
 */
package flight;

import java.io.Serializable;
import java.util.ArrayList;

//import driver.Driver;
import system.Database;

/**
 * A class that keeps track of all of the flights from the origin to the final
 * destination.
 */
public class Itinerary implements Serializable {

    /**
     * The Array List of all of the flights in an itinerary.
     */
    ArrayList<Flight> flights = new ArrayList<Flight>();

    /**
     * The total cost of all of the flights in an itinerary.
     */
    private double totalCost = 0;

    /**
     * The total travel time of all of the flights in an itinerary.
     */
    private int totalTime = 0;

    public Itinerary(ArrayList<Flight> flights) {
        super();
        this.flights = flights;
        this.totalCost = addTotalCost(getCosts(flights));
    }

    /**
     * Empty Itinerary constructor used for creating itineraries for user.
     */
    public Itinerary() {
        super();
    }

    /**
     * Returns how many flights are in this itinerary.
     *
     * @return the number of flights in itinerary.
     */
    public int size() {
        return flights.size();
    }

    /**
     * returns ArrayList of all the flights.
     *
     * @return the flights in the Itinerary.
     */
    public ArrayList<Flight> getFlights() {
        return flights;
    }

    /**
     * Adds an individual flight to itinerary.
     *
     * @param flight the flight to be added to this itinerary.
     */
    public void addIndividualFlight(Flight flight) {
        this.flights.add(flight);
    }

    /**
     * Returns all of the costs from each flight in an itinerary.
     *
     * @param flights is an array list of flights that a user has selected.
     * @return an array list of the total costs from each flight.
     */
    public ArrayList<Double> getCosts(ArrayList<Flight> flights) {
        ArrayList<Double> costs = new ArrayList<Double>();

        for (Flight flight : flights) {
            costs.add(flight.getCost());
        }

        return costs;
    }

    /**
     * Calculates the total cost from all flights in this itinerary.
     *
     * @param arrayList is list of all costs from each flight in itinerary.
     * @return the total of all the costs in this itinerary.
     */
    public double addTotalCost(ArrayList<Double> arrayList) {
        for (double cost : arrayList) {
            totalCost += cost;
        }

        return totalCost;

    }

    /**
     * Returns a Flight at the specified index.
     *
     * @param flightIndex is the index of a flight in the list of all Flights.
     * @return the Flight that is currently at the specified index.
     */
    public Flight getFlightFromIndex(int flightIndex) {
        return this.flights.get(flightIndex);
    }

    /**
     * Adds the flights from another itinerary to the current itinerary.
     *
     * @param otherItinerary is an itinerary that contains one or more Flights.
     */
    public void mergeItineraries(Itinerary otherItinerary) {
        this.flights.addAll(otherItinerary.getFlights());
    }

    /**
     * Calculates the total cost from all flights in this itinerary.
     *
     * @return the total of all the costs in an itinerary.
     */
    public double getCreatedItineraryCost() {
        double returnCost = 0.00;
        for (Flight currFlight : this.flights) {
            returnCost += currFlight.getCost();
        }
        return returnCost;
    }

    /**
     * Calculates the total travel time from all flights in this itinerary.
     *
     * @return the total travel time of all Flights in an itinerary.
     */
    public int getCreatedItineraryTime() {
        int returnTime = 0;
        long StopOver;
        int i = 1;

        for (Flight currFlight : flights) {
            returnTime += currFlight.getTravelTime();
            if (i < flights.size()) {
                StopOver = currFlight.getStopOverTime(flights.get(i));
                returnTime += StopOver;
            }
            i++;
        }
        return returnTime;
    }

    /**
     * Returns True if this itinerary is bookable.
     *
     * @return True if number of seats > 0 on all its flights.
     */
    public boolean ifBookable() {
        for (Flight currFlight : this.flights) {
            if (currFlight.getNumSeats() == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Itinerary other = (Itinerary) obj;
        boolean equal = false;
        for (Flight f : this.flights) {
            for (Flight o : other.flights) {
                if (f.equals(o)) {
                    equal = true;
                }
            }
        }
        return equal;
    }


    /**
     * returns a string representation of the Itinerary.
     * @return string representation of the Itinerary.
     */
    @Override
    public String toString() {
        String returnString = "";
        int time = this.getCreatedItineraryTime();
        double cost = this.getCreatedItineraryCost();
        int hours = time / 60;
        int minutes = time % 60;

        for (Flight flight : this.flights) {
            returnString += flight.getFlightNum() + ","
                    + flight.getDepartureDateTime() + ","
                    + flight.getArrivalDateTime() + "," + flight.getAirline()
                    + "," + flight.getOrigin() + "," + flight.getDestination()
                    + "\n";
        }
        returnString += String.format("%.2f", cost) + "\n"
                + String.format("%02d", hours) + ":"
                + String.format("%02d", minutes);
        return returnString;
    }
}