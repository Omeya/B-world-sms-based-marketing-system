package com.bworld.Activities.SBM;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bworld.R;
import com.bworld.Adapters.ContactsSMSAdapter;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.JsonIntegration;
import com.bworld.manager.Utils;
import com.bworld.models.ContactsModel;

public class Contacts extends Activity implements OnClickListener,URLConstants,IJSONTagConstants,IResponseConstants, IResultCodeConstants{


	private Button btnOk;
	public static  ContactsSMSAdapter adapter;
	public static ListView listContacts;
	private ArrayList<ContactsModel> arrayListContacts;
	private ArrayList<ContactsModel> selectedContacts;
//	private ArrayList<String> selectedPhoneNumbers;
	CustomProgressDialog progress;
	APIManager manager;
	private RelativeLayout relativeHeader;
	private EditText edtSearch;
	public static CheckBox chkAllContacts;
	GetRawContacts get;
	AddContacts add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contacts_sms);
		Log.e("", "ON CREATE");
		initialiseUI();
	}
	
	private void initialiseUI()
	{
		chkAllContacts		= (CheckBox)findViewById(R.id.checkAll);
		listContacts		= (ListView)findViewById(R.id.list_contacts);
		btnOk				=(Button)findViewById(R.id.btn_ok);
		edtSearch			=(EditText)findViewById(R.id.edtSearch);
		progress			=CustomProgressDialog.show(Contacts.this, false);
		relativeHeader		=(RelativeLayout)findViewById(R.id.relative_header);
		
		edtSearch.addTextChangedListener(txt);
		Utils.getHeader(Contacts.this, relativeHeader, getString(R.string.select_contacts));
		Log.e("", "INITIALISE");
		
		if(Utils.checkInternetConnection(Contacts.this))
		{
			get= new GetRawContacts();
			get.execute();
		}
		else
		{
			AlertManager.shorttoastMessage(Contacts.this,getString(R.string.Internet_check) );
		}
		
		clickListeners();
		
	}
	private void clickListeners()
	{
		btnOk.setOnClickListener(this);
		chkAllContacts.setOnClickListener(this);
	}
	public static TextWatcher txt = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(adapter!=null)
			{
				adapter.getFilter().filter(s.toString());
				adapter.notifyDataSetChanged();
				listContacts.setAdapter(adapter);
			}
			
		}
	};
	@Override
	public void onClick(View v) 
	{
		if(v==btnOk)
		{
			if(adapter==null)
			{
				finish();
				return;
			}
			
			/*if(edtSearch.getText().length()>0)
			{
				edtSearch.setText("");
				edtSearch.addTextChangedListener(txt);
				try {
					Thread.sleep(3000);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}*/
			int count  = adapter.getAllItems().size();
			Log.e("", "count"+count);
			ContactsModel model = new ContactsModel();
			selectedContacts = new ArrayList<ContactsModel>();
			for (int i = 0; i < count; i++)
			{
				model =adapter.getAllItems().get(i);
				if(model.getChecked())
				{
					selectedContacts.add(model);
					Log.e("", "selected count"+selectedContacts.size());
				}
				else
				{
					
				}
			}
			if(selectedContacts.size()>0)
			{
				
				if(Utils.checkInternetConnection(Contacts.this))
				{
					add = new AddContacts();
					add.execute();
				}
				else
				{
					AlertManager.shorttoastMessage(Contacts.this,getString(R.string.Internet_check) );
				}
			}
			else if(selectedContacts.size()>50)
			{
				AlertManager.shorttoastMessage(Contacts.this,getString(R.string.contact_limit) );
			}
			else
			{
				AlertManager.shorttoastMessage(Contacts.this,getString(R.string.select_contact) );
			}
		}
		else if(v==chkAllContacts)
		{
			if(adapter!=null && adapter.getCount()>0)
			{
				if (chkAllContacts.isChecked()) {
					adapter.selectAll();
					chkAllContacts.setButtonDrawable(R.drawable.checkbox_on_background);
				} else {
					adapter.deselectAll();
					chkAllContacts.setButtonDrawable(R.drawable.checkbox_off_background);
				}
			}
			
		}
		
	}
	private class AddContacts extends AsyncTask<Void, Integer, Void>
	{
		String [] name= {"contacts","token","user_id"};
		String [] values;
		String json="";
		String response;
		private boolean isException=false;

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progress.dismiss();
		//	adapter				= new ContactsSMSAdapter(Contacts.this,phoneContacts);
		//	listContacts.setAdapter(adapter);
			if(isException)
			{
				AlertManager.shorttoastMessage(Contacts.this, getString(R.string.common_error));
			}
			else
			{
				//Intent data = new Intent();
				//data.putExtra("contactsarray",selectedContacts);
				setResult(CONTACTS_SELECTED);
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress.show();
			manager = new APIManager(URL_ADD_CONTACTS);
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				values= new String[3];
				json=JsonIntegration.uploadContactsFromAdapter(Contacts.this, selectedContacts);
				values[0]=json.toString();
				values[1]=Utils.getDeviceID(Contacts.this); 
				values[2]=AppPreferenceManager.getUserId(Contacts.this);
				response=manager.commonPostData(URL_ADD_CONTACTS, name, values);
			}
			catch (Exception e) {
				Log.e("", "exc"+e.getMessage());
				isException=true;
			}
			return null;
		}
	}
	public class GetRawContacts extends AsyncTask<Void, Integer, Void>
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
			Log.e("", "POST EXECUTE ");
			if(isException)
			{
				AlertManager.shorttoastMessage(Contacts.this, getString(R.string.common_error));
			}
			else
			{
				try
				{
					JSONObject objGetCatogery = new JSONObject(response);
					if(objGetCatogery.optString(CODE).equals(SUCCESS_CODE_WITH_CONTENT))
					{
						JSONArray array = objGetCatogery.getJSONArray(CONTACTS);
						Log.e("", "contacts length"+array.length());
						for (int i = 0; i < array.length(); i++)
						{
							ContactsModel model = new ContactsModel();
							JSONObject postObject = array.getJSONObject(i);
							model.setName(postObject.optString(CONTACT_NAME));
							model.setPhoneNumber(postObject.optString(CONTACT_NUMBER));
						//	model.setSelected(postObject.optString(CONTACT_SELECTED));
							if(postObject.optString(CONTACT_SELECTED).equals("True"))
							{
								model.setChecked(true);
							}
							else
							{
								model.setChecked(false);
							}
							
							Log.e("", "name"+model.getName());
							Log.e("", "number"+model.getPhoneNumber());
							arrayListContacts.add(model);
						}
						adapter				= new ContactsSMSAdapter(Contacts.this,arrayListContacts,listContacts);
						listContacts.setAdapter(adapter);
					}
					else
					{
						AlertManager.shorttoastMessage(Contacts.this,objGetCatogery.optString(MESSAGE));
					}
				}
				catch (Exception e)
				{
					AlertManager.shorttoastMessage(Contacts.this, getString(R.string.common_error));
				}
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			manager= new APIManager(URL_GET_RAW_CONTACTS);
			progress.show();
			arrayListContacts	= new ArrayList<ContactsModel>();
			Log.e("", "PRE EXECUTE ");
		}
		@Override
		protected Void doInBackground(Void... params) 
		{
			try
			{
				response= manager.commonPostData(URL_GET_RAW_CONTACTS+"/"+Utils.getDeviceID(Contacts.this),name,value);
				Log.e("", "DO IN BACKGROUND EXECUTE ");
			} catch (Exception e) 
			{
				Log.e("", "error"+e.getMessage());
				isException=true;
			}
			return null;
		}
	}
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		if(get!=null)
		{   
			if(!get.isCancelled())
			{
				Log.e("", "cancelled the task");
				get.cancel(true);
			}
		}
		if(add!=null)
		{
			if(!add.isCancelled())
			{
				Log.e("", "cancelled the task");
				add.cancel(true);
			}
		}
	}
}
