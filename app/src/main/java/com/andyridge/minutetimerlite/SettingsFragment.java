package com.andyridge.minutetimerlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.andyridge.minutetimerlite.preferencesupport.PreferenceFragment;

import static com.andyridge.minutetimerlite.lib.Constants.TAG;
import static com.andyridge.minutetimerlite.lib.Constants.LOCALES;

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

        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.sharedPreferences = (this.sharedPreferences == null) ? sharedPreferences : this.sharedPreferences;
        Log.d(TAG, "Preference changed: " + key);

        if(key.equals(getString(R.string.sound)))
            HomeActivity.sound = Integer.parseInt(sharedPreferences.getString(this.getString(R.string.sound), "0"));
        else if (key.equals(getString(R.string.read_aloud))) {
            HomeActivity.readAloud = sharedPreferences.getBoolean(this.getString(R.string.read_aloud), false);
            if (HomeActivity.readAloud) {
                if (HomeActivity.tts.isLanguageAvailable(LOCALES[HomeActivity.locale]) != TextToSpeech.LANG_AVAILABLE) {
                    readAloudDialog();
                }
            }
        } else if (key.equals(getString(R.string.locale))) {
            HomeActivity.locale = Integer.parseInt(sharedPreferences.getString(this.getString(R.string.locale), "0"));
            HomeActivity.resetLocale();
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
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.read_aloud), false);
                        editor.commit();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("The Text To Speech (TTS) library is required to read out exercises. Install now?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }
}

