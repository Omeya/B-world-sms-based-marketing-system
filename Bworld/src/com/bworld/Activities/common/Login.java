package com.bworld.Activities.common;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bworld.R;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.Utils;

public class Login extends Activity implements OnClickListener,URLConstants,IResponseConstants,IJSONTagConstants,IResultCodeConstants {
	private Button btnLogin;
	private EditText edtUsername;
	private EditText edtPassword;
	/*private String username = "test@gmail.com";
	private String password = "test";*/
	private Intent i;
	CustomProgressDialog progress;
	private RelativeLayout relativeHeader;
	private boolean isLogout=false;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		context=this;
		Bundle extras= getIntent().getExtras();
		if(extras!=null)
		{
			isLogout = extras.getBoolean("logout");
		}
		
		initialiseUI();   
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(isLogout)
		{
			i = new Intent(Login.this,MainPage.class);
			startActivity(i);
		}
	}
	private void initialiseUI()
	{
		relativeHeader		= (RelativeLayout)findViewById(R.id.relative_header);
		btnLogin			= (Button)findViewById(R.id.btn_login);
		edtUsername			=(EditText)findViewById(R.id.edt_username);
		edtPassword			=(EditText)findViewById(R.id.edt_password);
		btnLogin.setOnClickListener(this);
		progress   = CustomProgressDialog.show(Login.this, false);
		
	//	Utils.getHeader(Login.this, relativeHeader, getString(R.string.login_head));
	}
	@Override
	public void onClick(View v) {
		if (v == btnLogin) 
		{
			if (edtUsername.getText().toString().trim().equals("")) 
			{
				AlertManager.shorttoastMessage(Login.this,
						getString(R.string.Enter_email));
			}
			else if (edtPassword.getText().toString().trim().equals(""))
			{
				AlertManager.shorttoastMessage(Login.this,
						getString(R.string.enter_password));
			}
			else if(Utils.checkInternetConnection(Login.this))
			{
					login l= new login();
					l.execute();
			}
			else
			{
					AlertManager.shorttoastMessage(Login.this,getString(R.string.Internet_check) );
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
		super.onActivityResult(requestCode, resultCode, data);
			 if(requestCode==TO_HOME_PAGE)
			    {
			    	if(resultCode==LOGOUT)
			    	{
			    		setResult(LOGOUT);
			    		finish();
			    	}
			    	
			    	else if(resultCode==LOGIN_SUCESS)
			    	{
			    		setResult(LOGIN_SUCESS);
			    		finish();
			    	}
			    	
			    }
		
	}
	public class login extends AsyncTask<Void, Integer, Void>
	{
		private boolean isException=false;
		private String response;
		APIManager manager;
		String name[] ={"username","password","regId"};
		String value[] ={edtUsername.getText().toString(),edtPassword.getText().toString(),AppPreferenceManager.getGCMRegid(context)};
		JSONObject resObj;
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress.dismiss();
			if(isException)
			{
				AlertManager.shorttoastMessage(Login.this, getString(R.string.common_error));
			}
			else
			{
				if(response.contains(SUCCESS_CODE))
				{
					try
					{
						
						resObj = new JSONObject(response);
						AppPreferenceManager.saveUserId(Login.this, resObj.optString(USER));
						i  = new Intent(Login.this,HomePage.class);
						startActivityForResult(i, TO_HOME_PAGE);
					//	finish();
						AlertManager.shorttoastMessage(Login.this, getString(R.string.login_success));
						AppPreferenceManager.getUserId(Login.this);
					}
					catch (Exception e) 
					{
						AlertManager.shorttoastMessage(Login.this, getString(R.string.common_error));
					}
				}
				else if(response.contains(INVALID_CREDENTIALS))
				{
					AlertManager.shorttoastMessage(Login.this, getString(R.string.check_credentials));
				}    
				else
				{
					AlertManager.shorttoastMessage(Login.this, getString(R.string.common_error));
				}
				
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			manager= new APIManager(URL_LOGIN);
			progress.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				response= manager.commonPostData(URL_LOGIN, name, value);
			} catch (Exception e) {
				isException=true;
			}
			return null;
		}
		
	}

}
