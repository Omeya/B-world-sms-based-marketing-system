package com.bworld.Activities.TrackFriends;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.bworld.R;
import com.bworld.Adapters.FriendListAdapter;
import com.bworld.Listeners.MapLocationListener;
import com.bworld.constants.GlobalConstants;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.Utils;
import com.bworld.models.FriendsModel;

public class FriendsList extends Activity implements  OnClickListener,IJSONTagConstants,URLConstants,IResponseConstants {

	private Button btnFriendsList;
	private Button btnTrustedFriends;
	private Button btnMap;
	private Intent i;
	private Context context;
	private FriendListAdapter adapter;
	private ListView list;
	private CustomProgressDialog progress;
	private GetFriends getFriends;
	private ArrayList<FriendsModel> arrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_friends);
		context=this;
		initialiseUI();
		
		clickListeners();
		if(Utils.checkInternetConnection(context))
		{
			getFriends  = new GetFriends();
			getFriends.execute();
		}
		else
		{
			AlertManager.shorttoastMessage(context, getString(R.string.Internet_check));
		}
	}
	
	private void initialiseUI()
	{
		progress				= CustomProgressDialog.show(context, false);
		btnFriendsList			=(Button)findViewById(R.id.btn_friends);
		btnTrustedFriends		=(Button)findViewById(R.id.btn_trusted_friends);
		btnMap					=(Button)findViewById(R.id.btn_map);
		list					=(ListView)findViewById(R.id.list);
		
		btnFriendsList.setBackgroundResource(R.drawable.blue);
		setButtons();
	}
	private void clickListeners()
	{
		btnFriendsList.setOnClickListener(this);
		btnTrustedFriends.setOnClickListener(this);
		btnMap.setOnClickListener(this);
	}
	
	private class GetFriends extends AsyncTask<Void,Void,Void>
	{
		private boolean isException=false;
		private APIManager manager;
		private String response;
		private String [] name={};
		private String [] value={};
		@Override
		protected void onPostExecute(Void arg0) {
			super.onPostExecute(arg0);
			progress.dismiss();
			if(isException)
			{
				AlertManager.shorttoastMessage(context, getString(R.string.common_error));
			}
			else
			{
				try
				{
					FriendsModel model;
					JSONObject jsonResponse=  new JSONObject(response);
					if(jsonResponse.optString(CODE).equals(SUCCESS_CODE))
					{
						JSONObject obj;
						JSONArray userobj=  jsonResponse.getJSONArray(FRIENDS_USERS) ;
						for (int i = 0; i < userobj.length(); i++)
						{
							model= new FriendsModel();
							obj= userobj.getJSONObject(i);
							Log.e("", "name"+obj.optString(FRIENDS_FIRST_NAME));
							model.setName(obj.optString(FRIENDS_FIRST_NAME));
							model.setProfilePicture(URLDecoder.decode(obj.optString(FRIENDS_IMAGE)));
							model.setId(obj.optString(FRIENDS_ID));
							model.setStatus(obj.optString(FRIENDS_STATUS));
							arrayList.add(model);
						}
						adapter = new FriendListAdapter(context,arrayList);
						list.setAdapter(adapter);
					}
					else
					{
						AlertManager.shorttoastMessage(context, "No friends Found");
					}
				}
				catch(Exception e)
				{
					AlertManager.shorttoastMessage(context, getString(R.string.common_error));
				}
				
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isException=false;
			progress.show();
			arrayList = new ArrayList<FriendsModel>();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try
			{
				manager  = new APIManager(URL_GET_FRIENDS);
				response = manager.commonPostData(URL_GET_FRIENDS+AppPreferenceManager.getUserId(context)+"/"+Utils.getDeviceID(context), name, value);
			}
			catch(Exception e)
			{
				isException=true;
			}
			return null;
		}
	}
	@Override
	public void onClick(View v) 
	{
		if(v==btnMap)
		{
			btnFriendsList.setBackgroundResource(R.drawable.white);
			btnMap.setBackgroundResource(R.drawable.blue);
			btnTrustedFriends.setBackgroundResource(R.drawable.white);
			setButtons();
			i = new Intent(context,MapTracking.class);
			startActivity(i);
			finish();
		}
		else if(v==btnTrustedFriends)
		{
			btnFriendsList.setBackgroundResource(R.drawable.white);
			btnMap.setBackgroundResource(R.drawable.white);
			btnTrustedFriends.setBackgroundResource(R.drawable.blue);
			setButtons();
			
			i = new Intent(context,TrustedFriendsList.class);
			startActivity(i);
			finish();
		}
	}

	private void setButtons()
	{
		Utils.setPaddings(btnFriendsList);
		Utils.setPaddings(btnMap);
		Utils.setPaddings(btnTrustedFriends);
	}

	

	
}
