package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import flight.Itinerary;
import system.Database;
import user.Admin;
import user.Client;
import user.UserDoesNotExistException;

/**
 * A class to view booked itineraries of a client.
 */
public class ClientViewItinerariesActivity extends AppCompatActivity {

    /** The client object that is looking at their booked itineraries.  */
    private Client client;
    /** The login type of a user. */
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booked_itineraries);

        String client_email = (String)
                getIntent().getExtras().get(getString(R.string.client_email));
        user = (String) getIntent().getExtras().get(
                getString(R.string.user_authority));

        try {
            Database.getInstance().readFromFile();
            client = (Client) Admin.getClient(client_email);
        } catch (UserDoesNotExistException |
                IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Itinerary> itineraries = client.getBookedItineraries();

        // Populate the ListView on the layout
        // with the booked itineraries of a client.
        ArrayAdapter adapter = new ArrayAdapter<Itinerary>(this,
                R.layout.activity_listview, itineraries);

        ListView listView =
                (ListView) findViewById(R.id.booked_itineraries_listview);
        listView.setAdapter(adapter);
    }

    /**
     * Returns the user to the client main activity
     * and home screen with their login type and username.
     * @param view the view of the clients booked itineraries.
     */
    public void returnHome(View view) {
        Intent intent = new Intent(this, ClientMainActivity.class);
        intent.putExtra(getString(R.string.client_email), client.getEmail());
        intent.putExtra(getString(R.string.user_authority), user);
        startActivity(intent);
        }
    }
