package in.xplorelogic.inveck.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.activity.FragmentMilestone;
import in.xplorelogic.inveck.activity.MilestoneListActivity;
import in.xplorelogic.inveck.activity.PersonDetailActivity;
import in.xplorelogic.inveck.activity.ResumeActivity;
import in.xplorelogic.inveck.activity.VerifiActivity;
import in.xplorelogic.inveck.database.DatabaseHelper;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.jsonparser.JsonHelper;
import in.xplorelogic.inveck.models.LocationNameDAO;
import in.xplorelogic.inveck.models.Milestone;
import in.xplorelogic.inveck.models.Sample;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.ConnectivityReceiver;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.Listener;
import in.xplorelogic.inveck.utils.Utils;
import in.xplorelogic.inveck.utils.WebClient;

import static in.xplorelogic.inveck.utils.PaginationListener.PAGE_START;


public class MileStonesListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Milestone> data;
    List<Sample> data1 = new ArrayList<>();
    Milestone current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;
    int number = 1;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String sms_type = "";


    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String centerListResponse = "", callbackStatusResponse = "";
    boolean status;
    String message = "";
    String msg = "";
    String user_id = "";
    String batch_id = "";
    String s_id = "";
    int clickfalg = 0, milestone_Id;
    private static Listener mListener;
    JSONObject jsonObject, jsonLeadObj1;
    String stockListResponse = "", locationResponse = "";
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();


    // create constructor to innitilize context and data sent from MainActivity
    public MileStonesListAdpter(Context context, List<Milestone> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        MyHolder holder = new MyHolder(view);


        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);
        myHolder.clickMileStone.setTag(position);

        myHolder.milestone_user.setText(current.getMilestoneName());
        myHolder.milestone_user.setTag(position);

        myHolder.from_date.setText(current.getFromDate());
        myHolder.from_date.setTag(position);
        myHolder.to_date.setText(current.getToDate());
        myHolder.to_date.setTag(position);

        myHolder.cust_name.setText(current.getCustName());
        myHolder.cust_name.setTag(position);

        myHolder.milestone_status.setText(current.getInchargeStatus());
        myHolder.milestone_status.setTag(position);

        myHolder.sync.setTag(position);
        myHolder.synced.setTag(position);
        if (current.getSync_status() == 1) {
            myHolder.sync.setVisibility(View.GONE);
            myHolder.synced.setVisibility(View.VISIBLE);
        } else {
            myHolder.synced.setVisibility(View.GONE);
            myHolder.sync.setVisibility(View.VISIBLE);
        }
        myHolder.newt.setTag(position);
        myHolder.inprogress.setTag(position);
        myHolder.completed.setTag(position);
        if (current.getMileStoneStatus().equals("N")) {
            myHolder.inprogress.setVisibility(View.GONE);
            myHolder.completed.setVisibility(View.GONE);
            myHolder.newt.setVisibility(View.VISIBLE);

        } else if (current.getMileStoneStatus().equals("S")) {
            myHolder.completed.setVisibility(View.GONE);
            myHolder.newt.setVisibility(View.GONE);
            myHolder.inprogress.setVisibility(View.VISIBLE);

        } else if (current.getMileStoneStatus().equals("P")) {
            myHolder.newt.setVisibility(View.GONE);
            myHolder.inprogress.setVisibility(View.GONE);
            myHolder.completed.setVisibility(View.VISIBLE);
        }
        myHolder.clickMileStone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                String check = current.getInchargeStatus();
                if (check.equals("N")) {
                    Intent milIntent = new Intent(context, PersonDetailActivity.class);
                    prefEditor.putInt("milestone_Id", current.getMilestoneId());
                    prefEditor.putInt("sync_status", current.getSync_status());
                    prefEditor.putInt("client_Id", current.getCustId());
                    prefEditor.commit();
                    context.startActivity(milIntent);
                    return;
                } else {
                    Intent milIntent2 = new Intent(context, ResumeActivity.class);
                    prefEditor.putInt("milestone_Id", current.getMilestoneId());
                    prefEditor.putInt("sync_status", current.getSync_status());
                    prefEditor.putInt("client_Id", current.getCustId());
                    prefEditor.commit();
                    context.startActivity(milIntent2);
                }
            }
        });
        /*myHolder.clickMileStone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to save data for offline ?")
                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    milestone_Id = current.getMilestoneId();
                                    Utils.showSyncDialog(context);
                                    new getStockList().execute();

                                } catch (Exception ex) {
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                                }
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Saving Data");
                alert.show();
                return false;
            }
        });*/
        myHolder.sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                if (AppStatus.getInstance(context).isOnline()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to save data for offline ?")
                            .setCancelable(false)
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    try {
                                        milestone_Id = current.getMilestoneId();
                                        Utils.showSyncDialog(context);
                                        new getStockList().execute();

                                    } catch (Exception ex) {
                                        Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                                    }
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Saving Data");
                    alert.show();
                } else {
                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView milestone_user, from_date, to_date, cust_name, milestone_status;
        LinearLayout clickMileStone;
        ImageView newt, inprogress, completed, sync, synced;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            milestone_user = (TextView) itemView.findViewById(R.id.milestone_user);
            from_date = (TextView) itemView.findViewById(R.id.from_date);
            to_date = (TextView) itemView.findViewById(R.id.to_date);
            cust_name = (TextView) itemView.findViewById(R.id.cust_name);
            milestone_status = (TextView) itemView.findViewById(R.id.milestone_status);
            clickMileStone = (LinearLayout) itemView.findViewById(R.id.clickMileStone);
            newt = (ImageView) itemView.findViewById(R.id.newt);
            inprogress = (ImageView) itemView.findViewById(R.id.inprogress);
            completed = (ImageView) itemView.findViewById(R.id.completed);
            sync = (ImageView) itemView.findViewById(R.id.sync);
            synced = (ImageView) itemView.findViewById(R.id.synced);
        }

    }

    /**
     * Sort shopping list by name descending
     */
    public void sortByNameDesc() {
        Comparator<Milestone> comparator = new Comparator<Milestone>() {

            @Override
            public int compare(Milestone object1, Milestone object2) {
                return object2.getMilestoneName().compareToIgnoreCase(object1.getMilestoneName());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }
//Sort shopping list by name ascending

    public void sortByNameAsc() {
        Comparator<Milestone> comparator = new Comparator<Milestone>() {

            @Override
            public int compare(Milestone object1, Milestone object2) {
                return object1.getMilestoneName().compareToIgnoreCase(object2.getMilestoneName());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    public void sortByCoustNameDesc() {
        Comparator<Milestone> comparator = new Comparator<Milestone>() {

            @Override
            public int compare(Milestone object1, Milestone object2) {
                return object2.getCustName().compareToIgnoreCase(object1.getCustName());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }


    public void sortByCoustNameAsc() {
        Comparator<Milestone> comparator = new Comparator<Milestone>() {

            @Override
            public int compare(Milestone object1, Milestone object2) {
                return object1.getCustName().compareToIgnoreCase(object2.getCustName());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }


    public void sortByStartDateDesc() {
        Comparator<Milestone> comparator = new Comparator<Milestone>() {

            @Override
            public int compare(Milestone object1, Milestone object2) {
                return object2.getFromDate().compareToIgnoreCase(object1.getFromDate());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }


    public void sortByStartDateAsc() {
        Comparator<Milestone> comparator = new Comparator<Milestone>() {

            @Override
            public int compare(Milestone object1, Milestone object2) {
                return object1.getFromDate().compareToIgnoreCase(object2.getFromDate());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    //
    private class getStockList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            stockListResponse = serviceAccess.SendHttpGet(Config.URL_MILESTONEDETAILSALL + "/" + Integer.parseInt(preferences.getString("inventry_user_id", "")) + "/" + milestone_Id, jsonLeadObj);
            Log.i("resp", stockListResponse);
            if (isJSONValid(stockListResponse)) {
                try {
                    JsonHelper jsonHelper = new JsonHelper();
                    data1 = jsonHelper.parseOfflineMiletonesList(stockListResponse);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {


                return null;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            Utils.dismissLoadingDialog();
            if (data1.size() > 0) {
                long id1 = databaseQueryClass.UpdateUpdateSyncOfflineStatus(milestone_Id);
                mListener.messageReceived("hello");
                new getStockLocationList().execute();
            }
        }
    }

    private class getStockLocationList extends AsyncTask<Void, Void, Void> {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            jsonLeadObj1 = new JSONObject() {
                {
                    try {
                        //  put("user_id", "" + preferences.getInt("user_id", 0));
                        put("milestoneId", preferences.getInt("mileStoneId", 0));


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            locationResponse = serviceAccess.SendHttpPost(Config.URL_STOCKLOCATION, jsonLeadObj1);
            Log.i("resp", locationResponse);
            if (isJSONValid(locationResponse)) {
                try {
                    JSONArray LeadSourceJsonObj = new JSONArray(locationResponse);
                    long id = -1;
                    for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                        JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                        // nameDAOArrayList.add(new LocationNameDAO("", json_data.getString("stockLocation")));
                        Date today = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                        String dateToStr = format.format(today);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Constant.IMILESTONEID, preferences.getInt("mileStoneId", 0));
                        contentValues.put(Constant.LOCATIONNAME, json_data.getString("stockLocation"));
                        contentValues.put(Constant.SYNC_STATUS, 1);
                        contentValues.put(Constant.CREATE_AT, dateToStr);
                        try {
                            id = sqLiteDatabase.insertOrThrow(Constant.TABLE_STOCKLOCATION, null, contentValues);
                        } catch (SQLiteException e) {
                            //Logger.d("Exception: " + e.getMessage());
                            //Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            //sqLiteDatabase.close();
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {


                return null;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

        }
    }

    protected boolean isJSONValid(String callReoprtResponse2) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(callReoprtResponse2);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(callReoprtResponse2);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static void bindListener(Listener listener) {
        mListener = listener;
    }

}
