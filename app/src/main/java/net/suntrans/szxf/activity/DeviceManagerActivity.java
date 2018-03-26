package net.suntrans.szxf.activity;

import android.os.Bundle;

import net.suntrans.szxf.R;
import net.suntrans.szxf.fragment.per.DevicesManagerFragment;

/**
 * Created by Looney on 2017/4/20.
 */

public class DeviceManagerActivity extends BasedActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicemanager);
        DevicesManagerFragment fragment = DevicesManagerFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


}
