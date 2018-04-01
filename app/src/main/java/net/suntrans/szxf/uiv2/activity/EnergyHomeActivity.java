package net.suntrans.szxf.uiv2.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.uiv2.bean.Monitor;
import net.suntrans.szxf.databinding.ActivityEnergyMoniBinding;
import net.suntrans.szxf.uiv2.fragment.EnergyListFragment;

import java.util.List;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class EnergyHomeActivity extends BasedActivity {
    private List<Monitor> datas;
    private ActivityEnergyMoniBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_energy_moni);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView txTitle = (TextView) findViewById(R.id.title);
        txTitle.setText("能耗");

        EnergyListFragment fragment = new EnergyListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,fragment)
                .commit();

    }




}
