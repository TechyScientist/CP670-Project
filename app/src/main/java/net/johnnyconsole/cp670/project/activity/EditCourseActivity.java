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
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityEditCourseBinding;
import net.johnnyconsole.cp670.project.helper.DatabaseStatement;
import net.johnnyconsole.cp670.project.helper.DatabaseTask;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Johnny Console
 * Registration App EditCourseActivity.java
 * Allows administrators to modify the information
 * for a course record (other than the term and CRN) in the database.
 * Last Modified: 21 July 2023
 */
public class EditCourseActivity extends AppCompatActivity {

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

    private ArrayList<String> terms = new ArrayList<>();
    private Spinner spCourseTerm;
    private EditText etCRN, etCourseCode, etCourseTitle, etPrerequisites,
            etExclusions, etInstructor, etDayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEditCourseBinding binding = ActivityEditCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.editCourse);

        spCourseTerm = findViewById(R.id.spCourseTerm);
        etCourseCode = findViewById(R.id.etCourseCode);
        etCRN = findViewById(R.id.etCRN);
        etCourseTitle = findViewById(R.id.etCourseTitle);
        etPrerequisites = findViewById(R.id.etPrerequisites);
        etExclusions = findViewById(R.id.etExclusions);
        etInstructor = findViewById(R.id.etInstructor);
        etDayTime = findViewById(R.id.etDayTime);

        Cursor c = database.rawQuery("SELECT code FROM terms;", null);
        while (c.moveToNext()) {
            terms.add(c.getString(0));
        }
        c.close();
        spCourseTerm.setAdapter(new TermAdapter(this));


        findViewById(R.id.btSearch).setOnClickListener(view -> {
            if (etCRN.getText().toString().isEmpty()) {
                Snackbar error = Snackbar.make(view,
                        R.string.noSearchText, Snackbar.LENGTH_INDEFINITE);
                error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();
            } else {
                Cursor cursor = database.rawQuery("SELECT * FROM courses WHERE term=? AND crn=?", new String[]{
                        ((TermAdapter) (spCourseTerm.getAdapter())).getItem(spCourseTerm.getSelectedItemPosition()),
                        etCRN.getText().toString()});
                if (cursor.moveToFirst()) {
                    spCourseTerm.setEnabled(false);
                    etCRN.setEnabled(false);
                    etCourseTitle.setText(cursor.getString(2));
                    etCourseCode.setText(cursor.getString(3));
                    etPrerequisites.setText(cursor.getString(4));
                    etExclusions.setText(cursor.getString(5));
                    etInstructor.setText(cursor.getString(6));
                    etDayTime.setText(cursor.getString(7));
                    findViewById(R.id.llWrapper).setVisibility(View.VISIBLE);
                } else {
                    Snackbar error = Snackbar.make(view,
                            R.string.noCourse, Snackbar.LENGTH_INDEFINITE);
                    error.setAction(R.string.dismiss, view1 -> error.dismiss()).show();
                }
                cursor.close();
            }
        });

        findViewById(R.id.btSaveChanges).setOnClickListener(view -> {
            if (etCourseCode.getText().toString().isEmpty() || etCourseTitle.getText().toString().isEmpty()) {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(R.string.missingInput)
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
                return;
            }
            new DatabaseTask().execute(new DatabaseStatement("UPDATE courses SET code=?," +
                    " title=?, prerequisites=?, exclusions=?, instructor=?, daytime=? WHERE term=? AND crn=?",
                    new String[]{
                            etCourseCode.getText().toString(),
                            etCourseTitle.getText().toString(),
                            etPrerequisites.getText().toString(),
                            etExclusions.getText().toString(),
                            etInstructor.getText().toString(),
                            etDayTime.getText().toString(),
                            ((TermAdapter) (spCourseTerm.getAdapter())).getItem(spCourseTerm.getSelectedItemPosition()),
                            etCRN.getText().toString()
                    }));
            setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.courseModified)));
            finish();
        });

        findViewById(R.id.btDeleteCourse).setOnClickListener(view ->
                new AlertDialog.Builder(this).setTitle(R.string.deleteUser)
                        .setMessage(R.string.confirmDeleteCourse)
                        .setPositiveButton(R.string.exitYes, (dialog, i) -> {
                            new DatabaseTask().execute(new DatabaseStatement("DELETE FROM courses WHERE term=? AND crn=?",
                                    new String[]{
                                            ((TermAdapter) (spCourseTerm.getAdapter())).getItem(spCourseTerm.getSelectedItemPosition()),
                                            etCRN.getText().toString()
                                    }));
                            setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.courseDeleted, etCRN.getText().toString(),
                                    ((TermAdapter) (spCourseTerm.getAdapter())).getItem(spCourseTerm.getSelectedItemPosition()))));
                            finish();
                        })
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.editCourse)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.editCourse), getString(R.string.editCourseInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}