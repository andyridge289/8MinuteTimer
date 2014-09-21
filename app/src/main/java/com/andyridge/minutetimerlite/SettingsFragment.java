package com.andyridge.minutetimerlite;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.andyridge.minutetimerlite.lib.Constants;

public class SettingsFragment extends Fragment
{
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
        if (getArguments() != null) {
//            exercise = getArguments().getInt(EXERCISE);
        }
    }
}
