package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.utils.GetFileFromServer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

public class FullScreenViewActivity1 extends AppCompatActivity {
    ImageView imgDisplay;
    LinearLayout btnClose;
    String path;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view1);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = (LinearLayout) findViewById(R.id.btnClose);
        if (preferences.getInt("sync_status", 0) == 0) {
            imgDisplay.setImageBitmap(GetFileFromServer.goforIt("", path));
        } else {
            Uri fileUri = Uri.parse(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            imgDisplay.setImageBitmap(bitmap);

        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
