package in.xplorelogic.inveck.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import in.xplorelogic.inveck.R;
import in.xplorelogic.inveck.adapter.ViewPagerAdapter;

import com.google.android.material.tabs.TabLayout;


public class FragmentMilestone extends AppCompatActivity {
    ImageView back_arrow1;
    Bundle bundle;
    int client_id;
    FragmentManager fragmentManager;
    final FragmentTransaction fragmentTransaction;
    FrameLayout frame_layout;
    int milestone_id;
    final SampleFragment samFrag = new SampleFragment();
    TabLayout tabLayout;
    String user_id;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Button completed;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    public FragmentMilestone() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fragmentManager = supportFragmentManager;
        this.fragmentTransaction = supportFragmentManager.beginTransaction();
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.fragment_milestone);
        this.tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        this.back_arrow1 = (ImageView) findViewById(R.id.back_arrow1);
        this.frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        completed = (Button) findViewById(R.id.completed);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        back_arrow1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FragmentMilestone.this, MilestoneListActivity.class);
                startActivity(intent);
            }
        });
        ViewPagerAdapter viewPagerAdapter2 = new ViewPagerAdapter(getSupportFragmentManager());
        this.viewPagerAdapter = viewPagerAdapter2;
        if (preferences.getInt("sync_status", 0) == 1) {
            viewPagerAdapter2.addFragment(new SampleFragment1(), "Sample");
            this.viewPagerAdapter.addFragment(new TotalStockFragment1(), "Total Stock");
        } else {
            viewPagerAdapter2.addFragment(new SampleFragment(), "Sample");
            this.viewPagerAdapter.addFragment(new TotalStockFragment(), "Total Stock");
        }
        this.viewPager.setAdapter(this.viewPagerAdapter);
        completed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (preferences.getInt("sync_status", 0) == 1) {
                    Intent intent = new Intent(FragmentMilestone.this, CompleteProcessOffLine.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FragmentMilestone.this, CompleteProcessActivity.class);
                    startActivity(intent);
                }
           /*     AlertDialog.Builder builder = new AlertDialog.Builder(FragmentMilestone.this);
                builder.setMessage("Do you want to end Physical verification ?")
                        .setCancelable(false)
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // Intent intent = new Intent(FragmentMilestone.this, CompleteProcessActivity.class);
                                Intent intent = new Intent(FragmentMilestone.this, CompleteProcessOffLine.class);
                                startActivity(intent);

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
                //  alert.setTitle("Logout");
                alert.show();*/
            }
        });
    }

    public Bundle getData() {
        Bundle hm = new Bundle();
        hm.putInt("client_id", this.client_id);
        hm.putInt("milestone_id", this.milestone_id);
        hm.putString("user_id", this.user_id);
        return hm;
    }
}
