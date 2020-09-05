package in.xplorelogic.inveck.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import androidx.fragment.app.DialogFragment;
import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.models.LocationNameDAO;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.WebClient;


public class UpdatePasswordEntryView extends DialogFragment {


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
    EditText cPassEdtTxt, NPassEdtTxt, NrPassEdtTxt;
    ImageView back_arrow1;
    LinearLayout placeBtn;
    String location = "";
    Spinner spinnerLocation;
    String notesFlag = "0", locationResponse = "";
    JSONObject jsonLeadObj1;
    ArrayList<LocationNameDAO> nameDAOArrayList;
    int quantity = 0;
    TextView itemName;
    String newpassword = "", oldpassword = "", reenterpassword = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_updatepassword, null);

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

        cPassEdtTxt = (EditText) registerView.findViewById(R.id.cPassEdtTxt);
        NPassEdtTxt = (EditText) registerView.findViewById(R.id.NPassEdtTxt);
        NrPassEdtTxt = (EditText) registerView.findViewById(R.id.NrPassEdtTxt);


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
                oldpassword = cPassEdtTxt.getText().toString().trim();
                newpassword = NPassEdtTxt.getText().toString().trim();
                reenterpassword = NrPassEdtTxt.getText().toString().trim();
                if (validate(oldpassword, newpassword, reenterpassword)) {
                    if (reenterpassword.equals(newpassword)) {
                        if (AppStatus.getInstance(getActivity()).isOnline()) {

                            // Toast.makeText(getApplicationContext(), "Valid", Toast.LENGTH_SHORT).show();
                            new updatePassword().execute();


                        } else {

                            Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Password mismatch. Please try again!", Toast.LENGTH_SHORT).show();
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


    public boolean validate(String userOldPass, String userNewPass, String userRENewPass) {
        boolean isValidate = false;
        if (userOldPass.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter your current password.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (userNewPass.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter your new password.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (userRENewPass.trim().equals("")) {
            Toast.makeText(getActivity(), "Please Re-Enter your  password.", Toast.LENGTH_LONG).show();
            isValidate = false;

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
    private class updatePassword extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Updating Password...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", Integer.parseInt(preferences.getString("inventry_user_id", "")));
                        put("currentPassword", oldpassword);
                        put("newPassword", newpassword);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            discontinueResponse = serviceAccess.SendHttpPost(Config.URL_CHANGEPASSWORD, jsonLeadObj);
            Log.i("resp", "discontinueResponse" + discontinueResponse);


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            mProgressDialog.dismiss();
            Toast.makeText(context, discontinueResponse, Toast.LENGTH_LONG).show();
            dismiss();

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