package com.gawdl3y.android.tasktimer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.view.MenuItem;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.layout.SettingsFragment;

import java.util.Map;

public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(TaskTimerApplication.THEME);
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, SettingsFragment.newInstance()).commit();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

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
     * Performs necessary actions when a preference is changed
     * @param pref the preference that was changed
     */
    public void onPreferenceChange(Preference pref) {
        String key = pref.getKey();

        // List preference changed
        if(pref instanceof ListPreference) {
            // Change the summary
            pref.setSummary(((ListPreference) pref).getEntry());

            // See if the theme was changed
            if(key.equals("pref_theme")) {
                String theme = TaskTimerApplication.PREFERENCES.getString("pref_theme", "0");
                int themeIdent = theme.equals("2") ? R.style.Theme_Light_DarkActionBar : (theme.equals("1") ? R.style.Theme_Light : R.style.Theme_Dark);

                // Make sure the new theme is different
                if(themeIdent != TaskTimerApplication.THEME) {
                    TaskTimerApplication.THEME = themeIdent;

                    // Create a dialog to ask about restarting now or later
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle(R.string.dialog_restart_title).setMessage(R.string.dialog_restart_message);

                    // Restart Now
                    dialogBuilder.setPositiveButton(R.string.dialog_restart_button_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                            // Tell the main activity to restart
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("restart", true);
                            startActivity(intent);
                        }
                    });

                    // Restart Later
                    dialogBuilder.setNegativeButton(R.string.dialog_restart_button_later, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    // Create and show the dialog
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
            }
        // Ringtone preference changed
        } else if(pref instanceof RingtonePreference) {
            // Change the summary to the name of the ringtone
            Uri ringtoneUri = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString(key, "content://settings/system/notification_sound"));
            Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
            pref.setSummary(ringtone != null ? ringtone.getTitle(this) : null);
        }
    }

    /**
     * Updates the summaries of every preference
     * @param fragment the fragment that contains the preferences
     */
    public void updateSummaries(SettingsFragment fragment) {
        for(Map.Entry<String, ?> entry : TaskTimerApplication.PREFERENCES.getAll().entrySet()) {
            Preference pref = fragment.findPreference(entry.getKey());
            if(pref != null) onPreferenceChange(pref);
        }
    }
}
