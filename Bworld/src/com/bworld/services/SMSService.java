package com.bworld.services;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;
import android.util.Log;

import com.bworld.Activities.common.SplashScreen;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.Utils;
import com.bworld.models.AdvertisementModel;
import com.bworld.models.ContactsModel;
import com.bworld.receivers.SMSDeliveryReceiver;

public class SMSService extends Service implements URLConstants,
		IResponseConstants, IJSONTagConstants, IResultCodeConstants {

	public static SplashScreen MAIN_ACTIVITY;
	private Timer timer = new Timer();
	private static long UPDATE_INTERVAL = 3 * 60 * 1000; // default
	private static long DELAY_INTERVAL = 0;
	AlarmManager am;
	PendingIntent sender;
	ArrayList<ContactsModel> contactsArray;
	ArrayList<AdvertisementModel> advertisementArray;
	String id;
	SMSDeliveryReceiver deliveryReceiver;
	
	private Timer timerCheckAdd = new Timer();
	private static long UPDATE_INTERVAL_CHECK_ADD = 1 * 60 * 1000; // default
	private static long DELAY_INTERVAL_CHECK_ADD = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("", "service created ");
	}

	public static void setMainActivity(SplashScreen activity) {
		MAIN_ACTIVITY = activity;
	}
	private void startProcess() {
		timer.scheduleAtFixedRate(
		new TimerTask() {
			public void run() {
				try {
					if(Utils.checkInternetConnection(SMSService.this))
					{
						DownloadAdvertisements download = new DownloadAdvertisements();
						download.execute();
					}
					// GetContacts get = new GetContacts();
					// get.execute();
					Thread.sleep(UPDATE_INTERVAL);
				} catch (InterruptedException ie) {
					Log.e(getClass().getSimpleName(),
							"Service InterruptedException" + ie.toString());
				} catch (Exception e) {
					Log.e("", "Service Exception" + e.toString());
				}
			}
		}, DELAY_INTERVAL, UPDATE_INTERVAL);
		Log.e("", "Download adds Timer started....");
	}    
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.e("", "sms service started");
	}

	private void shutdownProcess() {
		if (timer != null)
			timer.cancel();
		Log.e("", "Timer stopped...");
		if(timerCheckAdd!=null)
		{
			timerCheckAdd.cancel();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
			Log.e("", "service started via command");
		try 
		{
			startProcess();
		//	DownloadAdvertisements download = new DownloadAdvertisements();
		//	download.execute();
		} 
		catch (Exception e) 
		{
			Log.e("", "error" + e.getMessage());
		}
		return START_STICKY;
	}
	private void startCheckingTime() {
		timerCheckAdd.scheduleAtFixedRate(

		new TimerTask() {

			public void run()
			{
				for (int i = 0; i < AppPreferenceManager
						.getAdvertisementArray(
								SMSService.this).size(); i++) 
				{
					Log.e("", "inside add array ....");
					if (AppPreferenceManager
							.getAdvertisementArray(
									SMSService.this)
							.get(i).getTimestamp()
							.equals(Utils.getCalenderString())) {
						
						Log.e("", "same time" );
						id= AppPreferenceManager.getAdvertisementArray(SMSService.this).get(i).getAdId();
						Log.e("", "Add id"+id);
						
						if(Utils.checkInternetConnection(SMSService.this))
						{
							GetContacts get = new GetContacts(i);
							get.execute();
						}
					}
				}
				try {
					Thread.sleep(UPDATE_INTERVAL_CHECK_ADD);
				} catch (InterruptedException ie) {

					Log.e(getClass().getSimpleName(),
							"Service InterruptedException" + ie.toString());
				} catch (Exception e) {
					Log.e("", "Service Exception" + e.toString());
				}

			}
		}, DELAY_INTERVAL_CHECK_ADD, UPDATE_INTERVAL_CHECK_ADD);

		Log.e("", "checking contacts Timer started....");
	}
	public class DownloadAdvertisements extends AsyncTask<Void, Integer, Void> {
		private boolean isException = false;
		private String response;
		APIManager manager;
		String name[] = { "" };
		String value[] = { "" };

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (isException) {
			} else {
				try {
					JSONObject objGetCatogery = new JSONObject(response);
					JSONArray array = objGetCatogery
							.getJSONArray(ADVERTISMENTS);
					Log.e("", "contacts length" + array.length());
					for (int i = 0; i < array.length(); i++) {
						AdvertisementModel model = new AdvertisementModel();
						JSONObject postObject = array.getJSONObject(i);
						model.setAdvertisementTitle(postObject
								.optString(AD_TITLE));
						model.setDescription(postObject
								.optString(AD_DESCRIPTION));
						model.setImageUrl(postObject.optString(AD_IMAGE));
						model.setAdId(postObject.optString(AD_ID));
						model.setTimestamp(postObject.optString(AD_TIME));
						model.setSmsCount(postObject.optString(AD_SMS_LIMIT));
						model.setStartTime(postObject.optString(AD_START_TIME));
						model.setEndTime(postObject.optString(AD_END_TIME));

						Log.e("", "title" + model.getAdvertisementTitle());
						Log.e("", "desc" + model.getDescription());
						Log.e("", "id" + model.getAdId());
						Log.e("", "timestamp" + model.getTimestamp());

						advertisementArray.add(model);

					}
					
					Log.e("", "ad size"+advertisementArray.size());
					AppPreferenceManager.saveAdvertisementArray(SMSService.this,
							advertisementArray);

					
					Log.e("", "ad size preference"+AppPreferenceManager.getAdvertisementArray(SMSService.this).size());
					startCheckingTime();
					} catch (Exception e) {
					// AlertManager.shorttoastMessage(context,
					// getString(R.string.common_error));
					Log.e("", "Error" + e.getMessage());
				}

			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			advertisementArray = new ArrayList<AdvertisementModel>();
			manager = new APIManager(URL_GET_ADDS);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
					response = manager.commonPostData(URL_GET_ADDS + "/date", name,value);
			} catch (Exception e) {
				isException = true;
			}
			return null;
		}
	}
	class GetContacts extends AsyncTask<Void, Integer, Void> {
		private String[] names = {""};
		private String[] values = {""};
		private boolean isException = false;
		APIManager manager;
		private String response;
		private int pos;

		public GetContacts(int i) {
			pos=i;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try
			{
				if(isException)
				{
					Log.e("", "error do in background");
					//AlertManager.shorttoastMessage(Contacts.this, getString(R.string.common_error));
				}
				else
				{
					// receiver = new Receiver(); registerReceiver(receiver, new
					//		  IntentFilter(SENT));
							  
					 deliveryReceiver = new SMSDeliveryReceiver(); 
					 registerReceiver(deliveryReceiver, new IntentFilter(DELIVERED));
					
					 PendingIntent sentPI = PendingIntent.getBroadcast(SMSService.this, 0,new Intent(SENT), PendingIntent.FLAG_UPDATE_CURRENT);
			         
			 
					 Intent intent= new Intent(DELIVERED);
					 intent.putExtra("user_id",AppPreferenceManager.getUserId(SMSService.this));
			//		 PendingIntent deliveredPI = PendingIntent.getBroadcast(SMSService.this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT); 	
		    			JSONObject obj = new JSONObject(response);
						if(obj.optString(CODE).equals(SUCCESS_CODE))
						{
							JSONArray array = obj.getJSONArray(CONTACTS);
							Log.e("", "get contacts length"+array.length());
							for (int i = 0; i < array.length(); i++)
							{
								ContactsModel model = new ContactsModel();
								JSONObject postObject = array.getJSONObject(i);
								model.setName(postObject.optString(CONTACT_NAME));
								model.setPhoneNumber(postObject.optString(PHONE_NUMBER));
								
								Log.e("", "name"+model.getName());
								Log.e("", "number"+model.getPhoneNumber());
								contactsArray.add(model);
					
								intent.putExtra("ad_id", AppPreferenceManager.getAdvertisementArray(SMSService.this).get(pos).getAdId());
								PendingIntent deliveredPI = PendingIntent.getBroadcast(SMSService.this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
								 
								String messageText= AppPreferenceManager.getAdvertisementArray(SMSService.this).get(pos).getDescription();
							
							//	MessageManager.sendSMS(postObject.optString(PHONE_NUMBER), messageText, SMSService.this);
								
								  SmsManager sms = SmsManager.getDefault();
								  sms.sendTextMessage(postObject.optString(PHONE_NUMBER),null, messageText, sentPI,deliveredPI);
							}
						}
						else
						{
							//AlertManager.shorttoastMessage(Contacts.this,objGetCatogery.optString(MESSAGE));
						}
				}
			}
			catch(Exception e)
			{
				Log.e("", "error"+e.toString());
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isException = false;
			manager = new APIManager(URL_GET_CONTACTS);
			contactsArray = new ArrayList<ContactsModel>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				response = manager.commonPostData(URL_GET_CONTACTS+"/"+AppPreferenceManager.getUserId(SMSService.this)
						+"/"+id,names, values);
			} catch (Exception e) {
				e.printStackTrace();
				isException=true;
				Log.e("", "error do in background"+e.toString());
			}
			return null;
		}
	}
	 public class LocalBinder extends Binder {
	        SMSService getService() {
	            return SMSService.this;
	        }
	    }
	 
	private final IBinder mBinder = new LocalBinder();
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		shutdownProcess();
		if (contactsArray != null) {
			contactsArray = null;
		}
		if (advertisementArray != null) {
			advertisementArray = null;
		}
	}
}
