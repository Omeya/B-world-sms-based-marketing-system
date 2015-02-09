package com.bworld.Activities.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bworld.R;
import com.bworld.Accounts.Facebook.FacebookAccount;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.Utils;
import com.bworld.services.SMSService;

public class MainPage extends Activity implements OnClickListener, URLConstants,IResultCodeConstants{

	private Button btnSignup;
	private Button btnLogin;
	private Button btnFacebookSignup;
	FacebookAccount facebookAccount;
	private Intent i;
	String name[] = {"name","number"};
	String value[];
	CustomProgressDialog progress;
	private RelativeLayout relativeHeader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);
		initialiseUI();
	}
	private void initialiseUI()
	{
		relativeHeader		= (RelativeLayout)findViewById(R.id.relative_header);
		btnSignup  				 =(Button)findViewById(R.id.btn_signup);
		btnLogin    			 =(Button)findViewById(R.id.btn_login);
		btnFacebookSignup  		 =(Button)findViewById(R.id.btn_facebook);
		
		btnLogin.setOnClickListener(this);
		btnSignup.setOnClickListener(this);
		btnFacebookSignup.setOnClickListener(this);
		progress= CustomProgressDialog.show(MainPage.this, true);
	
	//	AlertManager.longtoastMessage(MainPage.this,"Device Id"+ Utils.getDeviceID(MainPage.this));
		
		
	}
	@Override
	public void onClick(View v) {
		if(v==btnLogin)
		{
			i = new Intent(MainPage.this, Login.class);
			startActivityForResult(i, TO_HOME_PAGE);
		//	startActivity(i);
		//	finish();   
		}
		else if(v==btnSignup)
		{
			i = new Intent(MainPage.this, Signup.class);
			startActivity(i);
		}
		else if(v==btnFacebookSignup)
		{
			if(Utils.checkInternetConnection(MainPage.this))
			{
				facebookAccount = new FacebookAccount(MainPage.this);
				if (facebookAccount.isLogin())
				{
					if(AppPreferenceManager.getFbUserId(MainPage.this).equals(""))
					{
						facebookAccount.showLogin();
					}
					else
					{
						AppPreferenceManager.saveUserId(MainPage.this, AppPreferenceManager.getFbUserId(MainPage.this));
						i = new Intent(MainPage.this, HomePage.class);
						startActivity(i);
						finish();
					}
					
				}
				else
				{
					facebookAccount.showLogin();
				}
			}
			else
			{
				AlertManager.shorttoastMessage(MainPage.this, getString(R.string.Internet_check));
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

}
