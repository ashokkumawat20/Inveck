package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.WebClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    String msg = "Android : ";
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    TextView frgtPasssword, msgPrint;
    String imeiNumber;
    Button loginButton;
    EditText login_pswrd;

    String pwd;
    TelephonyManager telephonyManager;
    String uId, loginResponse = "";
    EditText userId;
    ProgressDialog mProgressDialog;
    JSONObject jsonObj, jsonObject;
    boolean status;
    String emial = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
        }
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        deviceId();
        this.loginButton = (Button) findViewById(R.id.login_button);
        this.userId = (EditText) findViewById(R.id.user_id);
        this.login_pswrd = (EditText) findViewById(R.id.login_pswrd);
        frgtPasssword = (TextView) findViewById(R.id.frgt_pswrd);
        msgPrint = (TextView) findViewById(R.id.msgPrint);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.uId = loginActivity.userId.getText().toString();
                LoginActivity loginActivity2 = LoginActivity.this;
                loginActivity2.pwd = loginActivity2.login_pswrd.getText().toString();
                if (LoginActivity.this.uId.isEmpty()) {
                    LoginActivity.this.userId.setError("Please Enter User Name");
                    LoginActivity.this.userId.requestFocus();
                } else if (LoginActivity.this.pwd.isEmpty()) {
                    LoginActivity.this.login_pswrd.setError("Please Enter your Password");
                    LoginActivity.this.login_pswrd.requestFocus();
                } else if (LoginActivity.this.uId.isEmpty() && LoginActivity.this.pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                } else if (!LoginActivity.this.uId.isEmpty() || !LoginActivity.this.pwd.isEmpty()) {
                    if (AppStatus.getInstance(LoginActivity.this).isOnline()) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(loginButton.getWindowToken(), 0);
                        new getLogin().execute();
                    } else {

                        Toast.makeText(LoginActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        frgtPasssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(LoginActivity.this).isOnline()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setMessage("Please enter your register mail id");

                    final EditText input = new EditText(LoginActivity.this);
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    // alertDialog.setIcon(R.drawable.msg_img);

                    alertDialog.setPositiveButton("Submit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    msgPrint.setText("");
                                    emial = input.getText().toString();
                                    if (!emial.equals("")) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                                        new getSentMailLogin().execute();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please enter your mail id", Toast.LENGTH_LONG).show();
                                    }


                                }
                            });

                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                } else {

                    Toast.makeText(LoginActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private class getLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Login...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonObj = new JSONObject() {
                {
                    try {
                        put("UserName", uId);
                        put("password", pwd);
                        put("deviceId", LoginActivity.this.imeiNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            loginResponse = serviceAccess.SendHttpPost(Config.URL_LOGIN, jsonObj);
            Log.i("resp", "loginResponse" + loginResponse);


            if (loginResponse.compareTo("") != 0) {

                if (isJSONValid(loginResponse)) {


                    try {
                        jsonObject = new JSONObject(loginResponse);
                        if (!preferences.getString("sync_inventry_user_id", "").equals(jsonObject.getString("user_id"))) {
                            deleteDatabase("inveckdb.db");
                        }
                        prefEditor.putString("inventry_user_id", jsonObject.getString("user_id"));
                        prefEditor.putString("sync_inventry_user_id", jsonObject.getString("user_id"));
                        prefEditor.putString("inventry_user_title", jsonObject.getString("user_title"));
                        prefEditor.putString("inventry_user_firstname", jsonObject.getString("user_firstname"));
                        prefEditor.putString("inventry_user_surname", jsonObject.getString("user_surname"));
                        prefEditor.putString("inventry_user_contact_no", jsonObject.getString("user_contact_no"));
                        prefEditor.putString("inventry_user_mobile_no", jsonObject.getString("user_mobile_no"));
                        prefEditor.putString("inventry_user_Email", jsonObject.getString("user_Email"));
                        prefEditor.putString("inventry_user_user_del_sts", jsonObject.getString("user_del_sts"));
                        prefEditor.putString("inventry_user_user_status", jsonObject.getString("user_status"));

                        prefEditor.commit();
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
                            Toast.makeText(getApplicationContext(), "Please check your UserName and Password", Toast.LENGTH_LONG).show();
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
            mProgressDialog.dismiss();
            if (status) {
                //  Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this, MilestoneListActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private class getSentMailLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Login...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonObj = new JSONObject() {
                {
                    try {
                        put("registerdEmail", emial);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            loginResponse = serviceAccess.SendHttpPost(Config.URL_FORGOTPASSWORD, jsonObj);
            Log.i("resp", "loginResponse" + loginResponse);
            status = true;


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            if (status) {
                Toast.makeText(getApplicationContext(), loginResponse, Toast.LENGTH_LONG).show();
                msgPrint.setText(loginResponse);
                // Intent intent = new Intent(LoginActivity.this, MilestoneListActivity.class);
                //  startActivity(intent);
                //  finish();
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
    //

    private void deviceId() {
        this.telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") == 0) {
            String deviceId = this.telephonyManager.getDeviceId();
            this.imeiNumber = deviceId;
            Log.e("Device ID: ", deviceId);
        }
    }
}

