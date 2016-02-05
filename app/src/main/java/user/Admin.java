package user;

/**
 * A class to represent an Admin object.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import system.Database;
import flight.Flight;
import flight.FlightAlreadyExistsException;
import flight.Itinerary;

public class Admin extends User {
    /** a String storing the User's lastName */
    private String lastName;

    /** a String storing the User's firstNames */
    private String firstName;

    /** a String storing the User's email */
    private String email;

    /** a String storing the User's address */
    private String address;

    /** a String storing the credit card number */
    private String creditCardNumber;

    /** a String storing the credit card expiry date */
    private String expiryDate;

    public Admin(String lastName, String firstNames, String email,
                 String address, String creditCardNumber, String expiryDate) {

        this.lastName = lastName;
        this.firstName = firstNames;
        this.email = email;
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
    }

    /**
     * Takes the information from the given CSV file and enters it as
     * Users objects into the system.
     * @param file is an CSV file that holds the details of one or more
     * Users to be entered into the system.
     */
    public static void uploadUserFile(String file) throws IOException {
            BufferedReader bReader = new BufferedReader(
                    new FileReader(file));
            String lines;
            while ((lines = bReader.readLine()) != null) {
                String[] readLine = lines.split(",");

                enterPersonalAndBillingInfo(readLine[0], readLine[1],
                        readLine[2], readLine[3], readLine[4],
                        readLine[5]);
            }
    }

    /**
     * Takes the information from the given CSV file and enters it as
     * Flight objects into the system.
     * @param file is an CSV file that holds the details of one or more
     * Flights to be entered into the system.
     */
    public static void uploadFlightFile(String file)
            throws IOException {
        try {
            BufferedReader bReader = new BufferedReader(
                    new FileReader(file));
            String lines;
            while ((lines = bReader.readLine()) != null) {
                String[] readLine = lines.split(",");

                enterFlightInfo(readLine[0], readLine[1],
                        readLine[2], readLine[3], readLine[4],
                        readLine[5], Double.parseDouble(readLine[6]), 
                        Integer.parseInt(readLine[7]));
            }
        } catch (FlightAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a User if the User's email is in the system.
     * @param email is the unique email linked to a specific user.
     * @return the user associated to an email.
     */
    public static User getClient(String email)
            throws UserDoesNotExistException {
        if (Database.getInstance().getUsers().containsKey(email)) {
            return Database.getInstance().getUsers().get(email);
        }
        else {
            throw new UserDoesNotExistException
                    ("User does not exist in the database.");
        }
    }


    /**
     * Creates a Flight object with the given parameters.
     * @param flightNum is a unique ID to distinguish it from other Flights.
     * @param departureDateTime is the date and time at which a Flight leaves.
     * @param arrivalDateTime is the date and time at which a Flight arrives.
     * @param airline is the airline that a Flight is owned by.
     * @param origin is the original city that a Flight leaves from.
     * @param destination is the original city that a Flight arrives at.
     * @param d is the total cost of the Flight
     * @throws FlightAlreadyExistsException is an exception that handles when
     * a flight is entered with an flight number that has already been used.
     */
    public static void enterFlightInfo(
            String flightNum,
            String departureDateTime,
            String arrivalDateTime,
            String airline,
            String origin,
            String destination,
            double d,
            int numSeats) throws FlightAlreadyExistsException {
        Flight flight = new Flight(flightNum, departureDateTime,
                arrivalDateTime, airline, origin, destination, d, numSeats);
        Database.getInstance().addFlight(flight);
    }

    /**
     * returns a string representation of the User.
     * @return string representation of the User.
     */
    @Override
    public String toString() {
        return lastName + "," +
                firstName + "," +
                email + "," +
                address + "," +
                creditCardNumber + "," +
                expiryDate;
    }
}
