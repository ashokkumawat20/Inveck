package in.xplorelogic.inveck.view;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.activity.FragmentMilestone;
import in.xplorelogic.inveck.activity.PersonDetailActivity;
import in.xplorelogic.inveck.adapter.LocationListAdpter;
import in.xplorelogic.inveck.database.DatabaseHelper;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.jsonparser.JsonHelper;
import in.xplorelogic.inveck.models.LocationListDAO;
import in.xplorelogic.inveck.models.LocationNameDAO;
import in.xplorelogic.inveck.models.Sample;
import in.xplorelogic.inveck.models.StudentListDAO;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.ConnectivityReceiver;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.Listener;
import in.xplorelogic.inveck.utils.WebClient;


public class EntryDataLineItemView extends DialogFragment {
    Context context;
    SharedPreferences preferences;
    Editor prefEditor;
    int count = 0;
    View registerView;
    EditText itemNameEdtTxt, itemNumberEdtTxt, qntyEdtTxt, itemLocationEdtTxt;
    ImageView back_arrow1;
    LinearLayout placeBtn;
    String location = "", itemName = "", itemNumber = "", loginResponse = "", studentResponse = "";
    int quantity = 0;
    boolean status;
    private static Listener mListener;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();
    DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
    ProgressDialog mProgressDialog;
    ArrayList<LocationNameDAO> nameDAOArrayList;

    Spinner spinnerLocation;
    String notesFlag = "0", locationResponse = "", locationListResponse = "";
    JSONObject jsonLeadObj1;
    JSONArray jsonArray;
    AutoCompleteTextView autoCompleteTextViewItem;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    public ArrayAdapter<StudentListDAO> studentListDAOArrayAdapter;
    StudentListDAO studentListDAO;
    public List<StudentListDAO> studentArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_enter_lineitems, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();

        itemNameEdtTxt = (EditText) registerView.findViewById(R.id.itemNameEdtTxt);
        itemNumberEdtTxt = (EditText) registerView.findViewById(R.id.itemNumberEdtTxt);
        qntyEdtTxt = (EditText) registerView.findViewById(R.id.qntyEdtTxt);
        itemLocationEdtTxt = (EditText) registerView.findViewById(R.id.itemLocationEdtTxt);
        placeBtn = (LinearLayout) registerView.findViewById(R.id.btnSend);
        back_arrow1 = (ImageView) registerView.findViewById(R.id.back_arrow1);
        spinnerLocation = (Spinner) registerView.findViewById(R.id.spinnerLocation);
        autoCompleteTextViewItem = (AutoCompleteTextView) registerView.findViewById(R.id.SearchItem);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        studentArrayList = new ArrayList<StudentListDAO>();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        nameDAOArrayList = new ArrayList<>();
        back_arrow1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (preferences.getInt("sync_status", 0) == 1) {
            nameDAOArrayList.clear();
            nameDAOArrayList.add(new LocationNameDAO("0", "Select Location"));
            nameDAOArrayList.add(new LocationNameDAO("0", "Other"));
            nameDAOArrayList.addAll(databaseQueryClass.getAllLocationStock(preferences.getInt("milestone_Id", 0)));
            spinnerLocation.setVisibility(View.VISIBLE);
            ArrayAdapter<LocationNameDAO> adapter = new ArrayAdapter<LocationNameDAO>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nameDAOArrayList);
            //  CustomSpinnerAdapter adapter=new CustomSpinnerAdapter(StudentsListActivity.this,locationlist);
            spinnerLocation.setAdapter(adapter);
            spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                    LocationNameDAO LeadSource = (LocationNameDAO) parent.getSelectedItem();
                    // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getLocation_name(), Toast.LENGTH_SHORT).show();

