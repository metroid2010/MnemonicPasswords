package com.metroid2010.mnemonicpasswordgenerator;

import android.content.ClipboardManager;
import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    private ClipboardManager mClipboardManager;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void onCreateTest() {
        onView(withId(R.id.textview_password_box)).check(matches(withText(R.string.textview_password_box_placeholder)));
    }

    @Test
    public void generatePasswordTest() {
        onView(withId(R.id.button_generate_password)).perform(click());
        onView(withId(R.id.textview_password_box)).check(matches(not(withText(R.string.textview_error_generating_password))));
    }

    @Test
    public void copyToClipboardTest() {
        onView(withId(R.id.button_generate_password)).perform(click());
        onView(withId(R.id.button_copy_to_clipboard)).perform(click());

        final String[] clipboardContent = new String[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                final Context context = getApplicationContext();
                final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardContent[0] = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
            }
        });
        onView(withId(R.id.textview_password_box)).check(matches(withText(clipboardContent[0])));
    }
}
