package in.xplorelogic.inveck.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.xplorelogic.inveck.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class VerifiActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    int client_id;
    int milestone_id;
    LinearLayout startveri;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifi);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        this.client_id = getIntent().getExtras().getInt("client_Id");
        this.milestone_id = getIntent().getExtras().getInt("milestone_Id");
        user_id = preferences.getString("inventry_user_id","");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.startverification);
        this.startveri = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent start = new Intent(VerifiActivity.this, PersonDetailActivity.class);
                start.putExtra("client_Id", VerifiActivity.this.client_id);
                start.putExtra("milestone_Id", VerifiActivity.this.milestone_id);
                start.putExtra("user_Id",user_id);
                VerifiActivity.this.startActivity(start);
            }
        });
    }
}

