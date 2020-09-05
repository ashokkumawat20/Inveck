package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.xplorelogic.inveck.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

public class FullScreenViewActivity extends AppCompatActivity {
    ImageView imgDisplay;
    LinearLayout btnClose;
    String path = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = (LinearLayout) findViewById(R.id.btnClose);
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Inveck" + File.separator + path;
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        imgDisplay.setImageBitmap(bmp);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
