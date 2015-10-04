package ru.bolobanov.demoplotter;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Bolobanov Nikolay on 19.09.15.
 */

@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface PreferencesService {

    @DefaultBoolean(false)
    boolean free_move();

    @DefaultBoolean(true)
    boolean noise();

    @DefaultString("3")
    String listOfSources();

}
