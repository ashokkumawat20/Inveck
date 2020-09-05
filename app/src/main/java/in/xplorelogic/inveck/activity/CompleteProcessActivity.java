package in.xplorelogic.inveck.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.adapter.SampleRecyclerAdapter;
import in.xplorelogic.inveck.adapter.StockListAdpter;
import in.xplorelogic.inveck.adapter.TotalStockRecyclerAdapter;
import in.xplorelogic.inveck.jsonparser.JsonHelper;
import in.xplorelogic.inveck.models.Stock;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.PaginationListener;
import in.xplorelogic.inveck.utils.WebClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static in.xplorelogic.inveck.utils.PaginationListener.PAGE_START;

public class CompleteProcessActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    JSONObject jsonLeadObj, jsonObject;
    ProgressDialog mProgressDialog;
    String stockListResponse = "";
    //
    List<Stock> data;
    JSONArray jsonArray;
    ImageView serach_hide, clear, adList;
    EditText search_milestone;
    private boolean mAscendingOrder[] = {true, true, true, true};
    LinearLayout footer1;
    ImageView sortData, filterData, back_arrow1;
    RelativeLayout footer;
    Button Continue;
    TextView s_totalQty, s_totalValues,updatedQty, s_pendingQty, s_excessQty, s_shortageQty;
    private int currentPage = PAGE_START;

    @BindView(R.id.listView)
    RecyclerView mList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private TotalStockRecyclerAdapter adapter;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    TextView shideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_process);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        mList = (RecyclerView) findViewById(R.id.listView);
        serach_hide = (ImageView) findViewById(R.id.serach_hide);
        clear = (ImageView) findViewById(R.id.clear);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        back_arrow1 = (ImageView) findViewById(R.id.back_arrow1);
        this.search_milestone = (EditText) findViewById(R.id.search);
        adList = (ImageView) findViewById(R.id.adList);
        sortData = (ImageView) findViewById(R.id.sortData);
        filterData = (ImageView) findViewById(R.id.filterData);
        footer = (RelativeLayout) findViewById(R.id.footer);
        footer1 = (LinearLayout) findViewById(R.id.footer1);
        Continue = (Button) findViewById(R.id.Continue);
        s_totalQty = (TextView) findViewById(R.id.s_totalQty);
        s_totalValues= (TextView) findViewById(R.id.s_totalValues);
        updatedQty = (TextView) findViewById(R.id.updatedQty);
        s_pendingQty = (TextView) findViewById(R.id.s_pendingQty);
        s_excessQty = (TextView) findViewById(R.id.s_excessQty);
        s_shortageQty = (TextView) findViewById(R.id.s_shortageQty);
        shideo = (TextView) findViewById(R.id.shideo);
        back_arrow1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });

        if (AppStatus.getInstance(CompleteProcessActivity.this).isOnline()) {
            new getSummaryStockList().execute();
            new getStockList().execute();
        } else {

            Toast.makeText(CompleteProcessActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      /*          AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProcessActivity.this);
                builder.setMessage("Do you want to take sign Off ?")
                        .setCancelable(false)
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(CompleteProcessActivity.this, TakeSignOffActivity.class);
                                startActivity(intent);

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
                //  alert.setTitle("Logout");
                alert.show();*/
                Intent intent = new Intent(CompleteProcessActivity.this, ExShortProcessOnLine.class);
                startActivity(intent);
            }
        });
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
        addTextListener();


        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    footer1.setVisibility(View.GONE);

                } else if (dy > 0) {
                    footer1.setVisibility(View.VISIBLE);
                }
            }
        });
        filterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(CompleteProcessActivity.this, filterData);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_filtermilstone, popup.getMenu());
                //   popup.getMenu().add(preferences.getString("quantity_for_contacts", ""));
                popup.show();
                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Toast.makeText(DisplayStudentEditPreActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("All")) {
                            adapter = new TotalStockRecyclerAdapter(CompleteProcessActivity.this, data);
                            mList.setLayoutManager(new LinearLayoutManager(CompleteProcessActivity.this));
                            mList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed

                        } else if (item.getTitle().equals("New")) {

                            final List<Stock> filteredList = new ArrayList<Stock>();
                            if (data != null) {
                                if (data.size() > 0) {


                                    for (int i = 0; i < data.size(); i++) {
                                        String subject = data.get(i).getIsQtySync().toLowerCase();
                                        if (subject.contains("n")) {
                                            filteredList.add(data.get(i));
                                        }
                                    }
                                }
                            }


                            adapter = new TotalStockRecyclerAdapter(CompleteProcessActivity.this, filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(CompleteProcessActivity.this));
                            mList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed


                        } else if (item.getTitle().equals("In Progress")) {
                            final List<Stock> filteredList = new ArrayList<Stock>();
                            if (data != null) {
                                if (data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        String subject = data.get(i).getIsQtySync().toLowerCase();
                                        if (subject.contains("y")) {
                                            filteredList.add(data.get(i));
                                        }
                                    }
                                }
                            }

                            adapter = new TotalStockRecyclerAdapter(CompleteProcessActivity.this, filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(CompleteProcessActivity.this));
                            mList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed

                        } else if (item.getTitle().equals("Completed")) {
                            final List<Stock> filteredList = new ArrayList<Stock>();
                            if (data != null) {
                                if (data.size() > 0) {


                                    for (int i = 0; i < data.size(); i++) {
                                        String subject = data.get(i).getIsQtySync().toLowerCase();
                                        if (subject.contains("c")) {
                                            filteredList.add(data.get(i));
                                        }
                                    }
                                }
                            }
                            adapter = new TotalStockRecyclerAdapter(CompleteProcessActivity.this, filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(CompleteProcessActivity.this));

                            mList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed
                        }


                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
        sortData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(CompleteProcessActivity.this, sortData);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_sortstockaudit, popup.getMenu());
                //   popup.getMenu().add(preferences.getString("quantity_for_contacts", ""));
                popup.show();
                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Toast.makeText(DisplayStudentEditPreActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("Sort Highest Qtv to Lowest Qtv")) {

                            // Show items ascending
                            mAscendingOrder[0] = true;
                            adapter.sortByQtyDesc();

                        } else if (item.getTitle().equals("Sort Lowest Qtv to Highest Qtv")) {
                            if (mAscendingOrder[0]) {
                                // Show items descending
                                mAscendingOrder[0] = false;
                                adapter.sortByQtyAsc();

                            }


                        } else if (item.getTitle().equals("Sort Highest Value to Lowest Value")) {
                            // Show items ascending
                            mAscendingOrder[0] = true;
                            adapter.sortByValueDesc();

                        } else if (item.getTitle().equals("Sort Lowest Value to Highest Value")) {
                            if (mAscendingOrder[0]) {
                                // Show items descending
                                mAscendingOrder[0] = false;
                                adapter.sortByValueAsc();

                            }
                        }


                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });


        swipeRefresh.setOnRefreshListener(this);

        mList.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(CompleteProcessActivity.this);
        mList.setLayoutManager(layoutManager);

        adapter = new TotalStockRecyclerAdapter(CompleteProcessActivity.this, new ArrayList<>());
        mList.setAdapter(adapter);

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        mList.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                // doApiCall();
                new getStockList().execute();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


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
                final List<Stock> filteredList = new ArrayList<Stock>();
                if (data != null) {
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {

                            String subject = data.get(i).getItem_Name().toLowerCase();
                            String tag = data.get(i).getItem_Nom().toLowerCase();
                            String msg_txt = data.get(i).getStock_Location().toLowerCase();
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

                mList.setLayoutManager(new LinearLayoutManager(CompleteProcessActivity.this));
                adapter = new TotalStockRecyclerAdapter(CompleteProcessActivity.this, filteredList);
                mList.setAdapter(adapter);
                adapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    //

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
            data = new ArrayList<>();
            if (stockListResponse.compareTo("") != 0) {
                if (isJSONValid(stockListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                jsonObject = new JSONObject(stockListResponse);
                                s_totalQty.setText(jsonObject.getString("totalQuantity"));
                                s_totalValues.setText(" "+jsonObject.getString("totalValues"));
                                updatedQty.setText(jsonObject.getString("updatedQuantity"));
                                s_pendingQty.setText(jsonObject.getString("pendingQuantity"));
                                s_excessQty.setText(jsonObject.getString("excessQuantity"));
                                s_shortageQty.setText(jsonObject.getString("shortageQuantity"));

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
                        Toast.makeText(CompleteProcessActivity.this, "Please check your network connection.", Toast.LENGTH_LONG).show();
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

    @Override
    public void onRefresh() {
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        // doApiCall();
        new getStockList().execute();
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
            stockListResponse = serviceAccess.SendHttpGet(Config.URL_MILESTONEDETAILS + "/PENDING/" + Integer.parseInt(preferences.getString("inventry_user_id", "")) + "/" + preferences.getInt("milestone_Id", 0) + "/" + preferences.getInt("client_Id", 0) + "/" + currentPage, jsonLeadObj);
            Log.i("resp", stockListResponse);
            if (stockListResponse.compareTo("") != 0) {
                if (isJSONValid(stockListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                jsonObject = new JSONObject(stockListResponse);
                                totalPage = jsonObject.getInt("total_pages");
                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseStockList(stockListResponse);


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
                        //Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {
                if (currentPage != PAGE_START) adapter.removeLoading();
                adapter.addItems(data);
                swipeRefresh.setRefreshing(false);

                // check weather is last page or not
                if (currentPage < totalPage) {
                    adapter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;
            } else {
                swipeRefresh.setVisibility(View.GONE);
                shideo.setVisibility(View.VISIBLE);

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
