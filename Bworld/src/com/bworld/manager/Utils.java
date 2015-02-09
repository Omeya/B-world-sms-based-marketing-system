package com.bworld.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.support.v4.content.CursorLoader;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bworld.constants.GlobalConstants;
import com.bworld.models.ContactsModel;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
public class Utils  {

	 static ArrayList<ContactsModel>  contactsList = new ArrayList<ContactsModel>();
 	 
public static ArrayList<ContactsModel> queryAllRawContacts(Context context)
{
	// ArrayList<ContactsModel>  contacts = new ArrayList<ContactsModel>();
		final String[] projection = new String[] {
				RawContacts.CONTACT_ID,					// the contact id column
				RawContacts.DELETED						// column if this contact is deleted
		};
		
		final Cursor rawContacts = ((Activity) context).managedQuery(
				RawContacts.CONTENT_URI,				// the uri for raw contact provider
				projection,	
				null,									// selection = null, retrieve all entries
				null,									// not required because selection does not contain parameters
				null);									// do not order

		final int contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID);
		final int deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED);
		
		
		if(rawContacts.getCount()>0)
		{
			if(AppPreferenceManager.getContactCount(context)==rawContacts.getCount())
	    	{
	    		return null;
	    	}
			AppPreferenceManager.saveContactCount(context, rawContacts.getCount());
			if(rawContacts.moveToFirst()) {					// move the cursor to the first entry
				while(!rawContacts.isAfterLast()) {			// still a valid entry left?
					final int contactId = rawContacts.getInt(contactIdColumnIndex);
					final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);
					
					contactsList=queryDetailsForContactSpinnerEntry(contactId, context);
				
					
					rawContacts.moveToNext();				// move to the next entry
				}
			}
		}
		

		rawContacts.close();
		return contactsList;
	}
public static  ArrayList<ContactsModel> queryDetailsForContactSpinnerEntry(int contactId,Context context)
{
	
	//  ArrayList<ContactsModel>  contacts = new ArrayList<ContactsModel>();
	  ContactsModel contactModel  =null; 
	  String number = null;
	final String[] projection = new String[] {
			Contacts.DISPLAY_NAME					// the name of the contact
									// the id of the column in the data table for the image
	};

	final Cursor contact = ((Activity) context).managedQuery(
			Contacts.CONTENT_URI,
			projection,
			Contacts._ID + "=?",						// filter entries on the basis of the contact id
			new String[]{String.valueOf(contactId)},	// the parameter to which the contact id column is compared to
			null);
	
	if(contact.moveToFirst()) {
		final String name = contact.getString(
				contact.getColumnIndex(Contacts.DISPLAY_NAME));
	
		number=queryAllPhoneNumbersForContact(contactId, context);
		if(number!=null)
		{
			if(number.length()>14 || number.length()<10 )
			{
				
			}
			else
			{
				 if(number.startsWith("+91"))
		    	   {
					 number = number.replace("+91","");
		    	   }
		    	   if(number.startsWith("0"))
		    	   {
		    		   number=number.substring(1, number.length());
		    	   }
				if(checkIndianPhoneNumber(number))
				{
					contactModel = new ContactsModel();
					contactModel.setName(name);
					contactModel.setPhoneNumber(number);
					Log.e("", "Name"+name);
					contactsList.add(contactModel);
				}
				
			}
		}
		
		contact.close();
	}
	contact.close();
	
	return contactsList;
 }


