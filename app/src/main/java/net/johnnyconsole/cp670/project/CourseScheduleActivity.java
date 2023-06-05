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

    private Spinner spSearchField, spSearchTerm;
    private EditText etSearchText;
    private ListView lvCourses;
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Term> terms = new ArrayList<>();

    private class CourseAdapter extends ArrayAdapter<String> {

        public CourseAdapter(Context context) {
            super(context, R.layout.layout_list_item, R.id.tvCourse);
        }

        public int getCount() {
            return courses.size();
        }

        public String getItem(int position) {
            return courses.get(position).code + ": " + courses.get(position).title;
        }
    }

    private class TermAdapter extends ArrayAdapter<String> {
        public TermAdapter(Context context) {
            super(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        }

        public int getCount() {
            return terms.size();
        }

        public String getItem(int position) {
            return terms.get(position).code + ": " + terms.get(position).title;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCourseScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.schedule);

        spSearchField = findViewById(R.id.spSearchField);
        etSearchText = findViewById(R.id.etSearchText);
        lvCourses = findViewById(R.id.lvCourses);
        spSearchTerm = findViewById(R.id.spSearchTerm);

        //Get Terms from Database - required for term spinner items
        Cursor cursor = database.rawQuery("SELECT * FROM terms;", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            terms.add(new Term(cursor.getString(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        spSearchTerm.setAdapter(new TermAdapter(this));

        CourseAdapter adapter = new CourseAdapter(this);
        lvCourses.setAdapter(adapter);

        lvCourses.setOnItemClickListener((parent, view, position, id) -> {

            //TODO: Decide on weather to use another activity or a set of fragments here!
            /*
            Intent intent = new Intent(this, CourseDetailsActivity.class);
            intent.putExtra("term", terms.get(spSearchTerm.getSelectedItemPosition().code)
            intent.putExtra("crn", courses.get(position).crn);
            startActivity(intent);
             */
            Snackbar.make(lvCourses, "Selected Term: " + terms.get(spSearchTerm.getSelectedItemPosition()).code + "\nSelected CRN: " + courses.get(position).crn, Snackbar.LENGTH_LONG).show();
        });

        findViewById(R.id.btSearch).setOnClickListener(view -> {
            String searchValue = etSearchText.getText().toString(),
                    termValue = terms.get(spSearchTerm.getSelectedItemPosition()).code;
            int searchField = spSearchField.getSelectedItemPosition();
            if(!searchValue.isEmpty()) {

                String sql = "SELECT * FROM courses WHERE term=\"" + termValue + "\" ";
                switch (searchField) {
                    case 0:
                        sql += "AND crn=" + searchValue + ";";
                        break;
                    case 1:
                        sql += "AND code LIKE \"%" + searchValue + "%\";";
                        break;
                    case 2:
                        sql += "AND title LIKE \"%" + searchValue + "%\";";
                }
                Cursor courseList = database.rawQuery(sql, null);

                courses.clear();
                if (courseList.moveToFirst()) {
                    while (!courseList.isAfterLast()) {
                        courses.add(new Course(courseList.getInt(0), courseList.getString(2), courseList.getString(3)));
                        courseList.moveToNext();
                    }
                }
                courseList.close();
            } else {
                courses.clear();
            }
            adapter.notifyDataSetChanged();
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