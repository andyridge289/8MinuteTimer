package com.andyridge.minutetimerlite;

import com.andyridge.minutetimerlite.lib.Constants;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Stats extends Activity implements OnItemClickListener
{	
	// The names of the things in the list.
	private String[] linkNames = { "8 Minute Abs", "8 Minute Arms", "8 Minute Legs", "8 Minute Buns" };
	
	// The links in the list.
	private Uri[] links = { Uri.parse("http://www.youtube.com/watch?v=sWjTnBmCHTY"),
							Uri.parse("http://www.youtube.com/watch?v=sSby1UUhyts"),
							Uri.parse("http://www.youtube.com/watch?v=uLIfN-31Bgs"),
							Uri.parse("http://www.youtube.com/watch?v=dnBhn7YSsnM") };
	
	/**
	 * Sets the contents of the list
	 * 
	 * @param icicle The bundle.
	 */
	@Override public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		setContentView(R.layout.stats);
		
//		ListView list = (ListView) findViewById(R.id.linkList);
//		list.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, linkNames));
//		list.setOnItemClickListener(this);
	}
	
	
	/**
	 * Called when items in the list are clicked - just opens up the relevant link.
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Uri uri = links[position];
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
}
