package com.bworld.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bworld.R;
import com.bworld.manager.AppPreferenceManager;

/**
 * The Class HomeAdapter.
 */
public class HomeAdapter extends BaseAdapter {

	/** The inflater. */
	private LayoutInflater inflater;
	
	/** The m view. */
	private View mView;
	
	/** The main. */
	private RelativeLayout main;
	
	/** The m context. */
	private Context mContext;
	
	/** The auto reply. */
	private String [] icon;
	/** The m reply check box. */
	private ImageView mReplyImg, mReplyCheckBox;

	private final Integer[] mImageIds = {
            R.drawable.sbm,
            R.drawable.lbm,
            R.drawable.event,
            R.drawable.bdaysms,
            R.drawable.track_frnds,
            R.drawable.deals,
            R.drawable.hangouts,
            R.drawable.movies,
            R.drawable.favorites
    };
	
	/** The m reply text. */
	private TextView mReplyText;
	
	/** The layout params. */
	RelativeLayout.LayoutParams layoutParams;
	
	
	/**
	 * Instantiates a new home adapter.
	 *
	 * @param context the context
	 * @param autoReply the auto reply
	 */
	public HomeAdapter(Context context, String [] string) {
		mContext = context;
		icon		= new String[string.length];
		this.icon 	= string;
		inflater	= LayoutInflater.from(mContext);
	}

	
	@Override
	public int getCount() {

		return mImageIds.length;
	}

	
	@Override
	public Object getItem(int position) {

		return null;
	}

	
	@Override
	public long getItemId(int position) {

		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mView = inflater.inflate(R.layout.customhomegrid, null);
		} else {
			mView = convertView;
		}
		main = (RelativeLayout) mView.findViewById(R.id.main);
		mReplyImg = (ImageView) mView.findViewById(R.id.replyimg);
	//	mReplyText = (TextView) mView.findViewById(R.id.replytxt);
	//	mReplyText.setText(icon[position].toString());
		
		mReplyImg.setImageResource(mImageIds[position]);
		
	//	main.setPadding(20, 20, 20, 20);
		return mView;
	}

}
