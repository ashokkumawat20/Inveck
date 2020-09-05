package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.adapter.LocationListAdpter;
import in.xplorelogic.inveck.adapter.MileStonesListAdpter;
import in.xplorelogic.inveck.adapter.SignOffListAdpter;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.jsonparser.JsonHelper;
import in.xplorelogic.inveck.models.LocationListDAO;
import in.xplorelogic.inveck.models.LocationNameDAO;
import in.xplorelogic.inveck.models.Milestone;
import in.xplorelogic.inveck.models.TakeSignOffDAO;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.Listener;
import in.xplorelogic.inveck.utils.WebClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TakeSignOffActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    private FloatingActionButton fab;
    private JSONObject jsonLeadObj;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    String locationListResponse = "";
    JSONObject jsonLeadObj1;
    ArrayList<LocationNameDAO> nameDAOArrayList;
    int quantity = 0;
    TextView itemName;
    List<TakeSignOffDAO> data;
    SignOffListAdpter signOffListAdpter;
    private RecyclerView mList;
    ImageView back_arrow1;
    ImageView serach_hide, clear;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_sign_off);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        search = (EditText) findViewById(R.id.search);
        serach_hide = (ImageView) findViewById(R.id.serach_hide);
        clear = (ImageView) findViewById(R.id.clear);
        mList = (RecyclerView) findViewById(R.id.listView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        back_arrow1 = (ImageView) findViewById(R.id.back_arrow1);
        data = new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TakeSignOffActivity.this, TakeSignOffPersonDetailActivity.class);
                startActivity(intent);

            }
        });
        back_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (AppStatus.getInstance(TakeSignOffActivity.this).isOnline()) {
            new getTakeSignList().execute();
        } else {
            Toast.makeText(TakeSignOffActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }

        TakeSignOffPersonDetailActivity.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {

                if (AppStatus.getInstance(TakeSignOffActivity.this).isOnline()) {
                    new getTakeSignList().execute();
                } else {
                    Toast.makeText(TakeSignOffActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
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
                search.setText("");
                serach_hide.setVisibility(View.VISIBLE);
                clear.setVisibility(View.GONE);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }

    //
    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                clear.setVisibility(View.VISIBLE);
                serach_hide.setVisibility(View.GONE);

                query = query.toString().toLowerCase();
                final List<TakeSignOffDAO> filteredList = new ArrayList<TakeSignOffDAO>();
                if (data != null) {
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {

                            String subject = data.get(i).getFullName().toLowerCase();
                            String tag = data.get(i).getDesignation().toLowerCase();
                            String msg_txt = data.get(i).getEmail().toLowerCase();
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

                mList.setLayoutManager(new LinearLayoutManager(TakeSignOffActivity.this));
                signOffListAdpter = new SignOffListAdpter(TakeSignOffActivity.this, filteredList);
                mList.setAdapter(signOffListAdpter);
                signOffListAdpter.notifyDataSetChanged();  // data set changed

            }
        });
    }


    private class getTakeSignList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(TakeSignOffActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading Sign Off List...");
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
            locationListResponse = serviceAccess.SendHttpPost(Config.URL_MILESTONESIGNOFF + "/" + preferences.getInt("milestone_Id", 0) + "/" + Integer.parseInt(preferences.getString("inventry_user_id", "")), jsonLeadObj);

            Log.i("resp", locationListResponse);
            data.clear();

            if (locationListResponse.compareTo("") != 0) {
                if (isJSONValid(locationListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {


                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseSignOffList(locationListResponse);
                                jsonArray = new JSONArray(locationListResponse);

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
            mProgressDialog.dismiss();
            if (data.size() > 0) {
                signOffListAdpter = new SignOffListAdpter(TakeSignOffActivity.this, data);
                mList.setAdapter(signOffListAdpter);
                mList.setLayoutManager(new LinearLayoutManager(TakeSignOffActivity.this));

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
