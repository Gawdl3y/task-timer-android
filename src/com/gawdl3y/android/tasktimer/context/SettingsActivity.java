package com.gawdl3y.android.tasktimer.context;

import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.layout.SettingsFragment;

public class SettingsActivity extends SherlockPreferenceActivity {
	private TaskTimerApplication app;

	/* (non-Javadoc)
	 * The activity is created
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		app = (TaskTimerApplication) getApplication();
		setTheme(app.theme);
		super.onCreate(savedInstanceState);

		// Display the settings
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// Use the old preference activity methods for devices older than Honeycomb
			addPreferencesFromResource(R.xml.preferences);
			app.preferences.registerOnSharedPreferenceChangeListener(changeListener);
			updateSummaries();
		} else {
			// Use the fragment for Honeycomb and newer
			getFragmentManager().beginTransaction().replace(android.R.id.content, SettingsFragment.newInstance()).commit();
		}
		
		// Make the action bar use "home as up"
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	/* (non-Javadoc)
	 * The activity is stopped
	 * @see com.actionbarsherlock.app.SherlockPreferenceActivity#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		app.preferences.unregisterOnSharedPreferenceChangeListener(changeListener);
	}
	
	/**
	 * Performs necessary actions when a preference is changed
	 * @param pref the preference that was changed
	 */
	public void onPreferenceChange(Preference pref) {
		String key = pref.getKey();
		
		// List preference changed
		if (pref instanceof ListPreference) {
			// Change the summary
			pref.setSummary(((ListPreference) pref).getEntry());

			// See if the theme was changed
			if (key.equals("pref_theme")) {
				String theme = app.preferences.getString("pref_theme", "0");
				int themeIdent = theme.equals("2") ? R.style.Theme_Light_DarkActionBar : (theme.equals("1") ? R.style.Theme_Light : R.style.Theme_Dark);

				// Make sure the new theme is different
				if (themeIdent != app.theme) {
					// Create a dialog to ask about restarting now or later
					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
					dialogBuilder.setTitle(R.string.dialog_restart_title).setMessage(R.string.dialog_restart_message);
					dialogBuilder.setPositiveButton(R.string.dialog_restart_button_now, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// Restart Now
							Intent intent = new Intent(getBaseContext(), MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("RESTART", true);
							startActivity(intent);
							finish();
						}
					});
					dialogBuilder.setNegativeButton(R.string.dialog_restart_button_later, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// Restart Later
							dialog.dismiss();
						}
					});

					// Create and show the dialog
					AlertDialog dialog = dialogBuilder.create();
					dialog.show();
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * A button on the action bar was pressed
	 * @see com.actionbarsherlock.app.SherlockPreferenceActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
	        case android.R.id.home:
	        	// Home pressed, return to main activity
	        	Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * Updates the summaries of every preference - API < 11
	 */
	@SuppressWarnings("deprecation")
	public void updateSummaries() {
		for(Map.Entry<String, ?> entry : app.preferences.getAll().entrySet()) {
			Preference pref = findPreference(entry.getKey());
			if(pref != null) onPreferenceChange(pref);
		}
	}

	/**
	 * Updates the summaries of every preference - API >= 11
	 * @param fragment the fragment that contains the preferences
	 */
	@TargetApi(11)
	public void updateSummaries(SettingsFragment fragment) {
		for(Map.Entry<String, ?> entry : app.preferences.getAll().entrySet()) {
			Preference pref = fragment.findPreference(entry.getKey());
			if(pref != null) onPreferenceChange(pref);
		}
	}

	/**
	 * The change listener - API < 11
	 */
	@SuppressWarnings("deprecation")
	private final SharedPreferences.OnSharedPreferenceChangeListener changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			onPreferenceChange(findPreference(key));
		}
	};
}
