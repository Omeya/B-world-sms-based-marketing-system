package com.bworld.Activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bworld.R;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;

public class PushMessage extends Activity implements OnClickListener,URLConstants,IResultCodeConstants,IResponseConstants,IJSONTagConstants{

	private String message="";
	private String id="";
	private TextView txtMessage;
	private Button btnClose;
	private Button btnConfirm;
	private CustomProgressDialog progress;
	private Context context;
	ConfirmTrusted confirm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_message);
		Bundle extras= getIntent().getExtras();
		if(extras!=null)
		{
			message	 = extras.getString("message");
			id	 	 = extras.getString("id");
		}
		context=this;
		initialiseUI();
		clickListeners();
		Log.e("", "inside push message"+id);
	}
	private void clickListeners()
	{
		btnClose.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
	}
	private void initialiseUI()
	{
		btnClose		= (Button)findViewById(R.id.btn_close);
		btnConfirm		= (Button)findViewById(R.id.btn_confirm);
		txtMessage		=(TextView)findViewById(R.id.text_message);
		txtMessage.setText(message);
		progress		= CustomProgressDialog.show(context, false);
		
	}

	@Override
	public void onClick(View v) 
	{
		if(v==btnClose)
		{
			finish();
		}
		else if(v==btnConfirm)
		{
			confirm  = new ConfirmTrusted();
			confirm.execute();
		}
	}

	private class ConfirmTrusted extends AsyncTask<Void, Void, Void>
	{
		private String response="";
		private boolean isException=false;
		private String [] name={"user_id","friend_id"};
		private String [] value={};
		private APIManager apiManager;
		@Override
		protected Void doInBackground(Void... params) {
			
			try
			{
				value       = new String[2];
				value[0]	= id;//AppPreferenceManager.getUserId(context);
				value[1]	= AppPreferenceManager.getUserId(context);
				apiManager 	= new APIManager(URL_CONFIRM_TRUSTED_FRIENDS);
				response 	= apiManager.commonPostData(URL_CONFIRM_TRUSTED_FRIENDS, name, value);
			}
			catch(Exception e)
			{
				isException=true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress.dismiss();
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
			isException=false;
			progress.show();
		}
		
	}
	
}
