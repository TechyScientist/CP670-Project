package net.johnnyconsole.cp670.project;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.first;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import net.johnnyconsole.cp670.project.databinding.ActivityStudentHomeBinding;

import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App StudentHomeActivity.java
 * Activity allowing student users to access the
 * student options of the app
 * Last Modified: 22 May 2023
 */
public class StudentHomeActivity extends AppCompatActivity {
    private ActivityStudentHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.studentWelcome, first));

        findViewById(R.id.btSchedule).setOnClickListener(view ->
                startActivity(new Intent(this, CourseScheduleActivity.class))
        );

        /*
         findViewById(R.id.btRegistration).setOnClickListener(view ->
                startActivity(new Intent(this, RegistrationChangeActivity.class))
        );

         findViewById(R.id.btGrades).setOnClickListener(view ->
                startActivity(new Intent(this, ViewGradesActivity.class))
        );

         findViewById(R.id.btProgress).setOnClickListener(view ->
                startActivity(new Intent(this, DegreeProgressActivity.class))
        );

         findViewById(R.id.btChangepw).setOnClickListener(view ->
                startActivity(new Intent(this, ChangePasswordActivity.class))
        );
        */
         findViewById(R.id.btSignOut).setOnClickListener(view ->
                new AlertDialog.Builder(this).setTitle(R.string.signout)
                        .setMessage(R.string.confirmSignout)
                        .setPositiveButton(R.string.exitYes, (dialog, id) -> finish())
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.studentHome)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.studentHome), getString(R.string.mainActivityInfo)))
                    .setPositiveButton(R.string.dismiss, null)
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
}