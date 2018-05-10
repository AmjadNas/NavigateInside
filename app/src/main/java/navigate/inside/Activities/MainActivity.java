package navigate.inside.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import navigate.inside.Logic.FragmentAdapter;
import navigate.inside.Logic.MyApplication;
import navigate.inside.Logic.SysData;
import navigate.inside.R;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private FragmentAdapter fpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tags);
        SysData.getInstance();

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        fpa = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fpa);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0)
            ((MyApplication)getApplication()).unRegisterListener((MyLocationFragment)fpa.getItem(position+1));
        else if (position == 1)
            ((MyApplication)getApplication()).registerListener((MyLocationFragment)fpa.getItem(position));
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
