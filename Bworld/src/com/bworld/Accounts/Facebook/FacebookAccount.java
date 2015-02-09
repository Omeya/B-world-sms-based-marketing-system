package com.bworld.Accounts.Facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bworld.Accounts.Facebook.libs.DialogError;
import com.bworld.Accounts.Facebook.libs.Facebook;
import com.bworld.Accounts.Facebook.libs.Facebook.DialogListener;
import com.bworld.Accounts.Facebook.libs.FacebookError;
import com.bworld.Accounts.Facebook.libs.Util;
import com.bworld.Activities.common.FBActivity;
import com.bworld.constants.IAccountConstants;
import com.bworld.models.FriendsModel;


public class FacebookAccount implements IAccountConstants {
	private Facebook facebook;
	private Activity context;
	private boolean isShared = false;
	

	public FacebookAccount(Activity context) {
		this.context = context;
		facebook = new Facebook(APP_ID);
		restoreCredentials(facebook);
	}

	public String getUserName() {
		String userName = "";
		if (facebook.isSessionValid()) {
			JSONObject json;
			try {
				json = Util.parseJson(facebook.request("me"));
				if (!json.isNull("username")) {
					userName = json.optString("username");
				} else {
					userName = "";
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FacebookError e) {
				e.printStackTrace();
			}
		}
		return userName;
	}

	public String getEmail() {
		String email = "";
		if (facebook.isSessionValid()) {
			JSONObject json;
			try {
				json = Util.parseJson(facebook.request("me"));
				email = json.optString("email");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FacebookError e) {
				e.printStackTrace();
			}
		}

		return email;
	}
	public String getUserId() {
		String userId = "";
		if (facebook.isSessionValid()) {
			JSONObject json;
			try {
				json = Util.parseJson(facebook.request("me"));
				userId = json.getString("id");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FacebookError e) {
				e.printStackTrace();
			}
		}
		return userId;
	}
	// Save The Credentials in the Shared Preferences..
	public boolean saveCredentials(Facebook facebook) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		System.out.println("Shared Token " + facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		System.out.println("Shared Expires " + facebook.getAccessExpires());
		return editor.commit();
	}

	// Reset The Credentials in the Shared Preferences..
	public boolean resetCredentials() {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(TOKEN, null);
		editor.putLong(EXPIRES, 0);
		Util.clearCookies(context);
		return editor.commit();
	}

	
	public ArrayList<FriendsModel> getFacebookFriends() {
		ArrayList<FriendsModel> friendsList = new ArrayList<FriendsModel>();
		Bundle params = new Bundle();
		params.putString("fields", "name, picture");
		try {
			JSONObject jsonFriends = Util.parseJson(facebook.request(
					"me/friends", params));
			JSONArray resArr = jsonFriends.optJSONArray(FRIEND_LIST_ARRAY);
			System.out.println("Res" + resArr);
			for (int i = 0; i < resArr.length(); i++) {
				FriendsModel friendsListModel = new FriendsModel();
				JSONObject arrObject = resArr.getJSONObject(i);
				friendsListModel.setName(arrObject.optString(FRIEND_LIST_NAME));
				JSONObject picObj = arrObject.optJSONObject(FRIEND_LIST_PICOBJ);
				JSONObject picdata = picObj.optJSONObject(FRIEND_LIST_OBJ);
				friendsListModel.setProfilePicture(picdata.optString(FRIEND_PIC_URL));
				friendsList.add(friendsListModel);
			}
			
			Log.e("", "Count"+friendsList.size());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FacebookError e) {

			e.printStackTrace();
		}
		return friendsList;
	}
	
	
	// Set the Credentials in the Facebook Lib..
	public boolean restoreCredentials(Facebook facebook) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		System.out.println("Restore Token "
				+ sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		System.out.println("Restore Expires "
				+ sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	
	public void logout() {
		resetCredentials();
	}

	public String getAccessToken() {
		return facebook.getAccessToken();
	}

	
	public Boolean loginAndPostToWall(String message,String image,String desc) {
		facebook.authorize(context, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH,
				new LoginDialogListener(message,image,desc));
		return isShared;
	}

	public boolean isLogin() {
		if (facebook.isSessionValid()) {
			return true;
		} else {
			return false;
		}
	}

	public void showLogin() {
		facebook.authorize(context, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH,
				new LoginDialogListener());
	}

	/**
	 * This function is for Post to wall(Image & Text) and also for photos
	 * upload..
	 */
	public Boolean postToWall(String message,String imageUrl,String url) {
		String response;
		Bundle parameters = new Bundle();
		parameters.putString("message", message);	
		parameters.putString("link",url);
		/*parameters.putString("description", "test desc");
		if (!imageUrl.equals("")) {
			parameters.putString("picture", imageUrl);
		}*/
		
		try {
			try {				
				response = facebook.request("me/feed",parameters,"POST");					
				isShared = true;
				Log.d("Tests", "got response: " + response);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			showToast("Sharing failed!");
			e.printStackTrace();
		}
		return isShared;

	}

	

	class LoginDialogListener implements DialogListener {
		private String status;
		private String imageUrl;
		private String desc;
		
		public LoginDialogListener(String status,String image,String description) {
			this.status 	= status;
			this.imageUrl 	= image;
			this.desc 		= description;
		}

		public LoginDialogListener() {

		}

		public void onComplete(Bundle values) {
			saveCredentials(facebook);		
			/*if (status != null) {
				postToWall(status,imageUrl,desc);
				sharedMsg(isShared);
			}*/
			
			Intent i = new Intent(context, FBActivity.class);
			context.startActivity(i);
		}

		public void onFacebookError(FacebookError error) {
			showToast("Authentication with Facebook failed!");
		}

		public void onError(DialogError error) {
			showToast("Authentication with Facebook failed!");
		}

		public void onCancel() {
			showToast("Authentication with Facebook cancelled!");
		}
	}
	private void sharedMsg(Boolean isShare) {
		if (isShare) {
			/*AlertManager.toastMessage(this.context, this.context.getResources()
					.getString(R.string.shareSuccess));*/
		} else {
			/*AlertManager.toastMessage(this.context, this.context.getResources()
					.getString(R.string.shareFailure));*/
		}
	}

	private void showToast(String message) {
		Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
	}
}
