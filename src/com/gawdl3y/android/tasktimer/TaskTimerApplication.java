package com.gawdl3y.android.tasktimer;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;

/**
 * The base application class for Task Timer
 * @author Schuyler Cebulskie
 */
public class TaskTimerApplication extends Application {
    // Static properties
    private static final String TAG = "Application";
    public static final boolean DEBUG = true;
    public static String PACKAGE;
    public static Resources RESOURCES;
    public static SharedPreferences PREFERENCES;
    public static int THEME = R.style.Theme_Dark;

    // Data
    public static ArrayList<Group> GROUPS;

    /* (non-Javadoc)
     * The application is being created
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Set static properties
        PACKAGE = getPackageName();
        RESOURCES = getResources();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PREFERENCES = PreferenceManager.getDefaultSharedPreferences(this);

        // Set theme
        String themeStr = PREFERENCES.getString("pref_theme", "0");
        THEME = themeStr.equals("2") ? R.style.Theme_Light_DarkActionBar : (themeStr.equals("1") ? R.style.Theme_Light : R.style.Theme_Dark);

        Log.v(TAG, "Created");
    }
}
