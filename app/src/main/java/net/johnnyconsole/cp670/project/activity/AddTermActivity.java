package net.johnnyconsole.cp670.project.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import net.johnnyconsole.cp670.project.R;
import net.johnnyconsole.cp670.project.databinding.ActivityAddTermBinding;
import net.johnnyconsole.cp670.project.helper.DatabaseStatement;
import net.johnnyconsole.cp670.project.helper.DatabaseTask;

import java.util.Objects;

import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;

/**
 * @author Johnny Console
 * Registration App AddTermActivity.java
 * Collects information required to add a term
 * entry into the databse, if it doesn't exist.
 * Last Modified: 26 June 2023
 */
public class AddTermActivity extends AppCompatActivity {
    private EditText etTermCode, etTermTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        net.johnnyconsole.cp670.project.databinding.ActivityAddTermBinding binding = ActivityAddTermBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.addTerm);

        etTermCode = findViewById(R.id.etTermCode);
        etTermTitle = findViewById(R.id.etTermTitle);

        findViewById(R.id.btAddTerm).setOnClickListener(view -> {
            if(etTermCode.getText() == null || etTermCode.getText().toString().isEmpty() ||
                etTermTitle.getText() == null || etTermCode.getText().toString().isEmpty()) {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(R.string.missingInput)
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
                return;
            }

            Cursor cursor = database.rawQuery("SELECT * FROM terms WHERE code=?;",
                    new String[] {etTermCode.getText().toString()});

            if(!cursor.moveToFirst()) {
                new DatabaseTask().execute(new DatabaseStatement("INSERT INTO terms (code, title) VALUES (?,?)",
                        new String[]{etTermCode.getText().toString(), etTermTitle.getText().toString()}));
                setResult(RESULT_OK, new Intent().putExtra("result", getString(R.string.addTermSuccess, etTermCode.getText().toString())));
                finish();
            }
            else {
                new AlertDialog.Builder(this).setTitle(R.string.errorTitle)
                        .setMessage(getString(R.string.termExists, etTermCode.getText().toString()))
                        .setPositiveButton(R.string.dismiss, null)
                        .create()
                        .show();
            }
            cursor.close();
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.addTerm)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.addTerm), getString(R.string.addTermInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}