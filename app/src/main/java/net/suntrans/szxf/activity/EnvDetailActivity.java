package net.suntrans.szxf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.szxf.App;
import net.suntrans.szxf.R;
import net.suntrans.szxf.ROLE;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.EnvDetailEntity;
import net.suntrans.szxf.bean.SensusEntity;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.activity.EnvYichangActivity;
import net.suntrans.szxf.uiv2.activity.SensusConfigActivity;
import net.suntrans.szxf.uiv2.fragment.EnvDetailFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/22.
 */

public class EnvDetailActivity extends BasedActivity {
    LinearLayout rootLL;
    TextView time;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int Pwidth;
    private SwipeRefreshLayout refreshLayout;
    private String din;
    private EnvDetailFragment fragment;
    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_detail);
        rootLL = (LinearLayout) findViewById(R.id.rootLL);
        time = (TextView) findViewById(R.id.time);
        din = getIntent().getStringExtra("id");

        TextView tx = (TextView) findViewById(R.id.subTitle);
        source = getIntent().getStringExtra("source");

        if ("manager".equals(source)){
            tx.setText("报警配置");
            if (App.ROLE_ID== ROLE.STAFF){
                findViewById(R.id.subTitle).setVisibility(View.INVISIBLE);
            }else {
                findViewById(R.id.subTitle).setVisibility(View.VISIBLE);
            }

        }else {
            tx.setText("异常记录");
        }

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);//获取屏幕大小的信息
        Pwidth = displayMetrics.widthPixels;   //屏幕宽度,先锋的宽度是800px，小米2a的宽度是720px
        SensusEntity.SixDetailData info = getIntent().getParcelableExtra("info");
        if (info != null)
            info.setEva();
//        initView(info);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("name"));
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("manager".equals(source)){
                            Intent intent = new Intent(EnvDetailActivity.this, SensusConfigActivity.class);
                            intent.putExtra("dev_id",fragment.getDevID());
                            intent.putExtra("title",getIntent().getStringExtra("name"));
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(EnvDetailActivity.this, EnvYichangActivity.class);
                            intent.putExtra("house_id",din);
                            intent.putExtra("title",getIntent().getStringExtra("name"));
                            startActivity(intent);

                        }

                    }
                });
//        initView(null);
        fragment = EnvDetailFragment.Companion.newInstance(din);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }


//    private void initView(SensusEntity.SixDetailData data) {
//        if (data != null) {
//            time.setText(data.updated_at);
//        }
//        for (int i = 0; i < rootLL.getChildCount(); i++) {
//            if (i == 0 || i == 1 || i == 6 || i == 12)
//                continue;
//            initView(i, data);
//        }
//
//    }

    Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

