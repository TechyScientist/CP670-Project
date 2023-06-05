package net.johnnyconsole.cp670.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.snackbar.Snackbar;

import net.johnnyconsole.cp670.project.databinding.ActivityCourseScheduleBinding;
import net.johnnyconsole.cp670.project.objects.Course;
import net.johnnyconsole.cp670.project.objects.Term;

import java.util.ArrayList;
import java.util.Objects;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.*;

/**
 * @author Johnny Console
 * Registration App CourseScheduleActivity.java
 * Activity allowing the user to search the course
 * schedule.
 * Last Modified: 5 June 2023
 */
public class CourseScheduleActivity extends AppCompatActivity {
    private ActivityCourseScheduleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCourseScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.schedule);

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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.schedule)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.schedule), getString(R.string.scheduleInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}