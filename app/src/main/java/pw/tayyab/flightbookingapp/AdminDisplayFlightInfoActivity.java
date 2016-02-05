package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import system.Database;

/**
 * A class to represent the admin displaying
 * flight information change activity.
 */
public class AdminDisplayFlightInfoActivity extends AppCompatActivity {

    /** The changed information of the flight. */
    private String new_flight = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display_flight_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Result");
        TextView flight_info_text =
                (TextView) findViewById(R.id.new_flight_info);

        // Load the new flight info on the layout
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            new_flight = extras.getString("new_flight");
        }

        flight_info_text.setText(new_flight);

        // Save the changes
        try {
            Database.getInstance().saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the user to the Admin main activity
     * and home screen with their login type and username.
     * @param view the view of the display new flight info.
     */
    public void returnHome(View view) {
    Intent intent = new Intent(this, AdminMainActivity.class);
        intent.putExtra(getString(R.string.user_authority), getString(R.string.user_admin));
    startActivity(intent);

    }
}
