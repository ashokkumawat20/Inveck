package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.WebClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResumeActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    LinearLayout resume_button;
    JSONObject jsonLeadObj, jsonObject;
    ProgressDialog mProgressDialog;
    String stockListResponse = "";
    TextView s_totalQty, s_totalValues,updatedQty, s_pendingQty, s_excessQty, s_shortageQty;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();
    ProgressBar Progressbar, progressBar2, progressBar3;
    TextView ShowText, textView2, textView3;
    int progressBarValue = 0;
    int progressBarValue1 = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_resume);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        resume_button = (LinearLayout) findViewById(R.id.resumebutton);
        s_totalQty = (TextView) findViewById(R.id.s_totalQty);
        s_totalValues= (TextView) findViewById(R.id.s_totalValues);
        updatedQty = (TextView) findViewById(R.id.updatedQty);
        s_pendingQty = (TextView) findViewById(R.id.s_pendingQty);
        Progressbar = (ProgressBar) findViewById(R.id.progressBar1);
        ShowText = (TextView) findViewById(R.id.textView1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        textView2 = (TextView) findViewById(R.id.textView2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        textView3 = (TextView) findViewById(R.id.textView3);
        s_excessQty = (TextView) findViewById(R.id.s_excessQty);
        s_shortageQty = (TextView) findViewById(R.id.s_shortageQty);
        progressBar3.setProgress(100);
        textView3.setText("Total Quantity : " + 100 + "%");
        resume_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent start = new Intent(ResumeActivity.this, FragmentMilestone.class);
                ResumeActivity.this.startActivity(start);
                finish();
            }
        });
        if (preferences.getInt("sync_status", 0) == 1) {
            float t = databaseQueryClass.getSumTotalQuaValue(preferences.getInt("milestone_Id", 0));
            float u = databaseQueryClass.getSumTotalUQUaValue(preferences.getInt("milestone_Id", 0), "W");
            float p = databaseQueryClass.getSumTotalUQGValue(preferences.getInt("milestone_Id", 0), "P");
            float uq = (float) ((u / t) * 100);
            float pq = (float) ((p / t) * 100);

            s_totalQty.setText(" " + databaseQueryClass.getSumTotalQuaValue(preferences.getInt("milestone_Id", 0)));
            s_totalValues.setText(" " + databaseQueryClass.getSumTotalUNValue(preferences.getInt("milestone_Id", 0)));
            updatedQty.setText(" " + databaseQueryClass.getSumTotalUQUaValue(preferences.getInt("milestone_Id", 0), "W"));
            s_pendingQty.setText(" " + databaseQueryClass.getSumTotalUQGValue(preferences.getInt("milestone_Id", 0), "P"));
            s_excessQty.setText(" "+databaseQueryClass.getSumTotaEQValue(preferences.getInt("milestone_Id", 0)));
            s_shortageQty.setText(" "+databaseQueryClass.getSumTotaSQValue(preferences.getInt("milestone_Id", 0)));
            //  s_totalQty.setText("" + 100 + "%");
            //  updatedQty.setText("" + String.format("%.02f", uq) + "%");
            //  s_pendingQty.setText("" + String.format("%.02f", pq) + "%");


            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (progressBarValue < (int) uq) {
                        progressBarValue++;

                        handler.post(new Runnable() {

                            @Override
                            public void run() {

                                Progressbar.setProgress(progressBarValue);
                                ShowText.setText(progressBarValue + "%");

                            }
                        });
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    while (progressBarValue1 < (int) pq) {
                        progressBarValue1++;

                        handler.post(new Runnable() {

                            @Override
                            public void run() {

                                progressBar2.setProgress(progressBarValue1);
                                textView2.setText("Pending Quantity : " + progressBarValue1 + "%");

                            }
                        });
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            if (AppStatus.getInstance(ResumeActivity.this).isOnline()) {
                new getSummaryStockList().execute();

            } else {

                Toast.makeText(ResumeActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
            }
        }


    }

    private class getSummaryStockList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        //put("emailAddress", SampleFragment.this.user_id);
                        put("mileStoneId", preferences.getInt("milestone_Id", 0));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            stockListResponse = serviceAccess.SendHttpPost(Config.URL_STOCKSUMMARY, jsonLeadObj);
            Log.i("resp", stockListResponse);
            if (stockListResponse.compareTo("") != 0) {
                if (isJSONValid(stockListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                jsonObject = new JSONObject(stockListResponse);
                                long t = Long.parseLong(jsonObject.getString("totalStockItem_Count"));
                                long u = Long.parseLong(jsonObject.getString("updatedStockItem_Count"));
                                long p = Long.parseLong(jsonObject.getString("pendingStockItem_Count"));
                                float uq = (float) ((u / t) * 100);
                                float pq = (float) ((p / t) * 100);
                                s_totalQty.setText(" " + jsonObject.getString("totalStockItem_Count"));
                                updatedQty.setText(" " + jsonObject.getString("updatedStockItem_Count"));
                                s_pendingQty.setText(" " +jsonObject.getString("pendingStockItem_Count"));
                                s_totalValues.setText(" "+jsonObject.getString("totalValues"));
                                s_excessQty.setText(" "+jsonObject.getString("excessQuantity"));
                                s_shortageQty.setText(" "+jsonObject.getString("shortageQuantity"));
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        while (progressBarValue < (int) uq) {
                                            progressBarValue++;

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress(progressBarValue);
                                                    ShowText.setText(progressBarValue + "%");

                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        while (progressBarValue1 < (int) pq) {
                                            progressBarValue1++;

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    progressBar2.setProgress(progressBarValue1);
                                                    textView2.setText("Pending Quantity : " + progressBarValue1 + "%");

                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
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
                        Toast.makeText(ResumeActivity.this, "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            //  mProgressDialog.dismiss();

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

