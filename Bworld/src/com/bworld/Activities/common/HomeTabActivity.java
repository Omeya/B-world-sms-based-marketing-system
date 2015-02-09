package com.bworld.Activities.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bworld.R;
import com.bworld.Activities.Friends.HomeFriends;
import com.bworld.Activities.Profile.HomeProfile;
import com.bworld.Activities.Withdraw.Withdraw;

public class HomeTabActivity extends TabActivity   {
	private View tabview;
	TabHost tabHost;
	Intent intent;
	TabHost.TabSpec spec;
	public static Activity context;
	AlertDialog.Builder alert;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);
		context = HomeTabActivity.this;
	
		setTabs();
	}

	private void setTabs() {
		/*
		 * tabHost=(TabHost)findViewById(android.R.id.tabhost);
		 * 
		 * tabHost.setBackgroundResource(R.drawable.titlebar_qr);
		 */
		addTab("", R.drawable.tab_home_button_selector, HomeProfile.class);
	//	addTab("", R.drawable.tab_favorit_button_selector, HomePage.class);
		addTab("", R.drawable.tab_invite_friends_button_selector, HomeFriends.class);
	//	addTab("", R.drawable.tab_mypost_button_selector, HomeMap.class);
		addTab(" ", R.drawable.tab_invite_friends_button_selector,Withdraw.class);
		//addTab("", R.drawable.tab_favorit_button_selector, Favorites.class);
		//addTab("", R.drawable.tab_mypost_button_selector, MyPosts.class);
	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		/* TabHost.TabSpec */spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.customtab, getTabWidget(), false);
		// getTabWidget().setBackgroundResource(R.drawable.tab_bar_bg);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);

		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.tabImage);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

	
	

	
	@Override
	public void onBackPressed() 
	{
		//AlertManager.alertdialog(HomeTabActivity.this);
	}
}
