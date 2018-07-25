package de.tobi.slideshowwallpaper.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import java.util.HashSet;

import de.tobi.slideshowwallpaper.R;

public class WallpaperPreferencesFragment extends PreferenceFragmentCompat {

    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.wallpaper_preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSummaries();
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                updateSummary(sharedPreferences, key);
            }
        };
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    private void updateSummaries() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        updateSummary(sharedPreferences, getResources().getString(R.string.preference_add_images_key));
        updateSummary(sharedPreferences, getResources().getString(R.string.preference_seconds_key));
        updateSummary(sharedPreferences, getResources().getString(R.string.preference_ordering_key));
    }
    private void updateSummary(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getResources().getString(R.string.preference_add_images_key))) {
            findPreference(key).setSummary(sharedPreferences.getStringSet(key, new HashSet<String>()).size() + " " + getResources().getString(R.string.images_selected));
        } else if (key.equals(getResources().getString(R.string.preference_seconds_key))) {
            findPreference(key).setSummary(sharedPreferences.getString(key, "15"));
        } else if (key.equals(getResources().getString(R.string.preference_ordering_key))) {
            String[] orderings = getResources().getStringArray(R.array.orderings);
            String[] orderingValues = getResources().getStringArray(R.array.ordering_values);
            String currentValue = sharedPreferences.getString(key, "selection");
            int index = -1;
            for (int i = 0; (i < orderingValues.length) && (index < 0); i++) {
                if (orderingValues[i].equals(currentValue)) {
                    index = i;
                }
            }
            findPreference(key).setSummary(orderings[index]);
        }

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
        super.onPause();
    }
}
