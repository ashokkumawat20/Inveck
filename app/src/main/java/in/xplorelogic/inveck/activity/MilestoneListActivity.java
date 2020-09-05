package in.xplorelogic.inveck.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.xplorelogic.inveck.R;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


import in.xplorelogic.inveck.adapter.MileStonesListAdpter;
import in.xplorelogic.inveck.database.DatabaseHelper;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.jsonparser.JsonHelper;
import in.xplorelogic.inveck.models.Milestone;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.ConnectivityReceiver;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.Listener;
import in.xplorelogic.inveck.utils.WebClient;
import in.xplorelogic.inveck.view.EntryDataLineItemView;
import in.xplorelogic.inveck.view.UpdatePasswordEntryView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MilestoneListActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ProgressDialog mProgressDialog;


    EditText search_milestone;
    String user_id;
    JSONObject jsonLeadObj;
    String mileStoneListResponse = "";
    //
    List<Milestone> data;
    MileStonesListAdpter mileStonesListAdpter;
    private RecyclerView mList;
    JSONArray jsonArray;
    ImageView serach_hide, clear, adList;
    private boolean mAscendingOrder[] = {true, true, true, true};
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView logout, sortData, filterData;
    RelativeLayout footer;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String TAG = MilestoneListActivity.class.getSimpleName();
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_list);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        String string = preferences.getString("inventry_user_id", "");
        this.user_id = string;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.menu_drawer));
        this.search_milestone = (EditText) findViewById(R.id.search);
        serach_hide = (ImageView) findViewById(R.id.serach_hide);
        clear = (ImageView) findViewById(R.id.clear);
        sortData = (ImageView) findViewById(R.id.sortData);
        filterData = (ImageView) findViewById(R.id.filterData);
        mList = (RecyclerView) findViewById(R.id.listView);
        footer = (RelativeLayout) findViewById(R.id.footer);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        data = new ArrayList<>();
        data.clear();
        registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        data.addAll(databaseQueryClass.getAllStudent());
        if (VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
        }
        if (data.size() > 0) {


            mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, data);
            mList.setAdapter(mileStonesListAdpter);
            mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));


        } else {
            if (AppStatus.getInstance(MilestoneListActivity.this).isOnline()) {
                if (checkAndRequestPermissions()) {
                    new getListMilestone().execute();
                }
            } else {
                Toast.makeText(MilestoneListActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
            }
        }
        MileStonesListAdpter.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                data.clear();
                data.addAll(databaseQueryClass.getAllStudent());
                if (data.size() > 0) {
                    mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, data);
                    mList.setAdapter(mileStonesListAdpter);
                    mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));
                }
            }
        });
        addTextListener();
        serach_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                clear.setVisibility(View.VISIBLE);
                serach_hide.setVisibility(View.GONE);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_milestone.setText("");
                serach_hide.setVisibility(View.VISIBLE);
                clear.setVisibility(View.GONE);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

      /*  adList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAscendingOrder[0]) {
                    // Show items descending
                    mAscendingOrder[0] = false;
                    mileStonesListAdpter.sortByNameDesc();

                } else {
                    // Show items ascending
                    mAscendingOrder[0] = true;
                    mileStonesListAdpter.sortByNameAsc();

                }

                mAscendingOrder[1] = true;
                mAscendingOrder[2] = true;
            }
        });*/
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                data.addAll(databaseQueryClass.getAllStudent());
                if (data.size() > 0) {

                    mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, data);
                    mList.setAdapter(mileStonesListAdpter);
                    mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));

                }
                /*if (AppStatus.getInstance(MilestoneListActivity.this).isOnline()) {
                    if (checkAndRequestPermissions()) {
                        new getListMilestone().execute();
                    }
                } else {

                }*/
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    footer.setVisibility(View.GONE);

                } else if (dy > 0) {
                    footer.setVisibility(View.VISIBLE);
                }
            }
        });
        filterData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(MilestoneListActivity.this, filterData);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_filtermilstone, popup.getMenu());
                //   popup.getMenu().add(preferences.getString("quantity_for_contacts", ""));
                popup.show();
                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Toast.makeText(DisplayStudentEditPreActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("All")) {

                            mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, data);
                            mList.setAdapter(mileStonesListAdpter);
                            mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));
                            mileStonesListAdpter.notifyDataSetChanged();  // data set changed


                        } else if (item.getTitle().equals("New")) {

                            final List<Milestone> filteredList = new ArrayList<Milestone>();
                            if (data != null) {
                                if (data.size() > 0) {


                                    for (int i = 0; i < data.size(); i++) {
                                        String subject = data.get(i).getInchargeStatus().toLowerCase();
                                        if (subject.contains("n")) {
                                            filteredList.add(data.get(i));
                                        }
                                    }
                                }
                            }


                            mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, filteredList);
                            mList.setAdapter(mileStonesListAdpter);
                            mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));
                            mileStonesListAdpter.notifyDataSetChanged();  // data set changed


                        } else if (item.getTitle().equals("In Progress")) {
                            final List<Milestone> filteredList = new ArrayList<Milestone>();
                            if (data != null) {
                                if (data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        String subject = data.get(i).getInchargeStatus().toLowerCase();
                                        if (subject.contains("y")) {
                                            filteredList.add(data.get(i));
                                        }
                                    }
                                }
                            }


                            mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, filteredList);
                            mList.setAdapter(mileStonesListAdpter);
                            mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));
                            mileStonesListAdpter.notifyDataSetChanged();  // data set changed

                        } else if (item.getTitle().equals("Completed")) {
                            final List<Milestone> filteredList = new ArrayList<Milestone>();
                            if (data != null) {
                                if (data.size() > 0) {


                                    for (int i = 0; i < data.size(); i++) {
                                        String subject = data.get(i).getInchargeStatus().toLowerCase();
                                        if (subject.contains("c")) {
                                            filteredList.add(data.get(i));
                                        }
                                    }
                                }
                            }

                            mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, filteredList);
                            mList.setAdapter(mileStonesListAdpter);
                            mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));
                            mileStonesListAdpter.notifyDataSetChanged();  // data set changed
                        }


                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        sortData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(MilestoneListActivity.this, sortData);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_sortmilstone, popup.getMenu());
                //   popup.getMenu().add(preferences.getString("quantity_for_contacts", ""));
                popup.show();
                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Toast.makeText(DisplayStudentEditPreActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("Sort Milestone A to Z")) {

                            // Show items ascending
                            mAscendingOrder[0] = true;
                            mileStonesListAdpter.sortByNameAsc();

                        } else if (item.getTitle().equals("Sort Milestone Z to A")) {
                            if (mAscendingOrder[0]) {
                                // Show items descending
                                mAscendingOrder[0] = false;
                                mileStonesListAdpter.sortByNameDesc();

                            }


                        } else if (item.getTitle().equals("Sort Customer Name A to Z")) {
                            // Show items ascending
                            mAscendingOrder[0] = true;
                            mileStonesListAdpter.sortByCoustNameAsc();

                        } else if (item.getTitle().equals("Sort Customer Name Z to A")) {
                            if (mAscendingOrder[0]) {
                                // Show items descending
                                mAscendingOrder[0] = false;
                                mileStonesListAdpter.sortByCoustNameDesc();

                            }
                        } else if (item.getTitle().equals("Sort Start Date Min to High")) {
                            // Show items ascending
                            mAscendingOrder[0] = true;
                            mileStonesListAdpter.sortByStartDateAsc();

                        } else if (item.getTitle().equals("Sort Start Date High to Min")) {
                            if (mAscendingOrder[0]) {
                                // Show items descending
                                mAscendingOrder[0] = false;
                                mileStonesListAdpter.sortByStartDateDesc();

                            }
                        }

                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Profile:
                //  Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();
                if (AppStatus.getInstance(MilestoneListActivity.this).isOnline()) {
                    Intent intent = new Intent(MilestoneListActivity.this, UserDetailActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MilestoneListActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.Milestones:
                if (AppStatus.getInstance(MilestoneListActivity.this).isOnline()) {
                    if (checkAndRequestPermissions()) {
                        data.clear();
                        new getListMilestone().execute();
                    }
                } else {
                    Toast.makeText(MilestoneListActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();

                    data.clear();
                    data.addAll(databaseQueryClass.getAllStudent());
                    if (data.size() > 0) {
                        mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, data);
                        mList.setAdapter(mileStonesListAdpter);
                        mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));

                    }
                }

                return true;
            case R.id.ResetPassword:
                if (AppStatus.getInstance(MilestoneListActivity.this).isOnline()) {
                    UpdatePasswordEntryView updatePasswordEntryView = new UpdatePasswordEntryView();
                    updatePasswordEntryView.show(getSupportFragmentManager(), "updatePasswordEntryView");
                } else {

                    Toast.makeText(MilestoneListActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.clearDatabase:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MilestoneListActivity.this);
                builder1.setMessage("Do you want to Clear Offline Database ?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (AppStatus.getInstance(MilestoneListActivity.this).isOnline()) {
                                    if (checkAndRequestPermissions()) {
                                        registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                                        deleteDatabase("inveckdb.db");
                                        new getListMilestone().execute();
                                    }
                                } else {
                                    Toast.makeText(MilestoneListActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();


                            }
                        });

                //Creating dialog box
                AlertDialog alert1 = builder1.create();
                //Setting the title manually
                alert1.setTitle("Database");
                alert1.show();
                return true;
            case R.id.Logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MilestoneListActivity.this);
                builder.setMessage("Do you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                                prefEditor.remove("inventry_user_id");
                                prefEditor.commit();
                                Intent intent = new Intent(MilestoneListActivity.this, SplashScreen.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();


                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Logout");
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //
    public void addTextListener() {

        search_milestone.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                clear.setVisibility(View.VISIBLE);
                serach_hide.setVisibility(View.GONE);

                query = query.toString().toLowerCase();
                final List<Milestone> filteredList = new ArrayList<Milestone>();
                if (data != null) {
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {

                            String subject = data.get(i).getCustName().toLowerCase();
                            String tag = data.get(i).getMilestoneName().toLowerCase();
                            String msg_txt = data.get(i).getFromDate().toLowerCase();
                            if (subject.contains(query)) {
                                filteredList.add(data.get(i));
                            } else if (tag.contains(query)) {

                                filteredList.add(data.get(i));
                            } else if (msg_txt.contains(query)) {

                                filteredList.add(data.get(i));
                            }
                        }
                    }
                }

                mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));
                mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, filteredList);
                mList.setAdapter(mileStonesListAdpter);
                mileStonesListAdpter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    //
    private class getListMilestone extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //  mProgressDialog = new ProgressDialog(MilestoneListActivity.this);
            // Set progressdialog title
            // mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            //  mProgressDialog.setMessage("Loading Milestones...");
            //  mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //   mProgressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        //  put("emailAddress", MilestoneListActivity.this.user_id);
                        put("userId", MilestoneListActivity.this.user_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            mileStoneListResponse = serviceAccess.SendHttpPost(Config.URL_MILESTONES, jsonLeadObj);
            Log.i("resp", mileStoneListResponse);

            if (mileStoneListResponse.compareTo("") != 0) {
                if (isJSONValid(mileStoneListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {


                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseMileStonesList(mileStoneListResponse);
                                jsonArray = new JSONArray(mileStoneListResponse);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            // mProgressDialog.dismiss();
            if (data.size() > 0) {

                mileStonesListAdpter = new MileStonesListAdpter(MilestoneListActivity.this, data);
                mList.setAdapter(mileStonesListAdpter);
                mList.setLayoutManager(new LinearLayoutManager(MilestoneListActivity.this));

            } else {
                // Close the progressdialog
                //   mProgressDialog.dismiss();

            }
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

    private boolean checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        if (AppStatus.getInstance(MilestoneListActivity.this).isOnline()) {
                            new getListMilestone().execute();
                        } else {

                            Toast.makeText(MilestoneListActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:in.xplorelogic.inveck")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    //
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
