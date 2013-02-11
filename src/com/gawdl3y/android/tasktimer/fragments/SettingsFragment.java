package com.gawdl3y.android.tasktimer.fragments;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.SettingsActivity;

/**
 * @author Schuyler Cebulskie
 * Class used to display the settings on devices that are running Honeycomb or newer
 */
@TargetApi(11)
public class SettingsFragment extends PreferenceFragment {
	private SettingsActivity activity;

	/**
	 * Default constructor
	 */
	public SettingsFragment() {
		activity = null;
	}

	/**
	 * Get a fresh, clean, new instance of SettingsFragment
	 * @return a new instance of SettingsFragment
	 */
	public static final SettingsFragment newInstance() {
		SettingsFragment fragment = new SettingsFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	/* (non-Javadoc)
	 * The fragment is created
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize some stuff
		activity = (SettingsActivity) getActivity();

		// Add the preferences and the change listener
		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager.getDefaultSharedPreferences(activity).registerOnSharedPreferenceChangeListener(changeListener);

		// Update the summaries of all of the preferences
		activity.updateSummaries(this);
	}

	/**
	 * The change listener for API >= 11
	 */
	private final SharedPreferences.OnSharedPreferenceChangeListener changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs,
				String key) {
			activity.onPreferenceChange(findPreference(key));
		}
	};
}
