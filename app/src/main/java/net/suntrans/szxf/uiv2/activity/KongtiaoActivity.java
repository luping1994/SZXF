package net.suntrans.szxf.uiv2.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.databinding.ActivityEnergyMoniBinding;
import net.suntrans.szxf.uiv2.bean.Monitor;
import net.suntrans.szxf.uiv2.fragment.EnvListFragment;
import net.suntrans.szxf.uiv2.fragment.KongtiaoFragment;

import java.util.List;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class KongtiaoActivity extends BasedActivity {

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
        txTitle.setText("空调控制");

        findViewById(R.id.back)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        KongtiaoFragment fragment =  KongtiaoFragment.newInstance(getIntent().getStringExtra("channel_id"));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,fragment)
                .commit();

    }




}
