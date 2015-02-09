package com.bworld.receivers;

import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SMSDeliveryReceiver  extends BroadcastReceiver implements IResultCodeConstants,URLConstants{
	private String TAG="SMSDeliveryReceiver"; 
	
	APIManager manager;
	private Context ctx;
	String adid;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e(TAG,"delivery reciver");
		ctx=context;
		
		if (intent.getAction().equals(DELIVERED)) 
		{
			Bundle extras= intent.getExtras();
			if(extras!=null)
			{
				adid= extras.getString("ad_id");
				Log.e("", "Advertisement id"+adid);
				
				if(Utils.checkInternetConnection(ctx))
				{
					SMSDeliveryReport deliveryReport = new SMSDeliveryReport();
					deliveryReport.execute();
				}
				
			}
			Toast.makeText(context, "SMS delivered",Toast.LENGTH_LONG).show();
		}
		
	}
	
	class SMSDeliveryReport extends AsyncTask<Void, Void, Void>
	{
		String name [] ={""};
		String value [] ={""};
		@Override
		protected Void doInBackground(Void... params) {
			try
			{
				manager.commonPostData(URL_ADD_DELIVERY_REPORT+"/"+AppPreferenceManager.getUserId(ctx)+"/"+adid, name, value);
			}
			catch(Exception e)
			{
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			manager = new APIManager(URL_ADD_DELIVERY_REPORT);
		}
	}
}
