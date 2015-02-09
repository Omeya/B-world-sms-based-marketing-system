package com.bworld.Activities.common;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bworld.R;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.JsonIntegration;
import com.bworld.manager.Utils;
import com.bworld.manager.Version;
import com.bworld.models.ContactsModel;
import com.bworld.services.SMSService;
import com.google.android.gcm.GCMRegistrar;
import static com.bworld.GCMIntentService.EXTRA_MESSAGE;
import static com.bworld.GCMIntentService.DISPLAY_MESSAGE_ACTION;

public class SplashScreen extends Activity implements URLConstants 
{

APIManager manager;
FetchContacts contacts;
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	private ArrayList<ContactsModel> contactsArray;
	protected boolean _active = true;
	protected int _splashTime = 3000;
	private Context mContext;
	private Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		mContext=this;
		SMSService.setMainActivity(this);
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		 Log.v("", "registered"+regId);
		 registerReceiver(mHandleMessageReceiver,
	                new IntentFilter(DISPLAY_MESSAGE_ACTION));
		if (regId.equals(""))
		{
		  GCMRegistrar.register(this, "535629438514");
		}
		else
		{
		  Log.v("", "Already registered");
		  AppPreferenceManager.saveGCMRegid(mContext, regId);
		}
		
		//Utils.getCalender();
		Utils.getCalenderString();
		if(Utils.checkInternetConnection(mContext))
		{
			contacts= new FetchContacts();
			/*if(Version.sdkAboveOrEqual(Version.API11_HONEYCOMB_30))
			{
				//contacts.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	
			}
			else
			{
				
				contacts.execute();
			}*/
			contacts.execute();
			
		}
		else
		{
			AlertManager.shorttoastMessage(mContext, getString(R.string.Internet_check));
			finish();
		}
	}
	private class FetchContacts extends AsyncTask<Void, Integer, Void>
	{
		String [] name= {"contacts","token"};
		String [] values= {};
		String json="";
		String response;
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			if(AppPreferenceManager.getUserId(mContext).equals(""))
			{
				i= new Intent(SplashScreen.this,MainPage.class);
				startActivity(i);
				finish();
			}
			else
			{
				i= new Intent(SplashScreen.this,HomePage.class);
				startActivity(i);
				finish();
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			manager = new APIManager(URL_ADD_RAW_CONTACTS);
			contactsArray= new ArrayList<ContactsModel>();
		}
		@Override
		protected Void doInBackground(Void... params)
		{
			try {
				
				
				
				
				if(Version.sdkStrictlyBelow(Version.API14_ICE_CREAM_SANDWICH_40))
				{
					contactsArray=Utils.queryAllRawContacts(SplashScreen.this);
				}
				else
				{
					contactsArray=Utils.getAllPhoneContacts(SplashScreen.this);
				}
				if(contactsArray!=null)
				{
					Log.e("", " change in contacts");
					json=JsonIntegration.uploadContactsFromAdapter(SplashScreen.this, contactsArray);
					values= new String[2];
					values[0]=json.toString();
					values[1]=Utils.getDeviceID(mContext); 
					response=manager.commonPostData(URL_ADD_RAW_CONTACTS, name, values);
				}
				else
				{
					Log.e("", " No change in contacts, Applying some sleep");
					Thread.sleep(3000);
				}
			}
			catch (Exception e)
			{
				Log.e("", "error while uploading contacts");
				e.printStackTrace();
			}
			return null;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		contactsArray=null;
		if(contacts!=null)
		{
			if(!contacts.isCancelled())
			{
				contacts.cancel(true);
			}
		}
		try
		{
			//unregisterReceiver(mHandleMessageReceiver);
	      //  GCMRegistrar.onDestroy(this);
		}
		catch(Exception e)
		{
			
		}
	}
	private final BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
         //   mDisplay.append(newMessage + "\n");
            Log.e("", "message"+newMessage);
        }
    };
}