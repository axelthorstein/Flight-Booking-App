package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


/**
 * A class representing the user searching flights activity.
 */
public class UserSearchFlightsActivity extends AppCompatActivity {

    /** The login type of a user. */
    private String user;
    /** The username of the client. */
    private String client_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Search Flights");
        setContentView(R.layout.activity_user_search_flights);

        // Get username and login type.
        client_email = (String)
                getIntent().getExtras().get(getString(R.string.client_email));
        user = (String) getIntent().getExtras().get(
                        getString(R.string.user_authority));
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
     *  Take the departure date, origin,
     *  and dstination provided by the user and bundle them
     *  into the intent, as well as if they want a direct flight or not.
     * @param view is the search flights activity view.
     */
    public void searchFlights(View view) {

        EditText departure_date =
                (EditText) findViewById(R.id.search_departure_date_edit);
        EditText origin = (EditText) findViewById(R.id.search_origin_edit);
        EditText destination =
                (EditText) findViewById(R.id.search_destination_edit);
        CheckBox directFlight= (CheckBox) findViewById(R.id.checkBox2);

        String checkBoxResult = "" + directFlight.isChecked();

        Intent intent = new Intent(this, UserDisplayFlightsActivity.class);

        intent.putExtra("departure_date", departure_date.getText().toString());
        intent.putExtra("origin", origin.getText().toString());
        intent.putExtra("destination", destination.getText().toString());
        intent.putExtra(getString(R.string.client_email), client_email);
        intent.putExtra(getString(R.string.user_authority), user);

        intent.putExtra("is_direct_flight", checkBoxResult);

        startActivity(intent);
    }
}
