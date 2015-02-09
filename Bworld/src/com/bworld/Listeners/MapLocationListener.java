package com.bworld.Listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.bworld.constants.GlobalConstants;

public class MapLocationListener implements LocationListener {

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		GlobalConstants.CLATITUDE=location.getLatitude();
		GlobalConstants.CLONGITUDE=location.getLongitude();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
