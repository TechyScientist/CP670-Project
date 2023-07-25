package net.johnnyconsole.cp670.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static androidx.lifecycle.Lifecycle.State.RESUMED;

import android.view.Menu;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import net.johnnyconsole.cp670.project.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InstrumentalMainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void onCreate() {
        ActivityScenario<MainActivity> scenario = rule.getScenario();
        assertEquals("net.johnnyconsole.cp670.project.activity.MainActivity", MainActivity.class.getName());
        assertEquals(RESUMED, scenario.getState());
        scenario.onActivity(activity -> {
            Button btSchedule = activity.findViewById(R.id.btSchedule),
                    btProfile = activity.findViewById(R.id.btProfile);
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            Menu menu = toolbar.getMenu();
            assertNotNull(toolbar);
            assertNotNull(toolbar.getTitle());
            assertEquals(activity.getString(R.string.MainActivityTitle), toolbar.getTitle());
            assertNotNull(menu);
            assertNotNull(menu.getItem(0));
            assertEquals(R.id.action_help, menu.getItem(0).getItemId());
            assertNotNull(btSchedule);
            assertNotNull(btProfile);
            assertEquals(activity.getString(R.string.schedule), btSchedule.getText());
            assertEquals(activity.getString(R.string.profile), btProfile.getText());
        });
    }


}
