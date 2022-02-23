package com.metroid2010.mnemonicpasswords;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    private final long DEFAULT_TIMEOUT = (BuildConfig.DEBUG ? (5 * 1000) : (30 * 1000));

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
    public void filterCheckboxTest() {
        final String regex_filter_no_apostrophes = "([a-zA-Z\\s])+";
        final String regex_filter_no_proper_names = "([a-z'\\s])+";
        final String regex_filter_apostrophes = "([a-z'A-Z\\s])+";
        final String regex_filter_proper_names = "([a-zA-Z'\\s])+";
        final int MAX_TRIES = 20;

        // verify default state of checkbox: checked
        onView(withId(R.id.checkbox_filter_apostrophes)).check(matches(isChecked()));
        onView(withId(R.id.checkbox_filter_proper_names)).check(matches(isChecked()));

        // generate passwords with filters on, check filters work
        for (int i = 0; i < MAX_TRIES; i++) { // try several times to ensure some have apostrophe
            onView(withId(R.id.button_generate_password)).perform(click());
            onView(withId(R.id.textview_password_box)).check(matches(withPattern(regex_filter_apostrophes)));
            onView(withId(R.id.textview_password_box)).check(matches(withPattern(regex_filter_proper_names)));
        }

        // uncheck filter apostrophes, check filter was deactivated
        onView(withId(R.id.checkbox_filter_apostrophes)).perform(click());
        MatcherTriesData mtd1 = new MatcherTriesData(MAX_TRIES - 1); // for apostrophes
        for (int i = 0; i < MAX_TRIES; i++) {
            onView(withId(R.id.button_generate_password)).perform(click());
            // there should be apostrophes in some password generated now
            onView(withId(R.id.textview_password_box)).check(matches(withCharacterAfterSomeTries("'", mtd1)));
            // there should not be proper names (heuristic is uppercase characters in word)
            onView(withId(R.id.textview_password_box)).check(matches(withPattern(regex_filter_proper_names)));
        }

        // uncheck both filters, check both filters were deactivated
        onView(withId(R.id.checkbox_filter_proper_names)).perform(click());
        mtd1 = new MatcherTriesData(MAX_TRIES - 1); // reset counter
        MatcherTriesData mtd2 = new MatcherTriesData(MAX_TRIES - 1); // for proper names
        for (int i = 0; i < MAX_TRIES; i++) {
            onView(withId(R.id.button_generate_password)).perform(click());
            // there should be apostrophes
            onView(withId(R.id.textview_password_box)).check(matches(withCharacterAfterSomeTries("'", mtd1)));
            // there should be proper names (uppercase)
            onView(withId(R.id.textview_password_box)).check(matches(withUppercaseAfterSomeTries(mtd2)));
        }

        // check filter apostrophes, check filter was reactivated
        onView(withId(R.id.checkbox_filter_apostrophes)).perform(click());
        mtd2 = new MatcherTriesData(MAX_TRIES - 1); // reset counter
        for (int i = 0; i < MAX_TRIES; i++) {
            onView(withId(R.id.button_generate_password)).perform(click());
            // there should not be apostrophes
            onView(withId(R.id.textview_password_box)).check(matches(withPattern(regex_filter_apostrophes)));
            // there should be proper names (uppercase)
            onView(withId(R.id.textview_password_box)).check(matches(withUppercaseAfterSomeTries(mtd2)));
        }

        // check filter proper names again, check both filters are active again
        onView(withId(R.id.checkbox_filter_proper_names)).perform(click());
        for (int i = 0; i < MAX_TRIES; i++) {
            onView(withId(R.id.button_generate_password)).perform(click());
            // there should not be apostrophes
            onView(withId(R.id.textview_password_box)).check(matches(withPattern(regex_filter_apostrophes)));
            // there should not be proper names (uppercase)
            onView(withId(R.id.textview_password_box)).check(matches(withPattern(regex_filter_proper_names)));
        }
    }

    private static Matcher<View> withPattern(final String pattern) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            protected boolean matchesSafely(TextView item) {
                return item.getText().toString().matches(pattern);
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("matches regex " + pattern);
            }
        };
    }

    private class MatcherTriesData {
        public boolean condition = false;
        public int n_try = 0;
        public int max_tries;
        public MatcherTriesData(final int max_tries) {
            this.max_tries = max_tries;
        }
    }
    private static Matcher<View> withCharacterAfterSomeTries(final String character, MatcherTriesData mtd) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            protected boolean matchesSafely(TextView item) {
                if(item.getText().toString().contains(character)) {
                    mtd.condition = true;
                }
                if(!mtd.condition && mtd.max_tries == mtd.n_try) {
                    return false;
                }
                mtd.n_try++;
                return true;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("has character " + character + " after " + mtd.n_try + " tries from " + mtd.max_tries + " max");
            }
        };
    }

    private static Matcher<View> withUppercaseAfterSomeTries(MatcherTriesData mtd) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            protected boolean matchesSafely(TextView item) {
                // has any uppercase character?
                if(!item.getText().toString().equals(item.getText().toString().toLowerCase(Locale.ROOT))) {
                    mtd.condition = true;
                }
                if(!mtd.condition && mtd.max_tries == mtd.n_try) {
                    return false;
                }
                mtd.n_try++;
                return true;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("has uppercase character after " + mtd.n_try + " tries from " + mtd.max_tries + " max");
            }
        };
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
