package com.bworld.manager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.bworld.R;

public class HeaderManager {

	private Activity context;
	private LayoutInflater inflator;
	private View headerView;
	private TextView headingText;
	private RelativeLayout.LayoutParams relativeParams;
	String headingString;

	
	public HeaderManager(Activity context, String string) {
		this.setContext(context);
		inflator = LayoutInflater.from(context);
		this.headingString=string;
	}
	
	public int getHeader(RelativeLayout headerHolder) 
	{
		initializeUI();
		relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		headerHolder.addView(headerView, relativeParams);
		return headerView.getId();
	}
	
	private void initializeUI() 
	{
			inflator = LayoutInflater.from(getContext());
			headerView = inflator.inflate(R.layout.common_header_single_text, null);
			headerView=((RelativeLayout) headerView.findViewById(R.id.relative_main_header));
			headingText = (TextView) headerView.findViewById(R.id.heading1);
			headingText.setText(headingString);
	}
	public void setContext(Activity context) 
	{
		this.context = context;
	}
	public Activity getContext() 
	{
		return context;
	}
}
