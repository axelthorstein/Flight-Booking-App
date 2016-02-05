package user;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import system.Database;
import flight.Flight;
import flight.Itinerary;

/**
 * Abstract User Interface
 */
public abstract class User implements Serializable {

    /**
     * Enters a Client's personal and billing information into the system.
     * @param lastName is the last name of the Client.
     * @param firstNames is the first name of the Client.
     * @param email is the email of the Client.
     * @param address is the address of the Client.
     * @param creditCardNumber is the credit card number of the Client.
     * @param expiryDate is the expiry date of the clients credit card.
     */
    public static void enterPersonalAndBillingInfo(String lastName,
    		String firstNames, String email, String address,
    		String creditCardNumber, String expiryDate) {

        User user = (User) new Client(lastName, firstNames,
                email, address, creditCardNumber, expiryDate);

        Database.getInstance().addUser(email, (User) user);
    }

    /**
     * Returns Flight objects in a string format.
     * @param flights is an array list of Flight objects.
     * @return a string of a Flight objects
     */
    public static String flightsToString(
            ArrayList<Flight> flights) {
        String flightsString = "";
        for (Flight flight : flights) {
            flightsString += flight.toString() + "\n";
        }
        if (flightsString == "") {
            return flightsString;
        }
        return flightsString.substring(0, flightsString.length()-1);
    }

    /**
     * Returns an array list of direct flights.
     * @param departureDate is the date and time at which a Flight leaves.
     * @param origin is the original city that an itinerary departs from.
     * @param destination is the final city that it lands in.
     * @return an array list of all direct flights from the origin
     * to the destination.
     */
    public static ArrayList<Flight> getDirectFlights(String departureDate,
    		String origin, String destination) {
        ArrayList<Flight> directFlights = new ArrayList<Flight>();
        ArrayList<Itinerary> itineraries =
                searchItinerary(departureDate, origin, destination);
        for (Itinerary itinerary : itineraries) {
            if (itinerary.size() == 1) {
                directFlights.add(itinerary.getFlightFromIndex(0));
            }
        }
        return directFlights;
    }

    /**
     * Returns array list of bookable itineraries from SearchItinerary.
     * @param departureDate is the date and time at which a Flight leaves.
     * @param origin is the original city that an itinerary departs from.
     * @param destination is the final city that it lands in.
     * @return returns an array list of bookable
     * itineraries from SearchItinerary.
     */
    public static ArrayList<Itinerary> getBookableItineraries(
            String departureDate, String origin, String destination) {
        ArrayList<Itinerary> allItineraries =
                User.searchItinerary(departureDate, origin, destination);
        ArrayList<Itinerary> bookableItineraries = new ArrayList<Itinerary>();
        for(Itinerary currItinerary : allItineraries){
            if (currItinerary.ifBookable()){
                bookableItineraries.add(currItinerary);
            }
        }
        return bookableItineraries;
    }

    /**
     * Returns all of the itineraries connecting origin and destination.
     * @param origin is the original city that an itinerary departs from.
     * @param destination is the final city that it lands in.
     * @return ArrayList<Itinerary> is a list of all the possible itineraries
     * that a user could take.
     */
    public static ArrayList<Itinerary> searchItinerary (String departureDate,
    		String origin, String destination) {
        // An arraylist of itineraries that is returned by the function
        ArrayList<Itinerary> returnItineraries = new ArrayList<Itinerary>();

        // An itinerary of all the flights from an origin
        Itinerary values = Database.getInstance().
                getFlightsFromOriginAfterDate(origin, departureDate);

        //loop through each of the flights from the origin
        for(int i = 0; i < values.size(); i++){

            // an itinerary for the current flightpath taken
            Itinerary currPath = new Itinerary();

            // the current Flight
            Flight currFlight = values.getFlightFromIndex(i);

            if (currFlight.getDepartureDateTime().substring(0, 10).equals(
                    departureDate)) {
                // adds the flight to the current flightpath array
                currPath.addIndividualFlight(currFlight);

                String testDestination = currFlight.getDestination();
                // if the flight goes to destination, it is a valid itinerary


                if (testDestination.equals(destination)){
                    // add the flightpath array to
                    // the array of valid nested arrays
                    returnItineraries.add(currPath);

                } else{ //if flight does not go to destination

                    // Find all of the paths that the intermediate destination
                    // connects to the final destination
                    ArrayList<Itinerary> connections = allConnections(
                            currFlight.getDepartureDateTime(),
                            currFlight.getDestination(),
                            destination, currPath);

                    // add the valid paths through connecting flights
                    // as valid nested arrays
                    returnItineraries.addAll(connections);
                }
            }
        }
        return returnItineraries;
    }

