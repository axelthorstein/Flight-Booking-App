package pw.tayyab.flightbookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import flight.Flight;
import system.Database;

/**
 * A class to represent the searching of a flight by an admin.
 */
public class AdminSearchFlightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Search Flights");
        setContentView(R.layout.activity_admin_search_flight);
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
     * If the flight number inputted is in the
     * database start the edit flight activity.
     * @param view is the view of the search flights activity.
     */
    public void submitSearchFlightRequest(View view) {

        EditText flightNumber =
                (EditText) findViewById(R.id.flight_number_edit_text);
        ArrayList<Flight> flights = Database.getInstance().getFlights();

        // loop through the flights in the database,
        // if the inputted flight number is in the
        // database then set the intent with the flight found,
        // otherwise alert the failure.
        Intent intent = null;
        for (Flight flight : flights) {
            if (flight.getFlightNum().equals(
                    flightNumber.getText().toString())) {
                intent = new Intent(this, AdminEditFlightActivity.class);
                intent.putExtra("flight", flight);
                startActivity(intent);
            }
        }
        if (intent == null) {
            MainActivity.alert("Flight not found.",
                    "Flight number is not in the Database.", this);
        }
    }
}
