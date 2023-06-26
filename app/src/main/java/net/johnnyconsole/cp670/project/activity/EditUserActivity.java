package net.johnnyconsole.cp670.project.activity;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.username;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityAddUserBinding;
import net.johnnyconsole.cp670.project.databinding.ActivityEditUserBinding;
import net.johnnyconsole.cp670.project.helper.DatabaseStatement;
import net.johnnyconsole.cp670.project.helper.DatabaseTask;

import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App EditUserActivity.java
 * Allows administrators to modify the information
 * for a user (other than themselves) in the database.
 * Last Modified: 26 June 2023
 */
public class EditUserActivity extends AppCompatActivity {

    private EditText etUsername, etFirstName, etLastName, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEditUserBinding binding = ActivityEditUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.editStudent);

        etUsername = findViewById(R.id.etUsername);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btSearch).setOnClickListener(view -> {
            if(etUsername.getText().toString().isEmpty()) {
                Snackbar error = Snackbar.make(view,
                        R.string.noSearchText, Snackbar.LENGTH_INDEFINITE);
                error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();
            }
            else if(etUsername.getText().toString().equals(username)) {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(R.string.invalidInput)
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
            }
            else {
                Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username=?", new String[] {etUsername.getText().toString()});
                if(cursor.moveToFirst()) {
                    etUsername.setEnabled(false);
                    etLastName.setText(cursor.getString(1));
                    etFirstName.setText(cursor.getString(2));
                    etPassword.setText(cursor.getString(4));
                    findViewById(R.id.llWrapper).setVisibility(View.VISIBLE);
                }
                else {
                    Snackbar error = Snackbar.make(view,
                            R.string.noStudent, Snackbar.LENGTH_INDEFINITE);
                    error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();
                }
                cursor.close();
            }
        });

        findViewById(R.id.btSaveChanges).setOnClickListener(view -> {
            if(etFirstName.getText().toString().isEmpty() || etLastName.getText().toString().isEmpty() ||
            etPassword.getText().toString().isEmpty()) {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(R.string.missingInput)
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
                return;
            }
            new DatabaseTask().execute(new DatabaseStatement("UPDATE users SET first=?, last=?, password=? WHERE username=?",
                    new String[]{
                            etFirstName.getText().toString(),
                            etLastName.getText().toString(),
                            etPassword.getText().toString(),
                            etUsername.getText().toString()
                    }));
            setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.userModified)));
            finish();
        });

        findViewById(R.id.btDeleteUser).setOnClickListener(view ->
                new AlertDialog.Builder(this).setTitle(R.string.deleteUser)
                        .setMessage(R.string.confirmDeleteUser)
                        .setPositiveButton(R.string.exitYes, (dialog, i) -> {
                                new DatabaseTask().execute(new DatabaseStatement("DELETE FROM users WHERE username=?", new String[]{etUsername.getText().toString()}));
                                setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.userDeleted, etUsername.getText().toString())));
                                finish();
                        })
                        .setNegativeButton(R.string.exitNo, null)
                        .create()
                        .show()
        );

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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.editStudent)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.editStudent), getString(R.string.editStudentInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}