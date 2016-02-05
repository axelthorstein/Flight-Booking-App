package user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import system.Database;
import flight.Flight;
import flight.Itinerary;

/**
 * A class to represent a Client object.
 */
public class Client extends User {
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
    
    /** an ArrayList storing the booked itineraries*/
    private ArrayList<Itinerary> bookedItineraries;

	public Client(String lastName, String firstNames, String email,
                  String address, String creditCardNumber, String expiryDate
                  ) {

        this.lastName = lastName;
        this.firstName = firstNames;
        this.email = email;
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
        
        this.bookedItineraries = new ArrayList<Itinerary>();
    }


    /**
     * Returns the last name of this client.
     * @return the last name of this client.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the first name of this client.
     * @return the first name of this client.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the email of this client.
     * @return the email of this client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the address of this client.
     * @return the address of this client.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the credit card number of this client.
     * @return the credit card number of this client.
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Returns the expiry date of this credit card.
     * @return the expiry date of this credit card.
     */
    public String getExpiryDate() {
        return expiryDate;
    }
    
    /**
     * Returns the itineraries that are booked for this client.
     * @return the itineraries that are booked for this client.
     */
    public ArrayList<Itinerary> getBookedItineraries() {
		return bookedItineraries;
	}

    /**
     * Determines whether the Itinerary object has been booked by this
     * client before.
     * @param itinerary is the Itinerary object that will be booked.
     * @return returns whether the Itinerary object has been booked
     * by this client before.
     */
    public boolean itineraryAlreadyBooked(Itinerary itinerary) {
        for (Itinerary i : getBookedItineraries()) {
            if (i.equals(itinerary)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Edits the information of the client.
     * @param clientString is in the format of client.csv
     */
    public void editPersonalDetails(String clientString) {
        String[] client = clientString.split(",");
        this.lastName = client[0];
        this.firstName = client[1];
        this.email = client[2];
        this.address = client[3];
        this.creditCardNumber = client[4];
        this.expiryDate = client[5];
    }

	
    /**
     * Returns a User if the user's email is in the system.
     * @return the user associated to an email.
     */
    public User getClient()
            throws UserDoesNotExistException {
        if (Database.getInstance().getUsers().containsKey(this.email)) {
            return Database.getInstance().getUsers().get(this.email);
        }
        else {
            throw new UserDoesNotExistException
                    ("User does not exist in the database.");
        }
    }

    /**
     * Adds the selected itinerary to the clients Booked Itineraries
     * 			and decreases the number of seats of those flights by 1.
     * @param itinerary is the list of itineraries searched.
     */
    public void bookSingleItinerary(Itinerary itinerary) {

        bookedItineraries.add(itinerary);

        // Decreases numOfSeats of each flight in itinerary by 1
        for(Flight flight: itinerary.getFlights()) {
            flight.bookedSeat();
        }
    }

    /**
     * Returns a string representation of the User.
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