package com.bworld.Activities.TrackFriends;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bworld.R;
import com.bworld.Adapters.TrustedFriendsAdapter;
import com.bworld.Listeners.MapLocationListener;
import com.bworld.constants.GlobalConstants;
import com.bworld.constants.IJSONTagConstants;
import com.bworld.constants.IResponseConstants;
import com.bworld.constants.IResultCodeConstants;
import com.bworld.constants.URLConstants;
import com.bworld.manager.APIManager;
import com.bworld.manager.AlertManager;
import com.bworld.manager.AppPreferenceManager;
import com.bworld.manager.CustomProgressDialog;
import com.bworld.manager.Utils;
import com.bworld.manager.map.ItemizedOverlay;
import com.bworld.models.FriendsModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapTracking extends MapActivity implements OnClickListener,LocationListener,URLConstants,IResponseConstants,IResultCodeConstants,IJSONTagConstants {

	private Button btnFriendsList;
	private Button btnTrustedFriends;
	private Button btnMap;
	private Intent i;
	private Context context;
	private LocationManager lm;
	MapLocationListener mll;
	Location location;
	private boolean gps_enabled = false;
	private boolean net_enabled = false;
	private CustomProgressDialog progress;
	private AddLocation addLocation;
	MapController mc;
	MapView mapView;
	ItemizedOverlay itemizedOverlay;
	Drawable drawable;
	List<Overlay> mapOverlays;
	OverlayItem overLay;
	private ArrayList<FriendsModel> arrayList;
	GetTrustedFriends getTrustedFriends;
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.map_fragment);
		context=this;
		initialiseUI();
		CheckEnableGPS();
		clickListeners();
		if(Utils.checkInternetConnection(context))
		{
			getTrustedFriends = new GetTrustedFriends();
			getTrustedFriends.execute();
		}
		else
		{
			AlertManager.shorttoastMessage(context, getString(R.string.Internet_check));
		}
	}

	private void clickListeners()
	{
		btnFriendsList.setOnClickListener(this);
		btnTrustedFriends.setOnClickListener(this);
		btnMap.setOnClickListener(this);
	}
	private void initialiseUI()
	{
		btnFriendsList			=(Button)findViewById(R.id.btn_friends);
		btnTrustedFriends		=(Button)findViewById(R.id.btn_trusted_friends);
		btnMap					=(Button)findViewById(R.id.btn_map);
		progress				=CustomProgressDialog.show(context, true);
		
		btnMap.setBackgroundResource(R.drawable.blue);
		
		mapView 		= (MapView) findViewById(R.id.mapview);
		mapOverlays 	= mapView.getOverlays();
		mc 				= mapView.getController();
		setButtons();
	}
	private void getCurrentLocation() {
		
		GeoPoint point = null;
		System.out.println("Latitiude" + GlobalConstants.CLATITUDE+ "Longitude" + GlobalConstants.CLONGITUDE);
		point = new GeoPoint((int) (GlobalConstants.CLATITUDE * 1E6),
				(int) (GlobalConstants.CLONGITUDE * 1E6));
		mc.animateTo(point);
		mc.setZoom(12);
		
		drawable = getResources().getDrawable(R.drawable.marker_my_location);
		itemizedOverlay = new ItemizedOverlay(drawable, mapView);
		mapOverlays.clear();
		overLay = new OverlayItem(point, "My Location","");
		itemizedOverlay.addOverlay(overLay);
		mapOverlays.add(itemizedOverlay);
	}
	private void initLocation()
	{
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mll = new MapLocationListener();
		location = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(),
				false));
		try 
		{
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			net_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		} 
		catch (Exception ex) {
		}
		if(gps_enabled || net_enabled)
		{
			if (gps_enabled) 
			{
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

			}
			else if(net_enabled)
			{
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			}
			else
			{
				CheckEnableGPS();
			}
			getCurrentLocation();
			if(GlobalConstants.CLATITUDE!=0.0 || GlobalConstants.CLONGITUDE!=0.0)
			{
				updateLocationToServer();
			}
		}
		else
		{
			CheckEnableGPS();
		}
	}
	private void updateLocationToServer()
	{
		if(Utils.checkInternetConnection(context))
		{
			addLocation = new AddLocation();
			addLocation.execute();
		}
		else
		{
			AlertManager.shorttoastMessage(context, getString(R.string.Internet_check));
		}
	}
	@Override
	public void onClick(View v) {
		if(v==btnFriendsList)
		{
			btnFriendsList.setBackgroundResource(R.drawable.blue);
			btnMap.setBackgroundResource(R.drawable.white);
			btnTrustedFriends.setBackgroundResource(R.drawable.white);
			setButtons();
			i = new Intent(context,FriendsList.class);
			startActivity(i);
			finish();
		}
		else if(v==btnTrustedFriends)
		{
			btnFriendsList.setBackgroundResource(R.drawable.white);
			btnMap.setBackgroundResource(R.drawable.white);
			btnTrustedFriends.setBackgroundResource(R.drawable.blue);
			setButtons();
			
			i = new Intent(context,TrustedFriendsList.class);
			startActivity(i);
			finish();
		}
		
		}
	
	private void CheckEnableGPS() {
		String provider = Settings.Secure.getString(getContentResolver(),Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provider ==null ||provider.equals("null")|| provider.equals("")  ) 
		{
			AlertManager
			.alertGps(MapTracking.this,
					"Your gps settings is OFF. Do you wish to change it ON for getting updating location ?");
		} 
		else
		{
			initLocation();
		}
	}
	private void setButtons()
	{
		Utils.setPaddings(btnFriendsList);
		Utils.setPaddings(btnMap);
		Utils.setPaddings(btnTrustedFriends);
	}

	private class AddLocation extends AsyncTask<Void, Void, Void>
	{
		private boolean isException=false;
		private String response;
		private String [] names={"id","lattitude","longitude"};
		private String [] values ={AppPreferenceManager.getUserId(context),
				String.valueOf(GlobalConstants.CLATITUDE),String.valueOf(GlobalConstants.CLONGITUDE)};
		private APIManager manager;
		
		@Override
		protected Void doInBackground(Void... params) {
			try
			{
				manager  = new APIManager(URL_ADD_LOCATION);
				response=manager.commonPostData(URL_ADD_LOCATION, names, values);
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
			if(progress.isShowing())
			{
				progress.dismiss();
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(!progress.isShowing())
			{
				progress.show();
			}
			isException=false;
		}
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLocationChanged(Location location) {
		GlobalConstants.CLATITUDE = location.getLatitude();
		GlobalConstants.CLONGITUDE = location.getLongitude();
		/*GlobalConstants.CLATITUDE = 76;
		GlobalConstants.CLONGITUDE = 53;*/
		//getCurrentLocation();
	}
	@Override
	public void onProviderDisabled(String provider) {
		CheckEnableGPS();
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		initLocation();
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		
	}
	private void setTrustedFriendsOnMap(ArrayList<FriendsModel> array)
	{
		drawable = getResources().getDrawable(R.drawable.marker);
		GeoPoint point;
		for (int i = 0; i < array.size(); i++)
		{
			//point = new GeoPoint((int) (GlobalConstants.CLATITUDE * 1E6),(int) (GlobalConstants.CLONGITUDE * 1E6));
			
			double lat=Double.valueOf(array.get(i).getLatitude());
			double lng=Double.valueOf(array.get(i).getLongitude());
			
			point = new GeoPoint((int) (lat * 1E6),(int) (lng * 1E6));
			
			itemizedOverlay = new ItemizedOverlay(drawable, mapView);
			overLay = new OverlayItem(point, array.get(i).getName(),"");
			itemizedOverlay.addOverlay(overLay);
			mapOverlays.add(itemizedOverlay);
			
		}
	}
	private class GetTrustedFriends extends AsyncTask<Void,Void,Void>
	{
		private boolean isException=false;
		private APIManager manager;
		private String response;
		private String [] name={};
		private String [] value={};
		@Override
		protected void onPostExecute(Void arg0) {
			super.onPostExecute(arg0);
			if(progress.isShowing())
			{
				progress.dismiss();
			}
			if(isException)
			{
				AlertManager.shorttoastMessage(context, getString(R.string.common_error));
			}
			else
			{
				try
				{
					FriendsModel model;
					JSONObject jsonResponse=  new JSONObject(response);
					if(jsonResponse.optString(CODE).equals(SUCCESS_CODE))
					{
						JSONObject obj;
						JSONArray userobj=  jsonResponse.getJSONArray(FRIENDS_USERS) ;
						for (int i = 0; i < userobj.length(); i++)
						{
							model= new FriendsModel();
							obj= userobj.getJSONObject(i);
							Log.e("", "name"+obj.optString(FRIENDS_FIRST_NAME));
							model.setName(obj.optString(FRIENDS_FIRST_NAME));
							model.setProfilePicture(URLDecoder.decode(obj.optString(FRIENDS_IMAGE)));
							model.setId(obj.optString(FRIENDS_ID));
							model.setStatus(obj.optString(FRIENDS_STATUS));
							model.setLatitude(obj.optString(FRIENDS_LATITUDE));
							model.setLongitude(obj.optString(FRIENDS_LONGITUDE));
							arrayList.add(model);
						}
						if(arrayList.size()>0)
						{
							setTrustedFriendsOnMap(arrayList);
						}
					}
				}
				catch(Exception e)
				{
					AlertManager.shorttoastMessage(context, getString(R.string.common_error));
				}
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isException=false;
			if(!progress.isShowing())
			{
				progress.show();
			}
			arrayList = new ArrayList<FriendsModel>();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try
			{
				manager  = new APIManager(URL_GET_TRUSTED_FRIENDS);
				Log.e("", "userid"+AppPreferenceManager.getUserId(context));
				response = manager.commonPostData(URL_GET_TRUSTED_FRIENDS+AppPreferenceManager.getUserId(context), name, value);
			}
			catch(Exception e)
			{
				isException=true;
			}
			return null;
		}
		
	}
	
}
