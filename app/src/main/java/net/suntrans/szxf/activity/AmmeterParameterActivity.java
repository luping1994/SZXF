package net.suntrans.szxf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.suntrans.szxf.R;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.Ameter3;
import net.suntrans.szxf.bean.AmmeterInfos;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.adapter.DividerItemDecoration;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.OffsetDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/7/31.
 */

public class AmmeterParameterActivity extends BasedActivity {
    private String sno;
    private String id;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Map<String, String> dictionary = null;
    private Myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        initData();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new Myadapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,1));
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.addItemDecoration(new OffsetDecoration(UiUtils.dip2px(2)));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void initData() {
        sno = getIntent().getStringExtra("sno");
        id = getIntent().getStringExtra("id");

        LogUtil.e("id="+id);
        dictionary = new HashMap<>();
        dictionary.put("VolA", "A相电压");
        dictionary.put("VolAU", "V");
        dictionary.put("updated_at", "更新时间");
        dictionary.put("updated_atU", "");
        dictionary.put("VolB", "B相电压");
        dictionary.put("VolBU", "V");
        dictionary.put("VolC", "C相电压");
        dictionary.put("VolCU", "V");
        dictionary.put("IA", "A相电流");
        dictionary.put("IAU", "A");
        dictionary.put("IB", "B相电流");
        dictionary.put("IBU", "A");
        dictionary.put("IC", "C相电流");
        dictionary.put("ICU", "A");
        dictionary.put("ActivePower", "有功功率");
        dictionary.put("ActivePowerU", "kW");
        dictionary.put("ActivePowerA", "A相有功功率");
        dictionary.put("ActivePowerAU", "kW");
        dictionary.put("ActivePowerB", "B相有功功率");
        dictionary.put("ActivePowerBU", "kW");
        dictionary.put("ActivePowerC", "C相有功功率");
        dictionary.put("ActivePowerCU", "kW ");
        dictionary.put("ReactivePower", "无功功率");
        dictionary.put("ReactivePowerU", "kvar");
        dictionary.put("ReactivePowerA", "A相无功功率");
        dictionary.put("ReactivePowerAU", "kvar");
        dictionary.put("ReactivePowerB", "B相无功功率");
        dictionary.put("ReactivePowerBU", "kvar");
        dictionary.put("ReactivePowerC", "C相无功功率");
        dictionary.put("ReactivePowerCU", "kvar");
        dictionary.put("PowerFactor", "功率因数");
        dictionary.put("PowerFactorU", "");
        dictionary.put("PowerFactorA", "A相功率因数");
        dictionary.put("PowerFactorAU", "");
        dictionary.put("PowerFactorB", "B相功率因数");
        dictionary.put("PowerFactorBU", "");
        dictionary.put("PowerFactorC", "C相功率因数");
        dictionary.put("PowerFactorCU", "");
        dictionary.put("EletricityValue", "用电量");
        dictionary.put("EletricityValueU", "kW·h");
//        dictionary.put("EletricitySharp", "尖");
//        dictionary.put("EletricitySharpU", "");
        dictionary.put("EletricityPeak", "峰");
        dictionary.put("EletricityPeakU", "kW·h");
        dictionary.put("EletricityFlat", "平");
        dictionary.put("EletricityFlatU", "kW·h");
        dictionary.put("EletricityValley", "谷");
        dictionary.put("EletricityValleyU", "kW·h");
        dictionary.put("Temp", "电表温度");
        dictionary.put("TempU", "℃");


        dictionary.put("electricity", "用电量");
        dictionary.put("electricityU", "kW·h");


        dictionary.put("current", "电流");
        dictionary.put("currentU", "A");

        dictionary.put("voltage", "电压");
        dictionary.put("voltageU", "V");

        dictionary.put("power", "功率");
        dictionary.put("powerU", "kW");

        dictionary.put("power_rate", "功率因数");
        dictionary.put("power_rateU", "");
    }

    private void getData() {
//        System.out.println(sno);
        addSubscription(RetrofitHelper.getApi().getAmmeterInfo(id), new BaseSubscriber<AmmeterInfos>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onNext(AmmeterInfos data) {

                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
                Map<String, Object> map = data.data.get(0);
                datas.clear();
                for (Map.Entry<String, Object> entry : map.entrySet()) {

                    if (entry.getKey().equals("id")
                            || entry.getKey().equals("EletricitySharp")
                            || entry.getKey().equals("din") || entry.getKey().equals("user_id")) {
                        continue;
                    }
                    if (entry.getKey().equals("sno")){
                        sno = (String) entry.getValue();
                        continue;
                    }
                    Ameter3 ameter3 = new Ameter3(entry.getKey(), (String) entry.getValue(), dictionary.get(entry.getKey()),entry.getKey());
                    ameter3.unit = dictionary.get(entry.getKey() + "U");
                    datas.add(ameter3);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private List<Ameter3> datas = new ArrayList<>();

    class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        private Activity mActivity;

        public Myadapter(Activity mActivity) {
            this.mActivity = mActivity;
        }

        @Override

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.item_ameter3, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(position);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView value;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                value = (TextView) itemView.findViewById(R.id.value);
                itemView.findViewById(R.id.dianliull).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(mActivity, HistroyDataActivity.class);
//                        intent.putExtra("sno", sno);
//                        intent.putExtra("vtype", "3");
//                        intent.putExtra("name",getIntent().getStringExtra("name") );
//                        intent.putExtra("data_type", datas.get(getAdapterPosition()).name);
//                        intent.putExtra("shuoming", dictionary.get(datas.get(getAdapterPosition()).name));
//                        startActivity(intent);
                        if("更新时间".equals(datas.get(getAdapterPosition()).nameCH)){
                            return;
                        }
                        Intent intent = new Intent(AmmeterParameterActivity.this, ZHCurHisActivity.class);
                        intent.putExtra("paramName",datas.get(getAdapterPosition()).nameCH);
                        intent.putExtra("datapoint",datas.get(getAdapterPosition()).name);
                        intent.putExtra("unit",datas.get(getAdapterPosition()).unit);
                        intent.putExtra("sno",sno);
                        intent.putExtra("id",id);
                        intent.putExtra("title",datas.get(getAdapterPosition()).name+"历史记录");
                        startActivity(intent);

                    }
                });
            }

            public void setData(int position) {
                name.setText(datas.get(position).nameCH);
                value.setText(datas.get(position).value + datas.get(position).unit);
            }
        }
    }

}
