package com.example.thomas.sapiadvert;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleInstrumentedTest {

    private String mStringToBetyped;
    private String testEmail="x@x.com";
    private String testPassword="123456";
    private String testPhoneNumber="911";
    private String testFirstName="Che";
    private String testLastName="Guevara";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }

    @Test
    public void registationButtonTest() {
        logOut();
        setUp(RegistrationActivity.class.getName());
        onView(withId(R.id.registerTextView)).perform(click());
        testOpen();
    }
    @Test
    public void loginTest(){
        logIn();
    }

    @Test
    public void writeProfileTest(){
        logIn();
        setUp(EditProfileActivity.class.getName());
        onView(withId(R.id.main_ac_profilePictureImageView)).perform(closeSoftKeyboard(),click());
        testOpen();
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        editProfileTest();
        myAdvertisementsTest();
    }
    public void editProfileTest(){
        onView(withId(R.id.firstNameInput)).check(matches(withText(testFirstName)));
        onView(withId(R.id.lastNameInput)).check(matches(withText(testLastName)));
        onView(withId(R.id.emailInput)).check(matches(withText(testEmail)));
        onView(withId(R.id.phoneNumberInput)).check(matches(withText(testPhoneNumber)));
    }

    public void myAdvertisementsTest(){
        setUp(ViewMyAdvertisementsActivity.class.getName());
        onView(withId(R.id.viewMyAdvertisementsButton)).perform(closeSoftKeyboard(),click());
        testOpen();
    }
    /*
    @Test
    public void B() {





        setUp(EditProfileActivity.class.getName());
        onView(withId(R.id.main_ac_profilePictureImageView)).perform(closeSoftKeyboard(),click());
        testOpen();
        //Espresso.pressBack();
    }
    @Test
    public void C(){
        try {
            sleep(500);
        }
        catch(Throwable ex){

        }

    }
    */

    private void logOut(){
        try{
            onView(withId(R.id.logOutButton)).perform(click());
        }
        catch(Throwable ex){

        }
    }
    private void logIn(){
        logOut();
        onView(withId(R.id.login_ac_emailInput)).perform(clearText(), typeText(testEmail));
        onView(withId(R.id.login_ac_passwordInput)).perform(closeSoftKeyboard(),clearText(), typeText(testPassword));
        setUp(MainActivity.class.getName());
        onView(withId(R.id.login_ac_loginButton)).perform(closeSoftKeyboard(),click());
        testOpen();
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

