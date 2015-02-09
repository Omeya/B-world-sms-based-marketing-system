package com.bworld.manager;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONStringer;

import android.content.Context;
import android.util.Log;

import com.bworld.models.ContactsModel;

public class JsonIntegration {

	/*public static String writeJSONContent(CheckBoxifiedContacts model) {
		  JSONObject object = new JSONObject();
		  try {
		    object.put("name", model.getText());
		    object.put("number", model.getMobileno());
		   
		    
		  } catch (JSONException e) {
		    e.printStackTrace();
		  }
		  System.out.println(object);
		  return object.toString();
		} 
	*/
	

	public static String uploadContactsFromAdapter(Context context,ArrayList<ContactsModel> array) 
	{
		Log.e("", "arr size"+array.size());
		Log.e("", "arr name"+array.get(0).getName());
		ContactsModel cm = new ContactsModel();
		int adapter_size = array.size();
		JSONStringer js = new JSONStringer();
		if (adapter_size > 0)
		{
			try {
				js.object().key("contacts");
				js.array();
				for (int index = 0; index < adapter_size; index++) 
				{
					cm.setName(array.get(index).getName());
					cm.setPhoneNumber(array.get(index).getPhoneNumber());
				//	Log.e("", "name"+check_boxified_contacts.getName());
						if (cm.getName() == null || cm.getName().equals(""))
						{
							js.object().key("name").value("");
							
							Log.e("", "name null");
						} 
						else
						{
							js.object().key("name").value(
									cm.getName());
						}
						if (cm.getPhoneNumber() == null || cm.getPhoneNumber().equals(""))
						{
							js.key("number").value("");
							Log.e("", "number null");
						} 
						else
						{
							js.key("number").value(
									cm.getPhoneNumber());
						}
						js.endObject();
				}
				js.endArray();
				js.endObject();
			//	Log.e("", "js"+js.toString());
			} catch (JSONException e)
			{
				e.printStackTrace();
				Log.e("", "error"+e.toString());
			}
			
			
		}
		return js.toString();
	}
}