public static String queryAllPhoneNumbersForContact(int contactId,Context context)
{
	String number ="";
	final String[] projection = new String[] {
			Phone.NUMBER,
			Phone.TYPE,
	};

	final Cursor phone = ((Activity) context).managedQuery(
			Phone.CONTENT_URI,	
			projection,
			Data.CONTACT_ID + "=?",
			new String[]{String.valueOf(contactId)},
			null);

	if(phone.moveToFirst()) 
	{
		final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
		
		while(!phone.isAfterLast()) 
		{
			number = phone.getString(contactNumberColumnIndex);
			
			Log.e("", "Number"+number);
			phone.moveToNext();
		}
	}
	phone.close();
	return number;
}
	public static ArrayList<ContactsModel> getAllPhoneContacts(Context ctx) 
	{
	    Log.d("START","Getting all Contacts");
	  ArrayList<ContactsModel>  phoneContacts = new ArrayList<ContactsModel>();
	  ContactsModel phoneContactInfo=null;     
	    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
	    
	  
		
	    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	    Cursor cursor = ctx.getContentResolver().query(uri, new String[] 
	             {ContactsContract.CommonDataKinds.Phone.NUMBER,
	    		ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
	    		ContactsContract.CommonDataKinds.Phone._ID}, null, null, sortOrder);
	    
	    if(cursor.getCount()>0)
	    {
	    	if(AppPreferenceManager.getContactCount(ctx)==cursor.getCount())
	    	{
	    		return null;
	    	}
	    	AppPreferenceManager.saveContactCount(ctx, cursor.getCount());
	    	GlobalConstants.contactscount=cursor.getCount();
	    	 Log.d("","all Contacts"+cursor.getCount());
	    	cursor.moveToFirst();
		    while (cursor.isAfterLast() == false)
		    {
		       String contactNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
		       String contactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		    
		       if(contactNumber.length()>14 || contactNumber.length()<10)
		       {
		    	   
		       }
		       else
		       {
		    	   if(contactNumber.startsWith("+91"))
		    	   {
		    		   contactNumber = contactNumber.replace("+91","");
		    	   }
		    	   if(contactNumber.startsWith("0"))
		    	   {
		    		  contactNumber=contactNumber.substring(1, contactNumber.length());
		    	   }
		    	   if(checkIndianPhoneNumber(contactNumber))
		    	   {
		    		   Log.e("", "name"+contactName);
				        Log.e("", "number"+contactNumber);
				        
				        phoneContactInfo = new ContactsModel();
				        phoneContactInfo.setName(contactName);
				        phoneContactInfo.setPhoneNumber(contactNumber);
				        if (phoneContactInfo != null)
				        {
				        	phoneContacts.add(phoneContactInfo);
				        }
		    	   }
		    	   else
		    	   {
		    		   Log.d("", "number not matched"+contactNumber);
		    	   }
		    		  
		       }
		       
		       phoneContactInfo = null; 
		        cursor.moveToNext();
		    }       
		    cursor.close();
		    cursor = null;
		    Log.d("END","Got all Contacts");
	    }
	    
	    return phoneContacts;
	}
	
	
	public static ArrayList<ContactsModel> getPhoneContactsLowerVersion(Context ctx)
	{
		  ContactsModel phoneContactInfo=null; 
		  ArrayList<ContactsModel>  phoneContacts = new ArrayList<ContactsModel>();
		Cursor c = ctx.getContentResolver().query(People.CONTENT_URI, null,null, null, null);
	//	  Cursor c = ctx.getContentResolver().query(Phone.CONTENT_URI, null,null, null, null); 
		((Activity) ctx).startManagingCursor(c);
		if (c.moveToFirst()) 
		{
			String name;
			String phoneNumber;
			int nameColumn = c.getColumnIndex(People.NAME);
			int phoneColumn = c.getColumnIndex(People.Phones.NUMBER);
		//	int email_column = c.getColumnIndex(People.PRIMARY_ORGANIZATION_ID);
			Uri phoneUri = null;
			Uri emailUri = null;
			// int phoneColumn = c.getColumnIndex(People.TYPE_FAX_WORK);
			do {
				// Get the field values
				 phoneContactInfo = new ContactsModel();
				 phoneContactInfo.setName(c.getString(nameColumn));
			     phoneContactInfo.setPhoneNumber(c.getString(phoneColumn));
			        if (phoneContactInfo != null)
			        {
			        	phoneContacts.add(phoneContactInfo);
			        }
			        Log.e("", "name"+c.getString(nameColumn));
				    Log.e("", "number"+c.getString(phoneColumn));
			        
			       phoneContactInfo = null;
				 
				 
			//	name = c.getString(nameColumn);
			//	phoneNumber = c.getString(phoneColumn);
			//	CheckBoxifiedContacts checkboxifiedcontacts = new CheckBoxifiedContacts(name, phoneNumber, false,null,null,null,null);
				System.out.println("kjdskjskjfklsjl");
			//	publishProgress(checkboxifiedcontacts);
			} while (c.moveToNext());
		}
		return phoneContacts;
	}
	public static void getHeader(Context context, RelativeLayout layout,String heading)
	{
		HeaderManager manager = new HeaderManager((Activity) context, heading);
		manager.getHeader(layout);
	}
	
	public static String getCalender()
	{
		String zero="0";
		Calendar c = Calendar.getInstance();
		GlobalConstants.year = c.get(Calendar.YEAR);
		GlobalConstants.month = c.get(Calendar.MONTH)+1;
		
		GlobalConstants.day = c.get(Calendar.DAY_OF_MONTH);
		
		GlobalConstants.hour =c.get(Calendar.HOUR_OF_DAY);
		GlobalConstants.minute=c.get(Calendar.MINUTE);
		GlobalConstants.seconds=c.get(Calendar.SECOND);
		
		System.out.println("hour"+c.get(Calendar.HOUR_OF_DAY));
		System.out.println("minute"+c.get(Calendar.MINUTE));
		System.out.println("second"+c.get(Calendar.SECOND));
		System.out.println("month"+GlobalConstants.month);
		
		System.out.println("Month"+getMonth(GlobalConstants.month));
		
		String timestamp = GlobalConstants.year+"-"+
			GlobalConstants.month+"-"+GlobalConstants.day+" "+
			GlobalConstants.hour+":"+GlobalConstants.minute+":"+GlobalConstants.seconds;
		
		System.out.println("timestamp"+timestamp);
		
		return timestamp;
	}
	
	public static boolean checkIndianPhoneNumber(String email)
	{
		String expression = "^[7-9][0-9]{9}$";
		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email.toString());
		if (matcher.matches())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static String getCalenderString()
	{
		String zero="0";
		Calendar c = Calendar.getInstance();
		GlobalConstants.syear = String.valueOf(c.get(Calendar.YEAR));
		GlobalConstants.smonth = String.valueOf(c.get(Calendar.MONTH)+1);
		
		if(GlobalConstants.smonth.length()>1)
		{
			
		}
		else
		{
			GlobalConstants.smonth= zero+GlobalConstants.smonth;
		}
		GlobalConstants.sday = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if(GlobalConstants.sday.length()>1)
		{
			
		}
		else
		{
			GlobalConstants.sday= zero+GlobalConstants.sday;
		}
		
		GlobalConstants.shour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		if(GlobalConstants.shour.length()>1)
		{
			
		}
		else
		{
			GlobalConstants.shour= zero+GlobalConstants.shour;
		}
		GlobalConstants.sminute= String.valueOf(c.get(Calendar.MINUTE));
		if(GlobalConstants.sminute.length()>1)
		{
			
		}
		else
		{
			GlobalConstants.sminute= zero+GlobalConstants.sminute;
		}
	/*	GlobalConstants.sseconds= String.valueOf(c.get(Calendar.SECOND));
		if(GlobalConstants.sseconds.length()>1)
		{
			
		}
		else
		{
			GlobalConstants.sseconds= zero+GlobalConstants.sseconds;
		}*/
		
		String timestamp = GlobalConstants.syear+"-"+
			GlobalConstants.smonth+"-"+GlobalConstants.sday+" "+
			GlobalConstants.shour+":"+GlobalConstants.sminute+":"+"00";
		
		System.out.println("timestamp"+timestamp);
		
		return timestamp;
	}
	
	public static String getDate()
	{
		Calendar c = Calendar.getInstance();
		GlobalConstants.year = c.get(Calendar.YEAR);
		GlobalConstants.month = c.get(Calendar.MONTH)+1;
		GlobalConstants.day = c.get(Calendar.DAY_OF_MONTH);
		
		
	
		System.out.println("Month"+getMonth(c.get(Calendar.MONTH)));
		
		String date = GlobalConstants.year+"-"+GlobalConstants.month+"-"+GlobalConstants.day;
		
		System.out.println("date"+date);
		
		return date;
	}
	/**
	 * To file.
	 *
	 * @param bm the bm
	 * @return the string
	 * @throws IOException 
	 */
	
	public static String toFile(Bitmap bm) throws IOException {
		String filePath;
		filePath = "/sdcard/2.jpg";
		File f = new File(filePath);
		try {
			f.createNewFile();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 18, bos);
			byte[] bitmapdata = bos.toByteArray();
			FileOutputStream fos = null;

			fos = new FileOutputStream(f);
			fos.write(bitmapdata);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bm = null;
		return filePath;
	}
	public static String toJPEGFile(Bitmap bm) throws IOException {
		// For SDCard.
		// filePath = "/sdcard/2.jpg";
		// For Internal Storage.
		String filePath;
		byte[] bitmapdata=null;
		FileOutputStream fos = null;
		filePath = Environment.getDataDirectory() + "/data/com.topanga/image_"
				+ new Date().getTime() + ".jpeg";
		File f = new File(filePath);
		try {
			f.createNewFile();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 100, bos);
			 bitmapdata = bos.toByteArray();
			
			fos = new FileOutputStream(f);
			fos.write(bitmapdata);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bm.recycle();
		bm = null;
		fos.flush();
		fos.close();
		bitmapdata=null;
		return filePath;
	}
	
	public static  String getRealPathFromURI(Uri contentUri,Context mContext) {
		String path ="";
	    String[] proj = { MediaStore.Images.Media.DATA };
	    CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
	    Cursor cursor = loader.loadInBackground();
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    path = cursor.getString(column_index);
	    cursor.close();
	    loader = null ;
	    cursor= null ;
	    proj = null ; 
	    System.gc();
	    return path ;
	}
	
	public static  void setPaddings(View v)
	{
		v.setPadding(5, 15, 5, 15);
	}
	
	/**
	 * To file.
	 *
	 * @param uriVideo the uri video
	 * @return the string
	 */
	public static String toFile(Uri uriVideo) {
		String filePath;
		
		/*File sdCard = Environment.getExternalStorageDirectory();
		
		File dir = new File (sdCard.getAbsolutePath() +"/Topanga");
		dir.mkdirs();*/
		
		
		filePath = Environment.getDataDirectory() + "/data/com.topanga/video_"
				+ new Date().getTime() + ".mp3";
		File f = new File(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis;
		try {
			f.createNewFile();
			fis = new FileInputStream(new File(uriVideo.getPath()));
			byte[] buf = new byte[1024];
			int n;
			while (-1 != (n = fis.read(buf)))
				baos.write(buf, 0, n);
			byte[] videoBytes = baos.toByteArray();
			FileOutputStream fos = null;
			fos = new FileOutputStream(f);
			fos.write(videoBytes);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filePath;
	}
	
	public static String convertBase64VideoToFile(String base64)
	{
		String filePath = null;
		byte []imageAsBytes=null;
		try
		{
			imageAsBytes= Base64.decode(base64.getBytes(), Base64.DEFAULT);
			
			File imagefile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Topanga/video_mynote"
					/*+ new Date().getTime() +*/+ ".mp4");
		    File dir = new File(imagefile.getParent());
		    if (!dir.exists()) {
		        dir.mkdirs();
		    }
		    FileOutputStream fos = new FileOutputStream(imagefile);
		    fos.write(imageAsBytes);
		    fos.flush();
		    fos.close();
		    
		    filePath=imagefile.toString();
		}
		catch(Exception e)
		{
		}
			return filePath;
	}
	
	/**
	 * Check email is valid or not.
	 *
	 * @param email address typed
	 * @return true, if vaild email
	 */
	public static boolean checkValidEmail(String email)
	{
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{1,4}$";
		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email.toString());
		if (matcher.matches())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Gets the device serial number. If device is below gingerbread, imei will use else serial number
	 *
	 * @param context the context
	 * @return the device id
	 */
	public static String getDeviceID(Context context)
	{
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			if(telephonyManager!=null)
			{
			    return telephonyManager.getDeviceId();
			}
			else
			{
				// return android.os.Build.SERIAL;
				return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
			}
	}
	
	/**
	 * Gets the device model.
	 *
	 * @return the device model
	 */
	public static String getDeviceModel()
	{
		return  android.os.Build.MODEL;
	}
	
	/**
	 * Gets the device name.
	 *
	 * @return the device name
	 */
	public static String getDeviceName()
	{
		return android.os.Build.MANUFACTURER;
	}
	
	/**
	 * Gets the product name.
	 *
	 * @return the product name
	 */
	public static String getProductName()
	{
		return android.os.Build.PRODUCT;
	}
	
	/**
	 * File to data.
	 *
	 * @param path the path
	 * @return the string
	 */
	public static String FileToData(String path)
	{
		File file = new File(path);
		byte[] imageBufferBytes = null;
		String   imageBufferString = null;
		FileInputStream fis =null;
		 int fileSize=0;
		 int readCount=0;
		try{
		   fis = new FileInputStream(file);
		   fileSize = fis.available();
		   Log.e("", "File Size"+fileSize);
		  imageBufferBytes = new byte[fileSize];
		   if(fileSize>0)
		   {
			   readCount= fis.read(imageBufferBytes, 0, fileSize);
		        while(readCount < fileSize)
		        {
		            readCount+=fis.read(imageBufferBytes, readCount,(fileSize-readCount));
		        }
		   }
		  fis.close();
		  imageBufferString= Base64.encodeToString(imageBufferBytes, Base64.DEFAULT);
		  return imageBufferString;
		}
		catch (Exception e)
		{
			
		}
		return "";
	}
	
	public static String FileToDataNew(String path)
	{
		File file = new File(path);
		DataOutputStream dos = null;
		byte[] imageBufferBytes = null;
		String   imageBufferString = null;
		FileInputStream fis =null;
		 int fileSize=0;
		 int readCount=0;
		 int maxBufferSize = 1 * 1024 * 1024;
		try{
		   fis = new FileInputStream(file);
		   fileSize = fis.available();
		   Log.e("", "File Size"+fileSize);
		   fileSize= Math.min(fileSize, maxBufferSize);
		   Log.e("", "File Size"+fileSize);
		   imageBufferBytes = new byte[fileSize];
		   if(fileSize>0)
		   {
			   readCount= fis.read(imageBufferBytes, 0, fileSize);
		       
		        
		        while (readCount > 0) {
		        	  dos.write(imageBufferBytes, 0, fileSize);
		        	  fileSize = fis.available();
		        	  fileSize= Math.min(fileSize, maxBufferSize);
		        	   readCount= fis.read(imageBufferBytes, 0, fileSize);
		        	  }
		        dos.write(imageBufferBytes);
		        
		   }
		  fis.close();
		  imageBufferString= Base64.encodeToString(imageBufferBytes, Base64.DEFAULT);
		  return imageBufferString;
		}
		catch (Exception e)
		{
			
		}
		return "";
	}
	
	
	
	
	
	/**
	 * Gets the month.
	 *
	 * @param month the month
	 * @return the month
	 */
	public static String getMonth(int month) 
	{
	    return new DateFormatSymbols().getMonths()[month];
	}
	/**
	 * Password validation. checking contains length, uppercase,lowercase and digits
	 *
	 * @param mPassword the m password
	 * @return true, if successful
	 */
	public static boolean passwordValidation(String mPassword)
	{
		if(mPassword.length()<8 || mPassword.length()>12)
		{
			return false ;
		}
		boolean isUpper=false;
		boolean isLower=false;
		boolean isDigit=false;
		for (char c : mPassword.toCharArray())
		{
		      if (Character.isUpperCase(c))
		      {
		    	  isUpper= true;
		      }
		      else if (Character.isLowerCase(c))
		      {
		    	  isLower= true;
		      } 
		      else if (Character.isDigit(c))
		      {
		    	  isDigit= true;
		      }
		 }
		if(isUpper&&isDigit&&isLower)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	/**
	 * Hide the key board.
	 *
	 * @param context the context
	 * @param v the v
	 */
	public static void hideKeyBoard(Context context,View v)
	{
		 InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	 /**
 	 * Sets the list view height based on children.
 	 *
 	 * used in shop ui
 	 * @param listView the new list view height based on children
 	 */
 	public static void setListViewHeightBasedOnChildren(ListView listView,Context ctx) {
         ListAdapter listAdapter = listView.getAdapter(); 
         if (listAdapter == null) {
             // pre-condition
             return;
         }
         
         int totalHeight =0;
        
         Log.e("", "device height"+((Activity) ctx).getWindowManager().getDefaultDisplay().getHeight());
      //   int totalHeight=((Activity) ctx).getWindowManager().getDefaultDisplay().getHeight()-listView.getHeight();
         for (int i = 0; i < listAdapter.getCount(); i++) 
         {
             View listItem = listAdapter.getView(i, null, listView);
             listItem.measure(0, 20);
            
             
             totalHeight += listItem.getMeasuredHeight()+50;
         }
         listView.setScrollContainer(false);
         listView.setMinimumHeight(totalHeight);
         Log.e("", "List height"+listView.getHeight());
         ViewGroup.LayoutParams params = listView.getLayoutParams();
         params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
         listView.setLayoutParams(params);
         Log.e("", " height"+params.height);
         listView.requestLayout();
     }
	 
 	/**
 	 * Checks whether the alert service is running or not.
 	 *
 	 * @param ctx the ctx
 	 * @return true, if is my service running
 	 */
 	public static boolean isMyServiceRunning(Context ctx) 
	 {
		    ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		    {
		        if ("com.bworld.services.SMSService".equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
		}
 	
 	/**
 	 * Checks whether the date is Java Date(0)
 	 *
 	 * 
 	 * @return true, if date is 0
 	 * else
 	 * 
 	 * returns false
 	 */
 	public static boolean isJavaDateZero(String date)
 	{
 		if(date.contains("1/1/70"))
 		{
 			return true;
 		}
 		else
 		{
 			return false;
 		}
 	}
 	
 	public static void setVisiblityGone(View v)
 	{
 		v.setVisibility(View.GONE);
 	}
 	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
 	public  static Boolean checkInternetConnection(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		
		
		
		 NetworkInfo netInfo = connec.getActiveNetworkInfo();
		 if (netInfo != null && netInfo.isConnectedOrConnecting())
		 {
		        return true;
		 }
		else
		{
			return false;
		}
	}
 	
 	
 	
 	
 
 	
}
