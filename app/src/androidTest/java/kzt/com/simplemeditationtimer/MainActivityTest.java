package kzt.com.simplemeditationtimer;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatImageView = onView(allOf(withId(R.id.chane_time_button), isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction numberPicker = onView(withId(R.id.number_picker));
        numberPicker.perform(setTime(10));

        ViewInteraction appCompatButton = onView(allOf(withId(android.R.id.button1), withText("OK"),
                withParent(allOf(withId(R.id.buttonPanel), withParent(withId(R.id.parentPanel)))),
                isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.timer_text));
        textView.check(matches(withText("10:00")));
    }

    private static ViewAction setTime(final int minute) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(MaterialNumberPicker.class);
            }

            @Override
            public String getDescription() {
                return "set time into TimePicker";
            }

            @Override
            public void perform(UiController uiController, View view) {
                MaterialNumberPicker picker = (MaterialNumberPicker) view;
                picker.setValue(minute);
            }
        };
    }
}
