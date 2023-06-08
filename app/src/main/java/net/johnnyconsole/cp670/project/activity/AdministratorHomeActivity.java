package net.johnnyconsole.cp670.project.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityAdministratorHomeBinding;

import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App AdministratorHomeActivity.java
 * Activity presenting the user options
 * to view the course schedule or to access any
 * of the app's administrator features
 * Last Modified: 22 May 2023
 */

@SuppressWarnings("deprecation")
public class AdministratorHomeActivity extends AppCompatActivity {
    private ActivityAdministratorHomeBinding binding;

    private final int REQUEST_NEW_TERM = 100;
    private final int REQUEST_NEW_STUDENT = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdministratorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.adminWelcome);
        findViewById(R.id.btSchedule).setOnClickListener(view ->
                startActivity(new Intent(this, CourseScheduleActivity.class))
        );

        findViewById(R.id.btNewTerm).setOnClickListener(view ->
                startActivityForResult(new Intent(this, AddTermActivity.class),
                        REQUEST_NEW_TERM)
        );

        findViewById(R.id.btNewStudent).setOnClickListener(view ->
                startActivityForResult(new Intent(this, AddUserActivity.class),
                        REQUEST_NEW_STUDENT)
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.helpTitle, getString(R.string.schedule)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.schedule), getString(R.string.scheduleInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        else if(id == R.id.action_signout) {
            new AlertDialog.Builder(this).setTitle(R.string.signout)
                    .setMessage(R.string.confirmSignout)
                    .setPositiveButton(R.string.exitYes, (dialog, i) -> finish())
                    .setNegativeButton(R.string.exitNo, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(R.string.signout)
                .setMessage(R.string.confirmSignout)
                .setPositiveButton(R.string.exitYes, (dialog, id) -> finish())
                .setNegativeButton(R.string.exitNo, null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int request, int result, Intent intent) {
        if((request == REQUEST_NEW_TERM || request == REQUEST_NEW_STUDENT)
                && result == RESULT_OK) {
            String activityResult = intent.getStringExtra("result");
            Snackbar.make(binding.getRoot(), activityResult, Snackbar.LENGTH_LONG).show();
        }
        else super.onActivityResult(request, result, intent);
    }
}