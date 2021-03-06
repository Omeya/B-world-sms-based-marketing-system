package com.bworld.manager;

import java.io.IOException;
import java.util.ArrayList;

import com.bworld.models.AdvertisementModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class AppPreferenceManager {

	
	public static void saveAutoSmsFlag(Context ctx, boolean flag)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		preferences.edit().putBoolean("AutoSmsFlag", flag).commit();
	}
	
	public static boolean getAutoSmsFlag(Context ctx)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		boolean flag = preferences.getBoolean("AutoSmsFlag", false);
		Log.e("", "flag"+flag);
		return flag;
	}
	public static void saveContactCount(Context ctx, int count)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		preferences.edit().putInt("Contactcount"+Utils.getDeviceID(ctx), count).commit();
	}
	
	public static int getContactCount(Context ctx)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		int count = preferences.getInt("Contactcount"+Utils.getDeviceID(ctx), 0);
		Log.e("", "count"+count);
		return count;
	}
	public static void saveUserId(Context ctx, String id)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		preferences.edit().putString("UserId", id).commit();
	}
	
	public static String getUserId(Context ctx)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		String id = preferences.getString("UserId", "");
	//	Log.e("", "userid :  "+id);
		return id;
	}
	public static void saveFbUserId(Context ctx, String id)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		preferences.edit().putString("FBUserId", id).commit();
	}
	
	public static String getFbUserId(Context ctx)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		String id = preferences.getString("FBUserId", "");
		Log.e("", "FBUserId :  "+id);
		return id;
	}
	
	
	public static boolean saveFbUserDetails(String[] array, Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences(
				"BWorld", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		for (int i = 0; i < array.length; i++)
			editor.putString("FB_USER_DETAILS" + "_" + i, array[i]);
		return editor.commit();
	}

	public static String[] getFbUserDetails(Context mContext) {
		String[] value = { "", ""};
		SharedPreferences prefs = mContext.getSharedPreferences(
				"BWorld", Context.MODE_PRIVATE);
		for (int i = 0; i < value.length; i++)
			value[i] = prefs.getString("FB_USER_DETAILS" + "_" + i, value[i]);
		return value;
	}
	
	public static void saveAdvertisementArray(Context ctx,ArrayList<AdvertisementModel> searchQueryList)
	{
			SharedPreferences prefs = ctx.getSharedPreferences("BWorld", Context.MODE_PRIVATE);
	        Editor editor = prefs.edit();
	        editor.putString("advertisement", ObjectSerializer.serialize(searchQueryList));
	        editor.commit();
	}
	
	/**
	 * Gets the search history.
	 *
	 * @param ctx the ctx
	 * @param username the username
	 * @return the search history
	 */
	public static ArrayList<AdvertisementModel> getAdvertisementArray(Context ctx)
	{
		ArrayList<AdvertisementModel> listItems = null;
		 SharedPreferences prefs = ctx.getSharedPreferences("BWorld", Context.MODE_PRIVATE);

	     try {
	    	 listItems = (ArrayList<AdvertisementModel>) ObjectSerializer.deserialize(prefs.getString("advertisement", ObjectSerializer.serialize(new ArrayList<AdvertisementModel>())));
	     } 
	     catch (IOException e)
	     {
	         e.printStackTrace();
	     }
		return listItems;
	}
	
	
	public static void saveGCMRegid(Context ctx, String id)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		preferences.edit().putString("regId", id).commit();
	}
	
	public static String getGCMRegid(Context ctx)
	{
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("BWorld",Context.MODE_PRIVATE);
		String id = preferences.getString("regId", "");
	//	Log.e("", "userid :  "+id);
		return id;
	}
	
	
}
