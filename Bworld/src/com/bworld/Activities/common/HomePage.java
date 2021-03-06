package com.bworld.Activities.common;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bworld.R;
import com.bworld.Accounts.Facebook.FacebookAccount;
import com.bworld.Activities.Profile.HomeProfile;
import com.bworld.Activities.SBM.AutoSMS;
import com.bworld.Activities.TrackFriends.FriendsList;
import com.bworld.Activities.Withdraw.Withdraw;
import com.bworld.Adapters.HomeAdapter;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.Utils;
import com.bworld.services.SMSService;

public class HomePage extends Activity implements OnItemClickListener,OnClickListener, IResultCodeConstants {

	private GridView grid;
	private Context context;
	private HomeAdapter adapter;
	ArrayList<String> iconsArray;
	private Intent i;
	private TextView txtProfile;
	private TextView txtFriends;
	private TextView txtMaps;
	private TextView txtWithdraw;
	private ImageView imgLogout;
	FacebookAccount facebookAccount;
	
	
	String [] icons = {"SBM","LBM","Event","Bday SMS","Track Frnds","Deals","Hangouts","Movies","Favorites"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=this;
		setContentView(R.layout.homepage);
		initialiseUI();
		clickListeners();
		setResult(LOGIN_SUCESS);
		
	}
	
	private void initialiseUI()
	{
		facebookAccount 	= new FacebookAccount(HomePage.this);
		grid				=(GridView)findViewById(R.id.gridView1);
		txtProfile			=(TextView)findViewById(R.id.txt_profile);
		txtFriends			=(TextView)findViewById(R.id.txt_friends);
		txtMaps				=(TextView)findViewById(R.id.txt_maps);
		txtWithdraw			=(TextView)findViewById(R.id.txt_withdraw);
		
		imgLogout			=(ImageView)findViewById(R.id.img_logout);
		
		iconsArray		= new ArrayList<String>();
		adapter = new HomeAdapter(context, icons);
		grid.setAdapter(adapter);
	}

	private void clickListeners()
	{
		grid.setOnItemClickListener(this);
		txtProfile.setOnClickListener(this);
		txtFriends.setOnClickListener(this);
		txtMaps.setOnClickListener(this);
		txtWithdraw.setOnClickListener(this);
		imgLogout.setOnClickListener(this);
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		
		switch (arg2) {
		case 0: i = new Intent(HomePage.this,AutoSMS.class);
				startActivity(i);
			    break;
		case 4: i = new Intent(HomePage.this,FriendsList.class);
				startActivity(i);
				break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v==txtProfile)
		{
			i = new Intent(HomePage.this,HomeProfile.class);
			startActivity(i);
		}
		else if(v==txtFriends)
		{
			
		}
		else if(v==txtMaps)
		{
			
		}
		else if(v==txtWithdraw)
		{
			i = new Intent(HomePage.this,Withdraw.class);
			startActivity(i);
		}
		else if(v==imgLogout)
		{
			
			alertdialog(HomePage.this);
			
			
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	public  void alertdialog(final Activity context) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		//alert.setIcon(R.drawable.appicon);
		alert.setTitle(context.getResources().getString(R.string.app_name));
		alert.setMessage(R.string.logout_confirmation);
		alert.create();
		alert.setCancelable(false);
		alert.setPositiveButton(R.string.yes_logout_alert, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(Utils.isMyServiceRunning(context))
				{
					i = new Intent(context,SMSService.class);
					stopService(i);
				}
				AppPreferenceManager.saveUserId(context, "");
				AppPreferenceManager.saveFbUserId(context, "");
				facebookAccount.logout();
			//	setResult(LOGOUT);
				
				i = new Intent(HomePage.this,Login.class);
				i.putExtra("logout", true);
				startActivity(i);
				finish();
			}
		});
		alert.setNegativeButton(R.string.no_logout_alert, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		alert.show();
	}
	

}
