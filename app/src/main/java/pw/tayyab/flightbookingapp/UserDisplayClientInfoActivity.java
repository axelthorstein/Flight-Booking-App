package pw.tayyab.flightbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import system.Database;

/**
 * A class to represent the clients information displayed activity.
 */
public class UserDisplayClientInfoActivity extends AppCompatActivity {

    /** The information the client inputted to be changed. */
    private String new_client = "";
    /** The login type of a user. */
    private String user;
    /** The username of the client. */
    private String client_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display_client_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Result");

        TextView client_info_text =
                (TextView) findViewById(R.id.new_client_info);
        user = getIntent().getExtras().getString(
                getString(R.string.user_authority));
        client_email = (String) getIntent().getExtras().get(
                        getString(R.string.client_email));

        // Return the new client string on the activity.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            new_client = extras.getString("new_client");
        }
        client_info_text.setText(new_client);

        // Save the changes.
        try {
            Database.getInstance().saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Returns the user to the client home page,
     *  and keeps track of login type.
     * @param view the edited client display view.
     */
    public void returnHome(View view) {
        Intent intent = new Intent(this, ClientMainActivity.class);
        intent.putExtra(getString(R.string.client_email), client_email);
        intent.putExtra(getString(R.string.user_authority), user);
        startActivity(intent);
    }
}
