package com.gawdl3y.android.tasktimer;

import java.util.ArrayList;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gawdl3y.android.tasktimer.classes.Group;
import com.gawdl3y.android.tasktimer.classes.Task;

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
	
	// Properties
	public final boolean debug = DEBUG;
	public String packageName = PACKAGE;
	public Resources resources = RESOURCES;
	public SharedPreferences preferences = PREFERENCES;
	public int theme = THEME;
	
	// Data
	public static ArrayList<Task> TASKS;
	public static ArrayList<Group> GROUPS;
	public ArrayList<Task> tasks = TASKS;
	public ArrayList<Group> groups = GROUPS;
	
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
		
		// Set properties
		packageName = PACKAGE;
		resources = RESOURCES;
		preferences = PREFERENCES;
		
		// Set theme
		String themeStr = PREFERENCES.getString("pref_theme", "0");
		THEME = themeStr.equals("2") ? R.style.Theme_Light_DarkActionBar : (themeStr.equals("1") ? R.style.Theme_Light : R.style.Theme_Dark);
		theme = THEME;
		
		// Set data
		TASKS = new ArrayList<Task>();
		GROUPS = new ArrayList<Group>();
		tasks = TASKS;
		groups = GROUPS;
		
		Log.v(TAG, "Created");
	}
}
