package net.suntrans.szxf.uiv2.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.szxf.R;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.databinding.FragmentKongtiaoBinding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.BasedFragment2;
import net.suntrans.szxf.uiv2.air.Air0;
import net.suntrans.szxf.uiv2.air.Air1;
import net.suntrans.szxf.uiv2.bean.AirCmd;
import net.suntrans.szxf.utils.UiUtils;

import java.util.List;

/**
 * Created by Looney on 2018/4/9.
 * Des:
 */
public class KongtiaoFragment_type1 extends BasedFragment2 implements View.OnClickListener, Air1.AirStateChangedListener {

    private List<AirCmd> data;


    private String channel_id;

    public static KongtiaoFragment_type1 newInstance(String channel_id) {

        Bundle args = new Bundle();
        args.putString("channel_id", channel_id);
        KongtiaoFragment_type1 fragment = new KongtiaoFragment_type1();
        fragment.setArguments(args);
        return fragment;
    }

    FragmentKongtiaoBinding binding;
    private Air1 air;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_kongtiao, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        channel_id = getArguments().getString("channel_id");
        air = new Air1();
        air.setListener(this);
        getData(channel_id);

        binding.add
                .setOnClickListener(this);

        binding.sub
                .setOnClickListener(this);

        binding.moshi
                .setOnClickListener(this);

        binding.fegnxiang
                .setOnClickListener(this);

        binding.fengsu
                .setOnClickListener(this);

        binding.dingshi
                .setOnClickListener(this);

        binding.close
                .setOnClickListener(this);
    }

    private String air_type = "0";


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.moshi:
                binding.feng.setVisibility(View.VISIBLE);
                air.switchModel();
                air.setOn(true);
                break;

            case R.id.add:
                air.addWendu();
                air.setOn(true);
                binding.feng.setVisibility(View.VISIBLE);
                break;

            case R.id.sub:
                air.subWendu();
                air.setOn(true);
                binding.feng.setVisibility(View.VISIBLE);
                break;

            case R.id.fengsu:
                binding.feng.setVisibility(View.VISIBLE);
                air.setOn(true);

                air.switchFengsu();
                break;

            case R.id.fegnxiang:
                binding.feng.setVisibility(View.VISIBLE);
                air.setOn(true);
                air.switchFengXiang();
                break;

            case R.id.close:
                if (air.isOn()){
                    air.close();
                    binding.feng.setVisibility(View.INVISIBLE);
                }
                else{
                    binding.feng.setVisibility(View.VISIBLE);
                    air.open();
                }
                break;

            case R.id.dingshi:
                UiUtils.showToast("此类型空调暂不支持定时功能！");
                break;
        }

    }


    @Override
    public void sendCmd(String id) {
        sendCmdToServer(id);
    }

    @Override
    public void stateStringChanged(String modelText, String wenduText, String fengsuText, String fengxiangText) {

        binding.model.setText(modelText);
        binding.wendu.setText(wenduText);
        binding.fengsuText.setText(fengsuText);
        binding.fengxiangText.setText(fengxiangText);

    }


    private void getData(String channel_id) {
        addSubscription(api.getTotalCmd(channel_id), new BaseSubscriber<RespondBody<List<AirCmd>>>(getContext()) {
            @Override
            public void onNext(RespondBody<List<AirCmd>> body) {
                data = body.data;
                air_type = data.get(0).air_type;

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();

                super.onError(e);
            }
        });
    }

    private void sendCmdToServer(String id) {

        addSubscription(api.sendAirCmd(channel_id, id), new BaseSubscriber<RespondBody<List<AirCmd>>>(getContext()) {
            @Override
            public void onNext(RespondBody<List<AirCmd>> body) {

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();

                super.onError(e);
            }
        });
    }
}
