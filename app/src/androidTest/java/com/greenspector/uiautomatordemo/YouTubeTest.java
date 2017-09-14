package com.greenspector.uiautomatordemo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class YouTubeTest {

    private static final int TIMEOUT = 5000;

    private static final UiDevice DEVICE = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    private static final String YOUTUBE_PACKAGE = "com.google.android.youtube";

    @Before
    public void init() {
        // Start from the home screen
        DEVICE.pressHome();

        // Wait for launcher
        final String launcherPackage = DEVICE.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        DEVICE.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(YOUTUBE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        DEVICE.wait(Until.hasObject(By.pkg(YOUTUBE_PACKAGE).depth(0)), TIMEOUT);
    }

    @Test
    public void watchVideo() {
        try {
            // Click on the magnifying glass icon
            UiObject icon = DEVICE.findObject(new UiSelector().resourceId
                    (YOUTUBE_PACKAGE+":id/menu_search"));
            icon.waitForExists(TIMEOUT);

            icon.click();

            // Enter the desired search keyword
            UiObject editText = DEVICE.findObject(new UiSelector().text("Search YouTube"));
            editText.waitForExists(TIMEOUT);

            editText.setText("greenspector");
            DEVICE.pressEnter();

            // Launch the desired video
            UiObject video = DEVICE.findObject(new UiSelector()
                    .resourceId(YOUTUBE_PACKAGE+":id/title")
                    .text("Power Test Cloud - Greenspector (FR)"));
            video.waitForExists(TIMEOUT);

            video.click();

            // Wait 20 seconds
            synchronized (DEVICE) {
                try {
                    DEVICE.wait(20000);
                } catch (InterruptedException e) {
                    fail(e.getMessage());
                }
            }

        } catch (UiObjectNotFoundException e) {
            fail(e.getMessage());
        }
    }
}