    /**
     * Helper function for allItineraries that returns the
     * itineraries from connecting flights
     * @param origin is the original city that an itinerary departs from.
     * @param destination is the final city that a lands in.
     * @return ArrayList<Itinerary> that holds all the connections
     * an itinerary will make
     */
    private static ArrayList<Itinerary> allConnections (String departureDate,
    		String origin, String destination, Itinerary prevPath){
        // the nested arraylist that returns the flightpaths that
        // connect the origin and destination
        ArrayList<Itinerary> returnConnections = new ArrayList<Itinerary>();

        // arraylist of all possible flights from the origin
        Itinerary values = Database.getInstance().
                getFlightsFromOriginAfterDate(origin, departureDate);

        // check if there are flights flying from origin because if not,
        // cannot reach final destination
        if (values.size() != 0){

            // loop through each of the flights from the origin
            for(int i = 0; i < values.size(); i++){

                // the current Flight
                Flight currFlight = values.getFlightFromIndex(i);

                // last flight
                Flight lastFlight = prevPath.
                        getFlightFromIndex(prevPath.size() - 1);

                // check if destination already in flightpath,
                // if so then skip iteration
                if (ifAlreadyBeen(currFlight.getDestination(), prevPath)){
                    continue;
                }

                // check if year is not the same
                if (!lastFlight.sameYear(currFlight)){
                    continue;
                }

                // check if flight departure time is before or more
                // than 6 hours after arrival of last flight
                if(!lastFlight.getStopOver(currFlight)){

                    // skip iteration
                    continue;
                }

                // create a new flightpath
                Itinerary currPath = new Itinerary();

                // add the flights before connecting flight to flightpath
                currPath.mergeItineraries(prevPath);

                // add the flight to flightpath
                currPath.addIndividualFlight(currFlight);
                // if the flight goes to the destination
                if (currFlight.getDestination().equals(destination)) {
                    // add the complete flightpath
                    returnConnections.add(currPath);

                } else{ // if flight does not go to destination

                    // find all the paths that intermediate destination
                    // reaches final destination
                    ArrayList<Itinerary> connections = allConnections(
                            currFlight.getDepartureDateTime(),
                            currFlight.getDestination(),
                            destination, currPath);

                    // add allof the flightpaths through intermediate flights
                    returnConnections.addAll(connections);
                }
            }
        }
        return returnConnections;
    }

    /**
     * Returns true if there already exists a flight to that destination.
     * @param destination is the final city that it lands in.
     * @param flightPath is the Itinerary that has a
     * specified origin to a destination
     * @return true if an itinerary has already been to a destination.
     */
    private static boolean ifAlreadyBeen(String destination,
                                         Itinerary flightPath){
        for(int i = 0; i < flightPath.size(); i++){
            if(flightPath.getFlightFromIndex(i).getOrigin().equals(
                    destination)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns list of Itineraries sorted by cost from least to greatest.
     * @param itineraries is a list of Itineraries to be sorted.
     * @return a copy of the list of Itineraries sorted by cost.
     */
    public static ArrayList<Itinerary> sortItinerariesByCost(
            ArrayList<Itinerary> itineraries) {
        Collections.sort(itineraries, new Comparator<Itinerary>() {
            @Override
            public int compare(Itinerary itinerary1, Itinerary itinerary2) {
                return Double.compare(itinerary1.getCreatedItineraryCost(),
                        itinerary2.getCreatedItineraryCost());
            }
        });
        return itineraries;
    }

    /**
     * Returns list of Itineraries sorted by travel time
     * from least to greatest.
     * @param itineraries is a list of Itineraries to be sorted.
     * @return a copy of the list of Itineraries sorted by time.
     */
    public static ArrayList<Itinerary> sortItinerariesByTravelTime(
            ArrayList<Itinerary> itineraries) {
        Collections.sort(itineraries, new Comparator<Itinerary>() {
            @Override
            public int compare(Itinerary itinerary1, Itinerary itinerary2) {
                return (int) (itinerary1.getCreatedItineraryTime()
                        - itinerary2.getCreatedItineraryTime());
            }
        });
        return itineraries;
    }

    /**
     * Returns a String representation of the itineraries.
     * @param itineraries is the list of all itineraries from flights returned
     * from a search.
     * @return a string representation of all the returned itineraries.
     */
    public static String itinerariesToString(
            ArrayList<Itinerary> itineraries) {
        String flightsString = "";
        for (Itinerary itinerary : itineraries) {
            flightsString += itinerary.toString() + "\n";
        }

        if (flightsString == "") {
            return flightsString;
        }
        return flightsString.substring(0, flightsString.length()-1);
    }

    /**
     * returns a string representation of the User.
     * @return string representation of the User.
     */
    public abstract String toString();
}