package pw.tayyab.flightbookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import system.Database;
import user.Admin;
import user.UserDoesNotExistException;

/**
 * A class to represent the admin searching client activity.
 */
public class AdminSearchClientActivity extends AppCompatActivity {

    /** The users login type: client or admin */
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Search Clients");
        setContentView(R.layout.activity_admin_search_client);
        user = (String)
                getIntent().getExtras().get(getString(R.string.user_authority));
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
     * Gets the inputted email and if it exists in the database
     * sends the admin to the client main activity with the clients email
     * so they can edit and view details specific to that client.
     * @param view is the view of the admin searching client activity.
     */
    public void submitSearchClientRequest(View view) {

        EditText client_email =
                (EditText) findViewById(R.id.Client_email_edit_text);
        String client_email_string = client_email.getText().toString();

        try {
            // look in the database for the clients email and
            // if so start the client activity with the email.
            Admin.getClient(client_email_string);

            Intent intent = new Intent(this, ClientMainActivity.class);
            intent.putExtra(getString(R.string.client_email),
                    client_email_string);
            intent.putExtra(getString(R.string.user_authority), user);
            startActivity(intent);

        } catch (UserDoesNotExistException e) {
            MainActivity.alert("Client not found.",
                    "Client is not in the Database.", this);

        }
    }
}
