package unal.edu.co.reto9;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by FABIAN on 16/10/2017.
 */

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        final EditTextPreference proximity_radiusPref = (EditTextPreference) findPreference("proximity_radius");
        String proximity_radius = prefs.getString("proximity_radius","10000");
        proximity_radiusPref.setSummary(proximity_radius);

        proximity_radiusPref
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference,
                                                      Object newValue) {
                        proximity_radiusPref.setSummary((CharSequence) newValue);
                        // Since we are handling the pref, we must save it
                        SharedPreferences.Editor ed = prefs.edit();
                        ed.putString("proximity_radius", newValue.toString());
                        ed.commit();
                        return true;
                    }
                });
    }
}
