package in.xplorelogic.inveck.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.adapter.StockListAdpter;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.models.Stock;

public class ExShortProcessOffLine extends AppCompatActivity {
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
    TextView s_totalQty, updatedQty, s_pendingQty, s_excessQty, s_shortageQty;
    private RecyclerView mList;
    StockListAdpter stockListAdpter;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();
    TextView hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_short_process_off_line);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        mList = (RecyclerView) findViewById(R.id.listView);
        serach_hide = (ImageView) findViewById(R.id.serach_hide);
        clear = (ImageView) findViewById(R.id.clear);
        back_arrow1 = (ImageView) findViewById(R.id.back_arrow1);
        this.search_milestone = (EditText) findViewById(R.id.search);
        adList = (ImageView) findViewById(R.id.adList);
        sortData = (ImageView) findViewById(R.id.sortData);
        filterData = (ImageView) findViewById(R.id.filterData);
        footer = (RelativeLayout) findViewById(R.id.footer);
        footer1 = (LinearLayout) findViewById(R.id.footer1);
        Continue = (Button) findViewById(R.id.Continue);
        s_totalQty = (TextView) findViewById(R.id.s_totalQty);
        updatedQty = (TextView) findViewById(R.id.updatedQty);
        s_pendingQty = (TextView) findViewById(R.id.s_pendingQty);
        s_excessQty = (TextView) findViewById(R.id.s_excessQty);
        s_shortageQty = (TextView) findViewById(R.id.s_shortageQty);
        hide = (TextView) findViewById(R.id.hide);
        back_arrow1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });
        s_totalQty.setText("" + databaseQueryClass.getSumTotalQValue(preferences.getInt("milestone_Id", 0)));
        updatedQty.setText("" + databaseQueryClass.getSumTotalUQValue(preferences.getInt("milestone_Id", 0), "W"));
        s_pendingQty.setText("" + databaseQueryClass.getSumTotalUQValue(preferences.getInt("milestone_Id", 0), "P"));
        s_excessQty.setText("" + databaseQueryClass.getSumTotaEQValue(preferences.getInt("milestone_Id", 0)));
        s_shortageQty.setText("" + databaseQueryClass.getSumTotaSQValue(preferences.getInt("milestone_Id", 0)));
        data = new ArrayList<>();
        data.clear();
        data.addAll(databaseQueryClass.getAllExShortage(preferences.getInt("milestone_Id", 0)));
        if (data.size() == 0) {
            mList.setVisibility(View.GONE);
            hide.setVisibility(View.VISIBLE);
        }
        stockListAdpter = new StockListAdpter(getApplicationContext(), data);
        mList.setAdapter(stockListAdpter);
        mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        serach_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
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
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
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
                final PopupMenu popup = new PopupMenu(getApplicationContext(), filterData);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_filtermilstone, popup.getMenu());
                //   popup.getMenu().add(preferences.getString("quantity_for_contacts", ""));
                popup.show();
                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Toast.makeText(DisplayStudentEditPreActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("All")) {
                            stockListAdpter = new StockListAdpter(getApplicationContext(), data);
                            mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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


                            stockListAdpter = new StockListAdpter(getApplicationContext(), filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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

                            stockListAdpter = new StockListAdpter(getApplicationContext(), filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
                            stockListAdpter = new StockListAdpter(getApplicationContext(), filteredList);
                            mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
                final PopupMenu popup = new PopupMenu(getApplicationContext(), sortData);
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
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExShortProcessOffLine.this);
                builder.setMessage("Do you want to take sign Off ?")
                        .setCancelable(false)
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(ExShortProcessOffLine.this, TakeSignOffOfLineActivity.class);
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
                alert.show();
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

                mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                stockListAdpter = new StockListAdpter(getApplicationContext(), filteredList);
                mList.setAdapter(stockListAdpter);
                stockListAdpter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}