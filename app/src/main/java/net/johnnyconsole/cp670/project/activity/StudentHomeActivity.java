package net.johnnyconsole.cp670.project.activity;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.first;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityStudentHomeBinding;

import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App StudentHomeActivity.java
 * Activity allowing student users to access the
 * student options of the app
 * Last Modified: 26 June 2023
 */
public class StudentHomeActivity extends AppCompatActivity {

    private final int REQUEST_CHANGE_PASSWORD = 130;
    //private final int REQUEST_REGISTER_COURSES = 300;

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

        //TODO: Register for Courses
        /*
         findViewById(R.id.btRegistration).setOnClickListener(view ->
                startActivityForResult(new Intent(this, RegistrationChangeActivity.class),
                        REQUEST_REGISTER_COURSES)
        );*/

        //TODO: Finish View Grades/Degree Progress
         findViewById(R.id.btGrades).setOnClickListener(view ->
                startActivity(new Intent(this, ViewGradesActivity.class))
        );

        findViewById(R.id.btChangePw).setOnClickListener(view ->
                startActivityForResult(new Intent(this, ChangePasswordActivity.class),
                        REQUEST_CHANGE_PASSWORD)
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.studentHome)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.studentHome), getString(R.string.mainActivityInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        } else if (id == R.id.action_signout) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CHANGE_PASSWORD && resultCode == RESULT_OK) {
            String activityResult = intent.getStringExtra("result");
            Snackbar.make(binding.getRoot(), activityResult, Snackbar.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}