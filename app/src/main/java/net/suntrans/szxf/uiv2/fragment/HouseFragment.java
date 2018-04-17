package net.suntrans.szxf.uiv2.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.szxf.DeviceType;
import net.suntrans.szxf.R;
import net.suntrans.szxf.api.Api;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.AreaDetailEntity;
import net.suntrans.szxf.bean.ControlEntity;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.databinding.FragmentRecyclerviewBinding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.BasedFragment2;
import net.suntrans.szxf.uiv2.activity.KongtiaoActivity;
import net.suntrans.szxf.uiv2.bean.ChannelInfo;
import net.suntrans.szxf.uiv2.bean.SceneEntityV2;
import net.suntrans.szxf.uiv2.bean.SceneInfo;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.FullyGridLayoutManager;
import net.suntrans.szxf.views.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2018/4/3.
 * Des:
 */
public class HouseFragment extends BasedFragment2 {

    private String house_type;
    private Adapter adapter;

    public static HouseFragment newInstance(String house_type) {

        Bundle args = new Bundle();
        args.putString("house_type", house_type);
        HouseFragment fragment = new HouseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentRecyclerviewBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.recyclerView.setLayoutManager(new FullyGridLayoutManager(getContext(), 2));
        house_type = getArguments().getString("house_type");
        adapter = new Adapter(R.layout.item_mydevices, datas, getContext());
        binding.recyclerView.setAdapter(adapter);
//        adapter.bindToRecyclerView(binding.recyclerView);
//        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.close) {
                    sendCmd(position,0);
                } else if (view.getId() == R.id.open) {
                    sendCmd(position,1);

                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (datas.get(position).channel_type == DeviceType.AIR_CONDITIONER){
                    Intent intent = new Intent();
                    intent.putExtra("channel_id",datas.get(position).id);
                    intent.putExtra("title",datas.get(position).title);

                    intent.setClass(getActivity(), KongtiaoActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    static class Adapter extends BaseQuickAdapter<ChannelInfo, BaseViewHolder> {

        private Context context;

        public Adapter(int layoutResId, @Nullable List<ChannelInfo> data, Context context) {
            super(layoutResId, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, ChannelInfo item) {
            helper.setText(R.id.name, item.title);
            helper.setText(R.id.status, item.status == 1 ? "(已打开)" : "(已关闭)");
            helper.setTextColor(R.id.status, item.status == 1 ? context.getResources().getColor(R.color.online_color)
                    : context.getResources().getColor(R.color.grey));
            helper.addOnClickListener(R.id.close)
                    .addOnClickListener(R.id.open);


        }
    }

    private List<ChannelInfo> datas = new ArrayList<>();

    public void getData() {
        mCompositeSubscription.add(api.getMydevices(house_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<RespondBody<List<ChannelInfo>>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(RespondBody<List<ChannelInfo>> channelEntityResultBody) {
                        datas.clear();
                        datas.addAll(channelEntityResultBody.data);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    private Api api = RetrofitHelper.getApi();
    private LoadingDialog dialog;
    private void sendCmd(final int position, final int cmd) {
        if (position == -1) {
            UiUtils.showToast("请不要频繁操作！");
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(getActivity(), R.style.loading_dialog);
            dialog.setCancelable(false);

        }
        dialog.setWaitText("请稍后");
        dialog.show();
                mCompositeSubscription.add(api.switchChannel(datas.get(position).din,
                        cmd+"", datas.get(position).number)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new BaseSubscriber<ControlEntity>(getActivity()) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                e.printStackTrace();
                                dialog.dismiss();
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onNext(ControlEntity data) {
                                dialog.dismiss();
                                if (data.code == 200) {
                                    datas.get(position).status = cmd;
                                    adapter.notifyDataSetChanged();
                                    UiUtils.showToast(data.msg);
                                } else if (data.code == 102) {
                                    UiUtils.showToast("您没有控制权限");
                                } else {
                                    UiUtils.showToast(data.msg);
                                }
                            }
                        }));
    }

}
