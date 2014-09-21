package com.andyridge.minutetimerlite;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class About extends Activity
{	
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		setContentView(R.layout.about);
		
		TextView absLink = (TextView) findViewById(R.id.absLink);
		TextView armsLink = (TextView) findViewById(R.id.armsLink);
		TextView legsLink = (TextView) findViewById(R.id.legsLink);
		TextView bunsLink = (TextView) findViewById(R.id.bunsLink);
		
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
	}
}
