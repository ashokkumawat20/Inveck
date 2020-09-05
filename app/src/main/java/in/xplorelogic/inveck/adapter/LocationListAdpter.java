package in.xplorelogic.inveck.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.activity.PersonDetailActivity;
import in.xplorelogic.inveck.activity.ResumeActivity;
import in.xplorelogic.inveck.models.LocationListDAO;
import in.xplorelogic.inveck.models.Milestone;


public class LocationListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<LocationListDAO> data;
    LocationListDAO current;
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
    String centerListResponse = "", callbackStatusResponse = "";
    boolean status;
    String message = "";
    String msg = "";
    String user_id = "";
    String batch_id = "";
    String s_id = "";
    int clickfalg = 0;
    //   private static FeesListener mListener;

    // create constructor to innitilize context and data sent from MainActivity
    public LocationListAdpter(Context context, List<LocationListDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.location_list, parent, false);
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


        myHolder.location_name.setText(current.getLocation());
        myHolder.location_name.setTag(position);

        myHolder.quantity.setText("Quantity: " + current.getQuantity());
        myHolder.quantity.setTag(position);

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView location_name, quantity;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            location_name = (TextView) itemView.findViewById(R.id.location_name);
            quantity = (TextView) itemView.findViewById(R.id.quantity);

        }

    }


}
