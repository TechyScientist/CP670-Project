package net.johnnyconsole.cp670.project.activity;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.username;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityRegisterBinding;
import net.johnnyconsole.cp670.project.helper.DatabaseStatement;
import net.johnnyconsole.cp670.project.helper.DatabaseTask;
import net.johnnyconsole.cp670.project.objects.Course;
import net.johnnyconsole.cp670.project.objects.Term;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App RegisterActivity.java
 * Allows students to add or remove courses from
 * their current ungraded courses.
 * Last Modified: 24 July 2023
 */
public class RegisterActivity extends AppCompatActivity {

    private ListView lvCourseList, lvMyCourses;
    private Spinner spSearchField, spSearchTerm;
    private EditText etSearchText;
    private ArrayList<Course> courseList = new ArrayList<>(), myCourses = new ArrayList<>();
    private ArrayList<Term> terms = new ArrayList<>();

    private class CourseListAdapter extends ArrayAdapter<String> {

        public CourseListAdapter(Context context) {
            super(context, R.layout.layout_list_item, R.id.tvCourse);
        }

        public int getCount() {
            return courseList.size();
        }

        public String getItem(int position) {
            return courseList.get(position).code + ": " + courseList.get(position).title;
        }
    }

    private class MyCourseListAdapter extends ArrayAdapter<String> {

        public MyCourseListAdapter(Context context) {
            super(context, R.layout.layout_list_item, R.id.tvCourse);
        }

        public int getCount() {
            return myCourses.size();
        }

        public String getItem(int position) {
            return myCourses.get(position).code + ": " + myCourses.get(position).title;
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

        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.registration);

        lvCourseList = findViewById(R.id.lvCourseList);
        lvMyCourses = findViewById(R.id.lvMyCourses);
        spSearchField = findViewById(R.id.spSearchField);
        spSearchTerm = findViewById(R.id.spSearchTerm);
        etSearchText = findViewById(R.id.etSearchText);

        Cursor cursor = database.rawQuery("SELECT * FROM terms ORDER BY code;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            terms.add(new Term(cursor.getString(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        spSearchTerm.setAdapter(new TermAdapter(this));

        CourseListAdapter courseListAdapter = new CourseListAdapter(this);
        lvCourseList.setAdapter(courseListAdapter);

        MyCourseListAdapter myCourseListAdapter = new MyCourseListAdapter(this);
        lvMyCourses.setAdapter(myCourseListAdapter);

        findViewById(R.id.btSearch).setOnClickListener(v -> {
            String searchValue = etSearchText.getText().toString(),
                    termValue = terms.get(spSearchTerm.getSelectedItemPosition()).code;
            int searchField = spSearchField.getSelectedItemPosition();
            if (!searchValue.isEmpty()) {
                String sql = "SELECT * FROM courses WHERE term=\"" + termValue + "\" ";
                switch (searchField) {
                    case 0:
                        try {
                            Integer.parseInt(etSearchText.getText().toString());
                        } catch (NumberFormatException e) {
                            courseList.clear();
                            Snackbar error = Snackbar.make(v,
                                    R.string.noSearchText, Snackbar.LENGTH_INDEFINITE);

                            error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();
                            return;
                        }
                        sql += "AND crn=" + searchValue + ";";
                        break;
                    case 1:
                        sql += "AND code LIKE \"%" + searchValue + "%\";";
                        break;
                    case 2:
                        sql += "AND title LIKE \"%" + searchValue + "%\";";
                }
                Cursor c = database.rawQuery(sql, null);

                courseList.clear();
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        courseList.add(new Course(c.getInt(0), c.getString(2), c.getString(3)));
                        c.moveToNext();
                    }
                } else {
                    Snackbar error = Snackbar.make(v,
                            R.string.noCourse, Snackbar.LENGTH_INDEFINITE);

                    error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();

                }
                c.close();
            } else {
                courseList.clear();
                Snackbar error = Snackbar.make(v,
                        R.string.noSearchText, Snackbar.LENGTH_INDEFINITE);

                error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();
            }
            courseListAdapter.notifyDataSetChanged();
        });

        spSearchTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                myCourses.clear();
                Cursor c = database.rawQuery("SELECT courses.crn, courses.title, courses.code, registrations.grade FROM courses JOIN registrations ON courses.term=registrations.term AND courses.crn=registrations.crn WHERE registrations.term=? AND registrations.student=?;",
                        new String[]{terms.get(spSearchTerm.getSelectedItemPosition()).code, username});
                while (c.moveToNext()) {
                    int crn = c.getInt(0);
                    String title = c.getString(1),
                            code = c.getString(2),
                            grade = c.getString(3);
                    if (grade.equals("--")) {
                        myCourses.add(new Course(crn, code, title));
                    }
                }
                c.close();
                myCourseListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Intentionally left blank - do nothing
            }
        });

        lvCourseList.setOnItemClickListener((adapterView, view, position, id) -> {
            myCourses.add(courseList.remove(position));
            courseListAdapter.notifyDataSetChanged();
            myCourseListAdapter.notifyDataSetChanged();
        });

        lvMyCourses.setOnItemClickListener((adapterView, view, position, id) -> {
            courseList.add(myCourses.remove(position));
            courseListAdapter.notifyDataSetChanged();
            myCourseListAdapter.notifyDataSetChanged();
        });

        findViewById(R.id.btSaveChanges).setOnClickListener(view -> {
            new DatabaseTask().execute(new DatabaseStatement("DELETE FROM registrations WHERE student=? AND term=? AND grade=?;",
                    new String[]{username, terms.get(spSearchTerm.getSelectedItemPosition()).code, "--"}));
            for (Course c : myCourses) {
                new DatabaseTask().execute(new DatabaseStatement("INSERT INTO registrations (term, crn, student, grade) VALUES (?,?,?,?);",
                        new String[]{terms.get(spSearchTerm.getSelectedItemPosition()).code, c.crn + "", username, "--"}));
            }
            setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.registrationChanged)));
            finish();
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.registration)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.registration), getString(R.string.registrationInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}