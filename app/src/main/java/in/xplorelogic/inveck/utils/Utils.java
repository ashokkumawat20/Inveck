package in.xplorelogic.inveck.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Utils {

    private static ProgressDialog progress=null;
  static String addressFromLatLong = null;
    static JSONObject jsonObject;

    static List<Address> addresstList = null;
    static ArrayList<String> addressArrayList = new ArrayList<>();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideSoftKeyboardOnRequestFocus(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showLoadingDialog(Context context) {
        //if (progress == null) {
        progress = new ProgressDialog(context);
        progress.setMessage("Please wait uploading image...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // }

    }
    public static void showSyncDialog(Context context) {
        //if (progress == null) {
        progress = new ProgressDialog(context);
        progress.setMessage("Please wait Syncing Data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // }

    }
    public static void showDeleteLoadingDialog(Context context) {
        //if (progress == null) {
        progress = new ProgressDialog(context);
        progress.setMessage("Please wait deleting image...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // }

    }
    public static void showListLoadingDialog(Context context) {
        //if (progress == null) {
        progress = new ProgressDialog(context);
        progress.setMessage("Please wait Loading data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // }

    }
    public static void dismissLoadingDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }




}
