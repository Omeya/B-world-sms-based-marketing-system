package com.bworld.Adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.bworld.R;
import com.bworld.Activities.SBM.Contacts;
import com.bworld.models.ContactsModel;

public class ContactsSMSAdapter extends BaseAdapter implements Filterable{

	private Context mContext;
	private LayoutInflater mInflater;
	ArrayList<ContactsModel> arrayList;
	private ArrayList<ContactsModel> array;
	private TextView mText;
	private TextView mTxtNumber;
	private CheckBox mCheckBox;
	public Filter filter;
	public ArrayList<ContactsModel> cmodel		=null;
	public ArrayList<ContactsModel> cmodelfiltered;
	private ContactsSMSAdapter adapter;
	private ListView mList;
	
	public ContactsSMSAdapter(Context context,ArrayList<ContactsModel> arrayModel,ListView list)
	{
		mList           =list;
		array			=arrayModel;
		arrayList		=arrayModel;
		mContext		=context;
		mInflater		= LayoutInflater.from(mContext);
	}
	
	public ArrayList<ContactsModel> getAllItems() {
		return array;
	}
	
	public ArrayList<ContactsModel> getmItems() {
		return arrayList;
	}
	public void setmItems(ArrayList<ContactsModel> mItems) {
		this.arrayList =  mItems;
	}
	public void setListItems(ArrayList<ContactsModel> lit) {
		arrayList = lit;
	}
//	@Override
	public int getCount()
	{
		Log.e("", "Count inside adapter"+arrayList.size());
		return arrayList.size();
	}
//	@Override
	public Object getItem(int arg0) 
	{
		return arrayList.get(arg0);
	}
	public void setItem(int position,ContactsModel checkboxifiedcontacts) {
		arrayList.set(position, checkboxifiedcontacts);
	}
	public void setChecked(boolean value, int position) {
		arrayList.get(position).setChecked(value);
	}
	public void selectAll() {
		for (ContactsModel model : arrayList)
			model.setChecked(true);
		//this.notifyDataSetInvalidated();
		setAdapter();
	}
	
	public void deselectAll() {
		for (ContactsModel model : arrayList)
			model.setChecked(false);
	//	this.notifyDataSetInvalidated();
		setAdapter();
	}
	
	private void setAdapter() {
		adapter = new ContactsSMSAdapter(mContext,arrayList,mList);
		mList.setAdapter(adapter);
		this.notifyDataSetChanged();
		
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		 final ContactsModel mCheckBoxText=(ContactsModel)this.getItem(position);
			
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			View view = (View) inflate.inflate(R.layout.contacts_sms_item, null);
			
			TextView txtname = (TextView) view.findViewById(R.id.txtName);
			TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);
			txtname.setText(arrayList.get(position).getName());
			txtNumber.setText(arrayList.get(position).getPhoneNumber());
			mCheckBox = (CheckBox) view.findViewById(R.id.chk_item);
			if (mCheckBoxText. getChecked()) {			
				mCheckBox.setButtonDrawable(R.drawable.checkbox_on_background);
			}
			else
			{
				mCheckBox.setButtonDrawable(R.drawable.checkbox_off_background);
				System.out.println("checking touchtesteeeeeeee");
			}
			mCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					if (mCheckBoxText.getChecked()) 
					{
						mCheckBoxText.setChecked(false);
						((CheckBox)v).setButtonDrawable(R.drawable.checkbox_off_background);
						System.out.println("checking touch");
						Contacts.chkAllContacts.setChecked(false);
						Contacts.chkAllContacts.setButtonDrawable(R.drawable.checkbox_off_background);
					} 
					else 
					{
						mCheckBoxText.setChecked(true);
						((CheckBox)v).setButtonDrawable(R.drawable.checkbox_on_background);
					}
					
				}
			});
			return view;
		
		
	}
	
	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter =  new ArrayFilter();
        }
		return  filter;
	}
	public class ArrayFilter extends android.widget.Filter
	{
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results= new FilterResults();
			 if (cmodel == null) 
			 {
	                synchronized (this) {
	                	cmodel = new ArrayList<ContactsModel>(arrayList);
	                }
	         }
			 if (constraint == null || constraint.length() == 0) 
			 {
	                synchronized (this) {
	                    ArrayList<ContactsModel> list = new ArrayList<ContactsModel>(cmodel);
	                    results.values = list;
	                    results.count = list.size();
	                }
	            }
			 else {
	                String prefixString = constraint.toString().toLowerCase();

	                final ArrayList<ContactsModel> values = cmodel;
	                final int count = values.size();

	                final ArrayList<ContactsModel> newValues = new ArrayList<ContactsModel>(count);

	                for (int i = 0; i < count; i++) 
	                {
	                    final ContactsModel value = values.get(i);
	                    final String valueText = value.getName().toString().toLowerCase();
	                    if (valueText.startsWith(prefixString)) 
	                    {
	                        newValues.add(value);
	                    } 
	                    else {
	                        final String[] words = valueText.split(" ");
	                        final int wordCount = words.length;

	                        for (int k = 0; k < wordCount; k++) {
	                            if (words[k].startsWith(prefixString)) {
	                                newValues.add(value);
	                                break;
	                            }
	                        }
	                    }
	                }

	                results.values = newValues;
	                results.count = newValues.size();
	            }
	      return results;
		}
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) {
			arrayList = (ArrayList<ContactsModel>) results.values;
		    if (results.count > 0) 
		    {
                notifyDataSetChanged();
            } 
            else 
            {
                notifyDataSetInvalidated();
            }

		}	
			
	}

}
