package net.suntrans.szxf.uiv2.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.suntrans.szxf.R;
import net.suntrans.szxf.bean.AirState;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.databinding.FragmentKongtiaoBinding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.BasedFragment2;
import net.suntrans.szxf.uiv2.air.Air;
import net.suntrans.szxf.uiv2.air.Air0;
import net.suntrans.szxf.uiv2.bean.AirCmd;
import net.suntrans.szxf.utils.UiUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.suntrans.szxf.utils.UiUtils.showToast;

/**
 * Created by Looney on 2018/4/9.
 * Des:
 */
public class KongtiaoFragment extends BasedFragment2 implements View.OnClickListener, Air0.AirStateChangedListener {

    private List<AirCmd> data;


    private String channel_id;

    public static KongtiaoFragment newInstance(String channel_id) {

        Bundle args = new Bundle();
        args.putString("channel_id", channel_id);
        KongtiaoFragment fragment = new KongtiaoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    FragmentKongtiaoBinding binding;
    private Air0 air;

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
        air = new Air0();
        air.setListener(this);
        getData(channel_id);

        binding.add
                .setOnClickListener(this);

        binding.sub
                .setOnClickListener(this);


        binding.mode0.setOnClickListener(this);
        binding.mode1.setOnClickListener(this);
        binding.mode2.setOnClickListener(this);
        binding.mode3.setOnClickListener(this);
        binding.mode4.setOnClickListener(this);

        binding.windS0.setOnClickListener(this);
        binding.windS1.setOnClickListener(this);
        binding.windS2.setOnClickListener(this);
        binding.windS3.setOnClickListener(this);
        binding.windS4.setOnClickListener(this);
        binding.close
                .setOnClickListener(this);

        getAir(channel_id);
    }

    private String air_type = "0";


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add:
                if (!isOpen) {
                    open();
                    isOpen = true;
                    return;
                }
                if (mode==0){
                    showToast("自动模式下只能调风速!");
                    return;
                }
                if (mode==4){
                    showToast("送风模式无法调节温度!");
                    return;
                }
                temp++;
                if (temp > max_temp) {
                    temp = max_temp;
                    return;
                }
                sendCmdToServer(id);

                break;
            case R.id.sub:
                if (!isOpen) {
                    open();
                    isOpen = true;
                    break;
                }
                if (mode==0){
                    showToast("自动模式下只能调风速!");
                    break;
                }
                if (mode==4){
                    showToast("送风模式无法调节温度!");
                    break;
                }
                temp--;
                if (temp < min_temp) {
                    temp = min_temp;
                    break;
                }
                sendCmdToServer(id);
                break;

