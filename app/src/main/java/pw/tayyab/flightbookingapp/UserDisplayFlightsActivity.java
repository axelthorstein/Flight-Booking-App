package pw.tayyab.flightbookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;

import java.util.ArrayList;

import flight.Flight;
import flight.Itinerary;
import user.Admin;
import user.Client;
import user.User;
import user.UserDoesNotExistException;

/**
 * A class to represent the search flights display activity.
 */
public class UserDisplayFlightsActivity extends AppCompatActivity {

    /** The login type of a user. */
    private String user;
    /** The username of the client. */
    private String client_email;
    /** The flight that the user selects. */
    private Flight flight;
    /** The list of itineraries on the layout. */
    private ListView listView;
    /** The list of flights a user can choose from.  */
    private ArrayList flights;
    /** The username of the client. */
    private Itinerary itinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Display Flights");
        setContentView(R.layout.activity_user_display_flights);

        Intent intent = getIntent();

        // Get flight parameters.
        String departure_date = intent.getExtras().getString("departure_date")
                .substring(0, 10);
        String origin = intent.getExtras().getString("origin");
        String destination = intent.getExtras().getString("destination");
        String is_direct_flight = intent.getStringExtra("is_direct_flight");

        // Get client and user info.
        client_email = (String) getIntent().getExtras()
                .get(getString(R.string.client_email));
        user = (String) getIntent().getExtras()
                .get(getString(R.string.user_authority));


        if (is_direct_flight.equals("true")) {

            // search for direct flights and then populate the listview.
            flights = User.getDirectFlights(
                    departure_date, origin, destination);

            ArrayAdapter adapter = new ArrayAdapter<Flight>(this,
                    R.layout.activity_listview, flights);

            listView = (ListView) findViewById(R.id.flight_list);
            listView.setAdapter(adapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Set a listener for when a user selects a flight.
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    flight = (Flight) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                            "Flight number " + flight.getFlightNum()
                                    + " selected", Toast.LENGTH_LONG)
                            .show();
                }
            });

        } else {

            // search for itineraries and then populate the listview.
            flights = User.getBookableItineraries(
                    departure_date, origin, destination);

            ArrayAdapter adapter = new ArrayAdapter<Itinerary>(this,
                    R.layout.activity_listview, flights);

            listView = (ListView) findViewById(R.id.flight_list);
            listView.setAdapter(adapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Set a listener for when a user selects a itinerary.
            listView.setOnItemClickListener(new AdapterView.
                    OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    itinerary = (Itinerary) parent.getItemAtPosition(position);

                    Toast.makeText(getApplicationContext(),
                            "Itinerary selected", Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    /**
     * If a client has selected an itinerary and has not booked it before
     * then this passes the itinerary into the intent and sends the
     * user to the display booked itinerary activity.
     * @param view is the search itineraries view.
     */
    public void bookFlight(View view) {
            try {

                Client client = (Client) Admin.getClient(client_email);

                // if flight is null then a client has selected a itinerary
                // and this step is unnecessary
                Itinerary selectedItinerary = null;
                if (flight != null) {
                    if (flight.getNumSeats() < 1) {
                        MainActivity.alert("Failed",
                                "Flight is currently full, " +
                                "please select another Flight.",
                                this);
                    }
                    else {
                        selectedItinerary = new Itinerary();
                        selectedItinerary.addIndividualFlight(flight);
                    }
                }

                else {
                    selectedItinerary = itinerary;
                }

                if (client.itineraryAlreadyBooked(selectedItinerary)) {
                    MainActivity.alert("Failed",
                            "Client has already booked this Flight, " +
                            "please select another Flight.",
                            this);
                }

                else if ((flight == null) && (selectedItinerary == null)) {
                    MainActivity.alert("Failed",
                            "Please select a Flight.",
                            this);
                }
                if (selectedItinerary != null) {
                    Intent intent = new Intent(this,
                            UserBookItineraryActivity.class);
                    intent.putExtra("itinerary", selectedItinerary);
                    intent.putExtra(getString(R.string.user_authority),
                            user);
                    intent.putExtra(getString(R.string.client_email),
                            client_email);
                    startActivity(intent);
                }
            } catch (UserDoesNotExistException e) {
                e.printStackTrace();
            }
        }

    /**
     * Sorts the items in the list view by cost.
     * @param view is the search itineraries view.
     */
    public void sortByCost(View view) {
        try {
            ArrayList<Itinerary> sortedItinerary = new ArrayList<Itinerary>();

            // if the first item is an itinerary then sort, otherwise add
            // items to itineraries and then sort.
            if (flights.get(0).getClass() == Itinerary.class) {
                sortedItinerary = User.sortItinerariesByCost(flights);
            } else {
                ArrayList<Itinerary> finalItineraryList =
                        new ArrayList<Itinerary>();
                for (Object x : flights) {
                    Itinerary temp = new Itinerary();
                    temp.addIndividualFlight((Flight) x);
                    finalItineraryList.add(temp);
                }
                sortedItinerary =
                        User.sortItinerariesByCost(finalItineraryList);
            }

            ArrayAdapter adapter = new ArrayAdapter<Itinerary>(this,
                    R.layout.activity_listview, sortedItinerary);
            listView.setAdapter(adapter);
            listView.notifyAll();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Sorts the items in the list view by time.
     * @param view is the search itineraries view.
     */
    public void sortByTime(View view) {
        try {

            ArrayList<Itinerary> sortedItinerary = new ArrayList<Itinerary>();

            // if the first item is an itinerary then sort, otherwise add
            // items to itineraries and then sort.
            if (flights.get(0).getClass() == Itinerary.class) {
                sortedItinerary = User.sortItinerariesByTravelTime(flights);

            } else {
                ArrayList<Itinerary> finalItineraryList =
                        new ArrayList<Itinerary>();

                for (Object x : flights) {
                    Itinerary temp = new Itinerary();
                    temp.addIndividualFlight((Flight) x);
                    finalItineraryList.add(temp);
                }
                sortedItinerary = User.sortItinerariesByTravelTime(
                        finalItineraryList);
            }

            ArrayAdapter adapter = new ArrayAdapter<Itinerary>(this,
                    R.layout.activity_listview, sortedItinerary);
            listView.setAdapter(adapter);
            listView.notifyAll();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
