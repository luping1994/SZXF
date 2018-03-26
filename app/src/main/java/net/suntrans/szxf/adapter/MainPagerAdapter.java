package net.suntrans.szxf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.suntrans.szxf.fragment.AreaFragment;
import net.suntrans.szxf.fragment.AdminHomePageFragment;
import net.suntrans.szxf.fragment.EnergyConFragment2;
import net.suntrans.szxf.fragment.EnvHomeFragment;
import net.suntrans.szxf.fragment.PerCenFragment;

/**
 * Created by Looney on 2017/7/20.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        AdminHomePageFragment fragment1 = new AdminHomePageFragment();
        AreaFragment fragment2 = new AreaFragment();
        EnergyConFragment2 fragment3 = new EnergyConFragment2();
        PerCenFragment fragment4 = new PerCenFragment();
        EnvHomeFragment fragment5 = new EnvHomeFragment();
        fragments = new Fragment[]{fragment1, fragment2,fragment5, fragment3, fragment4};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
