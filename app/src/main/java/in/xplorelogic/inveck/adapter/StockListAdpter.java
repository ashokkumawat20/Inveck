package in.xplorelogic.inveck.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.activity.TakeImagesActivity;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.models.Stock;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.ConnectivityReceiver;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.WebClient;
import in.xplorelogic.inveck.view.UpdateLocationEntryView;


public class StockListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Stock> data;
    Stock current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;
    int number = 1;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String sms_type = "", fn = "";
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String updateQuantityResponse = "", flagftos = "", remark = "";
    boolean status;
    String message = "";
    String msg = "";
    String user_id = "";
    String batch_id = "";
    int flag, count = 0, editm = 0;
    int qu, s_id;
    private boolean isLoaderVisible = false;
    //   private static FeesListener mListener;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();
    AlertDialog dialog = null;

    // create constructor to innitilize context and data sent from MainActivity
    public StockListAdpter(Context context, List<Stock> data) {
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
        myHolder.is_Item_Type.setTag(position);
        if (current.getIs_Item_Type().equals("Y")) {
            myHolder.is_Item_Type.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Item_Type.setVisibility(View.GONE);
        }
        myHolder.is_Item_No.setTag(position);
        if (current.getIs_Item_No().equals("Y")) {
            myHolder.is_Item_No.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Item_No.setVisibility(View.GONE);
        }
        myHolder.is_Item_Name.setTag(position);
        myHolder.is_Item_No.setTag(position);
        if (current.getIs_Item_Name().equals("Y")) {
            myHolder.is_Item_Name.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Item_Name.setVisibility(View.GONE);
        }
        myHolder.is_Batch.setTag(position);
        if (current.getIs_Batch().equals("Y")) {
            myHolder.is_Batch.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Batch.setVisibility(View.GONE);
        }
        myHolder.is_BUn.setTag(position);
        if (current.getIs_BUn().equals("Y")) {
            myHolder.is_BUn.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_BUn.setVisibility(View.GONE);
        }
        myHolder.is_Eun.setTag(position);
        if (current.getIs_Eun().equals("Y")) {
            myHolder.is_Eun.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Eun.setVisibility(View.GONE);
        }
        myHolder.is_Lot_No.setTag(position);
        if (current.getIs_Lot_No().equals("Y")) {
            myHolder.is_Lot_No.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Lot_No.setVisibility(View.GONE);
        }
        myHolder.is_Container_No.setTag(position);
        if (current.getIs_Container_No().equals("Y")) {
            myHolder.is_Container_No.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Container_No.setVisibility(View.GONE);
        }
        myHolder.is_Total_Quantity.setTag(position);
        if (current.getIs_Total_Quantity().equals("Y")) {
            myHolder.is_Total_Quantity.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Total_Quantity.setVisibility(View.GONE);
        }
        myHolder.is_Make.setTag(position);
        if (current.getIs_Make().equals("Y")) {
            myHolder.is_Make.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Make.setVisibility(View.GONE);
        }
        myHolder.is_Unrestricted_Quantity.setTag(position);
        if (current.getIs_Unrestricted_Quantity().equals("Y")) {
            myHolder.is_Unrestricted_Quantity.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Unrestricted_Quantity.setVisibility(View.GONE);
        }
        myHolder.is_Value_Unrestricted.setTag(position);
        if (current.getIs_Value_Unrestricted().equals("Y")) {
            myHolder.is_Value_Unrestricted.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Value_Unrestricted.setVisibility(View.GONE);
        }
        myHolder.is_Blocked_Quantity.setTag(position);
        if (current.getIs_Blocked_Quantity().equals("Y")) {
            myHolder.is_Blocked_Quantity.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Blocked_Quantity.setVisibility(View.GONE);
        }
        myHolder.is_Value_Blocked_Stock.setTag(position);
        if (current.getIs_Value_Blocked_Stock().equals("Y")) {
            myHolder.is_Value_Blocked_Stock.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Value_Blocked_Stock.setVisibility(View.GONE);
        }
        myHolder.is_Factory.setTag(position);
        if (current.getIs_Factory().equals("Y")) {
            myHolder.is_Factory.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Factory.setVisibility(View.GONE);
        }
        myHolder.is_Warehouse.setTag(position);
        if (current.getIs_Warehouse().equals("Y")) {
            myHolder.is_Warehouse.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Warehouse.setVisibility(View.GONE);
        }
        myHolder.is_Whitem.setTag(position);
        if (current.getIs_Whitem().equals("Y")) {
            myHolder.is_Whitem.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Whitem.setVisibility(View.GONE);
        }
        myHolder.is_Stock_Zone.setTag(position);
        if (current.getIs_Stock_Zone().equals("Y")) {
            myHolder.is_Stock_Zone.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Stock_Zone.setVisibility(View.GONE);
        }
        myHolder.is_Stock_Location.setTag(position);
        if (current.getIs_Stock_Location().equals("Y")) {
            myHolder.is_Stock_Location.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Stock_Location.setVisibility(View.GONE);
        }
        myHolder.is_DF_store_location_level.setTag(position);
        if (current.getIs_DF_store_location_level().equals("Y")) {
            myHolder.is_DF_store_location_level.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_DF_store_location_level.setVisibility(View.GONE);
        }
        myHolder.is_Stock_Segment.setTag(position);
        if (current.getIs_Stock_Segment().equals("Y")) {
            myHolder.is_Stock_Segment.setVisibility(View.VISIBLE);
        } else {
            myHolder.is_Stock_Segment.setVisibility(View.GONE);
        }

        myHolder.quantity_stock.setText("" + current.getInputQty());
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
        myHolder.ftosheet.setTag(position);
        myHolder.card_view.setTag(position);
        myHolder.yellowsign.setTag(position);
        myHolder.remarks.setTag(position);
        if (current.getIsFloorToSheet().equals("Y")) {
            myHolder.card_view.setBackgroundColor(Color.parseColor("#C0C0C0"));
        } else {
            myHolder.card_view.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if (current.getFloorToSheetBy().equals("USER")) {
            myHolder.ftosheet.setVisibility(View.GONE);
        } else {
            myHolder.ftosheet.setVisibility(View.VISIBLE);
        }
        myHolder.remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                s_id = current.getId();
               /* AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Please enter remark!");
                final EditText input = new EditText(context);
                if (!current.getRemark().equals("null")) {
                    input.setText(current.getRemark());
                }
                input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                // alertDialog.setIcon(R.drawable.msg_img);
                alertDialog.setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                remark = input.getText().toString();
                                if (!remark.equals("")) {
                                    current.setRemark(remark);
                                    msg = "Remark Added Successfully";
                                    if (AppStatus.getInstance(context).isOnline()) {
                                        myHolder.redsign.setVisibility(View.GONE);
                                        myHolder.greensign.setVisibility(View.VISIBLE);
                                        long id1 = databaseQueryClass.UpdateRemark(s_id, remark);
                                        context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                                    } else {
                                        myHolder.greensign.setVisibility(View.GONE);
                                        myHolder.yellowsign.setVisibility(View.VISIBLE);
                                        long id1 = databaseQueryClass.UpdateRemark(s_id, remark);

                                    }
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Please enter remark!", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();*/

                // Create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Name");

                // set the custom layout
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View customLayout = li.inflate(R.layout.custom_layout, null);
                builder.setView(customLayout);
                LinearLayout send = customLayout.findViewById(R.id.btnSend);
                ImageView back_arrow1 = customLayout.findViewById(R.id.back_arrow1);
                EditText editText = customLayout.findViewById(R.id.descEdtTxt);
                TextView itemName = customLayout.findViewById(R.id.itemName);
                itemName.setText(""+current.getItem_Name());
                if (!current.getRemark().equals("null")) {
                    editText.setText(current.getRemark());
                }
                back_arrow1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }

                });
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (editText.getText().toString().trim().equals("")) {
                            Toast.makeText(context, "Please enter remark!", Toast.LENGTH_LONG).show();
                        } else {
                            msg = "Remark Added Successfully";
                            remark = editText.getText().toString();
                            current.setRemark(remark);
                            if (AppStatus.getInstance(context).isOnline()) {
                                myHolder.redsign.setVisibility(View.GONE);
                                myHolder.greensign.setVisibility(View.VISIBLE);
                                long id1 = databaseQueryClass.UpdateRemark(s_id, remark);
                                context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                            } else {
                                myHolder.greensign.setVisibility(View.GONE);
                                myHolder.yellowsign.setVisibility(View.VISIBLE);
                                long id1 = databaseQueryClass.UpdateRemark(s_id, remark);

                            }
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                });
                // add a button
            /*    builder
                        .setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {

                                        // send data from the
                                        // AlertDialog to the Activity
                                        EditText editText = customLayout.findViewById(R.id.cPassEdtTxt);

                                    }
                                });
*/
                // create and show
                // the alert dialog
                dialog = builder.create();
                dialog.show();

            }
        });
        myHolder.ftosheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                s_id = current.getId();
                if (current.getIsFloorToSheet().equals("N")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to Mark Floor to Sheet ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    current.setIsFloorToSheet("Y");
                                    if (AppStatus.getInstance(context).isOnline()) {
                                        // new submitData().execute();
                                        long id1 = databaseQueryClass.UpdateFtoSheet(s_id, "Y");
                                        myHolder.card_view.setBackgroundColor(Color.parseColor("#C0C0C0"));
                                        myHolder.redsign.setVisibility(View.GONE);
                                        myHolder.greensign.setVisibility(View.VISIBLE);

                                        if (AppStatus.getInstance(context).isOnline()) {
                                            context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                                        }
                                    } else {
                                        myHolder.greensign.setVisibility(View.GONE);
                                        myHolder.yellowsign.setVisibility(View.VISIBLE);
                                        long id1 = databaseQueryClass.UpdateFtoSheet(s_id, "Y");
                                        myHolder.card_view.setBackgroundColor(Color.parseColor("#C0C0C0"));

                                        // Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();


                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    //alert.setTitle("Logout");
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to Unmark Floor to Sheet ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    current.setIsFloorToSheet("N");
                                    if (AppStatus.getInstance(context).isOnline()) {
                                        // new submitData().execute();
                                        long id1 = databaseQueryClass.UpdateFtoSheet(s_id, "N");
                                        myHolder.card_view.setBackgroundColor(Color.parseColor("#ffffff"));
                                        myHolder.redsign.setVisibility(View.GONE);
                                        myHolder.greensign.setVisibility(View.VISIBLE);

                                        if (AppStatus.getInstance(context).isOnline()) {
                                            context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                                        }
                                    } else {
                                        myHolder.greensign.setVisibility(View.GONE);
                                        myHolder.yellowsign.setVisibility(View.VISIBLE);
                                        long id1 = databaseQueryClass.UpdateFtoSheet(s_id, "N");
                                        myHolder.card_view.setBackgroundColor(Color.parseColor("#ffffff"));

                                        // Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();


                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    //alert.setTitle("Logout");
                    alert.show();
                }
            }
        });
        myHolder.bluetick.setTag(position);
        myHolder.greentick.setTag(position);
        myHolder.redsign.setTag(position);
        myHolder.greensign.setTag(position);
        myHolder.takeImage.setTag(position);
        myHolder.location.setTag(position);
        myHolder.sh_totalQty.setTag(position);
        myHolder.sh_totalQty.setTag(position);


        if (current.getShrinkQty() != 0) {
            myHolder.sh_totalQty.setVisibility(View.VISIBLE);
            myHolder.sh_totalQty.setText("" + current.getShrinkQty());
        } else {
            myHolder.sh_totalQty.setVisibility(View.GONE);
        }
        if (current.getExtentedQty() != 0) {
            myHolder.ex_totalQty.setVisibility(View.VISIBLE);
            myHolder.ex_totalQty.setText("+" + current.getExtentedQty());
        } else {
            myHolder.ex_totalQty.setVisibility(View.GONE);
        }
        String s = current.getIsQtySync();
        if (s != null) {
            // Log.d("Value", s);
            if (s.equals("N")) {
                myHolder.greentick.setVisibility(View.GONE);
                //  myHolder.greensign.setVisibility(View.GONE);
                myHolder.bluetick.setVisibility(View.VISIBLE);
                // myHolder.redsign.setVisibility(View.VISIBLE);

            }
            if (current.getIsQtySync().equals("Y")) {
                myHolder.bluetick.setVisibility(View.GONE);
                // myHolder.redsign.setVisibility(View.GONE);
                myHolder.greentick.setVisibility(View.VISIBLE);
                // myHolder.greensign.setVisibility(View.VISIBLE);
            }
        }
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
            // myHolder.greensign.setVisibility(View.GONE);
            myHolder.bluetick.setVisibility(View.VISIBLE);
            //  myHolder.redsign.setVisibility(View.VISIBLE);

        } else {
            myHolder.bluetick.setVisibility(View.GONE);
            //  myHolder.redsign.setVisibility(View.GONE);
            myHolder.greentick.setVisibility(View.VISIBLE);
            //  myHolder.greensign.setVisibility(View.VISIBLE);
        }
        if (current.getSync_status() == 0) {
            myHolder.redsign.setVisibility(View.GONE);
            myHolder.greensign.setVisibility(View.GONE);
            myHolder.yellowsign.setVisibility(View.VISIBLE);
        }
        if (current.getSync_status() == 1) {
            myHolder.yellowsign.setVisibility(View.GONE);
            myHolder.redsign.setVisibility(View.GONE);
            myHolder.greensign.setVisibility(View.VISIBLE);
        }
        if (current.getSync_status() == 2) {

            myHolder.greensign.setVisibility(View.GONE);
            myHolder.yellowsign.setVisibility(View.GONE);
            myHolder.redsign.setVisibility(View.VISIBLE);
        }
        myHolder.right_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                fn = current.getIsQtySync();
                final String q = myHolder.quantity_stock.getText().toString();
                if (current.getFloorToSheetBy().equals("USER")) {

                } else if (q.equals("") && !fn.equals("N")) {
                    s_id = current.getId();
                    myHolder.greensign.setVisibility(View.GONE);
                    myHolder.redsign.setVisibility(View.VISIBLE);
                    myHolder.ex_totalQty.setVisibility(View.GONE);
                    myHolder.sh_totalQty.setVisibility(View.GONE);
                    current.setIsQtySync("N");
                    fn = "N";
                    prefEditor.putInt("edflag", 0);
                    prefEditor.putString("itemname", "");
                    prefEditor.commit();
                    count = 0;
                    int qu = 0, s = 0, e = 0;

                    if (AppStatus.getInstance(context).isOnline()) {
                        // new submitData().execute();

                        long id1 = databaseQueryClass.UpdateStockQuantity(qu, s_id, s, e, 2, "N", 1);
                        if (AppStatus.getInstance(context).isOnline()) {
                            myHolder.greensign.setVisibility(View.GONE);
                            myHolder.yellowsign.setVisibility(View.GONE);
                            myHolder.redsign.setVisibility(View.VISIBLE);
                            context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                        }
                    } else {
                        myHolder.greensign.setVisibility(View.GONE);
                        myHolder.yellowsign.setVisibility(View.GONE);
                        myHolder.redsign.setVisibility(View.VISIBLE);
                        long id1 = databaseQueryClass.UpdateStockQuantity(qu, s_id, s, e, 2, "N", 1);

                        // Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                } else if (q.equals("0") || q.equals("") && fn.equals("N") && editm == 0) {
                    //Toast.makeText(context, "N", Toast.LENGTH_LONG).show();
                    myHolder.bluetick.setVisibility(View.GONE);
                    // myHolder.redsign.setVisibility(View.GONE);
                    myHolder.greentick.setVisibility(View.VISIBLE);
                    qu = current.getTotal_Quantity();
                    myHolder.quantity_stock.setText("" + qu);
                    int s = 0, e = 0;
                    s_id = current.getId();
                    current.setIsQtySync("Y");
                    prefEditor.putInt("edflag", 0);
                    prefEditor.putString("itemname", "");
                    prefEditor.commit();
                    count = 0;
                    if (AppStatus.getInstance(context).isOnline()) {
                        // new submitData().execute();

                        long id1 = databaseQueryClass.UpdateStockQuantity(qu, s_id, s, e, 0, "Y", 0);
                        if (AppStatus.getInstance(context).isOnline()) {
                            myHolder.redsign.setVisibility(View.GONE);
                            myHolder.greensign.setVisibility(View.VISIBLE);
                            context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                        }
                    } else {
                        myHolder.greensign.setVisibility(View.GONE);
                        myHolder.yellowsign.setVisibility(View.VISIBLE);
                        long id1 = databaseQueryClass.UpdateStockQuantity(qu, s_id, s, e, 0, "Y", 0);

                        // Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                } else if (current.getId() != preferences.getInt("edflag", 0) && count == 1) {
                    Toast.makeText(context, "Process incomplete at previous item " + preferences.getString("itemname", ""), Toast.LENGTH_LONG).show();
                } else if (!q.equals("") && myHolder.bluetick.getVisibility() == View.VISIBLE && editm == 1) {
                    myHolder.bluetick.setVisibility(View.GONE);
                    // myHolder.redsign.setVisibility(View.GONE);
                    myHolder.greentick.setVisibility(View.VISIBLE);
                    //  myHolder.greensign.setVisibility(View.VISIBLE);
                    qu = Integer.parseInt(q);
                    int mq = current.getTotal_Quantity();
                    current.setIsQtySync("Y");
                    int s = 0, e = 0;
                    if (mq == qu) {
                        s = 0;
                        e = 0;
                        myHolder.ex_totalQty.setVisibility(View.GONE);
                        myHolder.sh_totalQty.setVisibility(View.GONE);
                    } else if (mq > qu) {
                        s = mq - qu;
                        myHolder.ex_totalQty.setVisibility(View.GONE);
                        myHolder.sh_totalQty.setVisibility(View.VISIBLE);
                        myHolder.sh_totalQty.setText("-" + s);


                    } else {
                        e = qu - mq;
                        myHolder.sh_totalQty.setVisibility(View.GONE);
                        myHolder.ex_totalQty.setVisibility(View.VISIBLE);
                        myHolder.ex_totalQty.setText("+" + e);
                    }
                    s_id = current.getId();
                    prefEditor.putInt("edflag", 0);
                    prefEditor.putString("itemname", "");
                    prefEditor.commit();
                    count = 0;
                    if (AppStatus.getInstance(context).isOnline()) {
                        // new submitData().execute();

                        long id1 = databaseQueryClass.UpdateStockQuantity(qu, s_id, s, e, 0, "Y", 0);
                        if (AppStatus.getInstance(context).isOnline()) {
                            myHolder.redsign.setVisibility(View.GONE);
                            myHolder.greensign.setVisibility(View.VISIBLE);
                            context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                        }
                    } else {
                        myHolder.greensign.setVisibility(View.GONE);
                        myHolder.redsign.setVisibility(View.VISIBLE);
                        long id1 = databaseQueryClass.UpdateStockQuantity(qu, s_id, s, e, 0, "Y", 0);

                        // Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }

                 /*   AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                                    int mq = preferences.getInt("tq", 0);
                                    int s = 0, e = 0;
                                    if (mq == qu) {
                                        s = 0;
                                        e = 0;
                                        myHolder.ex_totalQty.setVisibility(View.GONE);
                                        myHolder.sh_totalQty.setVisibility(View.GONE );
                                    } else if (mq > qu) {
                                        s = mq - qu;
                                        myHolder.ex_totalQty.setVisibility(View.GONE);
                                        myHolder.sh_totalQty.setVisibility(View.VISIBLE);
                                        myHolder.sh_totalQty.setText("" + s);


                                    } else {
                                        e = qu - mq;
                                        myHolder.sh_totalQty.setVisibility(View.GONE);
                                        myHolder.ex_totalQty.setVisibility(View.VISIBLE);
                                        myHolder.ex_totalQty.setText("+" + e);
                                    }
                                    s_id = current.getId();
                                    prefEditor.putInt("edflag", 0);
                                    prefEditor.putString("itemname", "");
                                    prefEditor.commit();
                                    count = 0;
                                   *//* if (AppStatus.getInstance(context).isOnline()) {
                                        new submitData().execute();
                                    } else {

                                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                                    }*//*
                                    long id1 = databaseQueryClass.UpdateStockQuantity(qu, s_id, s, e);

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
                    alert.show();*/

                } else if (myHolder.greentick.getVisibility() == View.VISIBLE) {
                    Toast.makeText(context, "Physical quantity updated", Toast.LENGTH_LONG).show();
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

/*        myHolder.quantity_stock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!fn.equals("N")) {
                    //  Toast.makeText(context, "Data Sync" + s.toString(), Toast.LENGTH_SHORT).show();
                    myHolder.greentick.setVisibility(View.GONE);
                    // myHolder.greensign.setVisibility(View.GONE);
                    myHolder.bluetick.setVisibility(View.VISIBLE);
                    //  myHolder.redsign.setVisibility(View.VISIBLE);
                }
            }
        });*/

        myHolder.quantity_stock.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Toast.makeText(context, "Data Sync" + s.toString(), Toast.LENGTH_SHORT).show();
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putInt("edflag", current.getId());
                prefEditor.putString("itemname", current.getItem_Name());
                prefEditor.commit();
                if (count == 0) {
                    count++;
                }
                myHolder.greentick.setVisibility(View.GONE);
                // myHolder.greensign.setVisibility(View.GONE);
                myHolder.bluetick.setVisibility(View.VISIBLE);
                editm = 1;
                return false;
            }
        });
    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView s_itemCode, s_itemType, s_itemName, s_batch, s_contnrNo, s_totalQty, s_make, s_unrstctdQty, s_valueUnrstctd, s_blockQty, s_valueBlcStck, s_bun, s_eun, s_lotNo, s_factory, sh_totalQty, ex_totalQty;
        LinearLayout clickMileStone, hide_layout, is_Item_Type, is_Item_No, is_Item_Name, is_Batch, is_BUn, is_Eun, is_Lot_No, is_Container_No, is_Total_Quantity, is_Make, is_Unrestricted_Quantity, is_Value_Unrestricted, is_Blocked_Quantity, is_Value_Blocked_Stock, is_Factory, is_Warehouse, is_Whitem, is_Stock_Zone, is_Stock_Location, is_DF_store_location_level, is_Stock_Segment;
        TextView s_wearhouse, s_whitem, s_stockZone, s_stcLctn, s_dfStckL, s_stockSgmnt;
        ImageView dArrow, bluetick, greentick, redsign, greensign, up_arrow, takeImage, ftosheet, yellowsign;
        FrameLayout right_click, location, remarks;
        EditText quantity_stock;
        CardView card_view;

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
            remarks = (FrameLayout) stock_row.findViewById(R.id.remarks);
            bluetick = (ImageView) stock_row.findViewById(R.id.bluetick);
            greentick = (ImageView) stock_row.findViewById(R.id.greentick);
            redsign = (ImageView) stock_row.findViewById(R.id.redsign);
            greensign = (ImageView) stock_row.findViewById(R.id.greensign);
            takeImage = (ImageView) stock_row.findViewById(R.id.takeImage);
            ftosheet = (ImageView) stock_row.findViewById(R.id.ftosheet);
            yellowsign = (ImageView) stock_row.findViewById(R.id.yellowsign);
            quantity_stock = (EditText) stock_row.findViewById(R.id.quantity_stock);
            card_view = (CardView) stock_row.findViewById(R.id.card_view);
            is_Item_Type = (LinearLayout) stock_row.findViewById(R.id.is_Item_Type);
            is_Item_No = (LinearLayout) stock_row.findViewById(R.id.is_Item_No);
            is_Item_Name = (LinearLayout) stock_row.findViewById(R.id.is_Item_Name);
            is_Batch = (LinearLayout) stock_row.findViewById(R.id.is_Batch);
            is_BUn = (LinearLayout) stock_row.findViewById(R.id.is_BUn);
            is_Eun = (LinearLayout) stock_row.findViewById(R.id.is_Eun);
            is_Lot_No = (LinearLayout) stock_row.findViewById(R.id.is_Lot_No);
            is_Container_No = (LinearLayout) stock_row.findViewById(R.id.is_Container_No);
            is_Total_Quantity = (LinearLayout) stock_row.findViewById(R.id.is_Total_Quantity);
            is_Make = (LinearLayout) stock_row.findViewById(R.id.is_Make);
            is_Unrestricted_Quantity = (LinearLayout) stock_row.findViewById(R.id.is_Unrestricted_Quantity);
            is_Value_Unrestricted = (LinearLayout) stock_row.findViewById(R.id.is_Value_Unrestricted);
            is_Blocked_Quantity = (LinearLayout) stock_row.findViewById(R.id.is_Blocked_Quantity);
            is_Value_Blocked_Stock = (LinearLayout) stock_row.findViewById(R.id.is_Value_Blocked_Stock);
            is_Factory = (LinearLayout) stock_row.findViewById(R.id.is_Factory);
            is_Warehouse = (LinearLayout) stock_row.findViewById(R.id.is_Warehouse);
            is_Whitem = (LinearLayout) stock_row.findViewById(R.id.is_Whitem);
            is_Stock_Zone = (LinearLayout) stock_row.findViewById(R.id.is_Stock_Zone);
            is_Stock_Location = (LinearLayout) stock_row.findViewById(R.id.is_Stock_Location);
            is_DF_store_location_level = (LinearLayout) stock_row.findViewById(R.id.is_DF_store_location_level);
            is_Stock_Segment = (LinearLayout) stock_row.findViewById(R.id.is_Stock_Segment);

        }

    }

    /**
     * Sort shopping list by name descending
     */
    public void sortByQtyDesc() {
        Comparator<Stock> comparator = new Comparator<Stock>() {

            @Override
            public int compare(Stock object1, Stock object2) {
                return object2.getTotal_Quantity() - object1.getTotal_Quantity();
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }
//Sort shopping list by name ascending

    public void sortByQtyAsc() {
        Comparator<Stock> comparator = new Comparator<Stock>() {

            @Override
            public int compare(Stock object1, Stock object2) {
                return object1.getTotal_Quantity() - (object2.getTotal_Quantity());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }


    public void sortByValueDesc() {
        Comparator<Stock> comparator = new Comparator<Stock>() {

            @Override
            public int compare(Stock object1, Stock object2) {
                return object2.getValue_Blocked_Stock() - object1.getValue_Blocked_Stock();
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }


    public void sortByValueAsc() {
        Comparator<Stock> comparator = new Comparator<Stock>() {

            @Override
            public int compare(Stock object1, Stock object2) {
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
            mProgressDialog.setMessage("Updating FloorToSheet...");
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
                        put("status", qu);
                        //put("isMileStoneIncharge", "S");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };


            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_UPDATESTOCKFROMFLOORTOSHEET, jsonLeadObj);
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
