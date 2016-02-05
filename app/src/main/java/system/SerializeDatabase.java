package system;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * A class to read and write files to the Database
 */
public class SerializeDatabase {

    /**
     * Writes to the database file.
     * @throws IOException
     */
    public static void saveToFile() throws IOException {
        try {
            FileOutputStream file =
                    new FileOutputStream(Environment.
                    		getExternalStorageDirectory().
                    		getPath() + "/database.ser");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutputStream output = new ObjectOutputStream(buffer);
            output.writeObject(Database.getInstance());
            output.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found: " + e);
        }
        catch (IOException ioe) {
            System.err.println("IOException: " + ioe);
        }
    }

    /**
     * Reads the database file.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void readFromFile() throws IOException,
            ClassNotFoundException {
        try {
            FileInputStream file =
                    new FileInputStream(Environment.
                    		getExternalStorageDirectory().
                    		getPath() + "/database.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInputStream input = new ObjectInputStream(buffer);

            Database item = (Database)input.readObject();

            Database.getInstance().setFlights(item.getFlights());
            Database.getInstance().setLocations(item.getLocations());
            Database.getInstance().setUsers(item.getUsers());

            input.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found: " + e);
        }
        catch (IOException ioe) {
            System.err.println("IOException: " + ioe);
        }

    }
}

