package ru.bolobanov.demoplotter;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Bolobanov Nikolay on 18.09.15.
 */
public class PreferencesFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
