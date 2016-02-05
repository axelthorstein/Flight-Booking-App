package pw.tayyab.flightbookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import flight.FlightAlreadyExistsException;
import system.Database;
import user.Admin;

/**
 * A class to represent the admin uploading flights activity.
 */
public class AdminUploadFlightsActivity extends AppCompatActivity {

    /** File path to the sd card. */
    private File filePath =
            new File(Environment.getExternalStorageDirectory().toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .permitDiskWrites()
                    .build());
            System.gc();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Upload Flights");
        setContentView(R.layout.activity_admin_upload_flight);
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
     * Upload the flights to the database,
     * and then serialize the database to make sure it persists.
     * @param view is the view of the Admin upload flights activity.
     */
    public void uploadFlightsFileButton(View view) {

        try {
            EditText text = (EditText)findViewById(R.id.flightUri);
            String value = text.getText().toString();
            File file = new File(filePath, value);

            // upload the flights and send an
            // alert to signal success or failure.
            Admin.uploadFlightFile(file.toString());

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setTitle("Success!")
                    .setMessage("The Flights we successfully uploaded.");
            dlgAlert.show();

            // serialize the database after change has been confirmed.
            Database.getInstance().saveToFile();
        } catch (IOException e) {
            MainActivity.alert("File Not Found.",
                    "Flights list not has been uploaded.", this);
        }
    }

}
