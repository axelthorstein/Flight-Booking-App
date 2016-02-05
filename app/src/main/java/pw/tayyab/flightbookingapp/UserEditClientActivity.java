package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import system.Database;
import user.Admin;
import user.Client;
import user.UserDoesNotExistException;

/**
 * A class to represent the user editing a clients information activity
 */
public class UserEditClientActivity extends AppCompatActivity {

    /** The client to be edited. */
    private Client client = null;
    /** The username of the client. */
    private String client_email;
    /** The last name of the client. */
    private EditText last_name;
    /** The first name of the client. */
    private EditText first_name;
    /** The email of the client. */
    private TextView email;
    /** The address of the client. */
    private EditText address;
    /** The credit card number of the client. */
    private EditText card_number;
    /** The expiry date of a clients credit card. */
    private EditText expiry_date;
    /** The login type of a user. */
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_client_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Result");
        Intent intent = getIntent();

        try {
            Database.getInstance().readFromFile();
            TextView success_view =
                    (TextView) findViewById(R.id.client_email_imput);

            client_email = (String)
                    intent.getExtras().get(getString(R.string.client_email));
            user = intent.getStringExtra(getString(R.string.user_authority));
            client = (Client) Admin.getClient(client_email);

            // Get all the text fields of the layout.
            email = (TextView) findViewById(R.id.returnedClient);
            last_name = (EditText) findViewById(R.id.client_last_name_edit);
            first_name = (EditText) findViewById(R.id.client_first_name_edit);
            address = (EditText) findViewById(R.id.client_address_edit);
            card_number =
                    (EditText) findViewById(R.id.client_card_number_edit);
            expiry_date = (EditText) findViewById(R.id.client_expiry_edit);

            // Set the text of all the text views
            // to the corresponding information from the client.
            success_view.setText(R.string.client_found);
            last_name.setText(client.getLastName());
            first_name.setText(client.getFirstName());
            email.setText(client.getEmail());
            address.setText(client.getAddress());
            card_number.setText(client.getCreditCardNumber());
            expiry_date.setText(client.getExpiryDate());

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            TextView success_view =
                    (TextView) findViewById(R.id.client_email_imput);
            success_view.setText(R.string.no_client_found);
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
     * A string combining all the inputs of a user for the personal
     * information they want changed.
     * @return a string representing the information that a user
     * wants to change to.
     */
    public String createClientString() {
        return last_name.getText().toString() + "," +
                first_name.getText().toString() + "," +
                email.getText().toString() + "," +
                address.getText().toString() + "," +
                card_number.getText().toString() + "," +
                expiry_date.getText().toString();
    }

    /**
     *  Enters the new client details into the system to be saved,
     *  and pushes them to an activity
     *  where they can see their changes. Includes their email and login type.
     * @param view is the view of the edit client activity.
     */
    public void submitEditFlightRequest(View view) {
        Intent intent = new Intent(this, UserDisplayClientInfoActivity.class);
        client.editPersonalDetails(createClientString());
        String message = client.toString();
        intent.putExtra("new_client", message);
        intent.putExtra(getString(R.string.user_authority), user);
        intent.putExtra(getString(R.string.client_email), client_email);
        startActivity(intent);
    }

}
