package ru.bolobanov.demoplotter;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by Bolobanov Nikolay on 18.09.15.
 */
@EActivity
public class PreferencesActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferencesFragment()).commit();

    }


}
