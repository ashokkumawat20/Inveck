package in.xplorelogic.inveck.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.adapter.MileStonesListAdpter;
import in.xplorelogic.inveck.adapter.SampleRecyclerAdapter;
import in.xplorelogic.inveck.adapter.StockListAdpter;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.jsonparser.JsonHelper;
import in.xplorelogic.inveck.models.Stock;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.Listener;
import in.xplorelogic.inveck.utils.PaginationListener;
import in.xplorelogic.inveck.utils.WebClient;
import in.xplorelogic.inveck.view.EntryDataLineItemView;
import in.xplorelogic.inveck.view.UpdateLocationEntryView;

import static in.xplorelogic.inveck.utils.PaginationListener.PAGE_START;

public class SampleFragment1 extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    JSONObject jsonLeadObj, jsonObject;
    ProgressDialog mProgressDialog;
    String stockListResponse = "";
    //
    List<Stock> data;
    StockListAdpter stockListAdpter;
    private RecyclerView mList;
    JSONArray jsonArray;
    ImageView serach_hide, clear, adList;
    EditText search_milestone;
    private boolean mAscendingOrder[] = {true, true, true, true};
    LinearLayout showhide;
    ImageView logout, sortData, filterData;
    RelativeLayout footer;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 5;
    private boolean isLoading = false;
    LinearLayoutManager layoutManager;
    String user_email = "";
    ImageView AddLineItems;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sample1, container, false);
        preferences = getActivity().getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        mList = (RecyclerView) v.findViewById(R.id.listView);
        serach_hide = (ImageView) v.findViewById(R.id.serach_hide);
        clear = (ImageView) v.findViewById(R.id.clear);
        this.search_milestone = (EditText) v.findViewById(R.id.search);
        adList = (ImageView) v.findViewById(R.id.adList);
        sortData = (ImageView) v.findViewById(R.id.sortData);
        filterData = (ImageView) v.findViewById(R.id.filterData);
        AddLineItems = (ImageView) v.findViewById(R.id.AddLineItems);
        footer = (RelativeLayout) v.findViewById(R.id.footer);
        data = new ArrayList<>();
        user_email = preferences.getString("inventry_user_Email", "");
        data.clear();
        data.addAll(databaseQueryClass.getAllSample(user_email, preferences.getInt("milestone_Id", 0)));
        stockListAdpter = new StockListAdpter(getActivity(), data);
        mList.setAdapter(stockListAdpter);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
       /* if (AppStatus.getInstance(getActivity()).isOnline()) {

          //  new getStockList().execute();

        } else {
            data.clear();
            data.addAll(databaseQueryClass.getAllSample(user_email));
            stockListAdpter = new StockListAdpter(getActivity(), data);
            mList.setAdapter(stockListAdpter);
            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }*/
        AddLineItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    EntryDataLineItemView entryDataLineItemView = new EntryDataLineItemView();
                    entryDataLineItemView.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "entryDataLineItemView");


            }
        });
        serach_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                clear.setVisibility(View.VISIBLE);
                serach_hide.setVisibility(View.GONE);

            }
        });
        EntryDataLineItemView.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                data.clear();
                data.addAll(databaseQueryClass.getAllSample(user_email, preferences.getInt("milestone_Id", 0)));
                stockListAdpter = new StockListAdpter(getActivity(), data);
                mList.setAdapter(stockListAdpter);
                mList.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_milestone.setText("");
                serach_hide.setVisibility(View.VISIBLE);
                clear.setVisibility(View.GONE);
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
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
                    footer.setVisibility(View.GONE);

                } else if (dy > 0) {
                    footer.setVisibility(View.VISIBLE);
                }
            }
        });
        filterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(getActivity(), filterData);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_filtermilstone, popup.getMenu());
                //   popup.getMenu().add(preferences.getString("quantity_for_contacts", ""));
                popup.show();
                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Toast.makeText(DisplayStudentEditPreActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("All")) {
                            stockListAdpter = new StockListAdpter(getActivity(), data);
                            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            mList.setAdapter(stockListAdpter);
                            stockListAdpter.notifyDataSetChanged();  // data set changed

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


                            stockListAdpter = new StockListAdpter(getActivity(), filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            mList.setAdapter(stockListAdpter);
                            stockListAdpter.notifyDataSetChanged();  // data set changed


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

                            stockListAdpter = new StockListAdpter(getActivity(), filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            mList.setAdapter(stockListAdpter);
                            stockListAdpter.notifyDataSetChanged();  // data set changed

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
                            stockListAdpter = new StockListAdpter(getActivity(), filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(getActivity()));

                            mList.setAdapter(stockListAdpter);
                            stockListAdpter.notifyDataSetChanged();  // data set changed
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
                final PopupMenu popup = new PopupMenu(getActivity(), sortData);
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
                            stockListAdpter.sortByQtyDesc();

                        } else if (item.getTitle().equals("Sort Lowest Qtv to Highest Qtv")) {
                            if (mAscendingOrder[0]) {
                                // Show items descending
                                mAscendingOrder[0] = false;
                                stockListAdpter.sortByQtyAsc();

                            }


                        } else if (item.getTitle().equals("Sort Highest Value to Lowest Value")) {
                            // Show items ascending
                            mAscendingOrder[0] = true;
                            stockListAdpter.sortByValueDesc();

                        } else if (item.getTitle().equals("Sort Lowest Value to Highest Value")) {
                            if (mAscendingOrder[0]) {
                                // Show items descending
                                mAscendingOrder[0] = false;
                                stockListAdpter.sortByValueAsc();

                            }
                        }


                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });


        return v;
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

                mList.setLayoutManager(new LinearLayoutManager(getActivity()));
                stockListAdpter = new StockListAdpter(getActivity(), filteredList);
                mList.setAdapter(stockListAdpter);
                stockListAdpter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    //
    private class getStockList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading Stock...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        //put("emailAddress", SampleFragment.this.user_id);
                        put("userId", Integer.parseInt(preferences.getString("inventry_user_id", "")));
                        put("mileStoneId", preferences.getInt("milestone_Id", 0));
                        put("customerId", preferences.getInt("client_Id", 0));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            stockListResponse = serviceAccess.SendHttpGet(Config.URL_MILESTONEDETAILS + "/SAMPLE/" + Integer.parseInt(preferences.getString("inventry_user_id", "")) + "/" + preferences.getInt("milestone_Id", 0) + "/" + preferences.getInt("client_Id", 0) + "/" + currentPage, jsonLeadObj);
            Log.i("resp", stockListResponse);

            if (stockListResponse.compareTo("") != 0) {
                if (isJSONValid(stockListResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                jsonObject = new JSONObject(stockListResponse);
                                totalPage = jsonObject.getInt("total_pages");
                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseStockList(stockListResponse);
                                jsonArray = new JSONArray(stockListResponse);


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
                        Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();
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


                stockListAdpter = new StockListAdpter(getActivity(), data);
                mList.setAdapter(stockListAdpter);
                mList.setLayoutManager(new LinearLayoutManager(getActivity()));
                /*mList.setItemViewCacheSize(data.size());

                if (currentPage != PAGE_START) stockListAdpter.removeLoading();
                stockListAdpter.addItems(data);
                //swipeRefresh.setRefreshing(false);

                // check weather is last page or not
                if (currentPage < totalPage) {
                    stockListAdpter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;*/
            } else {


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
