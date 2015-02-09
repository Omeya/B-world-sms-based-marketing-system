package com.bworld.Adapters;

import java.util.ArrayList;

import com.bworld.R;
import com.bworld.Adapters.AdvertisementAdapter.ViewHolder;
import com.bworld.constants.ICacheConstants;
import com.bworld.manager.Utils;
import com.bworld.misc.PhotosManager;
import com.bworld.models.ContactsModel;
import com.bworld.models.FriendsModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TrustedFriendsAdapter extends BaseAdapter implements ICacheConstants{

	private Context context;
	private ArrayList<FriendsModel> arrayList;
	private LayoutInflater mInflater;
	TextView   txtStatus;
	TextView   txtname;
	ImageView  imgProfile;
	ProgressBar progress;
	 private PhotosManager manager;
	public TrustedFriendsAdapter(Context ctx, ArrayList<FriendsModel> array)
	{
		super();
		context			=ctx;
		arrayList		=array;
		mInflater 		= LayoutInflater.from(context);
		manager   		= new PhotosManager(context, FRIENDS, 100);
	}

	public TrustedFriendsAdapter(Context ctx)
	{
		context=ctx;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arrayList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) 
		{
			convertView 				= mInflater.inflate(R.layout.friends_list_item, parent,false);
			txtname						=(TextView)convertView.findViewById(R.id.txt_name);
			txtStatus					=(TextView)convertView.findViewById(R.id.txt_status);
			imgProfile					=(ImageView)convertView.findViewById(R.id.img_profile_pic);
			progress					=(ProgressBar)convertView.findViewById(R.id.progress_image);
		} 
		txtname.setText(arrayList.get(position).getName());
		txtStatus.setBackgroundResource(R.drawable.blue);
		txtStatus.setText("Remove");
		Utils.setPaddings(txtStatus);
		
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
}
