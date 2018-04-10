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

import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.SceneDetailActivity;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.uiv2.activity.SceneDetailActivityV2;
import net.suntrans.szxf.uiv2.adapter.DividerItemDecoration;
import net.suntrans.szxf.uiv2.bean.SceneEntityV2;
import net.suntrans.szxf.uiv2.bean.SceneInfo;
import net.suntrans.szxf.databinding.FragmentSceneV2Binding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.BasedFragment2;
import net.suntrans.szxf.utils.UiUtils;


import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */

public class SceneV2ManagerFragment extends BasedFragment2 {


    private FragmentSceneV2Binding binding;
    private SceneAdapter adapter;
    private LoadingDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scene_v2, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter = new SceneAdapter(R.layout.item_scene_manager, SceneV2Fragment.datas, getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.deleteScene) {
                    new IosAlertDialog(getContext())
                            .builder()
                            .setMsg("是否删除?")
                            .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteScene(SceneV2Fragment.datas.get(position).id, position);
                                }
                            }).setNegativeButton(getString(R.string.cancel),null).show();
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SceneDetailActivityV2.class);
                intent.putExtra("sceneName", SceneV2Fragment.datas.get(position).name);
                intent.putExtra("sceneID", SceneV2Fragment.datas.get(position).id);
                intent.putExtra("img", SceneV2Fragment.datas.get(position).image);
                startActivityForResult(intent,2);
            }
        });
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.scene_header_view, null, false);

        adapter.setHeaderView(headerView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    static class SceneAdapter extends BaseQuickAdapter<SceneInfo, BaseViewHolder> {

        private Context context;

        public SceneAdapter(int layoutResId, @Nullable List<SceneInfo> data, Context context) {
            super(layoutResId, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneInfo item) {
            helper.setText(R.id.name, item.name);
            helper.addOnClickListener(R.id.deleteScene);
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

    private void getData() {
        mCompositeSubscription.add(api.getSceneV2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<RespondBody<SceneEntityV2>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(RespondBody<SceneEntityV2> channelEntityResultBody) {
                        SceneV2Fragment.datas.clear();
                        SceneV2Fragment.datas.addAll(channelEntityResultBody.data.lists);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    private void deleteScene(String id, final int position) {
        if (dialog == null) {

            dialog = new LoadingDialog(getActivity());
            dialog.setWaitText(getString(R.string.info_delete_scene));
            dialog.setCancelable(false);
        }

        dialog.show();
        mCompositeSubscription.add(api.deleteSceneV2(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<RespondBody>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(RespondBody channelEntityResultBody) {
                        dialog.dismiss();
                        adapter.remove(position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dialog.dismiss();
                    }
                }));
    }
}
