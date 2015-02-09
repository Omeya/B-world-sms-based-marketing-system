package com.bworld.Activities.Profile;

import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bworld.R;
import com.bworld.constants.ICacheConstants;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.Utils;
import com.bworld.misc.PhotosManager;

public class HomeProfile extends Activity implements URLConstants,IResponseConstants,IJSONTagConstants,ICacheConstants
{
	private ImageView imgProfile;
	private TextView txtName;
	private TextView txtLname;
	private TextView txtEarnings;
	private TextView txtTotalSMS;
	private TextView txtTodaySMS;
	private CustomProgressDialog progress;
	private Context context;
	private PhotosManager imageLoader;
	private ProgressBar progress_thumb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_page);
		context=this;
		initialiseUI();
		
		if(Utils.checkInternetConnection(context))
		{
			GetProfile get = new GetProfile();
			get.execute();
		}
		else
		{
			AlertManager.shorttoastMessage(context, getString(R.string.Internet_check));
		}
		
	}

	
	private void initialiseUI()
	{
		imgProfile  	 	 =	(ImageView)findViewById(R.id.img_profile);
		progress_thumb  	 =	(ProgressBar)findViewById(R.id.progress_Profile);
		
		txtName		 	 	 =	(TextView)findViewById(R.id.txt_name);
		txtLname 	 	     =	(TextView)findViewById(R.id.txt_lname);
		txtEarnings			 =	(TextView)findViewById(R.id.txt_earnings);
		txtTotalSMS 	 	 =	(TextView)findViewById(R.id.txt_total_count);
		txtTodaySMS			 =	(TextView)findViewById(R.id.txt_today_count);
		
		
		progress		 	 = CustomProgressDialog.show(context, false);
		imageLoader 	     = new PhotosManager(context, PROFILE,200 / 2);
	}
	
	
	class GetProfile extends AsyncTask<Void, Void, Void>
	{
		private APIManager manager;
		private boolean isException=false;
		private String response;
		private String [] names= {};
		private String [] values= {};
		
	//	private String [] names= {"user_id","advertisement_id","sms_count"};
	//	private String [] values= {AppPreferenceManager.getUserId(context),"2","2"};
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress.dismiss();
			if(isException)
			{
				AlertManager.shorttoastMessage(context, getString(R.string.common_error));
			}
			else
			{
				try
				{
					JSONObject obj = null;
					JSONObject jsonResponse=  new JSONObject(response);
					if(jsonResponse.optString(CODE).equals(SUCCESS_CODE))
					{
						JSONArray userobj=  jsonResponse.getJSONArray(PROFILE_USER) ;
						
						for (int i = 0; i < userobj.length(); i++) {
							
							 obj= userobj.getJSONObject(0);
							txtName.setText(obj. optString(PROFILE_FIRST_NAME));
							txtLname.setText(obj.optString(PROFILE_LAST_NAME));
							txtEarnings.setText(obj.optString(PROFILE_EARNING));
							txtTotalSMS.setText(obj.optString(PROFILE_TOTAL_SMS));
							txtTodaySMS.setText(obj.optString(PROFILE_SMS_SENT_TODAY));
							Log.e("", "name"+obj.optString(PROFILE_FIRST_NAME));
						}
						if(!obj.optString(PROFILE_IMAGE).equals(""))
						{
							try
							{
								imageLoader.DisplayImage(URLDecoder.decode(obj.optString(PROFILE_IMAGE)), imgProfile,progress_thumb);
							}
							catch(Exception e)
							{
								imgProfile.setImageResource(R.drawable.verify_info_thumb);
								Utils.setVisiblityGone(progress_thumb);
							}
						}
						else
						{
							imgProfile.setImageResource(R.drawable.verify_info_thumb);
							Utils.setVisiblityGone(progress_thumb);
						}
						
							
					}
				}
				catch(Exception e)
				{
					Log.e("", "error"+e.getMessage());
					imgProfile.setImageResource(R.drawable.verify_info_thumb);
					Utils.setVisiblityGone(progress_thumb);
				}
				
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress.show();
			manager = new APIManager(URL_GET_PROFILE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try
			{
				response= manager.commonPostData(URL_GET_PROFILE+"/"+AppPreferenceManager.getUserId(context),
						names,values);
				
			/*	response= manager.commonPostData(URL_ADD_DELIVERY_REPORT,
						names,values);*/
			}
			catch (Exception e) 
			{
			   isException=true;
			}
			
			return null;
		}
		
	}
	
	
}
