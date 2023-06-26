package net.johnnyconsole.cp670.project.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import net.johnnyconsole.cp670.project.databinding.ActivityChangePasswordBinding;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.helper.DatabaseStatement;
import net.johnnyconsole.cp670.project.helper.DatabaseTask;

import java.util.Objects;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.*;

/**
 * @author Johnny Console
 * Registration App ChangePasswordActivity.java
 * Activity allowing the user to change their password
 * Last Modified: 26 June 2023
 */
public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.changepw);

        Cursor cursor = database.rawQuery("SELECT password FROM users WHERE username=?",new String[] {username});
        cursor.moveToFirst();
        String currentpw = cursor.getString(0);
        cursor.close();

        findViewById(R.id.btChangePw).setOnClickListener(view -> {
            String current = ((EditText) findViewById(R.id.etCurrentPassword)).getText().toString(),
                    newpw = ((EditText) findViewById(R.id.etNewPassword)).getText().toString(),
                    confirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();

            if(current.isEmpty() || newpw.isEmpty() || confirm.isEmpty()) {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(R.string.missingInput)
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
            }
            else if(!current.equals(currentpw) || !newpw.equals(confirm) || currentpw.equals(newpw)) {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(R.string.invalidInput)
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
            }
            else {
                new DatabaseTask().execute(new DatabaseStatement("UPDATE users SET password=? WHERE username=?",
                        new String[] {newpw, username}));
                setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.changePasswordSuccess)));
                finish();
            }
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.changepw)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.changepw), getString(R.string.changepwInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}