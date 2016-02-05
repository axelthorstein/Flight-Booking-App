package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * A class representing the client home page activity.
 */
public class ClientMainActivity extends AppCompatActivity {

    /** The email of the client */
    private String client_email;

    /** The login type of user: client or admin */
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Client App");
        setContentView(R.layout.activity_client_main);

        client_email = (String)
                getIntent().getExtras().get(getString(R.string.client_email));
        user = (String)
                getIntent().getExtras().get("user");

        // Check if the user is an admin or client and
        // show the corresponding button. Logout for client,
        // and return home for admin.
        if (user.equals(getString(R.string.user_admin))) {
            Button logout =
                    (Button) findViewById(R.id.client_main_logout_button);
            logout.setVisibility(View.GONE);
            Button home =
                    (Button) findViewById(R.id.client_main_home_button);
            home.setVisibility(View.VISIBLE);
        } else if (user.equals(getString(R.string.user_client))) {
            Button logout =
                    (Button) findViewById(R.id.client_main_logout_button);
            logout.setVisibility(View.VISIBLE);
            Button home =
                    (Button) findViewById(R.id.client_main_home_button);
            home.setVisibility(View.GONE);
        }
    }

    /**
     * Sends user to the edit personal information activity
     * with their email and user type.
     * @param view the view of the clients main activity and home screen.
     */
    public void editClientInfo(View view) {
        Intent intent = new Intent(this, UserEditClientActivity.class);
        intent.putExtra(getString(R.string.client_email), client_email);
        intent.putExtra(getString(R.string.user_authority), user);
        startActivity(intent);
    }

    /**
     * Sends user to the search flights activity
     * with their email and user type.
     * @param view the view of the clients main activity and home screen.
     */
    public void searchFlights(View view) {
        Intent intent = new Intent(this, UserSearchFlightsActivity.class);
        intent.putExtra(getString(R.string.client_email), client_email);
        intent.putExtra(getString(R.string.user_authority), user);
        startActivity(intent);
    }

    /**
     * Sends user to view their booked itineraries
     * activity with their email and user type.
     * @param view the view of the clients main activity and home screen.
     */
    public void viewBookedItineraries(View view) {
        Intent intent = new Intent(this, ClientViewItinerariesActivity.class);
        intent.putExtra(getString(R.string.client_email), client_email);
        intent.putExtra(getString(R.string.user_authority), user);
        startActivity(intent);
    }

    /**
     * Returns the user to the client main activity
     * and home screen if they are a client.
     * @param view the view of the clients main activity and home screen.
     */
    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Returns the user to the Amdin main activity
     * and home screen if they are an admin.
     * @param view the view of the clients main activity and home screen.
     */
    public void returnToAdmin(View view) {
        Intent intent = new Intent(this, AdminMainActivity.class);
        intent.putExtra(getString(R.string.user_authority), user);
        startActivity(intent);
    }
}