package net.johnnyconsole.cp670.project;

import static androidx.lifecycle.Lifecycle.State.RESUMED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.text.InputType;
import android.view.Menu;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import net.johnnyconsole.cp670.project.activity.MainActivity;
import net.johnnyconsole.cp670.project.activity.SignInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InstrumentalSignInActivityTest {
    @Rule
    public ActivityScenarioRule<SignInActivity> rule = new ActivityScenarioRule<>(SignInActivity.class);

    @Test
    public void onCreate() {
        ActivityScenario<SignInActivity> scenario = rule.getScenario();
        assertEquals("net.johnnyconsole.cp670.project.activity.SignInActivity", SignInActivity.class.getName());
        assertEquals(RESUMED, scenario.getState());
        scenario.onActivity(activity -> {
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            Menu menu = toolbar.getMenu();
            assertNotNull(toolbar);
            assertNotNull(toolbar.getTitle());
            assertEquals(activity.getString(R.string.profile), toolbar.getTitle());
            assertNotNull(menu);
            assertNotNull(menu.getItem(0));
            assertEquals(R.id.action_help, menu.getItem(0).getItemId());
            Button btSignIn = activity.findViewById(R.id.btSignIn);
            assertNotNull(btSignIn);
            assertEquals(activity.getString(R.string.profile), btSignIn.getText());
            assertNotNull(activity.etUsername);
            assertEquals(activity.getString(R.string.usernameHint), activity.etUsername.getHint());
            assertTrue(activity.etUsername.getText().toString().isEmpty());
            assertEquals(InputType.TYPE_CLASS_TEXT, activity.etUsername.getInputType());
            assertNotNull(activity.etPassword);
            assertEquals(activity.getString(R.string.passwordHint), activity.etPassword.getHint());
            assertTrue(activity.etPassword.getText().toString().isEmpty());
            assertEquals(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, activity.etPassword.getInputType());
        });
    }


}
