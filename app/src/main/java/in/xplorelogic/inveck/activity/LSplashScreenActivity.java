package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.xplorelogic.inveck.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class LSplashScreenActivity extends AppCompatActivity {
    String msg = "Android : ";
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsplash_screen);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();

        init();
    }

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
        init();


    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
        init();


    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");

    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");

    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(msg, "The onDestroy() event");

    }

    public void init() {
        Intent intent = new Intent(LSplashScreenActivity.this, SplashScreen.class);
        startActivity(intent);
        //Remove activity
        finish();
        /*if (!preferences.getString("inventry_user_id", "").equals("")) {
            Intent intent = new Intent(LSplashScreenActivity.this, MilestoneListActivity.class);
            startActivity(intent);
            //Remove activity
            finish();
        } else {

            Intent intent = new Intent(LSplashScreenActivity.this, SplashScreen.class);
            startActivity(intent);
            //Remove activity
            finish();
        }*/

    }


}



