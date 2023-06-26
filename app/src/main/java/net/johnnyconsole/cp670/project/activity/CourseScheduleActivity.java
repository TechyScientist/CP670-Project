package net.johnnyconsole.cp670.project.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityCourseScheduleBinding;
import net.johnnyconsole.cp670.project.fragment.CourseInformationFragment;
import net.johnnyconsole.cp670.project.fragment.CourseListFragment;
import net.johnnyconsole.cp670.project.objects.Course;

import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App CourseScheduleActivity.java
 * Activity allowing the user to search the course
 * schedule.
 * Last Modified: 5 June 2023
 */
public class CourseScheduleActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Don't use the saved instance state to create the activity
        //onRestoreInstanceState handles doing it.
        super.onCreate(null);
        ActivityCourseScheduleBinding binding = ActivityCourseScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.schedule);

    }


    public void onSaveInstanceState(@NonNull Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        if(fragment instanceof CourseInformationFragment) {
            instanceState.putString("term", ((CourseInformationFragment) fragment).termCode);
            instanceState.putInt("crn", ((CourseInformationFragment) fragment).crn);
        }
    }
    @Override
    public void onRestoreInstanceState(Bundle instanceState) {
        super.onRestoreInstanceState(instanceState);
        fragment = new CourseListFragment();
        if(instanceState.getString("term", null) != null) {
            fragment = new CourseInformationFragment(instanceState.getString("term"),
                    instanceState.getInt("crn"), (CourseListFragment) fragment);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fvFragment, fragment).commitNow();
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

    public void notifyFragmentChanged(Fragment fragment) {
        this.fragment = fragment;
    }
}