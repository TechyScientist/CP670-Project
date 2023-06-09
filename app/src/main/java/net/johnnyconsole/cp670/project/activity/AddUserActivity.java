package net.johnnyconsole.cp670.project.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityAddUserBinding;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;

import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App AddStudentActivity.java
 * Collects information required to add a student
 * entry into the databse, if they doesn't exist.
 * Last Modified: 8 June 2023
 */
public class AddUserActivity extends AppCompatActivity {

    private EditText etUsername, etFirstName, etLastName, etPassword;
    private RadioButton rbUserStudent;

    private class InsertUserThread extends Thread {
        @Override
        public void run() {
            database.execSQL("INSERT INTO users (username, first, last, userType, password) VALUES (?,?,?,?,?)",
                    new String[]{etUsername.getText().toString().toLowerCase(), etFirstName.getText().toString(),
                            etLastName.getText().toString(), rbUserStudent.isChecked() ? "student" : "admin",
                            etPassword.getText().toString()});
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        net.johnnyconsole.cp670.project.databinding.ActivityAddUserBinding binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.newStudent);

        etUsername = findViewById(R.id.etUsername);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);

        rbUserStudent = findViewById(R.id.rbUserStudent);

        findViewById(R.id.btAddUser).setOnClickListener(view -> {
            if (etUsername.getText() == null || etUsername.getText().toString().isEmpty() ||
                    etFirstName.getText() == null || etFirstName.getText().toString().isEmpty() ||
                    etLastName.getText() == null || etLastName.getText().toString().isEmpty() ||
                    etPassword.getText() == null || etPassword.getText().toString().isEmpty()) {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(R.string.missingInput)
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
                return;
            }
            Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username=?;",
                    new String[] {etUsername.getText().toString()});

            if(!cursor.moveToFirst()) {
                new InsertUserThread().start();
                setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.addUserSuccess, etUsername.getText().toString())));
                finish();
            }
            else {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(getString(R.string.userExists, etUsername.getText().toString()))
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
            }
            cursor.close();
        });

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
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.helpTitle, getString(R.string.newStudent)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.newStudent), getString(R.string.addStudentInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}