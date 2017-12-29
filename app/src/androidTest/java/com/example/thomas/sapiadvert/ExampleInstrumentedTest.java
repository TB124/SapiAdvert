package com.example.thomas.sapiadvert;

import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
        try{
        onView(withId(R.id.logOutButton)).perform(click());
        }
        catch(Throwable ex){

        }
    }

    @Test
    public void registrationButonCheck() {

        setUp(RegistrationActivity.class.getName());
        onView(withId(R.id.registerTextView)).perform(click());
        testOpen();
        Espresso.pressBack();
    }
    @Test
    public void loginButonCheck() {



        onView(withId(R.id.login_ac_emailInput)).perform(clearText(), typeText("x@x.com"));

        onView(withId(R.id.login_ac_passwordInput)).perform(clearText(), typeText("123456"));

        setUp(MainActivity.class.getName());
        onView(withId(R.id.login_ac_loginButton)).perform(closeSoftKeyboard(),click());
        testOpen();
        //Espresso.pressBack();
    }

    private Instrumentation.ActivityMonitor mBrowserActivityMonitor;

    protected void setUp(String activity) {


        mBrowserActivityMonitor = new Instrumentation.ActivityMonitor(activity, null, false);
        getInstrumentation().addMonitor(mBrowserActivityMonitor);
        //...
    }


    public void testOpen()
    {
        //...

        Activity activity = mBrowserActivityMonitor.waitForActivityWithTimeout(5 * 1000);
        assertNotNull("Activity was not started", activity);

        //...

    }
}

