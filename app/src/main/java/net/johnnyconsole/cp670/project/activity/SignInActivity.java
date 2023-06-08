package net.johnnyconsole.cp670.project.activity;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.first;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.last;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.userType;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.username;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivitySignInBinding;

import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App SignInActivity.java
 * Activity to collect and validate users
 * to sign them in to the app and start a session
 * Last Modified: 22 May 2023
 */
public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.profile);

        //Setting up activity view objects
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.profile)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.profile), getString(R.string.profileInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
       onSignInClicked(View v)
       OnClickListener for the sign in button
       Retreives the user's information from the database and if
       the username is found and the password matches, show the right
       activity based on their user account type
    */
    public void onSignInClicked(View v) {
        String user = etUsername.getText().toString().toLowerCase(),
                password = etPassword.getText().toString();

        //If the username or password are empty, show an error dialog
        if(user.isEmpty() || password.isEmpty()) {
            showErrorDialog();
        }
        else {
            //Check the database for the entered username
            Cursor c = database.rawQuery("SELECT * FROM users WHERE username=?;", new String[] {user});

            //If there is a row, check the password.
            if(c.moveToFirst()) {
                //If the password matches, set up the session variables in ApplicationSession
                if(password.equals(c.getString(4))) {
                    username = user;
                    first = c.getString(2);
                    last = c.getString(1);
                    userType = c.getString(3);
                    c.close();

                    //Clear the username and password fields
                    etUsername.getText().clear();
                    etPassword.getText().clear();

                    //If the user is a student, show the student home page
                    if(userType.equals("student")) {
                        startActivity(new Intent(this, StudentHomeActivity.class));
                    }
                    //Otherwise, we know that the user must be an administrator, so show the
                    //administrator home page. Future iterations may distinguish between faculty
                    //and administrative users for purposes of grade entering, time-dependent.
                    else {
                        startActivity(new Intent(this, AdministratorHomeActivity.class));
                    }
                }
                //Show an error dialog if the user's password is incorrect
                else {
                    showErrorDialog();
                }
            }
            //Show an error dialog if the user isn't in the database
            else {
                showErrorDialog();
            }
        }
    }

    /*
        showErrorDialog()
        Creates and shows an error dialog.
        Used if there is an error during the sign in checking.
     */
    private void showErrorDialog() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.errorTitle))
                .setMessage(getString(R.string.signinErrorMessage))
                .setPositiveButton(getString(R.string.dismiss), null)
                .create()
                .show();
    }
}