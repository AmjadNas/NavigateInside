package navigate.inside.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.Beacon;

import navigate.inside.Logic.BeaconListener;
import navigate.inside.Logic.FragmentAdapter;
import navigate.inside.Logic.MyApplication;
import navigate.inside.Logic.SysData;
import navigate.inside.R;

public class MainActivity extends AppCompatActivity implements BeaconListener{

    private ViewPager viewPager;
    private FragmentAdapter fpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tags);


        viewPager = (ViewPager)findViewById(R.id.viewpager);
        fpa = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fpa);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        ((MyApplication)getApplication()).startRanging();
        ((MyApplication)getApplication()).registerListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //SysData.getInstance().closeDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((MyApplication)getApplication()).stopRanging();
        ((MyApplication)getApplication()).unRegisterListener(this);
    }

    @Override
    public void onBeaconEvent(Beacon beacon) {
        //todo when beacon invoked do stuff


    }
}
