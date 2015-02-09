package com.bworld.Adapters;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bworld.R;
import com.bworld.Activities.PushMessage;
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
import com.bworld.models.FriendsModel;

public class FriendListAdapter extends BaseAdapter implements URLConstants,IResponseConstants,ICacheConstants,IJSONTagConstants,IResultCodeConstants{

	TextView   txtStatus;
	TextView   txtname;
	ImageView  imgProfile;
	ProgressBar progress;
	private Context context;
	private ArrayList<FriendsModel> arrayList;
	private LayoutInflater mInflater;
    private PhotosManager manager;
    private CustomProgressDialog progressDialog;
    private Intent i;
	public FriendListAdapter(Context ctx, ArrayList<FriendsModel> array)
	{
		super();
		context			= ctx;
		arrayList		= array;
		mInflater 		= LayoutInflater.from(context);
		manager   		= new PhotosManager(context, FRIENDS, 100);
		progressDialog  = CustomProgressDialog.show(context, false);
	}
	public FriendListAdapter(Context ctx) {
		context=ctx;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) 
		{
			convertView 				= mInflater.inflate(R.layout.friends_list_item, parent,false);
			
			txtname						=(TextView)convertView.findViewById(R.id.txt_name);
			txtStatus					=(TextView)convertView.findViewById(R.id.txt_status);
			imgProfile					=(ImageView)convertView.findViewById(R.id.img_profile_pic);
			progress					=(ProgressBar)convertView.findViewById(R.id.progress_image);
			
		} 
		txtname.setText(arrayList.get(position).getName());
		
		txtStatus.setText(arrayList.get(position).getStatus());
		
		
		if(arrayList.get(position).getStatus().contains("ADD"))
		{
			txtStatus.setBackgroundResource(R.drawable.blue);
			Utils.setPaddings(txtStatus);
			
			txtStatus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) 
				{
					if(Utils.checkInternetConnection(context))
					{
						AddToTrusted add = new AddToTrusted(position);
						add.execute();
					}
					else
					{
						AlertManager.shorttoastMessage(context, context.getResources().getString(R.string.Internet_check));
					}
					
				}
			});
		}
		else
		{
			txtStatus.setBackgroundColor(android.R.color.transparent);
		}
		if(arrayList.get(position).getProfilePicture().equals(""))
		{
			imgProfile.setImageResource(R.drawable.verify_info_thumb);
			Utils.setVisiblityGone(progress);
		}
		else
		{
			try
			{
				Log.e("", "image url"+arrayList.get(position).getProfilePicture());
				/*manager.DisplayImage(URLDecoder.decode(arrayList.get(position).getProfilePicture()), imgProfile, progress);*/
				manager.DisplayImage(arrayList.get(position).getProfilePicture(), imgProfile, progress);
			}
			catch (Exception e) 
			{
				imgProfile.setImageResource(R.drawable.profile_thumb);
				Utils.setVisiblityGone(progress);
			}
		}
		
		return convertView;
	}
	private class AddToTrusted extends AsyncTask<Void,Void,Void>
	{
		private boolean isException=false;
		private String response;
		private int position;
		private String [] name={"user_id","friend_id"};
		private String [] value={};
		private APIManager apiManager;
		
		public AddToTrusted(int pos)
		{
			position=pos;
		}
		@Override
		protected Void doInBackground(Void... arg0) 
		{
			try
			{
				value       = new String[2];
				value[0]	= AppPreferenceManager.getUserId(context);
				value[1]	= arrayList.get(position).getId();
		//		value[2]	= AppPreferenceManager.getGCMRegid(context);
				apiManager 	= new APIManager(URL_ADD_TRUSTED_FRIENDS);
				response 	= apiManager.commonPostData(URL_ADD_TRUSTED_FRIENDS, name, value);
			}
			catch(Exception e)
			{
				isException=true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void arg0) {
			super.onPostExecute(arg0);
			progressDialog.dismiss();
			if(isException)
			{
				AlertManager.shorttoastMessage(context, context.getResources().getString(R.string.common_error));
			}
			else
			{
				try
				{
					JSONObject jsonResponse=  new JSONObject(response);
					if(jsonResponse.optString(CODE).equals(SUCCESS_CODE))
					{
						AlertManager.shorttoastMessage(context, context.getResources().getString(R.string.friend_request_success));
					//	i = new Intent(context,PushMessage.class);
					//	context.startActivity(i);
								
					}
				}
				catch(Exception e)
				{
					AlertManager.shorttoastMessage(context, context.getResources().getString(R.string.common_error));
				}
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
			 isException=false;
		}
		
	}
}
