package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.adapter.FilesListAdpter;
import in.xplorelogic.inveck.adapter.MileStonesListAdpter;
import in.xplorelogic.inveck.database.DatabaseQueryClass;
import in.xplorelogic.inveck.jsonparser.JsonHelper;
import in.xplorelogic.inveck.models.FilesModel;
import in.xplorelogic.inveck.models.Milestone;
import in.xplorelogic.inveck.models.PersonDetails;
import in.xplorelogic.inveck.utils.AppStatus;
import in.xplorelogic.inveck.utils.CompressFile;
import in.xplorelogic.inveck.utils.Config;
import in.xplorelogic.inveck.utils.ConnectivityReceiver;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.ExifUtil;
import in.xplorelogic.inveck.utils.FileUtils;
import in.xplorelogic.inveck.utils.GetFileFromServer;
import in.xplorelogic.inveck.utils.NetworkClient;
import in.xplorelogic.inveck.utils.UploadAPIs;
import in.xplorelogic.inveck.utils.Utils;
import in.xplorelogic.inveck.utils.WebClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.graphics.BitmapFactory.decodeFile;

public class TakeImagesActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ImageView back_arrow1;
    LinearLayout btnSend;
    EditText descEdtTxt;
    ImageView imageOne, imageTwo, imageThree, imageOneDelete, imageTwoDelete, imageThreeDelete;
    FrameLayout imageOneTake, imageTwoTake, imageThreeTake;
    String desc = "";
    String imageOneName = "", imageTwoName = "", imageThreename = "", addContactUsResponse = "", message = "";
    Bitmap myBitmap;
    Uri picUri;

    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

    public ArrayList<String> map = new ArrayList<String>();
    static List<String> imagesEncodedList;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj, jsonLeadObj1, jsonLeadObjReq;
    JSONArray jsonArray;
    boolean status;
    int count = 0, stockId, mileStoneId;
    private ProgressDialog dialog;

    String imageEncoded;
    private Uri fileUri; // file url to store image
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = TakeImagesActivity.class.getSimpleName();
    static final Integer CAMERA = 0x1;
    static String fileName = "";
    static File destination;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private Uri fileUri1 = null, fileUri2 = null, fileUri3 = null; // file url to store image
    static String fileName1 = "", fileName2 = "", fileName3 = "";
    private int REQUEST_CAMERA = 0;
    String imageName = "", imageString1 = "", imageString2 = "", imageString3 = "", stockListResponse = "";
    //
    List<FilesModel> data;
    FilesListAdpter filesListAdpter;
    private RecyclerView listView;
    TextView itemName;
    FilesModel filesModel;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_images);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        btnSend = (LinearLayout) findViewById(R.id.btnSend);
        descEdtTxt = (EditText) findViewById(R.id.descEdtTxt);
        imageOne = (ImageView) findViewById(R.id.imageOne);
        imageTwo = (ImageView) findViewById(R.id.imageTwo);
        imageThree = (ImageView) findViewById(R.id.imageThree);
        imageOneDelete = (ImageView) findViewById(R.id.imageOneDelete);
        imageTwoDelete = (ImageView) findViewById(R.id.imageTwoDelete);
        imageThreeDelete = (ImageView) findViewById(R.id.imageThreeDelete);
        listView = (RecyclerView) findViewById(R.id.listView);
        imageOneTake = (FrameLayout) findViewById(R.id.imageOneTake);
        imageTwoTake = (FrameLayout) findViewById(R.id.imageTwoTake);
        imageThreeTake = (FrameLayout) findViewById(R.id.imageThreeTake);
        back_arrow1 = (ImageView) findViewById(R.id.back_arrow1);
        itemName = (TextView) findViewById(R.id.itemName);
        Intent intent = getIntent();
        stockId = intent.getIntExtra("stockId", 0);
        mileStoneId = intent.getIntExtra("mileStoneId", 0);
        imagesEncodedList = new ArrayList<String>();
        data = new ArrayList<FilesModel>();
        itemName.setText(intent.getStringExtra("itemName"));
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getInt("sync_status", 0) == 0) {
                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                        mArrayUri.clear();
                        if (!fileName1.equals("")) {
                            mArrayUri.add(fileUri1);

                        }
                        if (!fileName2.equals("")) {
                            mArrayUri.add(fileUri2);

                        }
                        if (!fileName3.equals("")) {
                            mArrayUri.add(fileUri3);

                        }
                        ;

                    /*desc = descEdtTxt.getText().toString().trim();
                    if (!desc.equals("")) {
                       *//* //  new addContactUsDetails().execute();
                        // Toast.makeText(getApplicationContext(), imageOneName + "," + imageTwoName + "," + imageThreename , Toast.LENGTH_SHORT).show();
                        if (mArrayUri.size() > 0) {
                            for (int i = 0; i < imagesEncodedList.size(); i++) {
                                map.add(imagesEncodedList.get(i).toString());
                            }
                            //  getLogin(loginActivity3.uId, LoginActivity.this.pwd).execute(new String[0]);
                            Utils.showLoadingDialog(TakeImagesActivity.this);
                            new ImageUploadTask().execute(count + "", getFileName(mArrayUri.get(count)));
                        } else {


                            descEdtTxt.setText("");
                        }*//*

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_SHORT).show();

                    }*/

                        if (mArrayUri.size() > 0) {
                       /* for(int i=0;i<mArrayUri.size();i++) {
                            imageName = getFileName(mArrayUri.get(count));

                        }*/
                            Utils.showLoadingDialog(TakeImagesActivity.this);
                           //uploadToServer(getPathFromUri(TakeImagesActivity.this, mArrayUri.get(count)));
                             uploadToServerMultiple();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mArrayUri.clear();
                    if (!fileName1.equals("")) {
                        mArrayUri.add(fileUri1);

                    }
                    if (!fileName2.equals("")) {
                        mArrayUri.add(fileUri2);

                    }
                    if (!fileName3.equals("")) {
                        mArrayUri.add(fileUri3);

                    }
                    if (mArrayUri.size() > 0) {
                        for (int i = 0; i < mArrayUri.size(); i++) {
                            imageName = getFileName(mArrayUri.get(i));
                            filesModel = new FilesModel(stockId, mileStoneId, imageName, "" + mArrayUri.get(i));
                            DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass();
                            long id = databaseQueryClass.insertImagesDetails(filesModel);
                            if (id > 0) {
                                //   Toast.makeText(getApplicationContext(), "Local Database " + imageName + " " + id, Toast.LENGTH_SHORT).show();

                            }

                        }
                        if (AppStatus.getInstance(TakeImagesActivity.this).isOnline()) {
                            registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                        }
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        imageOneTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    if (imageOneName.equals("")) {

                    } else {

                    }
                    cameraIntent(200);
                }
            }
        });
        imageTwoTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    if (imageTwoName.equals("")) {

                    } else {
                    }
                    cameraIntent(300);
                }
            }
        });
        imageThreeTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    if (imageThreename.equals("")) {

                    } else {
                    }
                    cameraIntent(400);
                }

            }
        });

        imageOneDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageOneDelete.setVisibility(View.GONE);
                imageOne.setVisibility(View.GONE);
                imageOneTake.setVisibility(View.VISIBLE);
                fileName1 = "";
            }
        });
        imageTwoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTwoDelete.setVisibility(View.GONE);
                imageTwo.setVisibility(View.GONE);
                imageTwoTake.setVisibility(View.VISIBLE);
                fileName2 = "";

            }
        });
        imageThreeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageThreeDelete.setVisibility(View.GONE);
                imageThree.setVisibility(View.GONE);
                imageThreeTake.setVisibility(View.VISIBLE);
                fileName3 = "";
            }
        });
        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeImagesActivity.this, FullScreenViewActivity.class);
                intent.putExtra("path", fileName1);
                startActivity(intent);
            }
        });
        back_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (preferences.getInt("sync_status", 0) == 0) {
            if (AppStatus.getInstance(TakeImagesActivity.this).isOnline()) {
                new getStockFiles().execute();
            } else {

                Toast.makeText(TakeImagesActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
            }
        } else {
            data.clear();
            data.addAll(databaseQueryClass.getAllImages(stockId, mileStoneId));
            if (data.size() > 0) {
                filesListAdpter = new FilesListAdpter(TakeImagesActivity.this, data);

                listView.setLayoutManager(new LinearLayoutManager(TakeImagesActivity.this, LinearLayoutManager.HORIZONTAL, false));
                // Toast.makeText(getApplicationContext(), "" + data.size(), Toast.LENGTH_SHORT).show();
                listView.setAdapter(filesListAdpter);
            } else {

            }

        }

        // goforIt();
        //   imageOne.setImageBitmap(GetFileFromServer.goforIt());
    }

    private void cameraIntent(int r) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        // start the image capture Intent
        startActivityForResult(intent, r);

    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image
     */
    private static File getOutputMediaFile(int type) {
        // Internal sdcard location
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "Inveck");
        // Create the storage directory if it does not exist
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.d(TAG, "Oops! Failed create " + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;


        if (type == MEDIA_TYPE_IMAGE) {
            fileName = System.currentTimeMillis() + ".jpg";
            mediaFile = new File(folder.getPath() + File.separator + fileName);
            destination = new File(folder.getPath(), fileName);


        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == 200 && resultCode == RESULT_OK) {

                //  onCaptureImageResult(data);

                if (resultCode == RESULT_OK) {

                    // successfully captured the image
                    // launching upload activity
                    imageOneTake.setVisibility(View.GONE);
                    imageOne.setVisibility(View.VISIBLE);
                    launchUploadActivity1(true);


                } else if (resultCode == RESULT_CANCELED) {

                    // user cancelled Image capture
                    Toast.makeText(TakeImagesActivity.this,
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();

                } else {
                    // failed to capture image
                    Toast.makeText(TakeImagesActivity.this,
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }


            } else if (requestCode == 300 && resultCode == RESULT_OK) {
                //  onCaptureImageResult(data);

                if (resultCode == RESULT_OK) {

                    // successfully captured the image
                    // launching upload activity
                    imageTwoTake.setVisibility(View.GONE);
                    imageTwo.setVisibility(View.VISIBLE);
                    launchUploadActivity2(true);


                } else if (resultCode == RESULT_CANCELED) {

                    // user cancelled Image capture
                    Toast.makeText(TakeImagesActivity.this,
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();

                } else {
                    // failed to capture image
                    Toast.makeText(TakeImagesActivity.this,
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
            } else if (requestCode == 400 && resultCode == RESULT_OK) {
                //  onCaptureImageResult(data);

                if (resultCode == RESULT_OK) {

                    // successfully captured the image
                    // launching upload activity
                    imageThreeTake.setVisibility(View.GONE);
                    imageThree.setVisibility(View.VISIBLE);
                    launchUploadActivity3(true);


                } else if (resultCode == RESULT_CANCELED) {

                    // user cancelled Image capture
                    Toast.makeText(TakeImagesActivity.this,
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();

                } else {
                    // failed to capture image
                    Toast.makeText(TakeImagesActivity.this,
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("Exception", "" + e);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * ------------ Helper Methods ----------------------
     */

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private boolean checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow

                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:in.xplorelogic.inveck")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    private void launchUploadActivity1(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {

            imagesEncodedList = new ArrayList<String>();
            //  mArrayUri.add(Uri.fromFile(destination));
            fileUri1 = Uri.fromFile(destination);
            fileName1 = getFileName(fileUri1);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            imageOne.setImageBitmap(bitmap);
            imageOneDelete.setVisibility(View.VISIBLE);
            //  Bitmap orientedBitmap = ExifUtil.rotateBitmap(getPathFromUri(TakeImagesActivity.this, fileUri), bitmap);
            // imagesEncodedList.add(getPathFromUri(TakeImagesActivity.this, fileUri));


        } else {

        }


    }

    private void launchUploadActivity2(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {

            imagesEncodedList = new ArrayList<String>();
            //   mArrayUri.add(Uri.fromFile(destination));
            fileUri2 = Uri.fromFile(destination);
            fileName2 = getFileName(fileUri2);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            imageTwo.setImageBitmap(bitmap);
            imageTwoDelete.setVisibility(View.VISIBLE);
          /* Bitmap orientedBitmap = ExifUtil.rotateBitmap(getPathFromUri(TakeImagesActivity.this, fileUri), bitmap);
            imagesEncodedList.add(getPathFromUri (TakeImagesActivity.this, fileUri));
            imageTwo.setImageBitmap(orientedBitmap);*/


        } else {

        }
    }

    private void launchUploadActivity3(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {

            imagesEncodedList = new ArrayList<String>();
            //  mArrayUri.add(Uri.fromFile(destination));
            fileUri3 = Uri.fromFile(destination);
            fileName3 = getFileName(fileUri3);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            //1
            // File myFile = new File(fileUri.getPath());
            Bitmap orientedBitmap = ExifUtil.rotateBitmap(getPathFromUri(TakeImagesActivity.this, fileUri), bitmap);
            imageThree.setImageBitmap(orientedBitmap);
            imageThreeDelete.setVisibility(View.VISIBLE);

        } else {

        }
    }


    public void uploadToServer(String filePath) {
        imageName = getFileName(mArrayUri.get(count));
        String subs = stockId + "/" + mileStoneId + "/" + imageName;
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("fileStream", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call call = uploadAPIs.uploadImage(part, description, subs);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                count++;
                if (count < mArrayUri.size()) {
                    // new ImageUploadTask().execute(count + "", "hm" + count + ".jpg");
                    uploadToServer(getPathFromUri(TakeImagesActivity.this, mArrayUri.get(count)));
                    // new ImageUploadTask().execute(count + "", getFileName(mArrayUri.get(count)));
                    Utils.showLoadingDialog(TakeImagesActivity.this);
                } else {
                    Utils.dismissLoadingDialog();
                    Toast.makeText(getApplicationContext(), " Photo uploaded successfully", Toast.LENGTH_SHORT).show();

                    finish();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });


    }

    public void uploadToServerMultiple() {

        String subs = stockId + "/" + mileStoneId;
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        // create list of file parts (photo, video, ...)
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (mArrayUri != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < mArrayUri.size(); i++) {
                parts.add(prepareFilePart("fileStream", mArrayUri.get(i)));
            }
        }

        Call call = uploadAPIs.uploadMultipleFilesDynamic(parts, subs);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Utils.dismissLoadingDialog();
                Toast.makeText(getApplicationContext(), " Photo uploaded successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });


    }


    public MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        int compressionRatio = 1; //1 == originalImage, 2 = 50% compression, 4=25% compress
        // use the FileUtils to get the actual file by uri
        File file = new File(getPathFromUri(TakeImagesActivity.this, fileUri));

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), CompressFile.getCompressedImageFile(file, TakeImagesActivity.this));

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
    /////////////////////////////////////

    private class getStockFiles extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(TakeImagesActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("stockId", stockId);
                        put("mileStoneId", mileStoneId);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            stockListResponse = serviceAccess.SendHttpPost(Config.URL_STOCKFILES, jsonLeadObj);
            Log.i("resp", stockListResponse);
            if (stockListResponse.compareTo("") != 0) {
                if (isJSONValid(stockListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                data.clear();
                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseMileFilesList(stockListResponse);
                                jsonArray = new JSONArray(stockListResponse);

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
                            //Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            if (data.size() > 0) {
                filesListAdpter = new FilesListAdpter(TakeImagesActivity.this, data);

                listView.setLayoutManager(new LinearLayoutManager(TakeImagesActivity.this, LinearLayoutManager.HORIZONTAL, false));
                // Toast.makeText(getApplicationContext(), "" + data.size(), Toast.LENGTH_SHORT).show();
                listView.setAdapter(filesListAdpter);
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

    public void goforIt() {
        FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect("67.23.166.125");

            if (con.login("inveckftp", "h1Ybp%49")) {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);
                String data = "/sdcard/1590140798347.jpg";

                OutputStream out = new FileOutputStream(new File(data));
                //   boolean result = con.retrieveFile("/UploadFiles/1590140798347.jpg", out);
                InputStream input = con.retrieveFileStream("/UploadFiles/1590140798347.jpg");
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                imageOne.setImageBitmap(myBitmap);
                out.close();
                //  if (result) Log.v("download result", "succeeded");
                con.logout();
                con.disconnect();
            }
        } catch (Exception e) {
            Log.v("download result", "failed");
            e.printStackTrace();
        }


    }


}

