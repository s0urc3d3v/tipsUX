package net.techredesign.uxfortips;

import android.app.Application;
import android.content.Intent;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    @Rule public ActivityTestRule mainActivityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void canMainUiViewLaunch(){
        onView(withText("Calculate Your Tip")).check(ViewAssertions.matches(isDisplayed()));

    }


}