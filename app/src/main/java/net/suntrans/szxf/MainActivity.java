package net.suntrans.szxf;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.update.PgyUpdateManager;

import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.fragment.AreaFragment;
import net.suntrans.szxf.fragment.AdminHomePageFragment;
import net.suntrans.szxf.fragment.EnergyConFragment2;
import net.suntrans.szxf.fragment.EnvHomeFragment;
import net.suntrans.szxf.fragment.PerCenFragment;
import net.suntrans.szxf.uiv2.fragment.EnergyStaffHomeFragment;
import net.suntrans.szxf.uiv2.fragment.EnvListFragment;
import net.suntrans.szxf.uiv2.fragment.EnvStaffHomeFragment;
import net.suntrans.szxf.uiv2.fragment.StaffHomePageFragment;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.UiUtils;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;
import static android.support.design.widget.TabLayout.MODE_FIXED;
import static net.suntrans.szxf.BuildConfig.DEBUG;
import static net.suntrans.szxf.Config.FILE_PROVIDER;
import static net.suntrans.szxf.ROLE.LEADER;

import android.content.Context;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BasedActivity {

    private final int[] TAB_TITLES = new int[]{R.string.nav_tit, R.string.nav_area, R.string.nav_env, R.string.nav_power, R.string.nav_user};
    private final int[] TAB_IMGS = new int[]{
            R.drawable.select_home,
            R.drawable.select_area,
            R.drawable.select_env,
            R.drawable.select_power,
            R.drawable.select_user
    };
    private final int[] TAB_TITLES_RENT = new int[]{R.string.nav_tit, R.string.nav_env, R.string.nav_power, R.string.nav_user};
    private final int[] TAB_IMGS_RENT = new int[]{
            R.drawable.select_home,
            R.drawable.select_env,
            R.drawable.select_power,
            R.drawable.select_user
    };

    private TabLayout tabLayout;


    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;
    private Fragment fragment5;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.i("绑定服务成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        Intent intent = new Intent();
        intent.setClass(this, MyService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        if (!DEBUG)
            PgyUpdateManager.register(this, FILE_PROVIDER);
        init();

    }

    private Fragment[] fragments;

    private void init() {
        App.ROLE_ID = App.getSharedPreferences().getInt("role_id", -1);
        Fragment fragment1 = null;
        switch (App.ROLE_ID) {
            case LEADER:
            case ROLE.SUPERVISOR:
                fragment1 = new AdminHomePageFragment();
                fragment5 = new EnvHomeFragment();
                if (fragment3 == null){
                    fragment3 = new EnergyConFragment2();
                }
                break;
            case ROLE.STAFF:
                fragment1 = new StaffHomePageFragment();
                fragment5 = new EnvStaffHomeFragment();

                if (fragment3 == null){
                    fragment3 = new EnergyStaffHomeFragment();
                }
                break;
            default:
                fragment1 = new AdminHomePageFragment();
                new IosAlertDialog(this)
                        .builder()
                        .setCancelable(false)
                        .setMsg(getString(R.string.tips_account_error))
                        .setPositiveButton(getResources().getString(R.string.close), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                App.getSharedPreferences().edit().clear().commit();
                                finish();
                            }
                        }).show();
                break;
        }


        if (fragment2 == null)
            fragment2 = new AreaFragment();

        if (fragment3 == null){
            fragment3 = new EnergyConFragment2();
        }

        if (fragment4 == null)
            fragment4 = new PerCenFragment();




        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.setTabGravity(GRAVITY_FILL);


        if (App.ROLE_ID == ROLE.STAFF){
            fragments = new Fragment[]{fragment1, fragment5, fragment3, fragment4};
            setTabs(tabLayout, this.getLayoutInflater(), TAB_TITLES_RENT, TAB_IMGS_RENT);

        }else {
            fragments = new Fragment[]{fragment1, fragment2, fragment5, fragment3, fragment4};
            setTabs(tabLayout, this.getLayoutInflater(), TAB_TITLES, TAB_IMGS);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment1).commit();



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                System.out.println(tab.getPosition() + "");
                changFragment(tab.getPosition(), tab.getPosition() + "");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.tab, null);
            tab.setCustomView(view);

            TextView tvTitle = (TextView) view.findViewById(R.id.tv_tab);
            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);

            tvTitle.setText(tabTitlees[i]);
            imgTab.setImageResource(tabImgs[i]);

            tabLayout.addTab(tab);

        }
    }

    @Override
    protected void onDestroy() {
        if (connection != null)
            unbindService(connection);
        super.onDestroy();
    }


    private long[] mHits = new long[2];

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
//                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                Toast.makeText(this.getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    int currentIndex = 0;

    private void changFragment(int index, String tag) {
        if (currentIndex == index) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[currentIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.content, fragments[index]);
        }
        transaction.show(fragments[index]).commit();
        currentIndex = index;
    }

    private void initJpush() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity.this);
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

}
