package com.andyridge.minutetimerlite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment
{
    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);
		
		TextView absLink = (TextView) v.findViewById(R.id.absLink);
		TextView armsLink = (TextView) v.findViewById(R.id.armsLink);
		TextView legsLink = (TextView) v.findViewById(R.id.legsLink);
		TextView bunsLink = (TextView) v.findViewById(R.id.bunsLink);
		
		absLink.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
			    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=sWjTnBmCHTY"));
			    startActivity(myIntent);
			}
		});
		
		armsLink.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
			    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=4qXErYytP3c"));
			    startActivity(myIntent);
			}
		});
		
		legsLink.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
			    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=XRnGdc4i6zo"));
			    startActivity(myIntent);
			}
		});
		
		bunsLink.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
			    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=dnBhn7YSsnM"));
			    startActivity(myIntent);
			}
		});

        return v;
	}
}
