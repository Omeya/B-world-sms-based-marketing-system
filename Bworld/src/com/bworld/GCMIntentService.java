package com.bworld;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bworld.Activities.PushMessage;
import com.bworld.Activities.common.SplashScreen;
import com.bworld.manager.AppPreferenceManager;
import com.google.android.gcm.GCMBaseIntentService;

public  final class GCMIntentService extends GCMBaseIntentService{

	public static final String DISPLAY_MESSAGE_ACTION =
	            "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";
	 
	 public static final String EXTRA_MESSAGE = "message";
	 String message ;
	 String id	;
	
	@Override
	protected void onError(Context arg0, String arg1) {
		
		Log.e("", "error inside service");
	}

	@Override
	protected void onMessage(Context context, Intent arg1) 
	{
		Log.e("", "Registered inside message");
		 message = arg1.getExtras().getString("message");
		 id	   = arg1.getExtras().getString("user_id");
		
		Log.e("", "message"+message);
		Log.e("", "id"+id);
		
		 displayMessage(context, message);
	        // notifies user
	        generateNotification(context, message,id);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) 
	{
		Log.e("", "Registered inside service");
		AppPreferenceManager.saveGCMRegid(arg0, arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) 
	{
		Log.e("", "unRegistered inside service");
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// TODO Auto-generated method stub
		return super.onRecoverableError(context, errorId);
	}

	
	 private static void generateNotification(Context context, String message,String id) {
	        int icon = R.drawable.ic_launcher;
	        long when = System.currentTimeMillis();
	        NotificationManager notificationManager = (NotificationManager)
	                context.getSystemService(Context.NOTIFICATION_SERVICE);
	        Notification notification = new Notification(icon, message, when);
	        String title = context.getString(R.string.app_name);
	        Intent notificationIntent = new Intent(context, PushMessage.class);
	        notificationIntent.putExtra("message",message);
	        notificationIntent.putExtra("id",id);
	        // set intent so it does not start a new activity
	        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        PendingIntent intent =
	                PendingIntent.getActivity(context, 0, notificationIntent, 0);
	        notification.setLatestEventInfo(context, title, message, intent);
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	        notificationManager.notify(0, notification);
	    }
	 
	 static void displayMessage(Context context, String message) {
	        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
	        intent.putExtra(EXTRA_MESSAGE, message);
	        context.sendBroadcast(intent);
	    }
	
}
