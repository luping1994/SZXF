package net.suntrans.szxf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.suntrans.szxf.fragment.din.SceneFragment;

/**
 * Created by Looney on 2017/7/20.
 */

public class DiningPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments;
    private final String[] titles = new String[]{"场景"};

    public DiningPagerAdapter(FragmentManager fm) {
        super(fm);
        SceneFragment fragment1 = new SceneFragment();
//        LightFragment fragment2 = new LightFragment();
//        EnvFragment fragment3 = new EnvFragment();
//        fragments = new Fragment[]{fragment1, fragment2, fragment3};
        fragments = new Fragment[]{fragment1};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
