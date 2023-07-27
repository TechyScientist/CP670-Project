package net.johnnyconsole.cp670.project.activity;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityAddCourseBinding;
import net.johnnyconsole.cp670.project.helper.DatabaseStatement;
import net.johnnyconsole.cp670.project.helper.DatabaseTask;
import net.johnnyconsole.cp670.project.objects.Term;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App AddCourseActivity.java
 * Collects information required to add a course
 * entry into the databse, if they doesn't exist.
 * Last Modified: 26 June 2023
 */
public class AddCourseActivity extends AppCompatActivity {

    private EditText etCRN,
            etCourseCode,
            etCourseTitle,
            etPrerequisites,   //This field is nullable - it can have a null value in the database.
            etExclusions,     //This field is nullable - it can have a null value in the database.
            etInstructor,     //This field is nullable - it can have a null value in the database.
            etDateTime;
    private Spinner spCourseTerm;
    private final ArrayList<Term> terms = new ArrayList<>();

    private class CourseAdapter extends ArrayAdapter<String> {
        public CourseAdapter(Context context) {
            super(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        }

        @Override
        public String getItem(int position) {
            return terms.get(position).code + ": " + terms.get(position).title;
        }

        public int getCount() {
            return terms.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        net.johnnyconsole.cp670.project.databinding.ActivityAddCourseBinding binding = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.newCourse);

        etCRN = findViewById(R.id.etCRN);
        etCourseCode = findViewById(R.id.etCourseCode);
        etCourseTitle = findViewById(R.id.etCourseTitle);
        etPrerequisites = findViewById(R.id.etPrerequisites);     //This field is nullable - it can have a null value in the database.
        etExclusions = findViewById(R.id.etExclusions);     //This field is nullable - it can have a null value in the database.
        etInstructor = findViewById(R.id.etInstructor);     //This field is nullable - it can have a null value in the database.
        etDateTime = findViewById(R.id.etDateTime);     //This field is nullable - it can have a null value in the database.

        spCourseTerm = findViewById(R.id.spCourseTerm);
        CourseAdapter adapter = new CourseAdapter(this);
        spCourseTerm.setAdapter(adapter);

        //Get Terms from Database - required for term spinner items
        Cursor cursor = database.rawQuery("SELECT * FROM terms;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            terms.add(new Term(cursor.getString(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();

        adapter.notifyDataSetChanged();

        findViewById(R.id.btAddCourse).setOnClickListener(view -> {
            if (etCRN.getText() == null || etCRN.getText().toString().isEmpty() ||
                    etCourseCode.getText() == null || etCourseCode.getText().toString().isEmpty() ||
                    etCourseTitle.getText() == null || etCourseTitle.getText().toString().isEmpty()) {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(R.string.missingInput)
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
                return;
            }
            String sql = "SELECT * FROM courses WHERE crn=? AND term=?;";
            Cursor crnCursor = database.rawQuery(sql,
                    new String[]{terms.get(spCourseTerm.getSelectedItemPosition()).code, etCRN.getText().toString()});

            if (crnCursor.getCount() != 0) {
                String prerequisites = etPrerequisites.getText() == null ||
                        etPrerequisites.getText().toString().isEmpty() ? null :
                        etPrerequisites.getText().toString(),
                        exclusions = etExclusions.getText() == null ||
                                etExclusions.getText().toString().isEmpty() ? null :
                                etExclusions.getText().toString(),
                        instructor = etInstructor.getText() == null ||
                                etInstructor.getText().toString().isEmpty() ? null :
                                etInstructor.getText().toString(),
                        dateTime = etDateTime.getText() == null ||
                                etDateTime.getText().toString().isEmpty() ? null :
                                etDateTime.getText().toString();

                new DatabaseTask().execute(new DatabaseStatement("INSERT INTO courses (crn, term, code, title, prerequisites, exclusions, instructor, daytime) VALUES (?,?,?,?,?,?,?,?);",
                        new String[]{
                                etCRN.getText().toString(),
                                terms.get(spCourseTerm.getSelectedItemPosition()).code,
                                etCourseCode.getText().toString(),
                                etCourseTitle.getText().toString(),
                                prerequisites,
                                exclusions,
                                instructor,
                                dateTime
                        }));
                setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.addCourseSuccess, etCourseCode.getText().toString(),
                        terms.get(spCourseTerm.getSelectedItemPosition()).code, etCRN.getText().toString())));
                finish();
            } else {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(getString(R.string.courseExists, etCRN.getText().toString(),
                                terms.get(spCourseTerm.getSelectedItemPosition()).code))
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
            }
            crnCursor.close();
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.newCourse)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.newCourse), getString(R.string.newCourseInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}