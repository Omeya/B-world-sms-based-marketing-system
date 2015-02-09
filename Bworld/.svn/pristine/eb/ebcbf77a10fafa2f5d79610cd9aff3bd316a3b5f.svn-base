package com.bworld.Adapters;

import java.net.URLDecoder;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bworld.R;
import com.bworld.constants.ICacheConstants;
import com.bworld.manager.ImageDownloader;
import com.bworld.manager.Utils;
import com.bworld.misc.PhotosManager;
import com.bworld.models.AdvertisementModel;

public class AdvertisementAdapter extends BaseAdapter  implements ICacheConstants
{
	private ArrayList<AdvertisementModel> adList;
	private Context mContext;
	private LayoutInflater mInflater;
	ViewHolder holder = null;
	public PhotosManager imageLoader;
	
	public AdvertisementAdapter(Context ctx,ArrayList<AdvertisementModel> model) 
	{
		adList=model;
		mContext=ctx;
		mInflater = LayoutInflater.from(mContext);
		imageLoader = new PhotosManager(mContext, ADVERTISEMENT,200 / 2);
	}

	@Override
	public int getCount() {
		//return adModel.size();
		return adList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return adList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null) 
		{
			convertView 				= mInflater.inflate(R.layout.advertisement_item, parent,false);
			holder 						= new ViewHolder();
			holder.imgIcon		 		=(ImageView)convertView.findViewById(R.id.image_adicon);
			holder.txtTitle				=(TextView)convertView.findViewById(R.id.text_title);
			holder.progress				=(ProgressBar)convertView.findViewById(R.id.progressBar1);
			convertView.setTag(holder);
		} 
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.txtTitle.setText(adList.get(position).getAdvertisementTitle());
	//downloader.download(/*URLDecoder.decode(*/adList.get(position).getImageUrl(), holder.imgIcon);
		
		if(adList.get(position).getImageUrl().equals(""))
		{
			holder.imgIcon.setImageResource(R.drawable.ic_launcher);
			Utils.setVisiblityGone(holder.progress);
		}
		else
		{
			try
			{
				//holder.mProductImage.setMinimumHeight((int) mContext.getResources().getDimension(R.dimen.image_thumbnail_size));
			//	holder.mProductImage.setMinimumWidth((int) mContext.getResources().getDimension(R.dimen.image_thumbnail_size));
				Log.e("", "image url"+adList.get(position).getImageUrl());
				
				imageLoader.DisplayImage(URLDecoder.decode(adList.get(position).getImageUrl()), holder.imgIcon, holder.progress);
			}
			catch (Exception e) 
			{
				holder.imgIcon.setImageResource(R.drawable.ic_launcher);
				Utils.setVisiblityGone(holder.progress);
			}
		}
		
		
		
		return convertView;
	}
	class ViewHolder
	{
		TextView   txtTitle;
		ImageView  imgIcon;
		ProgressBar progress;
	}

}
