package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import flight.Flight;
import flight.Itinerary;
import system.Database;
import user.Admin;
import user.Client;
import user.UserDoesNotExistException;

/**
 * A class to represent a user booking an itinerary.
 */
public class UserBookItineraryActivity extends AppCompatActivity{

    /** The login type of a user. */
    private String user;
    /** The username of the client. */
    private String client_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Book Itineraries");
        setContentView(R.layout.activity_user_book_itineraries);
        client_email = (String) getIntent().getExtras().get(
                        getString(R.string.client_email));
        user = (String) getIntent().getExtras().get(
                getString(R.string.user_authority));

        // get the itinerary the user wants to book.
        Itinerary itinerary =
                (Itinerary) getIntent().getExtras().get("itinerary");
        TextView booked_flight =
                (TextView) findViewById(R.id.potential_flight);

        // if itinerary exists load it on the layout and book the flight.
        if (itinerary != null) {
            booked_flight.setText(itinerary.toString());
            bookItinerary(itinerary);
        }
        else {
            booked_flight.setText(R.string.no_flight_found);
        }

    }

    /**
     * Books the itinerary that the user selected and saves the database.
     * @param itinerary is the Itinerary object that the user selected.
     */
    private void bookItinerary(Itinerary itinerary) {
        Client client = null;
        try {
            client = (Client) Admin.getClient(client_email);
            client.bookSingleItinerary(itinerary);
            Database.getInstance().saveToFile();

        } catch (UserDoesNotExistException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Returns the user to the client home page,
     *  and keeps track of login type.
     * @param view the edited client display view.
     */
    public void returnHome(View view) {
        Intent intent = new Intent(this, ClientMainActivity.class);
        intent.putExtra(getString(R.string.client_email), client_email);
        intent.putExtra(getString(R.string.user_authority), user);
        startActivity(intent);
        }
    }

