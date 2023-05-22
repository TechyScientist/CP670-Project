package net.johnnyconsole.cp670.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import net.johnnyconsole.cp670.project.databinding.ActivityAdministratorHomeBinding;
import java.util.Objects;

public class AdministratorHomeActivity extends AppCompatActivity {
    private ActivityAdministratorHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdministratorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.adminWelcome);
        findViewById(R.id.btSchedule).setOnClickListener(view ->
                startActivity(new Intent(this, CourseScheduleActivity.class))
        );

        /*
         findViewById(R.id.btRegistration).setOnClickListener(view ->
                startActivity(new Intent(this, RegistrationChangeActivity.class))
        );

         findViewById(R.id.btGrades).setOnClickListener(view ->
                startActivity(new Intent(this, ViewGradesActivity.class))
        );

         findViewById(R.id.btProgress).setOnClickListener(view ->
                startActivity(new Intent(this, DegreeProgressActivity.class))
        );

         findViewById(R.id.btChangepw).setOnClickListener(view ->
                startActivity(new Intent(this, ChangePasswordActivity.class))
        );
        */
        findViewById(R.id.btSignOut).setOnClickListener(view ->
                new AlertDialog.Builder(this).setTitle(R.string.signout)
                        .setMessage(R.string.confirmSignout)
                        .setPositiveButton(R.string.exitYes, (dialog, id) -> finish())
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
                    .setTitle(getString(R.string.helpTitle, getString(R.string.schedule)))
                    .setMessage(getString(R.string.helpMessage, getString(R.string.schedule), getString(R.string.scheduleInfo)))
                    .setPositiveButton(R.string.dismiss, null)
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(R.string.signout)
                .setMessage(R.string.confirmSignout)
                .setPositiveButton(R.string.exitYes, (dialog, id) -> finish())
                .setNegativeButton(R.string.exitNo, null)
                .create()
                .show();
    }
}