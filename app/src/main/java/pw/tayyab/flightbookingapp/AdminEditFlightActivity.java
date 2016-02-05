package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import flight.Flight;
import system.Database;

/**
 * A class to represent the admin editing a flight activity.
 */
public class AdminEditFlightActivity extends AppCompatActivity {

    /** The flight to be edited. */
    private Flight flight = null;
    /** The departure date of the flight. */
    private EditText flight_departure;
    /** The arrival date of the flight. */
    private EditText flight_arrival;
    /** The airline of the flight. */
    private EditText flight_airline;
    /** The origin of the flight. */
    private EditText flight_origin;
    /** The destination of the flight. */
    private EditText flight_destination;
    /** The cost of the flight. */
    private EditText flight_cost;
    /** The number of seats in the flight. */
    private EditText flight_seats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_flight);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Result");

        Intent intent = getIntent();

        try {
            // Load any saved data from the serialized database.
            Database.getInstance().readFromFile();

            // Get all the text fields that correspond to flight fields.
            TextView success_view =
                    (TextView) findViewById(R.id.success_text_view);
            TextView flight_num =
                    (TextView) findViewById(R.id.flight_number_view_text);
            flight_departure =
                    (EditText) findViewById(R.id.departure_date_edit);
            flight_arrival =
                    (EditText) findViewById(R.id.arrival_date_edit);
            flight_airline =
                    (EditText) findViewById(R.id.airline_edit);
            flight_origin =
                    (EditText) findViewById(R.id.origin_edit);
            flight_destination =
                    (EditText) findViewById(R.id.destination_edit);
            flight_cost =
                    (EditText) findViewById(R.id.cost_edit);
            flight_seats =
                    (EditText) findViewById(R.id.seat_num_edit);

            // Get the flight
            flight = (Flight) intent.getExtras().get("flight");

            success_view.setText(R.string.flight_found);
            flight_num.setText(flight.getFlightNum());

            // Set all the text fields that correspond to flight fields.
            flight_departure.setText(flight.getDepartureDateTime());
            flight_arrival.setText(flight.getArrivalDateTime());
            flight_airline.setText(flight.getAirline());
            flight_origin.setText(flight.getOrigin());
            flight_destination.setText(flight.getDestination());
            flight_cost.setText(String.format("%.2f", flight.getCost()));
            flight_seats.setText(String.valueOf(flight.getNumSeats()));

        } catch (ClassNotFoundException | IOException e) {
             e.printStackTrace();
        }
    }

    /**
     * Returns the user to the previous screen.
     * @param item is the menu item that lets a user go back a page.
     * @return if the back button was pressed.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    /**
     * A string combining all the inputs of a
     * flight that the admin is changing.
     * @return a string representing the
     * information that a flight is changing to.
     */
    public String createFlightString() {
        return flight_departure.getText().toString() + "," +
                flight_arrival.getText().toString() + "," +
                flight_airline.getText().toString() + "," +
                flight_origin.getText().toString() + "," +
                flight_destination.getText().toString() + "," +
                flight_cost.getText().toString() + "," +
                flight_seats.getText().toString();
    }

    /**
     *  Enters the new flight details into the system to be saved,
     *  and pushes them to an activity where the admin can see the changes.
     * @param view is the view of the edit flight activity.
     */
    public void submitEditFlightRequest(View view) {
        Intent intent = new Intent(this, AdminDisplayFlightInfoActivity.class);
        flight.editFlight(createFlightString());
        String message = flight.toString();
        intent.putExtra("new_flight", message);
        startActivity(intent);
    }
}
