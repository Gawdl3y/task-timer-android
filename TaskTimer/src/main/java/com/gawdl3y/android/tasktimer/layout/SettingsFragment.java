package com.gawdl3y.android.tasktimer.layout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.activities.SettingsActivity;
import com.gawdl3y.android.tasktimer.util.Utilities;

/**
 * Fragment used to display the preferences
 * @author Schuyler Cebulskie
 */
public class SettingsFragment extends PreferenceFragment {
    private SettingsActivity mActivity;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize some stuff
        mActivity = (SettingsActivity) getActivity();

        // Add the preferences and the change listener
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(mActivity).registerOnSharedPreferenceChangeListener(changeListener);

        // Add click listener to the Play Store preference
        Preference playStorePref = findPreference("pref_playStore");
        playStorePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Utilities.openPlayStore(mActivity);
                return true;
            }
        });

        // Update the summaries of all of the preferences
        mActivity.updateSummaries(this);
    }

    /**
     * The change listener
     */
    private final SharedPreferences.OnSharedPreferenceChangeListener changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            mActivity.onPreferenceChange(findPreference(key));
        }
    };


    /**
     * Create a new instance of SettingsFragment
     * @return a new instance of SettingsFragment
     */
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
}
