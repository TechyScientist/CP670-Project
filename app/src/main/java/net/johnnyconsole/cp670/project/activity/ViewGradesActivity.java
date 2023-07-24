package net.johnnyconsole.cp670.project.activity;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.username;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityViewGradesBinding;
import net.johnnyconsole.cp670.project.objects.Course;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App ViewGradesActivity.java
 * Allows students to see their grades and
 * progress
 * Last Modified: 24 July 2023
 */
public class ViewGradesActivity extends AppCompatActivity {

    private ListView lvGradesList;
    private TextView tvCoursesAttempted, tvCoursesPassed, tvPercentComplete;
    private ProgressBar pbProgress;

    private double completed, percent;
    private ArrayList<Course> registrations = new ArrayList<>();


    private class GradesListAdapter extends ArrayAdapter<String> {
        public GradesListAdapter(Context context) {
            super(context, R.layout.layout_list_item, R.id.tvCourse);
        }

        public int getCount() {
            return registrations.size();
        }

        public String getItem(int position) {
            Course c = registrations.get(position);
            return c.code + " " + c.term + ": " + c.title + (c.grade != null && !c.grade.equals("--") ? " (" + c.grade + ")" : "");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityViewGradesBinding binding = ActivityViewGradesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.grades);

        lvGradesList = findViewById(R.id.lvGradesList);
        tvCoursesAttempted = findViewById(R.id.tvCoursesAttempted);
        tvCoursesPassed = findViewById(R.id.tvCoursesPassed);
        tvPercentComplete = findViewById(R.id.tvPercentComplete);
        pbProgress = findViewById(R.id.pbProgress);

        Cursor cursor = database.rawQuery("SELECT registrations.*, courses.code, courses.title FROM registrations JOIN courses ON registrations.term = courses.term AND registrations.crn = courses.crn WHERE registrations.student=? ORDER BY registrations.grade DESC, registrations.term DESC", new String[] {username});
        while(cursor.moveToNext()) {
            int crn = cursor.getInt(0);
            String term = cursor.getString(1),
                    grade = cursor.getString(3),
                    title = cursor.getString(5),
                    code = cursor.getString(4);
            Course c = new Course(crn, code, title);
            c.grade = grade;
            c.term = term;
            registrations.add(c);

            if(isPassingGrade(c.grade)) {
                completed++;
            }
        }
        percent = (completed / registrations.size()) * 100.0;
        pbProgress.setProgress((int)percent);

        tvCoursesAttempted.setText(registrations.size() + "");
        tvCoursesPassed.setText(((int)completed) + "");
        tvPercentComplete.setText(getString(R.string.percentComplete, percent, '%'));
        lvGradesList.setAdapter(new GradesListAdapter(this));

        cursor.close();
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.grades)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.grades), getString(R.string.viewGradesInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isPassingGrade(String grade) {
        if(grade == null) return false;
        switch(grade) {
            case "F":
            case "XF":
            case "--":
                return false;
            default:
                return true;
        }
    }
}