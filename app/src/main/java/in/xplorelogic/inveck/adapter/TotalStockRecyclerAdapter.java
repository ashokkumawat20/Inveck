package in.xplorelogic.inveck.adapter;

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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.activity.TakeImagesActivity;
import in.xplorelogic.inveck.models.Stock;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.ConnectivityReceiver;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.WebClient;
import in.xplorelogic.inveck.view.UpdateLocationEntryView;

public class TotalStockRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private Context context;
    private List<Stock> mPostItems;

    int qu, s_id;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String updateQuantityResponse = "";
    boolean status;
    String msg = "", flagftos = "", fn = "", ftos = "", remark = "";
    int flag, count = 0, editm = 0;
    AlertDialog dialog = null;
    public TotalStockRecyclerAdapter(Context context, List<Stock> postItems) {
        this.context = context;
        this.mPostItems = postItems;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_row, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPostItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }

    public void addItems(List<Stock> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new Stock());
        notifyItemInserted(mPostItems.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        Stock item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    Stock getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.quantity_stock)
        TextView quantity_stock;
        @BindView(R.id.s_itemCode)
        TextView s_itemCode;
        @BindView(R.id.s_itemType)
        TextView s_itemType;
        @BindView(R.id.s_itemName)
        TextView s_itemName;
        @BindView(R.id.s_batch)
        TextView s_batch;
        @BindView(R.id.s_contnrNo)
        TextView s_contnrNo;
        @BindView(R.id.s_totalQty)
        TextView s_totalQty;
        @BindView(R.id.ex_totalQty)
        TextView ex_totalQty;
        @BindView(R.id.sh_totalQty)
        TextView sh_totalQty;
        @BindView(R.id.s_make)
        TextView s_make;
        @BindView(R.id.s_unrstctdQty)
        TextView s_unrstctdQty;
        @BindView(R.id.s_valueUnrstctd)
        TextView s_valueUnrstctd;
        @BindView(R.id.s_blockQty)
        TextView s_blockQty;
        @BindView(R.id.s_valueBlcStck)
        TextView s_valueBlcStck;
        @BindView(R.id.s_bun)
        TextView s_bun;
        @BindView(R.id.s_eun)
        TextView s_eun;
        @BindView(R.id.s_lotNo)
        TextView s_lotNo;
        @BindView(R.id.s_factory)
        TextView s_factory;
        @BindView(R.id.s_wearhouse)
        TextView s_wearhouse;
        @BindView(R.id.s_whitem)
        TextView s_whitem;
        @BindView(R.id.s_stockZone)
        TextView s_stockZone;
        @BindView(R.id.s_stcLctn)
        TextView s_stcLctn;
        @BindView(R.id.s_dfStckL)
        TextView s_dfStckL;
        @BindView(R.id.s_stockSgmnt)
        TextView s_stockSgmnt;
        @BindView(R.id.down_arrow)
        ImageView dArrow;
        @BindView(R.id.up_arrow)
        ImageView up_arrow;
        @BindView(R.id.hide_layout)
        LinearLayout hide_layout;
        @BindView(R.id.greentick)
        ImageView greentick;
        @BindView(R.id.redsign)
        ImageView redsign;
        @BindView(R.id.greensign)
        ImageView greensign;
        @BindView(R.id.bluetick)
        ImageView bluetick;
        @BindView(R.id.right_click)
        FrameLayout right_click;
        @BindView(R.id.location)
        FrameLayout location;
        @BindView(R.id.takeImage)
        ImageView takeImage;
        @BindView(R.id.ftosheet)
        ImageView ftosheet;
        @BindView(R.id.card_view)
        CardView card_view;
        @BindView(R.id.remarks)
        FrameLayout remarks;
        @BindView(R.id.signature)
        FrameLayout signature;
        @BindView(R.id.is_Item_Type)
        LinearLayout is_Item_Type;
        @BindView(R.id.is_Item_No)
        LinearLayout is_Item_No;
        @BindView(R.id.is_Item_Name)
        LinearLayout is_Item_Name;
        @BindView(R.id.is_Batch)
        LinearLayout is_Batch;
        @BindView(R.id.is_BUn)
        LinearLayout is_BUn;
        @BindView(R.id.is_Eun)
        LinearLayout is_Eun;
        @BindView(R.id.is_Lot_No)
        LinearLayout is_Lot_No;
        @BindView(R.id.is_Container_No)
        LinearLayout is_Container_No;
        @BindView(R.id.is_Total_Quantity)
        LinearLayout is_Total_Quantity;
        @BindView(R.id.is_Make)
        LinearLayout is_Make;
        @BindView(R.id.is_Unrestricted_Quantity)
        LinearLayout is_Unrestricted_Quantity;
        @BindView(R.id.is_Value_Unrestricted)
        LinearLayout is_Value_Unrestricted;
        @BindView(R.id.is_Blocked_Quantity)
        LinearLayout is_Blocked_Quantity;
        @BindView(R.id.is_Value_Blocked_Stock)
        LinearLayout is_Value_Blocked_Stock;
        @BindView(R.id.is_Factory)
        LinearLayout is_Factory;
        @BindView(R.id.is_Warehouse)
        LinearLayout is_Warehouse;
        @BindView(R.id.is_Whitem)
        LinearLayout is_Whitem;
        @BindView(R.id.is_Stock_Zone)
        LinearLayout is_Stock_Zone;
        @BindView(R.id.is_Stock_Location)
        LinearLayout is_Stock_Location;
        @BindView(R.id.is_DF_store_location_level)
        LinearLayout is_DF_store_location_level;
        @BindView(R.id.is_Stock_Segment)
        LinearLayout is_Stock_Segment;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            Stock current = mPostItems.get(position);

            quantity_stock.setText("" + current.getInputQty());
            if (current.getIs_Item_No().equals("Y")) {
                s_itemCode.setText(current.getItem_Nom());
            } else {
                is_Item_No.setVisibility(View.GONE);
            }
            if (current.getIs_Item_Type().equals("Y")) {
                s_itemType.setText(current.getItem_Type());
            } else {
                is_Item_Type.setVisibility(View.GONE);
            }
            if (current.getIs_Item_Name().equals("Y")) {
                s_itemName.setText(current.getItem_Name());
            } else {
                is_Item_Name.setVisibility(View.GONE);
            }
            if (current.getIs_Batch().equals("Y")) {
                s_batch.setText(current.getBatch());
            } else {
                is_Batch.setVisibility(View.GONE);
            }
            if (current.getIs_Container_No().equals("Y")) {
                s_contnrNo.setText(current.getContainer_No());
            } else {
                is_Container_No.setVisibility(View.GONE);
            }
            if (current.getIs_Total_Quantity().equals("Y")) {
                s_totalQty.setText(Integer.toString(current.getTotal_Quantity()));
            } else {
                is_Total_Quantity.setVisibility(View.GONE);
            }
            if (current.getIs_Make().equals("Y")) {
                s_make.setText(current.getMake());
            } else {
                is_Make.setVisibility(View.GONE);
            }
            if (current.getIs_Unrestricted_Quantity().equals("Y")) {
                s_unrstctdQty.setText(Integer.toString(current.getUnrestricted_Quantity()));
            } else {
                is_Unrestricted_Quantity.setVisibility(View.GONE);
            }
            if (current.getIs_Value_Unrestricted().equals("Y")) {
                s_valueUnrstctd.setText(Integer.toString(current.getValue_Unrestricted()));
            } else {
                is_Value_Unrestricted.setVisibility(View.GONE);
            }
            if (current.getIs_Blocked_Quantity().equals("Y")) {
                s_blockQty.setText(Integer.toString(current.getBlocked_Quantity()));
            } else {
                is_Blocked_Quantity.setVisibility(View.GONE);
            }
            if (current.getIs_Value_Blocked_Stock().equals("Y")) {
                s_valueBlcStck.setText(Integer.toString(current.getValue_Blocked_Stock()));
            } else {
                is_Value_Blocked_Stock.setVisibility(View.GONE);
            }
            if (current.getIs_BUn().equals("Y")) {
                s_bun.setText(current.getbUn());
            } else {
                is_BUn.setVisibility(View.GONE);
            }
            if (current.getIs_Eun().equals("Y")) {
                s_eun.setText(current.getEun());
            } else {
                is_Eun.setVisibility(View.GONE);
            }
            if (current.getIs_Lot_No().equals("Y")) {
                s_lotNo.setText(current.getLot_No());
            } else {
                is_Lot_No.setVisibility(View.GONE);
            }
            if (current.getIs_Factory().equals("Y")) {
                s_factory.setText(current.getFactory());
            } else {
                is_Factory.setVisibility(View.GONE);
            }
            if (current.getIs_Warehouse().equals("Y")) {
                s_wearhouse.setText(current.getWarehouse());
            } else {
                is_Warehouse.setVisibility(View.GONE);
            }
            if (current.getIs_Whitem().equals("Y")) {
                s_whitem.setText(current.getWhitem());
            } else {
                is_Whitem.setVisibility(View.GONE);
            }
            if (current.getIs_Stock_Zone().equals("Y")) {
                s_stockZone.setText(current.getStock_Zone());
            } else {
                is_Stock_Zone.setVisibility(View.GONE);
            }
            if (current.getIs_Stock_Location().equals("Y")) {
                s_stcLctn.setText(current.getStock_Location());
            } else {
                is_Stock_Location.setVisibility(View.GONE);
            }
            if (current.getIs_DF_store_location_level().equals("Y")) {
                s_dfStckL.setText(current.getdF_store_location_level());
            } else {
                is_DF_store_location_level.setVisibility(View.GONE);
            }
            if (current.getIs_Stock_Segment().equals("Y")) {
                s_stockSgmnt.setText(current.getStock_Segment());
            } else {
                is_Stock_Segment.setVisibility(View.GONE);
            }

            if (current.getShrinkQty() != 0) {
                sh_totalQty.setVisibility(View.VISIBLE);
                sh_totalQty.setText("" + current.getShrinkQty());
            } else {
                sh_totalQty.setVisibility(View.GONE);
            }
            if (current.getExtentedQty() != 0) {
                ex_totalQty.setVisibility(View.VISIBLE);
                ex_totalQty.setText("+" + current.getExtentedQty());
            } else {
                ex_totalQty.setVisibility(View.GONE);
            }
            if (current.getIsFloorToSheet().equals("Y")) {
                card_view.setBackgroundColor(Color.parseColor("#C0C0C0"));
            } else {
                card_view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            if (current.getFloorToSheetBy().equals("USER")) {
                ftosheet.setVisibility(View.GONE);
            } else {
                ftosheet.setVisibility(View.VISIBLE);
            }
            if (current.getShrinkQty() != 0) {
                sh_totalQty.setVisibility(View.VISIBLE);
                sh_totalQty.setText("" + current.getShrinkQty());
            } else {
                sh_totalQty.setVisibility(View.GONE);
            }
            if (current.getExtentedQty() != 0) {
                ex_totalQty.setVisibility(View.VISIBLE);
                ex_totalQty.setText("+" + current.getExtentedQty());
            } else {
                ex_totalQty.setVisibility(View.GONE);
            }
            if (current.getIsFloorToSheet().equals("Y")) {
                card_view.setBackgroundColor(Color.parseColor("#C0C0C0"));
            } else {
                card_view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            if (current.getFloorToSheetBy().equals("USER")) {
                ftosheet.setVisibility(View.GONE);
            } else {
                ftosheet.setVisibility(View.VISIBLE);
            }
            remarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s_id = current.getId();
                    qu = current.getTotal_Quantity();
                    flagftos = current.getIsFloorToSheet();
                    /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
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
                                    if (AppStatus.getInstance(context).isOnline()) {
                                        if (!remark.equals("")) {
                                            current.setRemark(remark);
                                            msg = "Remark Added Successfully";
                                            new submitFtSData().execute();
                                        } else {
                                            Toast.makeText(context, "Please enter remark!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {

                                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
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
                    itemName.setText("" + current.getItem_Name());
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
                                    current.setRemark(remark);
                                    msg = "Remark Added Successfully";
                                    new submitFtSData().execute();

                                } else {

                                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                                }
                               // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
            ftosheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s_id = current.getId();
                    qu = current.getTotal_Quantity();
                    remark = current.getRemark();
                    if (current.getIsFloorToSheet().equals("N")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Do you want to Mark Floor to Sheet ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        current.setIsFloorToSheet("Y");
                                        card_view.setBackgroundColor(Color.parseColor("#C0C0C0"));
                                        flagftos = "Y";
                                        if (AppStatus.getInstance(context).isOnline()) {
                                            msg = "Floor to sheet Mark Successfully";
                                            new submitFtSData().execute();

                                        } else {
                                            Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
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
                                        card_view.setBackgroundColor(Color.parseColor("#ffffff"));
                                        flagftos = "N";
                                        if (AppStatus.getInstance(context).isOnline()) {
                                            msg = "Floor to sheet Unmark Successfully";
                                            new submitFtSData().execute();

                                        } else {
                                            Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
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
            String s = current.getIsQtySync();
            if (s != null) {
                // Log.d("Value", s);
                if (s.equals("N")) {
                    greentick.setVisibility(View.GONE);
                    greensign.setVisibility(View.GONE);
                    bluetick.setVisibility(View.VISIBLE);
                    redsign.setVisibility(View.VISIBLE);

                }
                if (current.getIsQtySync().equals("Y")) {
                    bluetick.setVisibility(View.GONE);
                    redsign.setVisibility(View.GONE);
                    greentick.setVisibility(View.VISIBLE);
                    greensign.setVisibility(View.VISIBLE);
                }
            }


            dArrow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dArrow.setVisibility(View.GONE);
                    hide_layout.setVisibility(View.VISIBLE);
                    up_arrow.setVisibility(View.VISIBLE);

                }
            });
            up_arrow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    hide_layout.setVisibility(View.GONE);
                    up_arrow.setVisibility(View.GONE);
                    hide_layout.setVisibility(View.GONE);
                    dArrow.setVisibility(View.VISIBLE);

                }
            });
            right_click.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final String q = quantity_stock.getText().toString();
                    fn = current.getIsQtySync();
                    ftos = current.getIsFloorToSheet();
                    remark = current.getRemark();
                    if (current.getFloorToSheetBy().equals("USER")) {
                        if (q.equals("")) {
                            qu = 0;
                        } else {
                            qu = Integer.parseInt(q);
                        }
                        current.setTotal_Quantity(qu);
                        s_totalQty.setText(q);
                        msg = "Floor to Sheet Quantity updated Successfully";
                        new submitUserData().execute();
                    } else if (q.equals("") && !fn.equals("N")) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            s_id = current.getId();
                            greensign.setVisibility(View.GONE);
                            redsign.setVisibility(View.VISIBLE);
                            ex_totalQty.setVisibility(View.GONE);
                            sh_totalQty.setVisibility(View.GONE);
                            current.setIsQtySync("N");
                            fn = "N";
                            prefEditor.putInt("edflag", 0);
                            prefEditor.putString("itemname", "");
                            prefEditor.commit();
                            count = 0;
                            msg = "Stock Quantity reset Successfully";
                            new restQuantityData().execute();

                        } else {
                            Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }
                        // Toast.makeText(context, "Quantity Can Not Be Empty", Toast.LENGTH_LONG).show();
                    } else if (q.equals("0") || q.equals("") && fn.equals("N") && editm == 0) {
                        //Toast.makeText(context, "N", Toast.LENGTH_LONG).show();
                        bluetick.setVisibility(View.GONE);
                        // myHolder.redsign.setVisibility(View.GONE);
                        greentick.setVisibility(View.VISIBLE);
                        qu = current.getTotal_Quantity();
                        quantity_stock.setText("" + qu);
                        int s = 0, e = 0;
                        s_id = current.getId();
                        current.setIsQtySync("Y");
                        prefEditor.putInt("edflag", 0);
                        prefEditor.putString("itemname", "");
                        prefEditor.commit();
                        count = 0;
                        if (AppStatus.getInstance(context).isOnline()) {
                            redsign.setVisibility(View.GONE);
                            greensign.setVisibility(View.VISIBLE);
                            msg = "Stock Quantity updated Successfully";
                            new submitData().execute();

                        } else {
                            Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }
                    } else if (current.getId() != preferences.getInt("edflag", 0) && count == 1) {
                        Toast.makeText(context, "Process incomplete at previous item " + preferences.getString("itemname", ""), Toast.LENGTH_LONG).show();
                    } else if (!q.equals("") && bluetick.getVisibility() == View.VISIBLE && editm == 1) {
                        bluetick.setVisibility(View.GONE);
                        //  redsign.setVisibility(View.GONE);
                        greentick.setVisibility(View.VISIBLE);
                        //   greensign.setVisibility(View.VISIBLE);
                        qu = Integer.parseInt(q);
                        int mq = current.getTotal_Quantity();
                        current.setIsQtySync("Y");
                        int s = 0, e = 0;
                        if (mq == qu) {
                            s = 0;
                            e = 0;
                            ex_totalQty.setVisibility(View.GONE);
                            sh_totalQty.setVisibility(View.GONE);
                        } else if (mq > qu) {
                            s = mq - qu;
                            ex_totalQty.setVisibility(View.GONE);
                            sh_totalQty.setVisibility(View.VISIBLE);
                            sh_totalQty.setText("" + s);


                        } else {
                            e = qu - mq;
                            sh_totalQty.setVisibility(View.GONE);
                            ex_totalQty.setVisibility(View.VISIBLE);
                            ex_totalQty.setText("+" + e);
                        }
                        s_id = current.getId();
                        prefEditor.putInt("edflag", 0);
                        prefEditor.putString("itemname", "");
                        prefEditor.commit();
                        count = 0;
                        editm = 0;
                        if (AppStatus.getInstance(context).isOnline()) {
                            redsign.setVisibility(View.GONE);
                            greensign.setVisibility(View.VISIBLE);
                            msg = "Stock Quantity updated Successfully";
                            new submitData().execute();

                        } else {
                            Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }

               /*         AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Do you want to update Quantity ?")
                                .setCancelable(false)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        bluetick.setVisibility(View.GONE);
                                        redsign.setVisibility(View.GONE);
                                        greentick.setVisibility(View.VISIBLE);
                                        greensign.setVisibility(View.VISIBLE);
                                        qu = Integer.parseInt(q);
                                        s_id = current.getId();
                                        prefEditor.putInt("edflag", 0);
                                        prefEditor.putString("itemname", "");
                                        prefEditor.commit();
                                        count = 0;
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
                        alert.show();*/

                    } else if (greentick.getVisibility() == View.VISIBLE) {
                        Toast.makeText(context, "Physical quantity updated", Toast.LENGTH_LONG).show();
                    }


                }
            });

            takeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TakeImagesActivity.class);
                    intent.putExtra("stockId", current.getId());
                    intent.putExtra("itemName", current.getItem_Name());
                    intent.putExtra("mileStoneId", current.getMilestoneId());
                    context.startActivity(intent);
                }
            });
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefEditor.putInt("stockId", current.getId());
                    prefEditor.putInt("mileStoneId", current.getMilestoneId());
                    prefEditor.putString("itemName", current.getItem_Name());
                    prefEditor.commit();
                    UpdateLocationEntryView updateLocationEntryView = new UpdateLocationEntryView();
                    updateLocationEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "updateLocationEntryView");

                }
            });

            quantity_stock.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // Toast.makeText(context, "Data Sync" + s.toString(), Toast.LENGTH_SHORT).show();
                    prefEditor.putInt("edflag", current.getId());
                    prefEditor.putString("itemname", current.getItem_Name());
                    prefEditor.commit();
                    if (count == 0) {
                        count++;
                    }
                    editm = 1;
                    greentick.setVisibility(View.GONE);
                    // greensign.setVisibility(View.GONE);
                    bluetick.setVisibility(View.VISIBLE);
                    return false;
                }
            });
        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }

    private class restQuantityData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Reset Quantity...");
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
                        //put("isMileStoneIncharge", "S");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };


            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_RESETSTOCKQUANTITY, jsonLeadObj);
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
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

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
                        put("remark", remark);
                        put("floortosheet_status", ftos);
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
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    private class submitUserData extends AsyncTask<Void, Void, Void> {
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
            updateQuantityResponse = serviceAccess.SendHttpPost(Config.URL_UPDATEFLOOTTOSHEETQUANTITY, jsonLeadObj);
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
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    private class submitFtSData extends AsyncTask<Void, Void, Void> {
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
                        put("quantity", qu);
                        put("remark", remark);
                        put("floortosheet_status", flagftos);
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
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
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
        Collections.sort(mPostItems, comparator);
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
        Collections.sort(mPostItems, comparator);
        notifyDataSetChanged();
    }


    public void sortByValueDesc() {
        Comparator<Stock> comparator = new Comparator<Stock>() {

            @Override
            public int compare(Stock object1, Stock object2) {
                return object2.getValue_Blocked_Stock() - object1.getValue_Blocked_Stock();
            }
        };
        Collections.sort(mPostItems, comparator);
        notifyDataSetChanged();
    }


    public void sortByValueAsc() {
        Comparator<Stock> comparator = new Comparator<Stock>() {

            @Override
            public int compare(Stock object1, Stock object2) {
                return object1.getValue_Blocked_Stock() - (object2.getValue_Blocked_Stock());
            }
        };
        Collections.sort(mPostItems, comparator);
        notifyDataSetChanged();
    }
}