            case R.id.close:
                if (isOpen) {
                    close();
                    isOpen = false;
                } else {
                    open();
                    isOpen = true;
                }
                break;
            case R.id.mode_0:
            case R.id.mode_1:
            case R.id.mode_2:
            case R.id.mode_3:
            case R.id.mode_4:
                if (!isOpen) {
                    open();
                    isOpen = true;
                    break;
                }
                isOpen = true;
                String tag = (String) v.getTag();
                mode = Integer.parseInt(tag);
                if (mode==4&&wind_s==0){
                    wind_s=1;
                }
                sendCmdToServer(id);
                break;
            case R.id.wind_s_0:
            case R.id.wind_s_1:
            case R.id.wind_s_2:
            case R.id.wind_s_3:
            case R.id.wind_s_4:
                if (!isOpen) {
                    open();
                    return;
                }
                isOpen = true;
                String wind_s_tag = (String) v.getTag();
                if (mode==4){
                    if (Integer.parseInt(wind_s_tag)==0){
                        showToast("通风模式可调1,2,3,4档风");
                        return;
                    }
                }
                wind_s = Integer.parseInt(wind_s_tag);
                sendCmdToServer(id);
                break;
        }

        updateUI();

    }


    @Override
    public void sendCmd(String id) {
    }

    @Override
    public void stateStringChanged(String modelText, String wenduText, String fengsuText, String fengxiangText) {

        binding.model.setText(modelText);
        binding.wendu.setText(wenduText);
        binding.fengsuText.setText(fengsuText);
        binding.fengxiangText.setText(fengxiangText);

    }


    private void getData(String channel_id) {

    }


    //往服务器发送命令 参数action【open:开，close：关，control：控制】，mode模式，temp温度如16,17 ，wind_s风速,wind_d风向
    private void sendCmdToServer(String id) {
//        System.out.println("id="+id);
        if (TextUtils.isEmpty(id)){
            showToast("出错了!");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("action", "control");
        map.put("mode", mode + "");
        map.put("temp", temp + "");
        map.put("wind_s", wind_s + "");
        map.put("wind_d", wind_d + "");
        addSubscription(api.sendAirCmd(map), new BaseSubscriber<RespondBody<List<AirCmd>>>(getContext()) {
            @Override
            public void onNext(RespondBody<List<AirCmd>> body) {
                showToast(body.msg);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                super.onError(e);
            }
        });
    }


    //打开空调
    private void open() {

        isOpen = true;
        if (TextUtils.isEmpty(id)){
            showToast("出错了!");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("action", "open");
        map.put("mode", mode + "");
        map.put("temp", temp + "");
        map.put("wind_s", wind_s + "");
        map.put("wind_d", wind_d + "");
        addSubscription(api.sendAirCmd(map), new BaseSubscriber<RespondBody<Map<String, String>>>(getContext()) {
            @Override
            public void onNext(RespondBody<Map<String, String>> body) {

                mode = Integer.parseInt(body.data.get("mode"));
                temp = Integer.parseInt(body.data.get("temp"));
                wind_d = Integer.parseInt(body.data.get("wind_d"));
                wind_s = Integer.parseInt(body.data.get("wind_s"));
                updateUI();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                super.onError(e);
            }
        });
    }

    //关闭空调
    private void close() {
        if (TextUtils.isEmpty(id)){
            showToast("出错了!");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("action", "close");
        map.put("mode", mode + "");
        map.put("temp", temp + "");
        map.put("wind_s", wind_s + "");
        map.put("wind_d", wind_d + "");
        addSubscription(api.sendAirCmd(map), new BaseSubscriber<RespondBody<Map<String, String>>>(getContext()) {
            @Override
            public void onNext(RespondBody<Map<String, String>> body) {


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    private String id="";
    private int max_temp=30;
    private int min_temp=16;
    private int temp=16;
    private int mode = 0;
    private int wind_s=0;
    private int wind_d=0;
    private boolean isOpen = false;

    private void getAir(String channel_id) {

        addSubscription(api.getAir(channel_id), new BaseSubscriber<RespondBody<List<AirState>>>(getContext()) {
            @Override
            public void onNext(RespondBody<List<AirState>> listRespondBody) {
                super.onNext(listRespondBody);
                max_temp = Integer.parseInt(listRespondBody.data.get(0).max_temp);
                min_temp = Integer.parseInt(listRespondBody.data.get(0).min_temp);
                id = listRespondBody.data.get(0).id;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    private String[] modelString = {"自动模式", "制冷", "制热", "抽湿", "送风"};

    private void updateUI() {
        binding.wendu.setText(temp + "℃");
        binding.model.setText(modelString[mode]);
        if (isOpen) {
            binding.wendu.setVisibility(View.VISIBLE);
            binding.model.setVisibility(View.VISIBLE);
            binding.feng.setVisibility(View.VISIBLE);

        } else {
            binding.wendu.setVisibility(View.INVISIBLE);
            binding.model.setVisibility(View.INVISIBLE);
            binding.feng.setVisibility(View.INVISIBLE);

        }

        for (int i = 0; i < binding.modelLL.getChildCount(); i++) {
            if (isOpen) {
                int mode_t = Integer.parseInt((String) binding.modelLL.getChildAt(i).getTag());
                if (mode_t == mode) {
                    ((TextView) (binding.modelLL.getChildAt(i))).setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ((TextView) (binding.modelLL.getChildAt(i))).setTextColor(Color.parseColor("#888888"));
                }
            } else {
                ((TextView) (binding.modelLL.getChildAt(i))).setTextColor(Color.parseColor("#888888"));

            }

        }

        for (int i = 0; i < binding.windSLL.getChildCount(); i++) {
            if (isOpen) {
                int wind_s_t = Integer.parseInt((String) binding.windSLL.getChildAt(i).getTag());
                if (wind_s_t == wind_s) {
                    ((TextView) (binding.windSLL.getChildAt(i))).setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ((TextView) (binding.windSLL.getChildAt(i))).setTextColor(Color.parseColor("#888888"));
                }
            } else {
                ((TextView) (binding.windSLL.getChildAt(i))).setTextColor(Color.parseColor("#888888"));

            }

        }
    }
}
