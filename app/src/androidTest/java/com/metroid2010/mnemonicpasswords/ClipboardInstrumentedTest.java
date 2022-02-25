package com.metroid2010.mnemonicpasswords;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ClipboardInstrumentedTest {
    private final long DEFAULT_TIMEOUT = (BuildConfig.DEBUG ? (5 * 1000) : (30 * 1000));

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void copyToClipboardWithGeneratedPasswordTest() {
        onView(withId(R.id.button_generate_password)).perform(click());
        onView(withId(R.id.button_copy_to_clipboard)).perform(click());

        final String[] clipboardContent = new String[1];
        getClipboardContent(clipboardContent);
        onView(withId(R.id.textview_password_box)).check(matches(withText(clipboardContent[0])));
    }

    @Test
    public void copyToClipboardWithEmptyPasswordTest() {
        getInstrumentation().runOnMainSync(() -> {
            final Context context = getApplicationContext();
            final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("", ""));
        });
        onView(withId(R.id.button_copy_to_clipboard)).perform(click());
        final String[] clipboardContent = new String[1];
        getClipboardContent(clipboardContent);
        MatcherAssert.assertThat(clipboardContent[0], is(""));
    }

    @Test
    public void copyToClipboardTimeoutTest() {
        onView(withId(R.id.button_generate_password)).perform(click());
        onView(withId(R.id.button_copy_to_clipboard)).perform(click());

        final String[] clipboardContent = new String[1];
        getClipboardContent(clipboardContent);
        onView(withId(R.id.textview_password_box)).check(matches(withText(clipboardContent[0])));

        final long timeout_delta = DEFAULT_TIMEOUT / 10;
        onView(isRoot()).perform(waitFor(DEFAULT_TIMEOUT - timeout_delta));
        getClipboardContent(clipboardContent);
        MatcherAssert.assertThat(clipboardContent[0], is(not("")));

        onView(isRoot()).perform(waitFor(timeout_delta));
        getClipboardContent(clipboardContent);
        MatcherAssert.assertThat(clipboardContent[0], is(""));
    }

    @Test
    public void copyToClipboardTimeoutOverlappingDifferentPasswordsTest() {
        final String[] clipboardContent = new String[1];
        final long timeout_delta = DEFAULT_TIMEOUT / 2;

        // generate a password and copy to clipboard
        onView(isRoot()).perform(waitFor(DEFAULT_TIMEOUT - timeout_delta));
        onView(withId(R.id.button_generate_password)).perform(click());
        onView(withId(R.id.button_copy_to_clipboard)).perform(click());
        getClipboardContent(clipboardContent);
        onView(withId(R.id.textview_password_box)).check(matches(withText(clipboardContent[0])));
        onView(isRoot()).perform(waitFor(timeout_delta));
        // DEFAULT_TIMEOUT has passed since first password, check the first clear instruction was
        //  not executed and erased the second, not intended for it, password
        getClipboardContent(clipboardContent);
        onView(withId(R.id.textview_password_box)).check(matches(withText(clipboardContent[0])));
        // verify that second password clears after intended time
        onView(isRoot()).perform(waitFor(DEFAULT_TIMEOUT - timeout_delta));
        getClipboardContent(clipboardContent);
        MatcherAssert.assertThat(clipboardContent[0], is(""));
    }

    @Test
    public void copyToClipboardTimeoutOverlappingSamePasswordsTest() {
        final String[] clipboardContent = new String[1];
        final long timeout_delta = DEFAULT_TIMEOUT / 2;

        // generate a password and check it gets deleted DEFAULT_TIMEOUT after
        // *last* click of copy button, not first
        onView(withId(R.id.button_generate_password)).perform(click());
        onView(withId(R.id.button_copy_to_clipboard)).perform(click());
        getClipboardContent(clipboardContent);
        onView(withId(R.id.textview_password_box)).check(matches(withText(clipboardContent[0])));
        onView(isRoot()).perform(waitFor(DEFAULT_TIMEOUT - timeout_delta));
        onView(withId(R.id.button_copy_to_clipboard)).perform(click());
        onView(isRoot()).perform(waitFor(DEFAULT_TIMEOUT));

        // DEFAULT_TIMEOUT seconds have passed since the first copy instruction,
        // check password was not cleared, respecting the second copy instruction
        getClipboardContent(clipboardContent);
        onView(withId(R.id.textview_password_box)).check(matches(withText(clipboardContent[0])));
    }

    private static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    private void getClipboardContent(String[] clipboardContent) {
        getInstrumentation().runOnMainSync(() -> {
            final Context context = getApplicationContext();
            final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboard.getPrimaryClip() != null) {
                clipboardContent[0] = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
            } else {
                clipboardContent[0] = "";
            }
        });
    }
}
