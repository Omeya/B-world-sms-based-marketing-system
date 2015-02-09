package com.bworld.manager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bworld.R;

// TODO: Auto-generated Javadoc
/**
 * The Class AlertManager.
 */
public class AlertManager {

	
	/**
	 * Alert.
	 *
	 * @param context the context
	 * @param msg the msg
	 */
	public static void alert(Activity context, String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setIcon(R.drawable.ic_launcher);
		alert.setTitle(context.getResources().getString(R.string.app_name));
		alert.setMessage(msg);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				
			}
		});
		alert.show();
	}
	public static void alertGps(final Activity context, String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setIcon(R.drawable.ic_launcher);
		alert.setTitle(context.getResources().getString(R.string.app_name));
		alert.setMessage(msg);
		alert.setCancelable(false);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
		});
		
		alert.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		alert.show();
	}
	/**
	 * Progress dialog.
	 *
	 * @param context the context
	 * @return the alert dialog. builder
	 */
	public static AlertDialog.Builder progressDialog(Context context) {
		View view;
		LayoutInflater inflater;
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.progress_layout, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setCancelable(false);
		return builder;
	}
	
	public static void longtoastMessage(Context ctx,String message)
	{
		Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
	}
	
	public static void shorttoastMessage(Context ctx,String message)
	{
		Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
	}

}
