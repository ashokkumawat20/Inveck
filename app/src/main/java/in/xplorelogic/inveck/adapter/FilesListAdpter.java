package in.xplorelogic.inveck.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.activity.FullScreenViewActivity;
import in.xplorelogic.inveck.activity.FullScreenViewActivity1;
import in.xplorelogic.inveck.activity.MilestoneListActivity;
import in.xplorelogic.inveck.activity.SplashScreen;
import in.xplorelogic.inveck.activity.TakeImagesActivity;
import in.xplorelogic.inveck.models.FilesModel;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.GetFileFromServer;
import in.xplorelogic.inveck.utils.NetworkClient;
import in.xplorelogic.inveck.utils.UploadAPIs;
import in.xplorelogic.inveck.utils.Utils;
import in.xplorelogic.inveck.utils.WebClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FilesListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<FilesModel> data;
    FilesModel current;
    int ID;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String deleteResponse = "";


    // create constructor to innitilize context and data sent from MainActivity
    public FilesListAdpter(Context context, List<FilesModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_files_details, parent, false);
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
        if (preferences.getInt("sync_status", 0) == 0) {
            myHolder.imageView.setImageBitmap(current.getBitmap());
            myHolder.imageView.setTag(position);
        } else {
            Uri fileUri = Uri.parse(current.getFile_path());
            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            myHolder.imageView.setImageBitmap(bitmap);
            myHolder.imageView.setTag(position);
        }


        myHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                if (preferences.getInt("sync_status", 0) == 0) {
                    if (AppStatus.getInstance(context).isOnline()) {
                        Intent intent = new Intent(context, FullScreenViewActivity1.class);
                        intent.putExtra("path", current.getFile_path());
                        context.startActivity(intent);
                    } else {

                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(context, FullScreenViewActivity1.class);
                    intent.putExtra("path", current.getFile_path());
                    context.startActivity(intent);
                }

            }
        });
        myHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                if (preferences.getInt("sync_status", 0) == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to Delete ?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // new deleteFile().execute();
                                    if (AppStatus.getInstance(context).isOnline()) {
                                        Utils.showDeleteLoadingDialog(context);
                                        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                                        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
                                        Call<ResponseBody> deleteRequest = uploadAPIs.deleteFile(current.getId());
                                        deleteRequest.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                // use response.code, response.headers, etc.
                                                Utils.dismissLoadingDialog();
                                                removeAt(ID);
                                                Toast.makeText(context, " Photo deleted successfully", Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                // handle failure
                                                Utils.dismissLoadingDialog();
                                                Toast.makeText(context, " Photo not deleted successfully", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    } else {

                                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                                    }


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
                    alert.setTitle("Delete");
                    alert.show();
                } else {

                }
                return false;
            }
        });
    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {


        ImageView imageView;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }

    }

    //
    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    private class deleteFile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Deleting file...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {

                        //put("isMileStoneIncharge", "S");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };


            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            deleteResponse = serviceAccess.SendHttpPost(Config.URL_UPDATESTOCKQUANTITY, jsonLeadObj);
            Log.i("resp", deleteResponse);
            Log.i("json", "json" + jsonLeadObj);
            ;


            return null;

        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            mProgressDialog.dismiss();
            Toast.makeText(context, deleteResponse, Toast.LENGTH_LONG).show();
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
