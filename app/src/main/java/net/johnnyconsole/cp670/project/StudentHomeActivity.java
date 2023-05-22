package net.johnnyconsole.cp670.project;

import static net.johnnyconsole.cp670.project.ApplicationSession.first;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import net.johnnyconsole.cp670.project.databinding.ActivityStudentHomeBinding;

import java.util.Objects;

public class StudentHomeActivity extends AppCompatActivity {
    private ActivityStudentHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.studentWelcome, first));
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.studentHome)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.studentHome), getString(R.string.mainActivityInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}