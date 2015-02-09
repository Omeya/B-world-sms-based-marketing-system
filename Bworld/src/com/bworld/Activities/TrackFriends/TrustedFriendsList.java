package com.bworld.Activities.TrackFriends;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.bworld.R;
import com.bworld.Adapters.FriendListAdapter;
import com.bworld.Adapters.TrustedFriendsAdapter;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.Utils;
import com.bworld.models.FriendsModel;

public class TrustedFriendsList extends Activity implements OnClickListener,URLConstants,IJSONTagConstants,IResultCodeConstants,IResponseConstants {

	private Button btnFriendsList;
	private Button btnTrustedFriends;
	private Button btnMap;
	private Intent i;
	private Context context;
	private TrustedFriendsAdapter adapter;
	private ListView list;
	private ArrayList<FriendsModel> arrayList;
	private CustomProgressDialog progress;
	GetTrustedFriends getFriends;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_friends);
		context=this;
		initialiseUI();
		clickListeners();
		
		if(Utils.checkInternetConnection(context))
		{
			getFriends   = new GetTrustedFriends();
			getFriends.execute();
		}
		else
		{
			AlertManager.shorttoastMessage(context, getString(R.string.Internet_check));
		}
	}
	private void initialiseUI()
	{
		btnFriendsList			=(Button)findViewById(R.id.btn_friends);
		btnTrustedFriends		=(Button)findViewById(R.id.btn_trusted_friends);
		btnMap					=(Button)findViewById(R.id.btn_map);
		list					=(ListView)findViewById(R.id.list);
		progress				= CustomProgressDialog.show(context, false);
		
		btnTrustedFriends.setBackgroundResource(R.drawable.blue);
		setButtons();
		
		
	//	adapter= new TrustedFriendsAdapter(context);
	//	list.setAdapter(adapter);
		
		
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
		else if(v==btnFriendsList)
		{
			btnFriendsList.setBackgroundResource(R.drawable.blue);
			btnMap.setBackgroundResource(R.drawable.white);
			btnTrustedFriends.setBackgroundResource(R.drawable.white);
			setButtons();
			
			i = new Intent(context,FriendsList.class);
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
	
	private class GetTrustedFriends extends AsyncTask<Void,Void,Void>
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
						adapter = new TrustedFriendsAdapter(context,arrayList);
						list.setAdapter(adapter);
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
				manager  = new APIManager(URL_GET_TRUSTED_FRIENDS);
				response = manager.commonPostData(URL_GET_TRUSTED_FRIENDS+AppPreferenceManager.getUserId(context), name, value);
			}
			catch(Exception e)
			{
				isException=true;
			}
			return null;
		}
		
	}
	private void clickListeners()
	{
		btnFriendsList.setOnClickListener(this);
		btnTrustedFriends.setOnClickListener(this);
		btnMap.setOnClickListener(this);
	}

}
