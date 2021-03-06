package net.suntrans.szxf.fragment.din;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.EnvDetailActivity;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.SensusEntity;
import net.suntrans.szxf.fragment.base.BasedFragment;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/20.
 */

public class EnvFragment extends BasedFragment {

    private RecyclerView recyclerView;
    private List<SensusEntity.SixInfo> datas;
    private EnvAdapter adapter;
    private Observable<SensusEntity> getDataOb;
    private SwipeRefreshLayout refreshLayout;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(1);

            }
        });
        adapter = new EnvAdapter(R.layout.item_env, datas);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.addFooterView(getFooterView());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), EnvDetailActivity.class);
                intent.putExtra("id", datas.get(position).id);
                intent.putExtra("info", datas.get(position).sub);
                intent.putExtra("name", datas.get(position).name);
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);

    }

    private View getFooterView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.footer, null, false);
        return view;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_env;
    }


    private class EnvAdapter extends BaseQuickAdapter<SensusEntity.SixInfo, BaseViewHolder> {

        public EnvAdapter(@LayoutRes int layoutResId, @Nullable List<SensusEntity.SixInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SensusEntity.SixInfo item) {
            helper.setText(R.id.name, item.name)
                    .setText(R.id.isOnline, item.is_online.equals("1") ? "" : "不在线")
                    .setText(R.id.wendu, item.sub.getWendu() + "℃")
                    .setText(R.id.shidu, item.sub.getShidu() + "%")
                    .setText(R.id.pm25, item.sub.getPm25())
                    .setText(R.id.guanzhao, item.sub.jiaquanEva);
        }
    }

    @Override
    protected void onFragmentFirstVisible() {

        super.onFragmentFirstVisible();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(0);
    }

    @Override
    public void onRetryClick() {
        getData(0);
        super.onRetryClick();
    }


    private void getData(final int refreshType) {
        if (refreshType == 0){
            stateView.showLoading();
            recyclerView.setVisibility(View.INVISIBLE);
        }
        if (getDataOb == null)
            getDataOb = RetrofitHelper.getApi().getHomeSceneNew()
                    .compose(this.<SensusEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        getDataOb.subscribe(new BaseSubscriber<SensusEntity>(getActivity()) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
                if (refreshType == 0){
                    stateView.showRetry();
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNext(SensusEntity data) {

                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
                if (data.data.lists == null || data.data.lists.size() == 0) {
                    stateView.showEmpty();
                    return;
                }
                stateView.showContent();
                recyclerView.setVisibility(View.VISIBLE);

                if (data.code == 200) {
                    for (SensusEntity.SixInfo ds : data.data.lists) {
                        ds.sub.setEva();
//                        System.out.println(ds.name);
                    }
                    datas.clear();
                    datas.addAll(data.data.lists);
                    adapter.notifyDataSetChanged();
                } else {
                    UiUtils.showToast("暂无信息");
                }
            }
        });
    }


}
