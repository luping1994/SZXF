package net.suntrans.szxf.uiv2.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.activity.RecyclerviewActivity;
import net.suntrans.szxf.api.Api;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.bean.EnvLog;
import net.suntrans.szxf.uiv2.bean.Floor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2018/4/8.
 * Des:
 */
public class EnvYichangActivity  extends RecyclerviewActivity<EnvLog> {

    private String house_id;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        house_id = getIntent().getStringExtra("house_id");
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    protected void setDataBySub(BaseViewHolder helper, EnvLog item) {

        helper.setText(R.id.msg,item.house_number+"-"+"-"+item.name+"-"+item.msg)
                .setText(R.id.time,item.created_at);

    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_yicahng;
    }

    @Override
    protected List<EnvLog> getDatas() {
        return new ArrayList<>();
    }

    @Override
    protected String getTitleText() {
        return getIntent().getStringExtra("title");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    //    private Api api = RetrofitHelper.getApi();
    private void getData(){
       addSubscription( api.getEnvLog(house_id),new BaseSubscriber<RespondBody<List<EnvLog>>>(this){
           @Override
           public void onNext(RespondBody<List<EnvLog>> listRespondBody) {
               super.onNext(listRespondBody);
               datas.addAll(listRespondBody.data);
               refreshLayout.setRefreshing(false);
               adapter.notifyDataSetChanged();
           }

           @Override
           public void onError(Throwable e) {
               super.onError(e);
               refreshLayout.setRefreshing(false);

           }
       });
    }
}
