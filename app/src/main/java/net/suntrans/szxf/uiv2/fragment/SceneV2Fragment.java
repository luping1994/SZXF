package net.suntrans.szxf.uiv2.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.szxf.App;
import net.suntrans.szxf.R;
import net.suntrans.szxf.ROLE;
import net.suntrans.szxf.activity.AddSceneActivity;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.uiv2.activity.AddSceneActivityV2;
import net.suntrans.szxf.uiv2.adapter.DividerItemDecoration;
import net.suntrans.szxf.uiv2.bean.SceneEntityV2;
import net.suntrans.szxf.uiv2.bean.SceneInfo;
import net.suntrans.szxf.databinding.FragmentSceneV2Binding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.BasedFragment2;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.LoadingDialog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */

public class SceneV2Fragment extends BasedFragment2 {


    public static final int LINEARLAYOUT = 0;
    public static final int GRIDELAYOUT = 1;
    private int layoutType;

    public static SceneV2Fragment newInstance(int layoutType) {

        Bundle args = new Bundle();
        args.putInt("layoutType",layoutType);
        SceneV2Fragment fragment = new SceneV2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static List<SceneInfo> datas = new CopyOnWriteArrayList<>();
    private FragmentSceneV2Binding binding;
    private SceneAdapter adapter;
    private static Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scene_v2, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));


        layoutType = getArguments().getInt("layoutType");
        if (layoutType == LINEARLAYOUT) {
            adapter = new SceneAdapter(R.layout.item_scene_v2_staff, datas, getContext());
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            adapter = new SceneAdapter(R.layout.item_scene_v2, datas, getContext());
            binding.recyclerView.setAdapter(adapter);
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.scene_header_view, null, false);
            View footerView = LayoutInflater.from(getContext()).inflate(R.layout.item_add_scene, null, false);
            adapter.setFooterView(footerView);
            adapter.setHeaderView(headerView);
            footerView.findViewById(R.id.addScene)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getActivity(), AddSceneActivityV2.class));
                        }
                    });
        }

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                new AlertDialog.Builder(getActivity())
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendOrder(datas.get(position).id);
                            }
                        })
                        .setMessage(R.string.warning_is_control)
                        .create().show();
            }
        });

    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


     class SceneAdapter extends BaseQuickAdapter<SceneInfo, BaseViewHolder> {

        private Context context;

        public SceneAdapter(int layoutResId, @Nullable List<SceneInfo> data, Context context) {
            super(layoutResId, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneInfo item) {
            helper.setText(R.id.name, item.name);
            ImageView imageView = helper.getView(R.id.image);

            Glide.with(context)
                    .load(item.image)
                    .centerCrop()
                    .override(UiUtils.dip2px(36), UiUtils.dip2px(36))
                    .crossFade()
                    .placeholder(R.drawable.ic_nopic)
                    .into(imageView);
        }
    }

    public void getData() {
        mCompositeSubscription.add(api.getSceneV2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<RespondBody<SceneEntityV2>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(RespondBody<SceneEntityV2> channelEntityResultBody) {
                        datas.clear();
                        datas.addAll(channelEntityResultBody.data.lists);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    boolean sending = false;

    private LoadingDialog dialog;

    public void sendOrder(String id) {
        if (sending) {
            UiUtils.showToast("请稍后...");
            return;
        }
        if (dialog == null) {

            dialog = new LoadingDialog(getActivity());
            dialog.setWaitText("请稍后...");
            dialog.setCancelable(false);
        }
        dialog.show();
        sending = true;
        mCompositeSubscription.add(api.switchSceneV2(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<RespondBody>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(RespondBody cmdMsg) {
                        UiUtils.showToast(cmdMsg.msg);
                        adapter.notifyDataSetChanged();
                        sending = false;
                        dialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dialog.dismiss();

                        sending = false;
                    }
                }));
    }
}