                    if (LeadSource.getLocation_name().equals("Other")) {
                        itemLocationEdtTxt.setVisibility(View.VISIBLE);
                        itemLocationEdtTxt.setText("");
                    } else {
                        itemLocationEdtTxt.setVisibility(View.GONE);
                        itemLocationEdtTxt.setText(LeadSource.getLocation_name());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });
        } else {
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                new LocationSpinner().execute();

            } else {
                Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
            }
        }

        autoCompleteTextViewItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextViewItem.getText())) {
                        if (preferences.getInt("sync_status", 0) == 1) {
                            getchannelOfflinePartnerSelect(autoCompleteTextViewItem.getText().toString());
                        } else {
                            getchannelPartnerSelect(autoCompleteTextViewItem.getText().toString());
                        }

                    } else {
                        itemNameEdtTxt.setText("");
                        itemNumberEdtTxt.setText("");
                    }
                }
                return false;
            }
        });

        placeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                itemName = itemNameEdtTxt.getText().toString().trim();
                itemNumber = itemNumberEdtTxt.getText().toString().trim();
                location = itemLocationEdtTxt.getText().toString().trim();
                if (!qntyEdtTxt.getText().toString().trim().equals("")) {
                    quantity = Integer.parseInt(qntyEdtTxt.getText().toString().trim());
                }
                if (validate(itemName, itemNumber, location, quantity)) {
                    if (preferences.getInt("sync_status", 0) == 1) {
                        int cf = databaseQueryClass.checkData(itemName, location);
                        if (cf == 0) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(placeBtn.getWindowToken(), 0);
                            try {

                                long id = -1;
                                Date today = new Date();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                String dateToStr = format.format(today);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(Constant.M_ID, databaseQueryClass.getMaxNo() + 1);
                                contentValues.put(Constant.CUSTOMERID, preferences.getInt("client_Id", 0));
                                contentValues.put(Constant.CLIENTID, preferences.getInt("client_Id", 0));
                                contentValues.put(Constant.MILE_STONEID, preferences.getInt("milestone_Id", 0));
                                contentValues.put(Constant.ASSIGNUSERID, Integer.parseInt(preferences.getString("inventry_user_id", "")));
                                contentValues.put(Constant.UOM, "0");
                                contentValues.put(Constant.ITEM_TYPE, "0");
                                contentValues.put(Constant.ITEM_NO, itemNumber);
                                contentValues.put(Constant.ITEM_NAME, itemName);
                                contentValues.put(Constant.BATCH, "0");
                                contentValues.put(Constant.BUN, "0");
                                contentValues.put(Constant.EUN, "0");
                                contentValues.put(Constant.LOT_NO, "0");
                                contentValues.put(Constant.CONTAINER_NO, "0");
                                contentValues.put(Constant.TOTAL_QUANTITY, quantity);
                                contentValues.put(Constant.MAKE, "0");
                                contentValues.put(Constant.UNRESTRICTED_QUANTITY, 0);
                                contentValues.put(Constant.VALUE_UNRESTRICTED, 0);
                                contentValues.put(Constant.BLOCKED_QUANTITY, 0);
                                contentValues.put(Constant.VALUE_BLOCKED_STOCK, 0);
                                contentValues.put(Constant.NAME, "0");
                                contentValues.put(Constant.FACTORY, "0");
                                contentValues.put(Constant.WAREHOUSE, "0");
                                contentValues.put(Constant.WHITEM, "0");
                                contentValues.put(Constant.STOCK_ZONE, "0");
                                contentValues.put(Constant.STOCK_LOCATION, location);
                                contentValues.put(Constant.DF_STORE_LOCATION_LEVEL, "0");
                                contentValues.put(Constant.STOCK_SEGMENT, "0");
                                contentValues.put(Constant.ASSIGNUSEREMAIL, preferences.getString("inventry_user_Email", ""));
                                contentValues.put(Constant.STATUSCODE, "0");
                                contentValues.put(Constant.INPUTQTY, 0);
                                contentValues.put(Constant.EXTENTEDQTY, 0);
                                contentValues.put(Constant.SHRINKQTY, 0);
                                contentValues.put(Constant.ISQTYSYNC, "Y");
                                contentValues.put(Constant.STOCKSTATUS, "P");
                                contentValues.put(Constant.ISFLOORTOSHEET, "Y");
                                contentValues.put(Constant.FLOORTOSHEETBY, "USER");
                                contentValues.put(Constant.SYNC_STATUS, 0);
                                contentValues.put(Constant.IU_SYNC_STATUS, 1);
                                contentValues.put(Constant.CREATE_AT, dateToStr);

                                try {
                                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
                                    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                                    id = sqLiteDatabase.insertOrThrow(Constant.TABLE_MILESTONEDETAILSALL, null, contentValues);
                                    if (id > 0) {
                                        mListener.messageReceived("hello");
                                        Toast.makeText(context, "Data Save Successfully", Toast.LENGTH_LONG).show();
                                        dismiss();
                                        if (AppStatus.getInstance(context).isOnline()) {
                                            context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                                        }
                                    } else {
                                        Toast.makeText(context, "Data Not Save Successfully", Toast.LENGTH_LONG).show();

                                    }
                                } catch (SQLiteException e) {
                                    //Logger.d("Exception: " + e.getMessage());
                                    Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                } finally {
                                    sqLiteDatabase.close();
                                }


                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, "We can not add this line item as this item is already exist in you sample", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (AppStatus.getInstance(getActivity()).isOnline()) {
                            new insertLineItemDetail().execute();
                        } else {
                            Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {

                }

            }
        });

        getDialog().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //Hide your keyboard here!!!
                    //Toast.makeText(getActivity(), "PLease enter your information to get us connected with you.", Toast.LENGTH_LONG).show();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });
        return registerView;
    }

    public boolean validate(String itemName, String itemNumber, String location, int quantity) {
        boolean isValidate = false;
        if (itemName.equals("")) {
            Toast.makeText(getActivity(), "Please Enter Item Name", Toast.LENGTH_LONG).show();
        } else if (itemNumber.equals("")) {
            Toast.makeText(getActivity(), "Please Enter Item Code", Toast.LENGTH_LONG).show();
        } else if (location.equals("")) {
            Toast.makeText(getActivity(), "Please Enter Location", Toast.LENGTH_LONG).show();
        } else if (location.equals("Select Location")) {
            Toast.makeText(getActivity(), "Please Enter Stock Location", Toast.LENGTH_LONG).show();
        } else if (quantity == 0) {
            Toast.makeText(getActivity(), "Please Enter Quantity", Toast.LENGTH_LONG).show();
        } else {
            isValidate = true;
        }
        return isValidate;
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        //Toast.makeText(getActivity(), "Your information is valuable for us and won't be misused.", Toast.LENGTH_SHORT).show();
                        count++;
                        if (count >= 1) {

                            dismiss();
                        }
                        return true;
                    } else {
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

    private class insertLineItemDetail extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Insert Line Item...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONObject json = new JSONObject();

            try {
                json.put("customerId", preferences.getInt("client_Id", 0));
                json.put("milestoneId", preferences.getInt("milestone_Id", 0));
                json.put("assignUserId", Integer.parseInt(preferences.getString("inventry_user_id", "")));
                json.put("item_No", itemNumber);
                json.put("item_Name", itemName);
                json.put("total_Quantity", quantity);
                json.put("stock_Location", location);
                json.put("assignUserEmail", preferences.getString("inventry_user_Email", ""));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + json);
            loginResponse = serviceAccess.SendHttpPost(Config.URL_MILESTONEFLOORTOSHEETSTOCK, json);
            Log.i("resp", "loginResponse" + loginResponse);
            if (loginResponse.compareTo("") != 0) {
                status = true;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            if (status) {
                if (loginResponse.contains("We can not add this line item as this item is already exist in you sample")) {
                    Toast.makeText(getActivity(), "We can not add this line item as this item is already exist in you sample", Toast.LENGTH_LONG).show();
                } else {
                    mListener.messageReceived("hello");
                    dismiss();
                    Toast.makeText(getActivity(), "Floort to sheet stock created successfully", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //
    private class LocationSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //  mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            //  mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            // mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
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
            Log.i("json", "json" + jsonLeadObj1);
            locationResponse = serviceAccess.SendHttpPost(Config.URL_STOCKLOCATION, jsonLeadObj1);
            Log.i("resp", "leadListResponse" + locationResponse);

            if (locationResponse.compareTo("") != 0) {
                if (isJSONValid(locationResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                spinnerLocation.setVisibility(View.VISIBLE);
                                nameDAOArrayList.add(new LocationNameDAO("0", "Select Location"));
                                nameDAOArrayList.add(new LocationNameDAO("0", "Other"));
                                JSONArray LeadSourceJsonObj = new JSONArray(locationResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    nameDAOArrayList.add(new LocationNameDAO("", json_data.getString("stockLocation")));
                                }

                                jsonArray = new JSONArray(locationResponse);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (locationResponse.compareTo("") != 0) {

                ArrayAdapter<LocationNameDAO> adapter = new ArrayAdapter<LocationNameDAO>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nameDAOArrayList);
                //  CustomSpinnerAdapter adapter=new CustomSpinnerAdapter(StudentsListActivity.this,locationlist);
                spinnerLocation.setAdapter(adapter);
                spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        LocationNameDAO LeadSource = (LocationNameDAO) parent.getSelectedItem();
                        // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getLocation_name(), Toast.LENGTH_SHORT).show();

                        if (LeadSource.getLocation_name().equals("Other")) {
                            itemLocationEdtTxt.setVisibility(View.VISIBLE);
                            itemLocationEdtTxt.setText("");
                        } else {
                            itemLocationEdtTxt.setVisibility(View.GONE);
                            itemLocationEdtTxt.setText(LeadSource.getLocation_name());
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });
                //  mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                //  mProgressDialog.dismiss();
            }
        }
    }

    protected boolean isJSONValid(String registrationResponse) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(registrationResponse);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(registrationResponse);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static void bindListener(Listener listener) {
        mListener = listener;
    }

    public void getchannelPartnerSelect(final String channelPartnerSelect) {

        JSONObject jsonSchedule = new JSONObject() {
            {
                try {
                    put("Prefixtext", channelPartnerSelect);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("json exception", "json exception" + e);
                }
            }
        };


        Thread objectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                Log.i("json", "json" + jsonSchedule);
                //SEND RESPONSE
                studentResponse = serviceAccess.SendHttpGet(Config.URL_STOCKITEMLIST + preferences.getInt("milestone_Id", 0) + "/" + channelPartnerSelect, jsonSchedule);
                Log.i("resp", "loginResponse" + studentResponse);


                try {
                    JSONArray callArrayList = new JSONArray(studentResponse);
                    studentArrayList.clear();
                    // user_id="";
                    for (int i = 0; i < callArrayList.length(); i++) {
                        studentListDAO = new StudentListDAO();
                        JSONObject cityJsonObject = callArrayList.getJSONObject(i);
                        studentArrayList.add(new StudentListDAO(cityJsonObject.getString("itemName"), cityJsonObject.getString("itemCode")));

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        studentListDAOArrayAdapter = new ArrayAdapter<StudentListDAO>(getActivity(), R.layout.item, studentArrayList);
                        autoCompleteTextViewItem.setAdapter(studentListDAOArrayAdapter);
                        if (studentArrayList.size() < 40)
                            autoCompleteTextViewItem.setThreshold(1);
                        else autoCompleteTextViewItem.setThreshold(2);
                        studentListDAOArrayAdapter.notifyDataSetChanged();
                        autoCompleteTextViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                StudentListDAO student = (StudentListDAO) parent.getAdapter().getItem(i);
                                itemName = student.getItemName();
                                if (!itemName.equals("")) {
                                    itemNameEdtTxt.setText(itemName);
                                    itemNumberEdtTxt.setText(student.getItemCode());
                                }


                                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                            }
                        });
                        studentListDAOArrayAdapter.notifyDataSetChanged();

                    }
                });


            }
        });

        objectThread.start();

    }

    public void getchannelOfflinePartnerSelect(final String channelPartnerSelect) {

        JSONObject jsonSchedule = new JSONObject() {
            {
                try {
                    put("Prefixtext", channelPartnerSelect);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("json exception", "json exception" + e);
                }
            }
        };


        Thread objectThread = new Thread(new Runnable() {

            @Override
            public void run() {


                Cursor c = databaseQueryClass.getSerachBatches(channelPartnerSelect, preferences.getInt("milestone_Id", 0));
                assert c != null;
                Log.d("Count", "" + c.getCount());
                studentArrayList.clear();
                studentListDAO = new StudentListDAO();
                if (c.moveToFirst()) {
                    do {
                        status = true;
                        studentArrayList.add(new StudentListDAO(c.getString(c.getColumnIndex("item_Name")), c.getString(c.getColumnIndex("item_No"))));

                    } while (c.moveToNext());
                }


                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        studentListDAOArrayAdapter = new ArrayAdapter<StudentListDAO>(getActivity(), R.layout.item, studentArrayList);
                        autoCompleteTextViewItem.setAdapter(studentListDAOArrayAdapter);
                        if (studentArrayList.size() < 40)
                            autoCompleteTextViewItem.setThreshold(1);
                        else autoCompleteTextViewItem.setThreshold(2);
                        studentListDAOArrayAdapter.notifyDataSetChanged();
                        autoCompleteTextViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                StudentListDAO student = (StudentListDAO) parent.getAdapter().getItem(i);
                                itemName = student.getItemName();
                                if (!itemName.equals("")) {
                                    itemNameEdtTxt.setText(itemName);
                                    itemNumberEdtTxt.setText(student.getItemCode());
                                }


                                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                            }
                        });
                        studentListDAOArrayAdapter.notifyDataSetChanged();

                    }
                });


            }
        });

        objectThread.start();

    }
}