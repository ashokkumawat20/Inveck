package in.xplorelogic.inveck.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.xplorelogic.inveck.activity.TakeImagesActivity;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConnectivityReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;
    //context and database helper object
    private Context context;
    //database helper object
    int totalsize;
    int countmu = 0;
    int countud = 0;
    int countusod = 0;
    int countftosheet = 0;
    int countinssertftosheet = 0;
    int countinssertimages = 0;
    int stockLocationInsert = 0;
    String updateQuantityResponse = "";
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {

        this.context = context;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }


        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

//getting all the unsynced users
                synchronized (context) {
                    Cursor cursor = databaseQueryClass.getUnsyncedMilestoneUpdate();
                    totalsize = cursor.getCount();
                    if (cursor.moveToFirst()) {
                        do {
                            //calling the method to save the unsynced name to MySQL
                            saveUser(cursor.getInt(cursor.getColumnIndex(Constant.M_ID)), cursor.getInt(cursor.getColumnIndex(Constant.INPUTQTY)), cursor.getString(cursor.getColumnIndex(Constant.REMARK)), cursor.getString(cursor.getColumnIndex(Constant.ISFLOORTOSHEET)));
                            countmu++;
                        } while (cursor.moveToNext());
                    }
                    if (totalsize == 0) {
                        userdetailsPre();
                    }
                }
            }
        }
    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) MyApp.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    private void userdetailsPre() {
        Cursor cursor = databaseQueryClass.getSyncUserPerDeaUpdate();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserDPre(cursor.getInt(cursor.getColumnIndex(Constant.MILESTONID)), cursor.getString(cursor.getColumnIndex(Constant.FULLNAME)), cursor.getString(cursor.getColumnIndex(Constant.DESIGNATION)), cursor.getString(cursor.getColumnIndex(Constant.EMAIL)), cursor.getString(cursor.getColumnIndex(Constant.CONTACTNO)), cursor.getInt(cursor.getColumnIndex(Constant.CREATEDBY)));
                countud++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            userdetailsSignOffPre();
        }
    }


    private void saveUser(int anInt, int cursorInt, String remark, String ftosheet) {
        JSONObject json = new JSONObject();

        try {
            json.put("stockId", anInt);
            json.put("quantity", cursorInt);
            json.put("remark", remark);
            json.put("floortosheet_status", ftosheet);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebClient serviceAccess = new WebClient();
        Log.i("json", "json" + json);
        updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_UPDATESTOCKQUANTITY, json);
        databaseQueryClass.UpdateSyncStatusQuantity(anInt);
        Log.i("resp", updateQuantityResponse);
        if (countmu == totalsize) {
            userdetailsPre();
        }
    }

    private void saveUserDPre(int anInt, String string, String string1, String string2, String string3, int anInt1) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", 0);
            json.put("mileStonId", anInt);
            json.put("fullName", string);
            json.put("designation", string1);
            json.put("email", string2);
            json.put("contactNo", string3);
            json.put("createdBy", anInt1);
            json.put("createdDate", "2020-06-02T15:35:09.202Z");
            json.put("appStatus", "");
            json.put("delStatus", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebClient serviceAccess = new WebClient();
        Log.i("json", "json" + json);
        updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_MILESTONEINCHARGE, json);
        databaseQueryClass.UpdateSyncUserPerDeaUpdate(anInt);
        Log.i("resp", updateQuantityResponse);
        if (countud == totalsize) {
            userdetailsSignOffPre();
        }
    }

    private void userdetailsSignOffPre() {
        Cursor cursor = databaseQueryClass.getSyncUserSOPerDeaUpdate();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserDSignOffPre(cursor.getInt(cursor.getColumnIndex(Constant.MILESTONID)), cursor.getString(cursor.getColumnIndex(Constant.FULLNAME)), cursor.getString(cursor.getColumnIndex(Constant.DESIGNATION)), cursor.getString(cursor.getColumnIndex(Constant.EMAIL)), cursor.getString(cursor.getColumnIndex(Constant.CONTACTNO)), cursor.getInt(cursor.getColumnIndex(Constant.CREATEDBY)));
                countusod++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            updateFtoSheetSync();
        }
    }

    private void saveUserDSignOffPre(int anInt, String string, String string1, String string2, String string3, int anInt1) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", 0);
            json.put("mileStonId", anInt);
            json.put("fullName", string);
            json.put("designation", string1);
            json.put("email", string2);
            json.put("contactNo", string3);
            json.put("createdBy", anInt1);
            json.put("createdDate", "2020-06-02T15:35:09.202Z");
            json.put("appStatus", "");
            json.put("delStatus", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebClient serviceAccess = new WebClient();
        Log.i("json", "json" + json);
        updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_MILESTONESIGNOFF, json);
        databaseQueryClass.UpdateSyncUserSOPerDeaUpdate(anInt);
        Log.i("resp", updateQuantityResponse);
        if (countusod == totalsize) {
            updateFtoSheetSync();
        }
    }

    private void updateFtoSheetSync() {
        Cursor cursor = databaseQueryClass.getUnsyncedFtoSheetUpdate();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                updateFtoSheet(cursor.getInt(cursor.getColumnIndex(Constant.M_ID)));
                countftosheet++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            insertFtoSheetSync();
        }
    }

    private void updateFtoSheet(int anInt) {
        JSONObject json = new JSONObject();

        try {
            json.put("stockId", anInt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebClient serviceAccess = new WebClient();
        Log.i("json", "json" + json);
        updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_RESETSTOCKQUANTITY, json);
        databaseQueryClass.UpdateSyncStatusFtoSheet(anInt);
        Log.i("resp", updateQuantityResponse);
        if (countftosheet == totalsize) {
            insertFtoSheetSync();
        }
    }

    private void insertFtoSheetSync() {
        Cursor cursor = databaseQueryClass.getUnsyncedFtoSheetInsert();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                insertFtoSheet(cursor.getInt(cursor.getColumnIndex(Constant.ID)), cursor.getInt(cursor.getColumnIndex(Constant.CUSTOMERID)), cursor.getInt(cursor.getColumnIndex(Constant.MILE_STONEID)), cursor.getInt(cursor.getColumnIndex(Constant.ASSIGNUSERID)), cursor.getString(cursor.getColumnIndex(Constant.ITEM_NO)), cursor.getString(cursor.getColumnIndex(Constant.ITEM_NAME)), cursor.getInt(cursor.getColumnIndex(Constant.TOTAL_QUANTITY)), cursor.getString(cursor.getColumnIndex(Constant.STOCK_LOCATION)), cursor.getString(cursor.getColumnIndex(Constant.ASSIGNUSEREMAIL)));
                countinssertftosheet++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {

            insertImagesSync();
        }
    }

    private void insertFtoSheet(int M_ID, int anInt, int cursorInt, int i, String string, String cursorString, int anInt1, String s, String string1) {
        JSONObject json = new JSONObject();

        try {
            json.put("customerId", anInt);
            json.put("milestoneId", cursorInt);
            json.put("assignUserId", i);
            json.put("item_No", string);
            json.put("item_Name", cursorString);
            json.put("total_Quantity", anInt1);
            json.put("stock_Location", s);
            json.put("assignUserEmail", string1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebClient serviceAccess = new WebClient();
        Log.i("json", "json" + json);
        updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_MILESTONEFLOORTOSHEETSTOCK, json);
        databaseQueryClass.UpdateSyncStatusFtoSheetInsert(M_ID, Integer.parseInt(updateQuantityResponse));
        Log.i("resp", updateQuantityResponse);
        if (countinssertftosheet == totalsize) {

            insertImagesSync();
        }
    }

    private void insertImagesSync() {
        Cursor cursor = databaseQueryClass.getUnsyncedImagesInsert();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                uploadToServer(cursor.getInt(cursor.getColumnIndex(Constant.ID)), cursor.getInt(cursor.getColumnIndex(Constant.STOCKID)), cursor.getInt(cursor.getColumnIndex(Constant.IMILESTONEID)), cursor.getString(cursor.getColumnIndex(Constant.IMAGEURI)), cursor.getString(cursor.getColumnIndex(Constant.IMAGENAME)));
                countinssertimages++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {

            insertStockLocationSync();
        }
    }

    public void uploadToServer(int id, int stockId, int mileStoneId, String filePath, String imageName) {

        String subs = stockId + "/" + mileStoneId + "/" + imageName;
        Retrofit retrofit = NetworkClient.getRetrofitClient(MyApp.context);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("fileStream", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call call = uploadAPIs.uploadImage(part, description, subs);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                databaseQueryClass.UpdateSyncStatusInsertImages(id);

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        if (countinssertimages == totalsize) {
            insertStockLocationSync();
        }
    }

    private void insertStockLocationSync() {
        Cursor cursor = databaseQueryClass.getSyncStockLocation();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveStockLocation(cursor.getInt(cursor.getColumnIndex(Constant.ID)), cursor.getInt(cursor.getColumnIndex(Constant.STOCKID)), cursor.getInt(cursor.getColumnIndex(Constant.MILE_STONEID)), cursor.getInt(cursor.getColumnIndex(Constant.QUANTITY)), cursor.getString(cursor.getColumnIndex(Constant.LOCATION)));
                stockLocationInsert++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            // userdetailsSignOffPre();
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            final String dateToStr = format.format(today);
            JSONObject json1 = new JSONObject();
            try {
                json1.put("userId", Integer.parseInt(preferences.getString("inventry_user_id", "")));
                json1.put("lastSyncDateTime", dateToStr);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + json1);
            updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_UPDATELASTSYNCUSER, json1);
            Log.i("resp", updateQuantityResponse);
        }
    }

    private void saveStockLocation(int id, int stockId, int mileStoneId, int quantity, String location) {
        JSONObject json = new JSONObject();
        try {
            json.put("stockId", stockId);
            json.put("mileStoneId", mileStoneId);
            json.put("quantity", quantity);
            json.put("location", location);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebClient serviceAccess = new WebClient();
        Log.i("json", "json" + json);
        updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_UPDATESTOCKLOCATION, json);
        databaseQueryClass.UpdateSyncStockLocation(id);
        Log.i("resp", updateQuantityResponse);
        if (stockLocationInsert == totalsize) {
            //userdetailsSignOffPre();
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            final String dateToStr = format.format(today);
            JSONObject json1 = new JSONObject();
            try {
                json1.put("userId", Integer.parseInt(preferences.getString("inventry_user_id", "")));
                json1.put("lastSyncDateTime", dateToStr);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("json", "json" + json1);
            updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_UPDATELASTSYNCUSER, json1);
            Log.i("resp", updateQuantityResponse);
        }
    }

}