package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.WebClient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class UserDetailActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    RadioGroup radioGroup;
    int pos;
    String gender = "", ufirstname = "", ulastname = "", uemail = "", umobno = "", ucontactno = "", updateResponse = "";
    private RadioButton male, female;
    TextView firstName, lastName, emailName, mobileNo, contactNo;
    LinearLayout editButton, updateButton;
    public TextInputEditText firstname, lastname, email, mobno, contactno;
    public TextInputLayout hidecontactno, hidefirstname, hidelastname, hideemail, hidemobno;
    JSONObject jsonObj;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("Prefrence", MODE_PRIVATE);
        prefEditor = preferences.edit();
        setContentView(R.layout.activity_user_detail);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        firstName = (TextView) findViewById(R.id.firstName);
        lastName = (TextView) findViewById(R.id.lastName);
        emailName = (TextView) findViewById(R.id.emailName);
        mobileNo = (TextView) findViewById(R.id.mobileNo);
        contactNo = (TextView) findViewById(R.id.contactNo);
        editButton = (LinearLayout) findViewById(R.id.editButton);
        updateButton = (LinearLayout) findViewById(R.id.updateButton);
        firstName.setText(preferences.getString("inventry_user_firstname", ""));
        lastName.setText(preferences.getString("inventry_user_surname", ""));
        emailName.setText(preferences.getString("inventry_user_Email", ""));
        mobileNo.setText(preferences.getString("inventry_user_mobile_no", ""));
        contactNo.setText(preferences.getString("inventry_user_contact_no", ""));
        firstname = (TextInputEditText) findViewById(R.id.firstname);
        lastname = (TextInputEditText) findViewById(R.id.lastname);
        email = (TextInputEditText) findViewById(R.id.email);
        mobno = (TextInputEditText) findViewById(R.id.mobno);
        contactno = (TextInputEditText) findViewById(R.id.contactno);
        hidefirstname = (TextInputLayout) findViewById(R.id.hidefirstname);
        hidelastname = (TextInputLayout) findViewById(R.id.hidelastname);
        hideemail = (TextInputLayout) findViewById(R.id.hideemail);
        hidemobno = (TextInputLayout) findViewById(R.id.hidemobno);
        hidecontactno = (TextInputLayout) findViewById(R.id.hidecontactno);

        if (preferences.getString("inventry_user_title", "").equals("Mrs.")) {
            female.setChecked(true);
        } else {
            male.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos = radioGroup.indexOfChild(findViewById(checkedId));
                switch (pos) {
                    case 1:

                        gender = "Mrs.";
                        //Toast.makeText(UserDetailActivity.this, "You have Clicked RadioButton 1" + gender, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        gender = "Mr.";
                        //  Toast.makeText(UserDetailActivity.this, gender, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        //The default selection is RadioButton 1
                        gender = "Mrs.";
                        //  Toast.makeText(UserDetailActivity.this, gender, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.setVisibility(View.GONE);
                firstName.setVisibility(View.GONE);
                lastName.setVisibility(View.GONE);
                emailName.setVisibility(View.GONE);
                mobileNo.setVisibility(View.GONE);
                contactNo.setVisibility(View.GONE);
                updateButton.setVisibility(View.VISIBLE);
                hidecontactno.setVisibility(View.VISIBLE);
                hidefirstname.setVisibility(View.VISIBLE);
                hidelastname.setVisibility(View.VISIBLE);
                //   hideemail.setVisibility(View.VISIBLE);
                hidemobno.setVisibility(View.VISIBLE);

                firstname.setText(preferences.getString("inventry_user_firstname", ""));
                lastname.setText(preferences.getString("inventry_user_surname", ""));
                email.setText(preferences.getString("inventry_user_Email", ""));
                mobno.setText(preferences.getString("inventry_user_mobile_no", ""));
                contactno.setText(preferences.getString("inventry_user_contact_no", ""));
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ufirstname = firstname.getText().toString().trim();
                ulastname = lastname.getText().toString().trim();
                uemail = email.getText().toString().trim();
                umobno = mobno.getText().toString().trim();
                ucontactno = contactno.getText().toString().trim();

                if (validate(ufirstname, ulastname, uemail, umobno)) {
                    updateButton.setVisibility(View.GONE);
                    hidecontactno.setVisibility(View.GONE);
                    hidefirstname.setVisibility(View.GONE);
                    hidelastname.setVisibility(View.GONE);
                    hideemail.setVisibility(View.GONE);
                    hidemobno.setVisibility(View.GONE);
                    updateButton.setVisibility(View.GONE);
                    hidecontactno.setVisibility(View.GONE);
                    hidefirstname.setVisibility(View.GONE);
                    hidelastname.setVisibility(View.GONE);
                    hideemail.setVisibility(View.GONE);
                    hidemobno.setVisibility(View.GONE);
                    editButton.setVisibility(View.VISIBLE);
                    firstName.setVisibility(View.VISIBLE);
                    lastName.setVisibility(View.VISIBLE);
                    emailName.setVisibility(View.VISIBLE);
                    mobileNo.setVisibility(View.VISIBLE);
                    contactNo.setVisibility(View.VISIBLE);
                    firstName.setText(ufirstname);
                    lastName.setText(ulastname);
                    emailName.setText(uemail);
                    mobileNo.setText(umobno);
                    contactNo.setText(ucontactno);
                    if (AppStatus.getInstance(UserDetailActivity.this).isOnline()) {
                        new UpdatingProfile().execute();
                    } else {

                        Toast.makeText(UserDetailActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }

    public boolean validate(String ufirstname, String ulastname, String uemail, String umobno) {
        boolean isValidate = false;
        if (ufirstname.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter first name.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (ulastname.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter last name.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (uemail.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Email Id.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (!validateEmail(uemail)) {
            Toast.makeText(getApplicationContext(), "Please enter valid Email Id.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (umobno.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (umobno.length() != 10) {
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

    private class UpdatingProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(UserDetailActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Updating Profile...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("inventry_user_id", ""));
                        put("user_title", gender);
                        put("user_firstname", ufirstname);
                        put("user_surname", ulastname);
                        put("user_contact_no", ucontactno);
                        put("user_mobile_no", umobno);
                        put("user_Email", uemail);
                        put("user_del_sts", "");
                        put("user_status", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            updateResponse = serviceAccess.SendHttpPost(Config.URL_UPDATEPROFILE, jsonObj);
            Log.i("updateResponse", "updateResponse" + updateResponse);

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            prefEditor.putString("inventry_user_title", gender);
            prefEditor.putString("inventry_user_firstname", ufirstname);
            prefEditor.putString("inventry_user_surname", ulastname);
            prefEditor.putString("inventry_user_contact_no", ucontactno);
            prefEditor.putString("inventry_user_mobile_no", umobno);
            prefEditor.putString("inventry_user_Email", uemail);
            prefEditor.commit();
            Toast.makeText(getApplicationContext(), updateResponse, Toast.LENGTH_LONG).show();

        }
    }


}
