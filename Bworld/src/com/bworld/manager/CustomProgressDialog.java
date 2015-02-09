/**
 * 
 */
package com.bworld.manager;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.bworld.R;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomProgressDialog.
 *
 * @author jiju.ki
 */
public class CustomProgressDialog extends Dialog {

    /**
     * Show.
     *
     * @param context the context
     * @param cancelable the cancelable
     * @return the custom progress dialog
     */
    public static CustomProgressDialog show(Context context,boolean cancelable) {
    	CustomProgressDialog dialog = new CustomProgressDialog(context);        
        dialog.setCancelable(cancelable);
       
        /* The next line will add the ProgressBar to the dialog. */
        //dialog.addContentView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 
        View v=LayoutInflater.from(context).inflate(R.layout.progress_layout, null);
        dialog.addContentView(v, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));    
        return dialog;
    }

    /**
     * Instantiates a new custom progress dialog.
     *
     * @param context the context
     */
    public CustomProgressDialog(Context context) {
        super(context, R.style.NewDialog);
    }
}