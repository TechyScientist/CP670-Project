package net.johnnyconsole.cp670.project.activity;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityEnterGradesBinding;
import net.johnnyconsole.cp670.project.helper.DatabaseStatement;
import net.johnnyconsole.cp670.project.helper.DatabaseTask;
import net.johnnyconsole.cp670.project.objects.Student;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App EnterGradesActivity.java
 * Allows administrators to enter grades for a student
 * in a course
 * Last Modified: 23 July 2023
 */
public class EnterGradesActivity extends AppCompatActivity {

    private class TermAdapter extends ArrayAdapter<String> {

        public TermAdapter(Context context) {
            super(context, android.R.layout.simple_spinner_dropdown_item);
        }

        public String getItem(int position) {
            return terms.get(position);
        }

        public int getCount() {
            return terms.size();
        }
    }

    private class StudentListAdapter extends ArrayAdapter<String> {
        public StudentListAdapter(Context context) {
            super(context, R.layout.layout_list_item_select, R.id.tvCourse);
        }

        public int getCount() {
            return students.size();
        }

        public String getItem(int position) {
            Student s = students.get(position);
            return getString(R.string.studentGradeListItem, s.lastName, s.firstName, s.username, s.grade);
        }
    }


    private ArrayList<String> terms = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private Spinner spCourseTerm;
    private ListView lvStudentList;
    private EditText etCRN;

    private String code, term, CRN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEnterGradesBinding binding = ActivityEnterGradesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.enterGrades);

        spCourseTerm = findViewById(R.id.spCourseTerm);
        lvStudentList = findViewById(R.id.lvStudentList);
        etCRN = findViewById(R.id.etCRN);

        Cursor c = database.rawQuery("SELECT code FROM terms;", null);
        while (c.moveToNext()) {
            terms.add(c.getString(0));
        }
        c.close();

        StudentListAdapter adapter = new StudentListAdapter(this);
        spCourseTerm.setAdapter(new TermAdapter(this));

        lvStudentList.setAdapter(adapter);

        findViewById(R.id.btSearch).setOnClickListener(view -> {
            if (etCRN.getText().toString().isEmpty()) {
                Snackbar error = Snackbar.make(view,
                        R.string.noSearchText, Snackbar.LENGTH_INDEFINITE);
                error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();
            } else {
                students.clear();
                term = ((TermAdapter) (spCourseTerm.getAdapter())).getItem(spCourseTerm.getSelectedItemPosition());
                CRN = etCRN.getText().toString();
                Cursor cursor = database.rawQuery("SELECT code FROM courses WHERE term=? AND crn=?", new String[]{
                        term, CRN});
                if (cursor.moveToFirst()) {
                    code = cursor.getString(0);
                    spCourseTerm.setEnabled(false);
                    etCRN.setEnabled(false);
                    Cursor studentsList = database.rawQuery("SELECT users.first, users.last, users.username, registrations.grade FROM users JOIN registrations ON registrations.term=? AND registrations.crn=? AND users.username = registrations.student;", new String[]{
                            term, CRN});
                    while (studentsList.moveToNext()) {
                        String first = studentsList.getString(0),
                                last = studentsList.getString(1),
                                username = studentsList.getString(2),
                                grade = studentsList.getString(3);
                        Student student = new Student(first, last, username);
                        if (grade != null) {
                            student.grade = grade;
                        }
                        students.add(student);
                    }
                    studentsList.close();
                    lvStudentList.setVisibility(View.VISIBLE);
                    findViewById(R.id.btSaveChanges).setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar error = Snackbar.make(view,
                            R.string.noCourse, Snackbar.LENGTH_INDEFINITE);
                    error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();
                }
                cursor.close();
            }
        });

        lvStudentList.setOnItemClickListener((adapterView, view, position, id) -> {
            Student s = students.get(position);
            View gradeView = View.inflate(view.getContext(), R.layout.grade_dialog, null);
            ((TextView) gradeView.findViewById(R.id.tvCourse)).setText(getString(R.string.gradeCourse, code, term, CRN));
            ((TextView) gradeView.findViewById(R.id.tvStudent)).setText(getString(R.string.gradeStudent, s.lastName, s.firstName, s.username));

            new AlertDialog.Builder(view.getContext())
                    .setTitle(R.string.enterGrades)
                    .setView(gradeView)
                    .setPositiveButton(R.string.saveChanges, (dialog, i) -> {
                        s.grade = (String) (((Spinner) gradeView.findViewById(R.id.spGrade)).getSelectedItem());
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.dismiss, null)
                    .create()
                    .show();
        });

        findViewById(R.id.btSaveChanges).setOnClickListener(view -> {
            for (Student s : students) {
                new DatabaseTask().execute(new DatabaseStatement("UPDATE registrations SET grade=? WHERE student=? AND term=? AND crn=?;",
                        new String[]{s.grade, s.username, term, CRN}));
            }
            setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.gradesModified)));
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.enterGrades)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.enterGrades), getString(R.string.enterGradesInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}