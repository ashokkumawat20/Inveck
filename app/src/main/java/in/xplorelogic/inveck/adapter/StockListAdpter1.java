package in.xplorelogic.inveck.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.activity.TakeImagesActivity;
import in.xplorelogic.inveck.models.Sample;
import in.xplorelogic.inveck.models.Stock;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.WebClient;
import in.xplorelogic.inveck.view.UpdateLocationEntryView;


public class StockListAdpter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Sample> data;
    Sample current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;
    int number = 1;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String sms_type = "";


    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String updateQuantityResponse = "";
    boolean status;
    String message = "";
    String msg = "";
    String user_id = "";
    String batch_id = "";

    int qu, s_id;
    private boolean isLoaderVisible = false;
    //   private static FeesListener mListener;

    // create constructor to innitilize context and data sent from MainActivity
    public StockListAdpter1(Context context, List<Sample> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.stock_row, parent, false);
        MyHolder holder = new MyHolder(view);


        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);
        myHolder.quantity_stock.setText("" + current.getTotal_Quantity());
        myHolder.quantity_stock.setTag(position);
        myHolder.s_itemCode.setText(current.getItem_Nom());
        myHolder.s_itemCode.setTag(position);
        myHolder.s_itemType.setText(current.getItem_Type());
        myHolder.s_itemType.setTag(position);
        myHolder.s_itemName.setText(current.getItem_Name());
        myHolder.s_itemName.setTag(position);
        myHolder.s_batch.setText(current.getBatch());
        myHolder.s_batch.setTag(position);
        myHolder.s_contnrNo.setText(current.getContainer_No());
        myHolder.s_contnrNo.setTag(position);
        myHolder.s_totalQty.setText(Integer.toString(current.getTotal_Quantity()));
        myHolder.s_totalQty.setTag(position);
        myHolder.s_make.setText(current.getMake());
        myHolder.s_make.setTag(position);
        myHolder.s_unrstctdQty.setText(Integer.toString(current.getUnrestricted_Quantity()));
        myHolder.s_unrstctdQty.setTag(position);
        myHolder.s_valueUnrstctd.setText(Integer.toString(current.getValue_Unrestricted()));
        myHolder.s_valueUnrstctd.setTag(position);
        myHolder.s_blockQty.setText(Integer.toString(current.getBlocked_Quantity()));
        myHolder.s_blockQty.setTag(position);
        myHolder.s_valueBlcStck.setText(Integer.toString(current.getValue_Blocked_Stock()));
        myHolder.s_valueBlcStck.setTag(position);
        myHolder.s_bun.setText(current.getbUn());
        myHolder.s_bun.setTag(position);
        myHolder.s_eun.setText(current.getEun());
        myHolder.s_eun.setTag(position);
        myHolder.s_lotNo.setText(current.getLot_No());
        myHolder.s_lotNo.setTag(position);
        myHolder.s_factory.setText(current.getFactory());
        myHolder.s_factory.setTag(position);
        myHolder.s_wearhouse.setText(current.getWarehouse());
        myHolder.s_wearhouse.setTag(position);
        myHolder.s_whitem.setText(current.getWhitem());
        myHolder.s_whitem.setTag(position);
        myHolder.s_stockZone.setText(current.getStock_Zone());
        myHolder.s_stockZone.setTag(position);
        myHolder.s_stcLctn.setText(current.getStock_Location());
        myHolder.s_stcLctn.setTag(position);
        myHolder.s_dfStckL.setText(current.getdF_store_location_level());
        myHolder.s_dfStckL.setTag(position);
        myHolder.s_stockSgmnt.setText(current.getStock_Segment());
        myHolder.s_stockSgmnt.setTag(position);
        myHolder.dArrow.setTag(position);
        myHolder.up_arrow.setTag(position);

        myHolder.right_click.setTag(position);
        myHolder.hide_layout.setTag(position);


        myHolder.bluetick.setTag(position);
        myHolder.greentick.setTag(position);
        myHolder.redsign.setTag(position);
        myHolder.greensign.setTag(position);
        myHolder.takeImage.setTag(position);
        myHolder.location.setTag(position);

        myHolder.dArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                myHolder.dArrow.setVisibility(View.GONE);
                myHolder.hide_layout.setVisibility(View.VISIBLE);
                myHolder.up_arrow.setVisibility(View.VISIBLE);

            }
        });
        myHolder.up_arrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                myHolder.hide_layout.setVisibility(View.GONE);
                myHolder.up_arrow.setVisibility(View.GONE);
                myHolder.hide_layout.setVisibility(View.GONE);
                myHolder.dArrow.setVisibility(View.VISIBLE);

            }
        });
        if (current.getIsQtySync().equals("N")) {
            myHolder.greentick.setVisibility(View.GONE);
            myHolder.greensign.setVisibility(View.GONE);
            myHolder.bluetick.setVisibility(View.VISIBLE);
            myHolder.redsign.setVisibility(View.VISIBLE);

        } else {
            myHolder.bluetick.setVisibility(View.GONE);
            myHolder.redsign.setVisibility(View.GONE);
            myHolder.greentick.setVisibility(View.VISIBLE);
            myHolder.greensign.setVisibility(View.VISIBLE);
        }
        myHolder.right_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                String str2 = "user_Id";
                String str3 = "milestone_Id";
                String str4 = "client_Id";
                final String q = myHolder.quantity_stock.getText().toString();
                if (q.equals("")) {
                    Toast.makeText(context, "Quantity Can Not Be Empty", Toast.LENGTH_LONG).show();
                } else if (myHolder.bluetick.getVisibility() == View.VISIBLE) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to update Quantity ?")
                            .setCancelable(false)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    myHolder.bluetick.setVisibility(View.GONE);
                                    myHolder.redsign.setVisibility(View.GONE);
                                    myHolder.greentick.setVisibility(View.VISIBLE);
                                    myHolder.greensign.setVisibility(View.VISIBLE);
                                    qu = Integer.parseInt(q);
                                    s_id = current.getId();
                                    if (AppStatus.getInstance(context).isOnline()) {
                                        new submitData().execute();
                                    } else {

                                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                                    }

                                    //  Toast.makeText(context, "Data Sync"+q, Toast.LENGTH_SHORT).show();

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
                    alert.setTitle("Update Quantity");
                    alert.show();

                } else if (myHolder.greentick.getVisibility() == View.VISIBLE) {
                    Toast.makeText(context, "Data Already Sync", Toast.LENGTH_LONG).show();
                }


            }
        });

        myHolder.takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                Intent intent = new Intent(context, TakeImagesActivity.class);
                intent.putExtra("stockId", current.getId());
                intent.putExtra("itemName", current.getItem_Name());
                intent.putExtra("mileStoneId", current.getMilestoneId());
                context.startActivity(intent);
            }
        });
        myHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putInt("stockId", current.getId());
                prefEditor.putInt("mileStoneId", current.getMilestoneId());
                prefEditor.putString("itemName", current.getItem_Name());
                prefEditor.commit();
                UpdateLocationEntryView updateLocationEntryView = new UpdateLocationEntryView();
                updateLocationEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "updateLocationEntryView");

            }
        });
    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void addItems(List<Sample> postItems) {
        data.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        data.add(new Sample());
        notifyItemInserted(data.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = data.size() - 1;
        Sample item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    Sample getItem(int position) {
        return data.get(position);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView s_itemCode, s_itemType, s_itemName, s_batch, s_contnrNo, s_totalQty, s_make, s_unrstctdQty, s_valueUnrstctd, s_blockQty, s_valueBlcStck, s_bun, s_eun, s_lotNo, s_factory, sh_totalQty, ex_totalQty;
        LinearLayout clickMileStone, hide_layout;
        TextView s_wearhouse, s_whitem, s_stockZone, s_stcLctn, s_dfStckL, s_stockSgmnt;
        ImageView dArrow, bluetick, greentick, redsign, greensign, up_arrow, takeImage;
        FrameLayout right_click, location;
        EditText quantity_stock;

        // create constructor to get widget reference
        public MyHolder(View stock_row) {
            super(stock_row);
            sh_totalQty = (TextView) stock_row.findViewById(R.id.sh_totalQty);
            ex_totalQty = (TextView) stock_row.findViewById(R.id.ex_totalQty);
            s_itemCode = (TextView) stock_row.findViewById(R.id.s_itemCode);
            s_itemType = (TextView) stock_row.findViewById(R.id.s_itemType);
            s_itemName = (TextView) stock_row.findViewById(R.id.s_itemName);
            s_batch = (TextView) stock_row.findViewById(R.id.s_batch);
            s_contnrNo = (TextView) stock_row.findViewById(R.id.s_contnrNo);
            s_totalQty = (TextView) stock_row.findViewById(R.id.s_totalQty);
            s_make = (TextView) stock_row.findViewById(R.id.s_make);
            s_unrstctdQty = (TextView) stock_row.findViewById(R.id.s_unrstctdQty);
            s_valueUnrstctd = (TextView) stock_row.findViewById(R.id.s_valueUnrstctd);
            s_blockQty = (TextView) stock_row.findViewById(R.id.s_blockQty);
            s_valueBlcStck = (TextView) stock_row.findViewById(R.id.s_valueBlcStck);
            s_bun = (TextView) stock_row.findViewById(R.id.s_bun);
            s_eun = (TextView) stock_row.findViewById(R.id.s_eun);
            s_lotNo = (TextView) stock_row.findViewById(R.id.s_lotNo);
            s_factory = (TextView) stock_row.findViewById(R.id.s_factory);
            s_wearhouse = (TextView) stock_row.findViewById(R.id.s_wearhouse);
            s_whitem = (TextView) stock_row.findViewById(R.id.s_whitem);
            s_stockZone = (TextView) stock_row.findViewById(R.id.s_stockZone);
            s_stcLctn = (TextView) stock_row.findViewById(R.id.s_stcLctn);
            s_dfStckL = (TextView) stock_row.findViewById(R.id.s_dfStckL);
            s_stockSgmnt = (TextView) stock_row.findViewById(R.id.s_stockSgmnt);
            dArrow = (ImageView) stock_row.findViewById(R.id.down_arrow);
            up_arrow = (ImageView) stock_row.findViewById(R.id.up_arrow);
            hide_layout = (LinearLayout) stock_row.findViewById(R.id.hide_layout);
            right_click = (FrameLayout) stock_row.findViewById(R.id.right_click);
            location = (FrameLayout) stock_row.findViewById(R.id.location);
            bluetick = (ImageView) stock_row.findViewById(R.id.bluetick);
            greentick = (ImageView) stock_row.findViewById(R.id.greentick);
            redsign = (ImageView) stock_row.findViewById(R.id.redsign);
            greensign = (ImageView) stock_row.findViewById(R.id.greensign);
            takeImage = (ImageView) stock_row.findViewById(R.id.takeImage);
            quantity_stock = (EditText) stock_row.findViewById(R.id.quantity_stock);
        }

    }

    /**
     * Sort shopping list by name descending
     */
    public void sortByQtyDesc() {
        Comparator<Sample> comparator = new Comparator<Sample>() {

            @Override
            public int compare(Sample object1, Sample object2) {
                return object2.getTotal_Quantity() - object1.getTotal_Quantity();
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }
//Sort shopping list by name ascending

    public void sortByQtyAsc() {
        Comparator<Sample> comparator = new Comparator<Sample>() {

            @Override
            public int compare(Sample object1, Sample object2) {
                return object1.getTotal_Quantity() - (object2.getTotal_Quantity());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }


    public void sortByValueDesc() {
        Comparator<Sample> comparator = new Comparator<Sample>() {

            @Override
            public int compare(Sample object1, Sample object2) {
                return object2.getValue_Blocked_Stock() - object1.getValue_Blocked_Stock();
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }


    public void sortByValueAsc() {
        Comparator<Sample> comparator = new Comparator<Sample>() {

            @Override
            public int compare(Sample object1, Sample object2) {
                return object1.getValue_Blocked_Stock() - (object2.getValue_Blocked_Stock());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    /* *
    public void sortByTimingsDesc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object2.getTimings().compareToIgnoreCase(object1.getTimings());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    *//**
     * Sort shopping list by name ascending
     *//*
    public void sortByTimingsAsc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object1.getTimings().compareToIgnoreCase(object2.getTimings());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    *//**
     * Sort shopping list by name descending
     *//*
    public void sortByBranchDesc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object2.getBranch_name().compareToIgnoreCase(object1.getBranch_name());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    */

    /**
     * Sort shopping list by name ascending
     *//*
    public void sortByBranchAsc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object1.getBranch_name().compareToIgnoreCase(object2.getBranch_name());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    public static void bindListener(FeesListener listener) {
        mListener = listener;
    }*/

    //

    private class submitData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Updating Quantity...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("stockId", s_id);
                        put("quantity", qu);
                        //put("isMileStoneIncharge", "S");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };


            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_UPDATESTOCKQUANTITY, jsonLeadObj);
            Log.i("resp", updateQuantityResponse);
            Log.i("json", "json" + jsonLeadObj);
            ;


            if (updateQuantityResponse.compareTo("") != 0) {
                if (isJSONValid(updateQuantityResponse)) {


                    try {

                        JSONObject jsonObject = new JSONObject(updateQuantityResponse);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {

                    // Toast.makeText(context, "Please check your webservice", Toast.LENGTH_LONG).show();

                }
            } else {

                // Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            mProgressDialog.dismiss();
            Toast.makeText(context, updateQuantityResponse, Toast.LENGTH_LONG).show();
        }
    }

    //
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
