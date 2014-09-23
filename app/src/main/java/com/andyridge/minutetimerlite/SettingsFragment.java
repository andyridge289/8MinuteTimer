package com.andyridge.minutetimerlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.andyridge.minutetimerlite.preferencesupport.PreferenceFragment;

import static com.andyridge.minutetimerlite.lib.Constants.LOCALES;
import static com.andyridge.minutetimerlite.lib.Constants.TAG;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private SharedPreferences sharedPreferences = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimerFragment.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);

        if (!prefs.getBoolean(getString(R.string.read_aloud),false)) {
            findPreference(getString(R.string.locale)).setEnabled(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.sharedPreferences = (this.sharedPreferences == null) ? sharedPreferences : this.sharedPreferences;

        if (isAdded()) {
            if (key.equals(getString(R.string.sound)))
                HomeActivity.sound = Integer.parseInt(sharedPreferences.getString(this.getString(R.string.sound), "0"));
            else if (key.equals(getString(R.string.read_aloud))) {

                // See what state it's in. If it's positive, do a check to see if the thing is available
                boolean readAloud = sharedPreferences.getBoolean(this.getString(R.string.read_aloud), false);
                if(readAloud) {
                    int availability = HomeActivity.tts.isLanguageAvailable(LOCALES[HomeActivity.locale]);
                    switch (availability) {
                        case TextToSpeech.LANG_AVAILABLE:
                        case TextToSpeech.LANG_COUNTRY_AVAILABLE:
                        case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
                            Log.d(TAG, "TTS available");
                            HomeActivity.readAloud = true;
                            break;

                        case TextToSpeech.LANG_MISSING_DATA:
                            readAloudDialog();
                            break;

                        case TextToSpeech.LANG_NOT_SUPPORTED:
                            Toast.makeText(getActivity(), "The language isn't supported by TTS.", Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(getString(R.string.read_aloud), false); // Does this change the preference tick box?
                            editor.commit();
                            break;
                    }
                    findPreference(getString(R.string.locale)).setEnabled(true);
                } else {
                    findPreference(getString(R.string.locale)).setEnabled(false);
                }

            } else if (key.equals(getString(R.string.locale))) {
                HomeActivity.locale = Integer.parseInt(sharedPreferences.getString(this.getString(R.string.locale), "0"));
                HomeActivity.resetLocale();
            }
        }
    }

    private void readAloudDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int result)
            {
                switch (result)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent ttsInstall = new Intent();
                        ttsInstall.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(ttsInstall);
                        HomeActivity.readAloud = true;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.read_aloud), false); // Does this change the preference tick box?
                        editor.commit();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("The Text To Speech (TTS) library is required to read out exercise names. Install now?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }
}

