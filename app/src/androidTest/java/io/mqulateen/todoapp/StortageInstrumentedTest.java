package io.mqulateen.todoapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class StortageInstrumentedTest {
    @Test
    public void equalStorageTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        MyPreferences mPrefs = new MyPreferences(appContext);

        ArrayList<String> tasks = new ArrayList<>(Arrays.asList("task number one", "task number two"));
        mPrefs.storeList(tasks);

        assertTrue(tasks.equals(mPrefs.retrieveList()));
    }

    @Test
    public void unequalStorageTest() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        MyPreferences mPrefs = new MyPreferences(appContext);

        ArrayList<String> tasks = new ArrayList<>(Arrays.asList("task number one"));
        mPrefs.storeList(tasks); // stored one element to shared pref

        tasks.add("task number two");

        assertFalse(tasks.equals(mPrefs.retrieveList())); //should not be equal as tasks size = 2 and shared pref size = 1
    }
}
