package pw.tayyab.flightbookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

import system.Database;
import user.Admin;
import user.UserAlreadyExistsException;

/**
 * A class to represent the admin uploading clients activity.
 */
public class AdminUploadClientsActivity extends AppCompatActivity {

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
        setTitle("Upload Clients");
        setContentView(R.layout.activity_admin_upload_client);
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
     * Upload the clients to the database,
     * and then serialize the database to make sure it persists.
     * @param view is the view of the client upload flights activity.
     */
    public void uploadClientsFileButton(View view) {
        try {
            EditText text = (EditText)findViewById(R.id.clientUri);
            String value = text.getText().toString();
            File file = new File(filePath, value);

            // upload the clients and send an alert
            // to signal success or failure.
            Admin.uploadUserFile(file.toString());

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setTitle("Success!")
                    .setMessage("The clients we successfully uploaded.");
            dlgAlert.show();

            // save the database.
            Database.getInstance().saveToFile();
        } catch (IOException ex) {
            MainActivity.alert("File Not Found.",
                    "Clients list not has been uploaded.", this);
        }
    }
}
