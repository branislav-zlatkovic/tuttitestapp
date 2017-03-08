package net.pe3.tuttitestapp.ui;

import android.app.ProgressDialog;
import android.content.Context;

public class UiUtils {

    /**
     * Construct a progress dialog containing indeterminate spinner and a short message
     * @param context
     * @param msg to be shown next to the spinner
     * @return created dialog
     */
    public static ProgressDialog createProgressDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }
}
