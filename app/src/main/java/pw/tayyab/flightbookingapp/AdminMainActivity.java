package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * A class to represent the Admin home page.
 */
public class AdminMainActivity extends AppCompatActivity {

    /** The login type of user: client or admin */
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Administrator Panel");
        setContentView(R.layout.activity_admin_main);
        user = (String)
                getIntent().getExtras().get(getString(R.string.user_authority));
    }

    /**
     * Sends the user to the upload flights activity.
     * @param view is the view of the Admin home page activity.
     */
    public void uploadFlightsButton(View view) {
        Intent intent = new Intent(this, AdminUploadFlightsActivity.class);
        startActivity(intent);
    }

    /**
     * Sends the user to the upload clients activity.
     * @param view is the view of the Admin home page activity.
     */
    public void uploadClientsButton(View view) {
        Intent intent = new Intent(this, AdminUploadClientsActivity.class);
        startActivity(intent);
    }

    /**
     * Sends the user to the search and edit flights activity.
     * @param view is the view of the Admin home page activity.
     */
    public void editFlightsButton(View view) {
        Intent intent = new Intent(this, AdminSearchFlightActivity.class);
        startActivity(intent);
    }

    /**
     * Sends the user to the search clients activity.
     * @param view is the view of the Admin home page activity.
     */
    public void searchClientButton(View view) {
        Intent intent = new Intent(this, AdminSearchClientActivity.class);
        intent.putExtra(getString(R.string.user_authority), user);
        startActivity(intent);
    }

    /**
     * Sends the user to main activity and logs them out.
     * @param view is the view of the Admin home page activity.
     */
    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
