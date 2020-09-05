package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.xplorelogic.inveck.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressbar;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        setContentView(R.layout.activity_splash_screen);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressbar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (!preferences.getString("inventry_user_id", "").equals("")) {
                    Intent intent = new Intent(SplashScreen.this, MilestoneListActivity.class);
                    startActivity(intent);
                    //Remove activity
                    finish();
                } else {


                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    SplashScreen.this.progressbar.setVisibility(View.GONE);
                    //Remove activity
                    finish();
                }

            }
        }, (long) 4000);
    }
}
