package com.bworld.Activities.common;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bworld.R;
import com.bworld.Accounts.Facebook.FacebookAccount;
import com.bworld.Activities.SBM.AutoSMS;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.Utils;

public class FBActivity  extends Activity implements URLConstants,IResponseConstants,IJSONTagConstants,IResultCodeConstants{

	private CustomProgressDialog progress;
	private Context context;
	private FacebookAccount facebookAccount;
	private Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		context=this;
		initialiseUI();
		
		if(Utils.checkInternetConnection(context))
		{
			Signup fbSignup = new Signup();
			fbSignup.execute();
		}
		else
		{
			AlertManager.shorttoastMessage(context, getString(R.string.Internet_check));
		}
		
	}
	
	
	private void initialiseUI()
	{
		progress 			=CustomProgressDialog.show(context, false);
		facebookAccount		=new FacebookAccount(FBActivity.this);
	}
	
	class Signup extends AsyncTask<Void, Void, Void>
	{
		private boolean isException=false;
		APIManager manager;
		private String response;
		private String [] name={"username","password","name"};
		String[] value ;
		@Override
		protected Void doInBackground(Void... params) {
				try
				{
					value= new String[3];
					value[0]=facebookAccount.getUserName();
					value[1]="fb";
					value[2]=facebookAccount.getUserName();
					Log.e("", "user id"+facebookAccount.getUserId());
					Log.e("", "user name"+facebookAccount.getUserName());
					response=manager.commonPostData(URL_FB_LOGIN, name, value);
					
					facebookAccount.getFacebookFriends();
				}
				catch (Exception e) {
					Log.e("", "exception");
					isException=true;
				}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress.dismiss();
			if(isException)   
			{    
				AlertManager.shorttoastMessage(context, getString(R.string.common_error));
			}
			else if(response.contains(SUCCESS_CODE) || response.contains(SUCCESS_CODE_WITH_CONTENT))
				{
					try
					{
						JSONObject resObj;
						resObj = new JSONObject(response);
						AppPreferenceManager.saveUserId(FBActivity.this, resObj.optString(USER));
						AppPreferenceManager.saveFbUserId(context, resObj.optString(USER));
						i  = new Intent(FBActivity.this,HomePage.class);
						startActivity(i);
						finish();
						AlertManager.shorttoastMessage(FBActivity.this, getString(R.string.login_success));
						AppPreferenceManager.getUserId(FBActivity.this);
					}
					catch (Exception e) 
					{
						AlertManager.shorttoastMessage(FBActivity.this, getString(R.string.common_error));
					}
				}
			else
				{
					AlertManager.shorttoastMessage(FBActivity.this, getString(R.string.common_error));
					finish();
				}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress.show();
			manager = new APIManager(URL_FB_LOGIN);
		}
	}
}
