package in.xplorelogic.inveck.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.activity.FragmentMilestone;
import in.xplorelogic.inveck.activity.MilestoneListActivity;
import in.xplorelogic.inveck.activity.PersonDetailActivity;
import in.xplorelogic.inveck.adapter.LocationListAdpter;
import in.xplorelogic.inveck.adapter.MileStonesListAdpter;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.jsonparser.JsonHelper;
import in.xplorelogic.inveck.models.LocationListDAO;
import in.xplorelogic.inveck.models.LocationNameDAO;
import in.xplorelogic.inveck.models.Milestone;
import in.xplorelogic.inveck.models.PersonDetails;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.ConnectivityReceiver;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.NetworkClient;
import in.xplorelogic.inveck.utils.UploadAPIs;
import in.xplorelogic.inveck.utils.Utils;
import in.xplorelogic.inveck.utils.WebClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UpdateLocationEntryView extends DialogFragment {


    Context context;
    SharedPreferences preferences;
    Editor prefEditor;
    String discontinueResponse = "", message = "";
    JSONObject jsonObj;
    Boolean status;
    int count = 0;
    View registerView;
    private JSONObject jsonLeadObj;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    EditText resonEdtTxt, descEdtTxt, qntyEdtTxt;
    ImageView back_arrow1;
    LinearLayout placeBtn;
    String location = "";
    Spinner spinnerLocation;
    String notesFlag = "0", locationResponse = "", locationListResponse = "";
    JSONObject jsonLeadObj1;
    ArrayList<LocationNameDAO> nameDAOArrayList;
    int quantity = 0;
    TextView itemName;
    List<LocationListDAO> data;
    LocationListAdpter locationListAdpter;
    private RecyclerView mList;
    int totalQuantity = 0;
    TextView totalQuantity1;
    LocationListDAO locationListDAO;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_location, null);

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
        spinnerLocation = (Spinner) registerView.findViewById(R.id.spinnerLocation);
        descEdtTxt = (EditText) registerView.findViewById(R.id.descEdtTxt);
        qntyEdtTxt = (EditText) registerView.findViewById(R.id.qntyEdtTxt);
        itemName = (TextView) registerView.findViewById(R.id.itemName);
        totalQuantity1 = (TextView) registerView.findViewById(R.id.totalQuantity);
        mList = (RecyclerView) registerView.findViewById(R.id.listView);
        itemName.setText(preferences.getString("itemName", ""));
        nameDAOArrayList = new ArrayList<>();
        data = new ArrayList<>();
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
                        descEdtTxt.setVisibility(View.VISIBLE);
                        descEdtTxt.setText("");
                    } else {
                        descEdtTxt.setVisibility(View.GONE);
                        descEdtTxt.setText(LeadSource.getLocation_name());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });

            data.clear();
            data.addAll(databaseQueryClass.getAllLocationListStock(preferences.getInt("stockId", 0)));
            totalQuantity = databaseQueryClass.getSumTotalLQValue(preferences.getInt("milestone_Id", 0), preferences.getInt("stockId", 0));
            locationListAdpter = new LocationListAdpter(getActivity(), data);
            mList.setAdapter(locationListAdpter);
            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
            if(totalQuantity>0) {
                totalQuantity1.setText("Total Quantity: " + totalQuantity);
            }
        } else {
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                new LocationSpinner().execute();
                new getListLocation().execute();
            } else {
                Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
            }
        }

        placeBtn = (LinearLayout) registerView.findViewById(R.id.btnSend);
        back_arrow1 = (ImageView) registerView.findViewById(R.id.back_arrow1);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        back_arrow1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        placeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                location = descEdtTxt.getText().toString().trim();
                if (!qntyEdtTxt.getText().toString().trim().equals("")) {
                    quantity = Integer.parseInt(qntyEdtTxt.getText().toString().trim());
                }
                if (validate(location)) {
                    if (preferences.getInt("sync_status", 0) == 1) {
                        locationListDAO = new LocationListDAO(1, quantity, preferences.getInt("mileStoneId", 0), preferences.getInt("stockId", 0), location);
                        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();
                        long id = databaseQueryClass.insertStockLocationDetails(locationListDAO);
                        if (id > 0) {
                            // dismiss();
                            data.clear();
                            data.addAll(databaseQueryClass.getAllLocationListStock(preferences.getInt("stockId", 0)));
                            totalQuantity = databaseQueryClass.getSumTotalLQValue(preferences.getInt("milestone_Id", 0), preferences.getInt("stockId", 0));
                            locationListAdpter = new LocationListAdpter(getActivity(), data);
                            mList.setAdapter(locationListAdpter);
                            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            if(totalQuantity>0) {
                                totalQuantity1.setText("Total Quantity: " + totalQuantity);
                            }
                        }
                        if (AppStatus.getInstance(getActivity()).isOnline()) {
                            getActivity().registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                        }
                    } else {
                        if (AppStatus.getInstance(context).isOnline()) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(placeBtn.getWindowToken(), 0);
                            new upDateLocation().execute();

                        } else {

                            Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }
                    }


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


    public boolean validate(String discontinue_reason) {
        boolean isValidate = false;
        if (discontinue_reason.equals("")) {
            Toast.makeText(getActivity(), "Please Enter Location", Toast.LENGTH_LONG).show();
        } else if (discontinue_reason.equals("Select Location")) {
            Toast.makeText(getActivity(), "Please Select Location", Toast.LENGTH_LONG).show();
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

    //
    private class upDateLocation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Updating Location...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("stockId", preferences.getInt("stockId", 0));
                        put("mileStoneId", preferences.getInt("mileStoneId", 0));
                        put("quantity", quantity);
                        put("location", location);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            discontinueResponse = serviceAccess.SendHttpPost(Config.URL_UPDATESTOCKLOCATION, jsonLeadObj);
            Log.i("resp", "discontinueResponse" + discontinueResponse);
            if (discontinueResponse.compareTo("") != 0) {
                status = true;
                if (isJSONValid(discontinueResponse)) {

                    try {

                        JSONObject jObject = new JSONObject(discontinueResponse);
                        status = jObject.getBoolean("status");
                        message = jObject.getString("message");
                        jsonArray = new JSONArray(discontinueResponse);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {

                    // Toast.makeText(context, "Please check your network connection", Toast.LENGTH_LONG).show();

                }
            } else {
                status = false;
                // Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            mProgressDialog.dismiss();

            if (status) {

                Toast.makeText(context, discontinueResponse, Toast.LENGTH_LONG).show();
                qntyEdtTxt.setText("");
                new LocationSpinner().execute();
                new getListLocation().execute();

            } else {
                Toast.makeText(context, discontinueResponse, Toast.LENGTH_LONG).show();
                dismiss();

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
                                nameDAOArrayList.clear();
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
                            descEdtTxt.setVisibility(View.VISIBLE);
                            descEdtTxt.setText("");
                        } else {
                            descEdtTxt.setVisibility(View.GONE);
                            descEdtTxt.setText(LeadSource.getLocation_name());
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

    private class getListLocation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading Location List...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        //  put("emailAddress", MilestoneListActivity.this.user_id);
                        //   put("userId", MilestoneListActivity.this.user_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            locationListResponse = serviceAccess.SendHttpPost(Config.URL_STOCKLOCATION + "/" + preferences.getInt("stockId", 0), jsonLeadObj);
            Log.i("resp", locationListResponse);

            if (locationListResponse.compareTo("") != 0) {
                if (isJSONValid(locationListResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                JSONObject jsonObject = new JSONObject(locationListResponse);
                                totalQuantity = jsonObject.getInt("totalQuantity");
                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseLocationList(locationListResponse);
                                jsonArray = new JSONArray(locationListResponse);

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
                            //  Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            if (data.size() > 0) {
                // Close the progressdialog
                mProgressDialog.dismiss();
                locationListAdpter = new LocationListAdpter(getActivity(), data);
                mList.setAdapter(locationListAdpter);
                mList.setLayoutManager(new LinearLayoutManager(getActivity()));
                totalQuantity1.setText("Total Quantity: " + totalQuantity);
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

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
}