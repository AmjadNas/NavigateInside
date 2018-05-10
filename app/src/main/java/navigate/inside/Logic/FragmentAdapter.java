package navigate.inside.Logic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import navigate.inside.Activities.GetDirectionsFragment;
import navigate.inside.Activities.MyLocationFragment;


public class FragmentAdapter extends FragmentPagerAdapter {

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Get Directions";
            case 1:
                return "Find Location";
        }
        return null;
    }

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GetDirectionsFragment.newInstance();
            case 1:
                return MyLocationFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
