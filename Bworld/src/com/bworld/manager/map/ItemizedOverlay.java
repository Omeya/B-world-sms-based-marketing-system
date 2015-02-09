/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.bworld.manager.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context c;
	
	private Paint paint1, paint2;
	
	public ItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
	}

	public void addOverlay(OverlayItem overlay) {
	    m_overlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}
	 public void clear() {
		 m_overlays.clear();
	        populate();
	    }
	 
//	 @Override
//		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
//		 
//		 paint1 = new Paint();
//		    paint1.setARGB(128, 0, 0, 255);
//		    paint1.setStrokeWidth(2);
//		    paint1.setStrokeCap(Paint.Cap.ROUND);
//		    paint1.setAntiAlias(true);
//		    paint1.setDither(false);
//		    paint1.setStyle(Paint.Style.STROKE);
//
//		    paint2 = new Paint();
//		    paint2.setARGB(64, 0, 0, 255);
//		    GeoPoint point = new GeoPoint((int) (GlobalConstants.CLATITUDE * 1E6),
//					(int) (GlobalConstants.CLONGITUDE * 1E6));
//
//		    Point pt = mapView.getProjection().toPixels(point, null);
//		    float projectedRadius = mapView.getProjection().metersToEquatorPixels(500);
//		System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//		    canvas.drawCircle(pt.x, pt.y, projectedRadius, paint2);
//		    canvas.drawCircle(pt.x, pt.y, projectedRadius, paint1);
//
//		}
	 
//	 @Override
//	 public void draw(Canvas canvas, MapView mapView, boolean shadow) {
//	     // Transform geo-position to Point on canvas
//	     Projection projection = mapView.getProjection();
//	     Point point = new Point();
//	     int rad=50;
//	     GeoPoint geopoint=new GeoPoint((int)(GlobalConstants.CLATITUDE*1E6), (int)(GlobalConstants.CLONGITUDE*1E6));
//	     //store the transformed geopoint into a point with pixel values
//	     projection.toPixels(geopoint, point);
//
//	     /*// text "My Location"
//	     Paint text = new Paint();
//	     text.setAntiAlias(true);
//	     text.setColor(Color.BLUE);
//	     text.setTextSize(12);
//	     text.setTypeface(Typeface.MONOSPACE);*/
//
//	     // the circle to mark the spot
//	     Paint circlePaint = new Paint();
//	     circlePaint.setAntiAlias(true);
//	     //fill region
//	     circlePaint.setColor(Color.argb(125, 0, 0, 255));
//	     circlePaint.setAlpha(90);
//	     circlePaint.setStyle(Paint.Style.FILL);
//	    // canvas.drawCircle(point.x, point.y, rad, circlePaint);
//	     //border region
//	     circlePaint.setColor(Color.WHITE);
//	     circlePaint.setAlpha(255);
//	     circlePaint.setStyle(Paint.Style.STROKE);
//	     circlePaint.setStrokeWidth(3);
//	     canvas.drawCircle(point.x, point.y, rad, circlePaint);
//
//	     /*canvas.drawText("My Location", point.x + 3 * CIRCLERADIUS, point.y + 3
//	             * CIRCLERADIUS, text);*/
//	 }
	 
	 

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		
		return true;
	}
	
}
