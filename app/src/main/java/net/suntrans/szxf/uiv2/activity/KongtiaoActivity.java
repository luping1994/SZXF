package net.suntrans.szxf.uiv2.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.szxf.App;
import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.databinding.ActivityEnergyMoniBinding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.bean.AirCmd;
import net.suntrans.szxf.uiv2.bean.Monitor;
import net.suntrans.szxf.uiv2.fragment.EnvListFragment;
import net.suntrans.szxf.uiv2.fragment.KongtiaoFragment;
import net.suntrans.szxf.uiv2.fragment.KongtiaoFragment_type1;

import java.util.List;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class KongtiaoActivity extends BasedActivity {

    private ActivityEnergyMoniBinding binding;
    private String channel_id;

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

        channel_id = getIntent().getStringExtra("channel_id");


        getData(channel_id);

    }


    private void getData(String channel_ids) {
        addSubscription(api.getTotalCmd(channel_ids), new BaseSubscriber<RespondBody<List<AirCmd>>>(this) {
            @Override
            public void onNext(RespondBody<List<AirCmd>> body) {

                if (body.data != null && body.data.size() > 1) {
                    if ("0".equals(body.data.get(0).air_type)) {
                        KongtiaoFragment fragment = KongtiaoFragment.newInstance(channel_id);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, fragment)
                                .commit();
                    } else if ("1".equals(body.data.get(0).air_type)){

                        KongtiaoFragment_type1 fragment = KongtiaoFragment_type1.newInstance(channel_id);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, fragment)
                                .commit();
                    }else {
                        new IosAlertDialog(KongtiaoActivity.this)
                                .builder()
                                .setCancelable(false)
                                .setMsg("不支持的空调类型...")
                                .setPositiveButton(getResources().getString(R.string.close), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }).show();
                    }

                }else {
                    new IosAlertDialog(KongtiaoActivity.this)
                            .builder()
                            .setCancelable(false)
                            .setMsg("不支持的空调类型...")
                            .setPositiveButton(getResources().getString(R.string.close), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).show();
                }

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();

                super.onError(e);
            }
        });
    }


}
