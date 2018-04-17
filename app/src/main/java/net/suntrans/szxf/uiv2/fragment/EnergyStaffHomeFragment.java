package net.suntrans.szxf.uiv2.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.looney.widgets.SegmentedGroup;
import net.suntrans.szxf.R;
import net.suntrans.szxf.adapter.FragmentAdapter;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.SharedPreferencesHepler;
import net.suntrans.szxf.utils.StatusBarCompat;

/**
 * Created by Administrator on 2017/8/14.
 */

public class EnergyStaffHomeFragment extends RxFragment {
    private final String TAG = this.getClass().getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_env_home_staff,container,false);
        View statusBar = view.findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeight = StatusBarCompat.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            params.height = statusBarHeight;
            statusBar.setLayoutParams(params);
            statusBar.setVisibility(View.VISIBLE);
        }else {
            statusBar.setVisibility(View.GONE);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String officeHouseId = SharedPreferencesHepler.getOfficeHouseId();
        String dormHouseId = SharedPreferencesHepler.getDormHouseId();
        LogUtil.i(TAG,officeHouseId);
        LogUtil.i(TAG,dormHouseId);
        EnergyDetailFragment fragment = EnergyDetailFragment.newInstance(officeHouseId,officeHouseId);
        EnergyDetailFragment fragment2 = EnergyDetailFragment.newInstance(dormHouseId,dormHouseId);
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager());
        adapter.addFragment(fragment,"office");
        adapter.addFragment(fragment2,"dorm");

        view.findViewById(R.id.menu)
                .setVisibility(View.INVISIBLE);

        final RadioButton button0 = (RadioButton) view.findViewById(R.id.radio0);
        final RadioButton button1 = (RadioButton) view.findViewById(R.id.radio1);
        final ViewPager pager = (ViewPager) view.findViewById(R.id.viewPager);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    button0.setChecked(true);
                }else if (position == 1){
                    button1.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SegmentedGroup segmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmented_group);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio0){
                    pager.setCurrentItem(0);
                }else if (checkedId == R.id.radio1){
                    pager.setCurrentItem(1);
                }
            }
        });
    }
}
