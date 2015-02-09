package com.bworld.Activities.SBM;

import com.bworld.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AdvertisementDetail extends Activity{

	String description;
	TextView txtDescription;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advertisement_details);
		
		Bundle extras= getIntent().getExtras();
		if(extras!=null)
		{
			description 	= extras.getString("adDescription");
		}
		initialisUI();
	}

	private void initialisUI()
	{
		txtDescription		=(TextView)findViewById(R.id.txtDescription);
		
		if(description!=null)
		{
			txtDescription.setText(description);
		}
		
	}
}
