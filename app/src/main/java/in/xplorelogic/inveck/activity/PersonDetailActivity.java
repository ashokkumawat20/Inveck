package in.xplorelogic.inveck.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.internal.view.SupportMenu;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.models.PersonDetails;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.ConnectivityReceiver;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.WebClient;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonDetailActivity extends AppCompatActivity {
    int client_id;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String descpt;
    /* access modifiers changed from: private */
    public TextInputEditText icharge_desgn;
    /* access modifiers changed from: private */
    public TextInputEditText icharge_email;
    /* access modifiers changed from: private */
    public TextInputEditText icharge_mobno;
    /* access modifiers changed from: private */
    public TextInputEditText icharge_name;
    /* access modifiers changed from: private */
    int milestone_id;
    private LinearLayout register;
    String user_id;
    private TextInputLayout vDesignation;
    private TextInputLayout vEmailLayout;
    private TextInputLayout vPasswordLayout;
    private TextInputLayout vUsernameLayout;
    String uId, loginResponse = "";
    EditText userId;
    ProgressDialog mProgressDialog;
    JSONObject jsonObj, jsonObject;
    boolean status;
    String icharge_name1, icharge_desgn1, icharge_email1, icharge_mobno1;
    PersonDetails personDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_person_detail);

        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        vUsernameLayout = (TextInputLayout) findViewById(R.id.fullname);
        vEmailLayout = (TextInputLayout) findViewById(R.id.emailid);
        vDesignation = (TextInputLayout) findViewById(R.id.designtn);
        vPasswordLayout = (TextInputLayout) findViewById(R.id.paswrd);
        register = (LinearLayout) findViewById(R.id.regiButton);
        icharge_name = (TextInputEditText) findViewById(R.id.icharge_name);
        icharge_desgn = (TextInputEditText) findViewById(R.id.icharge_desgn);
        icharge_email = (TextInputEditText) findViewById(R.id.icharge_email);
        icharge_mobno = (TextInputEditText) findViewById(R.id.icharge_mobno);
        vUsernameLayout.setErrorTextColor(ColorStateList.valueOf(SupportMenu.CATEGORY_MASK));
        String str = "#425170";
        this.vDesignation.setErrorTextColor(ColorStateList.valueOf(SupportMenu.CATEGORY_MASK));
        this.vDesignation.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor(str)));
        this.vEmailLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor(str)));
        this.vPasswordLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor(str)));
        this.vUsernameLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor(str)));
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                icharge_name1 = icharge_name.getText().toString().trim();
                icharge_desgn1 = icharge_desgn.getText().toString().trim();
                icharge_email1 = icharge_email.getText().toString().trim();
                icharge_mobno1 = icharge_mobno.getText().toString().trim();
                if (validate(icharge_name1, icharge_desgn1, icharge_email1, icharge_mobno1)) {
                    if (preferences.getInt("sync_status", 0) == 1) {
                        personDetails = new PersonDetails(preferences.getInt("milestone_Id", 0), Integer.parseInt(preferences.getString("inventry_user_id", "")), icharge_name1, icharge_desgn1, icharge_mobno1, icharge_email1);
                        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();
                        long id = databaseQueryClass.insertPersonDetails(personDetails);
                        if (id > 0) {
                            long id1 = databaseQueryClass.updatePersonDetails(personDetails);
                            Intent intent = new Intent(PersonDetailActivity.this, FragmentMilestone.class);
                            intent.putExtra("client_Id", client_id);
                            intent.putExtra("milestone_Id", milestone_id);
                            intent.putExtra("user_Id", preferences.getString("inventry_user_id", ""));
                            startActivity(intent);
                            finish();
                        }
                        if (AppStatus.getInstance(PersonDetailActivity.this).isOnline()) {
                            registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                        }
                    } else {
                        if (AppStatus.getInstance(PersonDetailActivity.this).isOnline()) {
                            new sendInchargeDetail().execute();
                        } else {
                            Toast.makeText(PersonDetailActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }

    public boolean validate(String icharge_name1, String icharge_desgn1, String icharge_email1, String icharge_mobno1) {
        boolean isValidate = false;
        if (icharge_name1.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Client Name.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (icharge_desgn1.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Designation.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (icharge_email1.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Email Id.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (!validateEmail(icharge_email1)) {
            Toast.makeText(getApplicationContext(), "Please enter valid Email Id.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (icharge_mobno1.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (icharge_mobno1.length() != 10) {
            Toast.makeText(getApplicationContext(), "Please enter a 10 digit valid Mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else {
            isValidate = true;
        }
        return isValidate;
    }

    /**
     * email validation
     */
    private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");

    public boolean validateEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private class sendInchargeDetail extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            // mProgressDialog = new ProgressDialog(PersonDetailActivity.this);
            // Set progressdialog title
            //   mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            //   mProgressDialog.setMessage("Login...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonObj = new JSONObject() {
                {
                    try {
                        put("id", 0);
                        put("mileStonId", preferences.getInt("milestone_Id", 0));
                        put("fullName", icharge_name1);
                        put("designation", icharge_desgn1);
                        put("email", icharge_email1);
                        put("contactNo", icharge_mobno1);
                        put("createdBy", Integer.parseInt(preferences.getString("inventry_user_id", "")));
                        put("createdDate", "2020-06-02T15:35:09.202Z");
                        put("appStatus", "");
                        put("delStatus", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            loginResponse = serviceAccess.SendHttpPost(Config.URL_MILESTONEINCHARGE, jsonObj);
            Log.i("resp", "loginResponse" + loginResponse);


            if (loginResponse.compareTo("") != 0) {
                status = true;
                if (isJSONValid(loginResponse)) {


                    try {

                        jsonObject = new JSONObject(loginResponse);

                        status = true;
                       /* if (!jsonObject.isNull("user_id")) {
                            JSONArray ujsonArray = jsonObject.getJSONArray("user_id");
                            for (int i = 0; i < ujsonArray.length(); i++) {
                                JSONObject UJsonObject = ujsonArray.getJSONObject(i);
                                // prefEditor.putString("inventry_user_id", this.uId);
                                prefEditor.commit();
                            }
                        }*/


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Toast.makeText(getApplicationContext(), "Please check your UserName and Password", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
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
            //  mProgressDialog.dismiss();
            if (status) {
                //  Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PersonDetailActivity.this, FragmentMilestone.class);
                intent.putExtra("client_Id", client_id);
                intent.putExtra("milestone_Id", milestone_id);
                intent.putExtra("user_Id", preferences.getString("inventry_user_id", ""));
                startActivity(intent);
                finish();
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
}
