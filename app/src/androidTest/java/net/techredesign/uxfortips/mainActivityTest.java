package net.techredesign.uxfortips;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Matthewelbing on 10/24/15.
 */
@RunWith(AndroidJUnit4.class)
public class mainActivityTest {
    @Rule
    ActivityTestRule mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, true);
    @Test
    public void isUiWelcomeTextShown(){
        onView(withText("Calculate your Tip")).check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void isSubtotalFeildAvaiable(){
        onView(withId(R.id.subTotalInputFieldET)).check(ViewAssertions.matches(isClickable()));
    }



}
