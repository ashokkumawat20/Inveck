package in.xplorelogic.inveck.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.models.LocationListDAO;
import in.xplorelogic.inveck.models.TakeSignOffDAO;


public class SignOffListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<TakeSignOffDAO> data;
    TakeSignOffDAO current;
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
    public SignOffListAdpter(Context context, List<TakeSignOffDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.signoff_list, parent, false);
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


        myHolder.location_name.setText(current.getFullName());
        myHolder.location_name.setTag(position);

        myHolder.designation.setText(current.getDesignation());
        myHolder.designation.setTag(position);

        myHolder.email.setText(current.getEmail());
        myHolder.email.setTag(position);

        myHolder.contactNo.setText(current.getContactNo());
        myHolder.contactNo.setTag(position);


    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView location_name, designation,email,contactNo;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            location_name = (TextView) itemView.findViewById(R.id.location_name);
            designation = (TextView) itemView.findViewById(R.id.designation);
            email = (TextView) itemView.findViewById(R.id.email);
            contactNo = (TextView) itemView.findViewById(R.id.contactNo);
        }

    }


}
