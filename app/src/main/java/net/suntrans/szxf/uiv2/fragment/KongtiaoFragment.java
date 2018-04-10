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
import net.suntrans.szxf.uiv2.bean.AirCmd;

import java.util.List;

/**
 * Created by Looney on 2018/4/9.
 * Des:
 */
public class KongtiaoFragment extends BasedFragment2 implements View.OnClickListener {

    private List<AirCmd> data;


    //定义四种模式
    private static final String MOSHI_ZHILENG = "制冷模式";
    private static final String MOSHI_ZHIRE = "制热模式";
    private static final String MOSHI_SONGFENG = "送风模式";
    private static final String MOSHI_SLEEP = "睡眠模式";
    private static final String MOSHI_CHUSHI = "除湿模式";

    private static final int MOSHI_ZHILENG_INDEX = 0;
    private static final int MOSHI_ZHIRE_INDEX = 1;
    private static final int MOSHI_SONGFENG_INDEX = 2;
    private static final int MOSHI_SLEEP_INDEX = 3;
    private static final int MOSHI_CHUSHI_INDEX = 4;

    private static final int MOSHI_SLEEP_AUTO_INDEX = 4;
    private static final int MOSHI_SLEEP_ZHILENG_INDEX = 5;
    private static final int MOSHI_SLEEP_CHUSHI_INDEX = 6;

    //air_type=1
    private static final String MOSHI_SLEEP_AUTO = "自动模式下的睡眠模式";
    private static final String MOSHI_SLEEP_ZHILENG = "制冷模式下睡眠模式";
    private static final String MOSHI_SLEEP_CHUSHI = "除湿模式下睡眠模式";

    private static final String[] MODELS = {MOSHI_ZHILENG, MOSHI_ZHIRE, MOSHI_SONGFENG, MOSHI_CHUSHI, MOSHI_SLEEP};
    private static final String[] FENGSUS = {"","强劲风"};
    private static final String[] FENGXIANG = {"上下扫风","左右扫风"};

    private static final String[] MODELSTYPE2 = {MOSHI_ZHILENG, MOSHI_ZHIRE, MOSHI_SONGFENG, MOSHI_CHUSHI, MOSHI_SLEEP_AUTO, MOSHI_SLEEP_ZHILENG, MOSHI_SLEEP_CHUSHI};

    //制冷模式温度上限
    private static final int MAX_WENDU_COLD = 23;
    private static final int MIN_WENDU_COLD = 18;

    //制热模式温度上限
    private static final int MAX_WENDU_HOT = 30;
    private static final int MIN_WENDU_HOT = 20;

    private int currentMoshiIndex = 0;
    private int currentFengsuIndex = 0;
    private int currentFengxiangIndex = 0;

    private int currentHotWendu = 18;
    private int currentColdWendu = 20;
    private String channel_id;

    public static KongtiaoFragment newInstance(String channel_id) {

        Bundle args = new Bundle();
        args.putString("channel_id", channel_id);
        KongtiaoFragment fragment = new KongtiaoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    FragmentKongtiaoBinding binding;

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

    }

    private String air_type = "0";

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.moshi:
                currentMoshiIndex++;

                if (air_type.equals("0")) {
                    if (currentMoshiIndex == MODELS.length) {
                        currentMoshiIndex = 0;
                    }
                } else {
                    if (currentMoshiIndex == MODELSTYPE2.length) {
                        currentMoshiIndex = 0;
                    }
                }

                break;
            case R.id.add:
                break;

            case R.id.sub:
                break;

            case R.id.fengsu:
                currentFengsuIndex++;
                if (currentFengsuIndex==2){
                    currentFengsuIndex=0;
                }
                break;

            case R.id.fegnxiang:
                currentFengxiangIndex++;
                if (currentFengxiangIndex==2){
                    currentFengxiangIndex=0;
                }
                break;
        }
        updateView();

    }

    private void updateView() {

        String[] models = null;
        if (air_type.equals("0")) {
            models = MODELS;
        } else {
            models = MODELSTYPE2;
        }
        binding.model.setText(models[currentMoshiIndex]);
        binding.fengsuText.setText(FENGSUS[currentFengsuIndex]);
        binding.fengxiangText.setText(FENGXIANG[currentFengxiangIndex]);
        switch (currentMoshiIndex) {
            case MOSHI_ZHILENG_INDEX:
            case MOSHI_ZHIRE_INDEX:
                binding.sub.setEnabled(true);
                binding.add.setEnabled(true);
                binding.fengsu.setEnabled(true);
                binding.fegnxiang.setEnabled(true);
                binding.dingshi.setEnabled(true);
                binding.wendu.setVisibility(View.VISIBLE);

                break;
            case MOSHI_SONGFENG_INDEX:
            case MOSHI_SLEEP_INDEX:
            case MOSHI_CHUSHI_INDEX:
                binding.sub.setEnabled(false);
                binding.add.setEnabled(false);
                binding.fengsu.setEnabled(true);
                binding.fegnxiang.setEnabled(true);
                binding.dingshi.setEnabled(true);
                binding.wendu.setVisibility(View.INVISIBLE);
                break;

        }
    }
}
