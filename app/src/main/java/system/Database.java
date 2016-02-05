package system;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



import flight.Flight;
import flight.Itinerary;
import user.User;

/**
 * A class to represent the Database.
 */
public class Database implements Serializable{

    /** The instance of database if it already doesn't exist. */
    private static Database database = new Database();

    /** The Array List that holds all of the Flight objects in the system. */
    private ArrayList<Flight> flights = new ArrayList<Flight>();

    /** The Array List that holds all of the User objects in the system. */
    private Map<String, User> users = new
            HashMap<String, User>();

    /** A map that has the origin as the key and the values
     * as all the flights from origin in an array list */
    private Map<String, Itinerary> locations =
            new HashMap<String, Itinerary>();

    private Database() {
        super();
    }

    /**
     * Sets the database as a singleton.
     * @return an instance of the database
     */
    public static Database getInstance( ) {
        return database;
    }

    /**
     * Retrieve all the flights from the database.
     * @return an Array List that will hold all Flight objects in the system.
     */
    public ArrayList<Flight> getFlights() {
        return this.flights;
    }

    /**
     * Set the flights to the database.
     * @param flights is an array list of Flight objects.
     */
    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }


    /**
     * Sets the map of emails to User objects to an updated map.
     * @param users is the map of User objects to be set.
     */
    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    /**
     * Returns map of User's.
     * @return the map that holds all of the User objects in the system.
     * that are associated using a unique email address.
     */
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * Adds a user object to the map of all Users.
     * @param email is the unique email address of a user.
     * @param user is a User object.
     */
    public void addUser(String email, User user) {
        this.users.put(email, user);
    }

    /**
     * Adds a Flight object to the array list of all Flights.
     * @param flight is a Flight object.
     */
    public void addFlight(Flight flight) {
    	int indexOfDuplicate = Database.getInstance().
    			getIndexInArrayOfFlight(flight); 
        if (indexOfDuplicate >= 0) {
        	Flight oldFlight = Database.getInstance().
        			getDuplicateFlightInArrayList(indexOfDuplicate);
            Database.getInstance().getFlights().remove(indexOfDuplicate);
            Database.getInstance().removeDuplicateFlightFromMap(oldFlight);
        }

        this.flights.add(flight);

        if (!this.locations.containsKey(flight.getOrigin())) {
            Itinerary destinations = new Itinerary();
            destinations.addIndividualFlight(flight);
            this.locations.put(flight.getOrigin(), destinations);

        } else{
            this.getFlightsFromOrigin(flight.getOrigin()).
                    addIndividualFlight(flight);
        }

    }

    /**
     * Returns index of flight in arrayList
     * @param flight is the Flight object to be indexed.
     * @return int for index of flightNum in array or -1 if doesn't exist
     */
    private int getIndexInArrayOfFlight(Flight flight){
        for(int i = 0; i < Database.getInstance().getFlights().size(); i++){
            if (Database.getInstance().getFlights().get(i).getFlightNum().
                    equals(flight.getFlightNum())){
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns duplicate flight in arraylist.
     * @param index is the index of the Flight in Flights ArrayList.
     * @return the Flight object at that index in the Flights ArrayList.
     */
    private Flight getDuplicateFlightInArrayList(int index){
    	return Database.getInstance().flights.get(index);
    }

    /**
     * Returns index of flight in locations map.
     * @param flight
     * @return
     */
    private int getIndexInMapOfFlight(Flight flight){
        Itinerary allFlightsFromOrigin = Database.getInstance().
                getFlightsFromOrigin(flight.getOrigin());
        for(int i = 0; i < allFlightsFromOrigin.size(); i++){
            if (allFlightsFromOrigin.getFlightFromIndex(i).getFlightNum().
                    equals(flight.getFlightNum())){
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes flight from locations map of all destinations
     * @param flight is the flight the potential duplicate flight.
     */
    private void removeDuplicateFlightFromMap(Flight flight){
        int indexToRemove = Database.getInstance().
        		getIndexInMapOfFlight(flight);
        Itinerary destinations = Database.getInstance().
        		getFlightsFromOrigin(flight.getOrigin());

        if(destinations.size() == 1){
            Database.getInstance().getLocations().remove(flight.getOrigin());
        } else {destinations.getFlights().remove(indexToRemove);}
    }

    /**
     * Returns the locations map that consists of origin as key
     * and array list of flights as values.
     * @return a map of all the itineraries associated with a location.
     */
    public  Map<String, Itinerary> getLocations(){
        return this.locations;
    }

    /*
     * Sets the map of origins to Itinerary objects to an updated map.
     * @param locations is the Map of locations to be set.
     */
    public void setLocations(Map<String,Itinerary> locations) {
        this.locations = locations;
    }

    /**
     * Returns an Itinerary of flights from the original city.
     * @param origin is the original city that a Flight leaves from.
     * @return an Itinerary of flights from the original city.
     */
    public Itinerary getFlightsFromOrigin(String origin){
        return this.locations.get(origin);
    }

    /**
     * Returns the arrival or departure times as a date object.
     * @param dateTime is the String format of the flights
     * 				arrival or departure date.
     * @return a date object representing the flights
     * 				arrival or departure date.
     */
    public static Date getDateTime(String dateTime) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
     * Returns the arrival or departure times as a date object.
     * @param date is the String format of the flights
     * 				arrival or departure date.
     * @return a date object representing the flights
     * 				arrival or departure date.
     */
    public static Date getDate(String date) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = new Date();
        try {
            newDate = simpleDateFormat.parse(date);
        }
        catch (ParseException ex) {
            System.out.println("Exception " + ex);
        }
        return newDate;
    }

    /**
     * Returns an Itinerary of flights from origin after departure date
     * @param origin is the original city that a Flight leaves from.
     * @param departureDateString 
     * 				is the date and time that a Flight leaves a city.
     * @return an itinerary of Flights from the origin
     * after the departure date.
     */
    public Itinerary getFlightsFromOriginAfterDate(
            String origin, String departureDateString) {
        Itinerary allFlights = this.getFlightsFromOrigin(origin);
        Itinerary flightsAfterDeparture = new Itinerary();
        Date departureDate = Database.getDate(departureDateString);

        if (this.ifFlightsFromOrigin(origin)) {
            for (int i = 0; i < allFlights.size(); i++) {
                Flight currFlight = allFlights.getFlightFromIndex(i);
                if (currFlight.getDateTime(currFlight.getDepartureDateTime())
                        .after(departureDate)) {
                    flightsAfterDeparture.addIndividualFlight(currFlight);
                }
            }
        }
        return flightsAfterDeparture;
    }

    /**
     * Returns if any flights are departing from original city.
     * @param origin is the original city that a Flight leaves from.
     * @return whether any flights are departing from the original city.
     */
    public boolean ifFlightsFromOrigin(String origin){
        return this.locations.containsKey(origin);
    }

    
    /**
     * Writes to the database file.
     * @throws IOException
     */
    public void saveToFile() throws IOException {
        SerializeDatabase.saveToFile();
    }

    /**
     * Reads the database file.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readFromFile() throws IOException,
            ClassNotFoundException {
        SerializeDatabase.readFromFile();

    }
}