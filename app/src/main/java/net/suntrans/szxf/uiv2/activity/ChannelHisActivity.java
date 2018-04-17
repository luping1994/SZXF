package net.suntrans.szxf.uiv2.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.HisEntity;
import net.suntrans.szxf.databinding.ActivityEnergyMoniBinding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.bean.Monitor;
import net.suntrans.szxf.uiv2.fragment.TimeChartFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class ChannelHisActivity extends BasedActivity implements TimeChartFragment.OnFragmentInteractionListener{
    private List<Monitor> datas;
    private ActivityEnergyMoniBinding binding;
    private String field;
    private String channel_id;
    private TimeChartFragment fragment;

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


        String unit = getIntent().getStringExtra("unit");
        String title = getIntent().getStringExtra("title");
        String name = getIntent().getStringExtra("name");
        TextView txTitle = (TextView) findViewById(R.id.title);
        txTitle.setText(title+name+"历史记录");
        fragment = TimeChartFragment.newInstance(unit,name);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
        field = getIntent().getStringExtra("field");
        channel_id = getIntent().getStringExtra("channel_id");

    }

    //    getEnvHis
    private void getDatas(String startTime, String endTime) {
        Map<String, String> map = new HashMap<>();
        map.put("channel_id", channel_id);
        map.put("field",field);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        addSubscription(RetrofitHelper.getApi().getChannelHis(map), new BaseSubscriber<HisEntity>(this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onNext(HisEntity hisEntity) {
                super.onNext(hisEntity);
                try {
                    fragment.setData(hisEntity);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }


    @Override
    public void getData(String startTime, String endTime) {
        getDatas(startTime,endTime);
    }
}