//    private void initView(int position, SensusEntity.SixDetailData data) {
//        TextView nameTx = (TextView) rootLL.getChildAt(position).findViewById(R.id.name);
//        TextView evaluateTx = (TextView) rootLL.getChildAt(position).findViewById(R.id.evaluate);
//        TextView valueTx = (TextView) rootLL.getChildAt(position).findViewById(R.id.value);
//        LinearLayout layout_arrow = (LinearLayout) rootLL.getChildAt(position).findViewById(R.id.layout_arrow);
//        ImageView standard = (ImageView) rootLL.getChildAt(position).findViewById(R.id.standard);   //等级划分条
//        ImageView arrow = (ImageView) rootLL.getChildAt(position).findViewById(R.id.arrow);   //箭头
//
//        switch (position) {
//            case 2:
//                nameTx.setText("温度");
//                standard.setImageResource(R.drawable.ic_wendu_progress);
//                if (data != null) {
//                    valueTx.setText(data.getWendu() + "℃");
//                    evaluateTx.setText(data.wenduEva);
//                    setPading(data.wenduPro, layout_arrow, valueTx);
//                }
//                break;
//            case 3:
//                standard.setImageResource(R.drawable.ic_shidu_progress);
//                nameTx.setText("湿度");
//                if (data != null) {
//                    valueTx.setText(data.getShidu() + "%RH");
//                    evaluateTx.setText(data.shiduEva);
//                    setPading(data.shiduPro, layout_arrow, valueTx);
//                }
//                break;
//            case 4:
//                nameTx.setText("大气压");
//                standard.setImageResource(R.drawable.ic_daqiya_progress);
//                if (data != null) {
//                    valueTx.setText(data.getDaqiya() + "kPa");
//                    evaluateTx.setText(data.daqiYaEva);
//                    setPading(data.daqiyaPro, layout_arrow, valueTx);
//                }
//                break;
////            case 5:
////                nameTx.setText("人员信息");
////                valueTx.setVisibility(View.GONE);
////                layout_arrow.setVisibility(View.INVISIBLE);
////                if (data != null) {
////                    evaluateTx.setText(data.getRenyuan().equals("1") ? "有人" : "没人");
////                }
////                break;
//            case 5:
//                nameTx.setText("光照强度");
//                standard.setImageResource(R.drawable.ic_guanzhao_progress);
//                if (data != null) {
//                    valueTx.setText(data.getLight() + "");
//                    evaluateTx.setText(data.guanzhaoEva);
//                    setPading(data.guanzhaoPro, layout_arrow, valueTx);
//                }
//                break;
//            case 7:
//                nameTx.setText("烟雾");
//                standard.setImageResource(R.drawable.ic_pm_progress);
//                if (data != null) {
//                    valueTx.setText(data.getYanwu() + "ug/m³");
//                    evaluateTx.setText(data.yanwuEva);
//                    setPading(data.yanwuPro, layout_arrow, valueTx);
//                }
//                break;
//            case 8:
//                nameTx.setText("甲醛");
//                standard.setImageResource(R.drawable.ic_pm_progress);
//                if (data != null) {
//                    valueTx.setText(data.getJiaquan() + "ug/m³");
//                    evaluateTx.setText(data.jiaquanEva);
//                    setPading(data.jiaquanPro, layout_arrow, valueTx);
//                }
//                break;
//            case 9:
//                nameTx.setText("PM1");
//                standard.setImageResource(R.drawable.ic_pm_progress);
//                if (data != null) {
//                    valueTx.setText(data.getPM1() + "ug/m³");
//                    evaluateTx.setText(data.pm1Eva);
//                    setPading(data.pm1Pro, layout_arrow, valueTx);
//                }
//                break;
//            case 10:
//                nameTx.setText("PM2.5");
//                standard.setImageResource(R.drawable.ic_pm_progress);
//                if (data != null) {
//                    valueTx.setText(data.getPm25() + "ug/m³");
//                    evaluateTx.setText(data.pm25Eva);
//                    setPading(data.pm25Pro, layout_arrow, valueTx);
//                }
//                break;
//            case 11:
//                nameTx.setText("PM10");
//                standard.setImageResource(R.drawable.ic_pm_progress);
//                if (data != null) {
//                    valueTx.setText(data.getPm10() + "ug/m³");
//                    evaluateTx.setText(data.pm10Eva);
//                    setPading(data.pm10Pro, layout_arrow, valueTx);
//                }
//                break;
////            case 14:
////                nameTx.setText("X轴角度");
////                valueTx.setVisibility(View.GONE);
////                layout_arrow.setVisibility(View.INVISIBLE);
////                if (data!=null){
////                    evaluateTx.setText(data.xEva);
////                }
////                break;
////            case 15:
////                valueTx.setVisibility(View.GONE);
////                layout_arrow.setVisibility(View.INVISIBLE);
////                nameTx.setText("Y轴角度");
////                if (data!=null){
////                    evaluateTx.setText(data.yEva);
////                }
////                break;
//            case 13:
//                valueTx.setVisibility(View.GONE);
//                layout_arrow.setVisibility(View.GONE);
//                standard.setVisibility(View.GONE);
//                nameTx.setText("建筑姿态");
//                if (data != null) {
//                    valueTx.setText(data.getOffset() + "°");
//                    evaluateTx.setText(data.zEva);
//                }
//                break;
//            case 16:
////                valueTx.setVisibility(View.GONE);
////                layout_arrow.setVisibility(View.INVISIBLE);
////                nameTx.setText("振动强度");
////                if (data!=null){
////                    evaluateTx.setText(data.ge+"G");
////                }
//                break;
//
//        }
//    }
//
//    private void setPading(int progress, LinearLayout layout_arrow, TextView value) {
//        value.setVisibility(View.VISIBLE);
//        layout_arrow.setVisibility(View.VISIBLE);
//        layout_arrow.setPadding(Pwidth * progress / 200, 0, 0, 0);
//        if (progress < 50) {
//            value.setGravity(Gravity.LEFT);
//            value.setPadding(Pwidth * progress / 200, 0, 0, 0);   //设置左边距
//        } else {
//            value.setGravity(Gravity.RIGHT);
////            System.out.println(Pwidth);
//            value.setPadding(0, 0, Pwidth * (90 - progress) / 200, 0);  //设置右边距
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        getData(din);
    }

//    private void getData(String din) {
//        RetrofitHelper.getApi().getEnvDetail(din)
//                .compose(this.<EnvDetailEntity>bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<EnvDetailEntity>(this) {
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        if (refreshLayout != null)
//                            refreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onNext(EnvDetailEntity info) {
//                        info.data.setEva();
//                        initView(info.data);
//                        if (refreshLayout != null)
//                            refreshLayout.setRefreshing(false);
//                    }
//                });
//
//    }
}
