package com.bworld.Activities.SBM;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bworld.R;
import com.bworld.Activities.Withdraw.Withdraw;
import com.bworld.Adapters.AdvertisementAdapter;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.Utils;
import com.bworld.models.AdvertisementModel;
import com.bworld.models.ContactsModel;
import com.bworld.services.SMSService;

public class AutoSMS extends Activity implements OnItemClickListener,OnItemLongClickListener,OnClickListener,URLConstants,IResultCodeConstants,IJSONTagConstants{

	
	private CheckBox chkAutosms;
	private ListView listAdvertisement;
	private Button btnContacts;
	private AdvertisementAdapter adapter;
	private ArrayList<AdvertisementModel> adArray;
	private Context context;
	private Intent i;
	private CustomProgressDialog progress;
	private ArrayList<ContactsModel> contactsArray;
	private RelativeLayout relativeHeader;
	DownloadAdvertisements download;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_sms);
		context=this;
		initialiseUI();
	}
	private void initialiseUI()
	{
		relativeHeader		= (RelativeLayout)findViewById(R.id.relative_header);
		listAdvertisement       =(ListView)findViewById(R.id.list_advertisement);
		chkAutosms				=(CheckBox)findViewById(R.id.check_auto_sms);
		btnContacts				=(Button)findViewById(R.id.btn_contacts);
		
		//Utils.getHeader(AutoSMS.this, relativeHeader, getString(R.string.advertisements_head));
		progress= CustomProgressDialog.show(context, true);
	//	i= new Intent(AutoSMS.this,SMSService.class);
	//	startService(i);
		
		Utils.getCalender();
		if(Utils.checkInternetConnection(context))
		{
			download= new DownloadAdvertisements();  
			download.execute();
		}
		else
		{
			AlertManager.shorttoastMessage(context,getString(R.string.Internet_check) );
		}
		
		
		if(AppPreferenceManager.getAutoSmsFlag(context))
		{
			chkAutosms.setChecked(true);
		}
		else
		{
			chkAutosms.setChecked(false);
		}
		btnContacts.setOnClickListener(this);
		listAdvertisement.setOnItemClickListener(this);
		
		
		
		chkAutosms.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
				if(chkAutosms.isChecked())
				{
					AppPreferenceManager.saveAutoSmsFlag(context, true);
					
					if(!Utils.isMyServiceRunning(context))
					{
						i= new Intent(AutoSMS.this,SMSService.class);
					//	i.putExtra("contactsarray", contactsArray);
						startService(i);
					}
					
					/*if(contactsArray==null)
					{
						//AlertManager.shorttoastMessage(context, getString(R.string.select_contact_for_sms));
					}
					else
					{
						if(!Utils.isMyServiceRunning(context))
						{
							i= new Intent(AutoSMS.this,SMSService.class);
							i.putExtra("contactsarray", contactsArray);
							startService(i);
						}
					}*/
					
				}
				else
				{
					AppPreferenceManager.saveAutoSmsFlag(context, false);
					i= new Intent(AutoSMS.this,SMSService.class);
					stopService(i);
				}
			}
		});
		
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==TO_CONTACTS)
		{
			if(resultCode==CONTACTS_SELECTED)
			{
				//contactsArray = new ArrayList<ContactsModel>();
			//	Log.e("", "Result received");
			//	contactsArray  = (ArrayList<ContactsModel>) data.getExtras().getSerializable("contactsarray");
			//	Log.e("", "Result received size"+contactsArray.size());
				
				if(chkAutosms.isChecked())
				{
					AppPreferenceManager.saveAutoSmsFlag(context, true);
					
					i= new Intent(AutoSMS.this,SMSService.class);
					if(Utils.isMyServiceRunning(context))
					{
						stopService(i);
						startService(i);
					}
					else
					{
						startService(i);
					}
					/*if(contactsArray==null)
					{
						AlertManager.shorttoastMessage(context, getString(R.string.select_contact_for_sms));
					}
					else
					{
						if(adArray==null || adArray.size()==0)
						{
							
						}
						else
						{
							if(!Utils.isMyServiceRunning(context))
							{
								i= new Intent(AutoSMS.this,SMSService.class);
							//	i.putExtra("contactsarray", contactsArray);
							//	i.putExtra("advertisementsArray",adArray);
								startService(i);
							}
						}
						
					}*/
					
				}
				else
				{
					AppPreferenceManager.saveAutoSmsFlag(context, false);
					i= new Intent(AutoSMS.this,SMSService.class);
					stopService(i);
				}
			}
		}
	}
	@Override
	public void onClick(View v) {
		if(v==btnContacts)
		{
			
		//	SMSManager.sendSMS("9447394340", "dvndkjnvjd", AutoSMS.this);
			if(chkAutosms.isChecked())
			{
				Log.e("", "contacts loading");
				i= new Intent(AutoSMS.this,Contacts.class);
				startActivityForResult(i, TO_CONTACTS);
			}
			else
			{
			  AlertManager.shorttoastMessage(context, getString(R.string.check_autosms));	
			}
		}
		
	}
	
	public class DownloadAdvertisements extends AsyncTask<Void, Integer, Void>
	{
		private boolean isException=false;
		private String response;
		APIManager manager;
		String name [] ={""};
		String value [] ={""};
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
					JSONObject objGetCatogery = new JSONObject(response);
					JSONArray array = objGetCatogery.getJSONArray(ADVERTISMENTS);
					Log.e("", "contacts length"+array.length());
					for (int i = 0; i < array.length(); i++)
					{
						AdvertisementModel model = new AdvertisementModel();
						JSONObject postObject = array.getJSONObject(i);
						model.setAdvertisementTitle(postObject.optString(AD_TITLE));
						model.setDescription(postObject.optString(AD_DESCRIPTION));
						model.setImageUrl(postObject.optString(AD_IMAGE));
						model.setAdId(postObject.optString(AD_ID));
						model.setTimestamp(postObject.optString(AD_TIME));
						model.setSmsCount(postObject.optString(AD_SMS_LIMIT));
						model.setStartTime(postObject.optString(AD_START_TIME));
						model.setEndTime(postObject.optString(AD_END_TIME));
						
						Log.e("", "title"+model.getAdvertisementTitle());
						Log.e("", "desc"+model.getDescription());
						Log.e("", "id"+model.getAdId());
						Log.e("", "timestamp"+model.getTimestamp());
						
						
						adArray.add(model);
						
					}
					adapter				= new AdvertisementAdapter(AutoSMS.this,adArray);
					listAdvertisement.setAdapter(adapter);
				}
				catch (Exception e) {
					AlertManager.shorttoastMessage(context, getString(R.string.common_error));
				}
				
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			progress.show();
			adArray = new ArrayList<AdvertisementModel>();
			manager= new APIManager(URL_GET_ADDS);
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				response= manager.commonPostData(URL_GET_ADDS,name,value);
			} catch (Exception e) {
				isException=true;
			}
			return null;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		if(arg0==listAdvertisement)
		{
				//Log.e("", "contacts loading");
				i= new Intent(AutoSMS.this,AdvertisementDetail.class);
				i.putExtra("adDescription",adArray.get(arg2).getDescription());
				startActivity(i);
			
		}
		
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		i= new Intent(AutoSMS.this,Withdraw.class);
		startActivity(i);
		return false;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(download!=null)
		{
			if(download.isCancelled())   
			{
				Log.e("", "cancelled the task");
				download.cancel(true);
			}
		}
	}

}
