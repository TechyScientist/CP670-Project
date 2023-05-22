/**
 * @author Johnny Console
 * Registration App MainActivity.java
 * Launcher activity presenting the user options
 * to view the course schedule or to sign in to
 * the app to use the remaining features.
 * Created: 22 May 2023
 * Last Modified: 22 May 2023
 */
package net.johnnyconsole.cp670.project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import net.johnnyconsole.cp670.project.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.MainActivityTitle);

        findViewById(R.id.btSchedule).setOnClickListener(view ->
                startActivity(new Intent(this, CourseScheduleActivity.class))
        );

        //Show the SignInActivity when the bottom button is pressed
        findViewById(R.id.btProfile).setOnClickListener(view ->
                startActivity(new Intent(this, SignInActivity.class))
        );

        //Create (or open) the database and save a reference to it
        //in ApplicationSession variables
        ApplicationSession.database = openDatabase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle(getString(R.string.helpTitle, getString(R.string.MainActivityTitle)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.MainActivityTitle), getString(R.string.mainActivityInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //If the user presses the back button, prompt them to confirm
        //before closing the app
        new AlertDialog.Builder(this).setTitle(R.string.exitTitle)
                .setMessage(R.string.exitMessage)
                .setPositiveButton(R.string.exitYes, (dialogInterface, i) -> finish())
                .setNegativeButton(R.string.exitNo, (dialogInterface, i) -> dialogInterface.dismiss())
                .create()
                .show();
    }

    @Override
    public void finish() {
        super.finish();
        //Close the database reference when finishing the activity
        ApplicationSession.database.close();
    }

    /*
        openDatabase()
        Create or open the database for the app and
        return a reference to it
     */
    private SQLiteDatabase openDatabase() {
        SQLiteDatabase db = openOrCreateDatabase("cons3250_project", MODE_PRIVATE, null);
        //This file defines the tables and some initial data for the database
        Scanner sqlFile = new Scanner(getResources().openRawResource(R.raw.database));
        //While there are statements to read, read and execute them
        while(sqlFile.hasNextLine()) {
            StringBuilder line = new StringBuilder(sqlFile.nextLine());
            //Because SQL Statements can be done over multiple lines, until a semicolon is found,
            //keep reading the current statement before executing it
            while(sqlFile.hasNextLine() && !line.toString().contains(";")) {
                line.append(sqlFile.nextLine());
            }
            db.execSQL(line.toString());
        }
        return db;
    }

}