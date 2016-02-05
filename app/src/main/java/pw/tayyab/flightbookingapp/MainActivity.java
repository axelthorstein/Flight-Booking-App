package pw.tayyab.flightbookingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import system.Database;

/**
 * A class to represent the main page login screen activity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPreviousSession();
        File password_dir =
                new File(this.getApplicationContext().getFilesDir()
                + "/passwords.txt");

        // If the passwords file doesn't exist,
        // create a new one with one client and one admin.
        if (!password_dir.exists()) {
            try {
                FileOutputStream fos = openFileOutput("passwords.txt",
                        Context.MODE_PRIVATE);
                String output = "client@email.com,test," +
                        "client\nadmin@email.com,test,admin";
                fos.write(output.getBytes());
                fos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Deserialize the Database object from a previous session. If none exists,
     * initialize a new database instance.
     */
    public void loadPreviousSession() {
        try {
            Database.getInstance().readFromFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the path to the passwords file, then check
     * if the username and password provided match any logins in the file.
     * @param view is the view of the main activity screen.
     */
    public void checkLogin(View view) {
        String fileName = "passwords.txt";
        String user = null;

        File passwordsPath = new File(this.getApplicationContext()
                .getFilesDir() + "/" +fileName);
        try {

            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(passwordsPath)));

            String line;

            while ((line = bReader.readLine()) != null) {
                String[] readLine = line.split(",");

                EditText username = (EditText) findViewById(R.id.
                        username_edit_text);
                EditText password = (EditText) findViewById(R.id.
                        password_edit_text);

                // Check if the any username and password match.
                if (username.getText().toString().equals(readLine[0]) &&
                        password.getText().toString().equals(readLine[1])) {

                    // If so check if the user is a client or a admin
                    // and pass the according info.
                    user = getString(R.string.user_authority);
                    if (readLine[2].equals("client")) {

                        Intent intent = new Intent(this,
                                ClientMainActivity.class);
                        // Pass the clients email, and the fact
                        // that they are a client to the next intent
                        intent.putExtra(getString(R.string.client_email),
                                username.getText().toString());
                        intent.putExtra(user, getString(R.string.user_client));
                        startActivity(intent);

                    } else if (readLine[2].equals("admin")) {

                        Intent intent = new Intent(this,
                                AdminMainActivity.class);
                        // Pass to the next intent the
                        // fact that this user is an admin.
                        intent.putExtra(user, getString(R.string.user_admin));
                        startActivity(intent);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user == null) {
            // If username or password do not match, then trigger an alert.
            alert("Failed.", "Username or password not found.", this);
        }
    }

    /**
     * Displays an alert when called with the given title and message.
     * @param title is the title of the alert.
     * @param message is the message of the alert.
     * @param activity is the activity object where this is called.
     */
    public static void alert(String title, String message, Activity activity) {
        AlertDialog loginFailDialog =
                new AlertDialog.Builder(activity).create();
        loginFailDialog.setTitle(title);
        loginFailDialog.setMessage(message);
        loginFailDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        loginFailDialog.show();
    }
}
