package cz.vysinsky.circleguard.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import cz.vysinsky.circleguard.R;

public class SettingsFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
