package net.suntrans.szxf.uiv2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.szxf.App;
import net.suntrans.szxf.R;
import net.suntrans.szxf.ROLE;
import net.suntrans.szxf.activity.AddSceneChannelActivity;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.activity.SceneDetailActivity;
import net.suntrans.szxf.activity.SceneTimingActivity;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.databinding.ActivitySceneDetailV2Binding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.adapter.DividerItemDecoration;
import net.suntrans.szxf.uiv2.bean.ChannelInfo;
import net.suntrans.szxf.uiv2.bean.SceneItem;
import net.suntrans.szxf.uiv2.bean.SceneItemlEntity;
import net.suntrans.szxf.uiv2.fragment.AllChannelFragment;
import net.suntrans.szxf.uiv2.fragment.PicChooseFragment;
import net.suntrans.szxf.utils.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/15.
 * Des:
 */

public class SceneDetailActivityV2 extends BasedActivity implements PicChooseFragment.onItemChooseListener, AllChannelFragment.onChannelSelectedListener {

    ActivitySceneDetailV2Binding binding;
    private String sceneID;

    private List<SceneItem> datas;
    private SceneItemAdapter adapter;
    private LoadingDialog dialog;
    private PicChooseFragment fragment;
    private String imgId = "-1";
    private AllChannelFragment allChannelFragment;
    private String sceneName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scene_detail_v2);

        sceneName = getIntent().getStringExtra("sceneName");
        sceneID = getIntent().getStringExtra("sceneID");
        binding.title.setText(sceneName);
        binding.sceneName.setText(sceneName);
        datas = new ArrayList<>();
        adapter = new SceneItemAdapter(R.layout.item_scene_channel, datas);
//        adapter.bindToRecyclerView(binding.recyclerView);
//        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sceneName.equals(binding.sceneName.getText().toString())) {
                    new AlertDialog.Builder(SceneDetailActivityV2.this)
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    setResult(3, new Intent());

                                }
                            })
                            .setMessage(R.string.warning_not_save)
                            .create().show();
                } else {
                    finish();
                }

            }
        });

        binding.dingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SceneDetailActivityV2.this, SceneTimingActivity.class);
                intent.putExtra("scene_id", sceneID);
                startActivity(intent);
            }
        });

        binding.bgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PicChooseFragment();
                fragment.setOnItemChooseListener(SceneDetailActivityV2.this);
                fragment.show(getSupportFragmentManager(), "choosePic");
            }
        });

        binding.addDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.ROLE_ID == ROLE.STAFF) {
                    allChannelFragment = new AllChannelFragment();
                    allChannelFragment.setOnChannelSelectedListener(SceneDetailActivityV2.this);
                    allChannelFragment.show(getSupportFragmentManager(), "allChannel");
                } else {
                    Intent intent = new Intent(SceneDetailActivityV2.this, AddSceneChannelActivity.class);
                    intent.putExtra("showType", AddSceneChannelActivity.MODIFY);
                    intent.putExtra("scene_id", sceneID);
                    startActivity(intent);
                }

            }
        });
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showModifyDialog(position);
            }
        });
        String img = getIntent().getStringExtra("img");
        Glide.with(this)
                .load(img)
                .into(binding.sceneImg);
    }

    private void showModifyDialog(final int position) {
        String[] items = {getString(R.string.open), getString(R.string.close), getString(R.string.delete)};
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            updateSceneChannenl("1", sceneID, datas.get(position).channel_id + "");
                        } else if (which == 1) {
                            updateSceneChannenl("0", sceneID, datas.get(position).channel_id + "");

                        } else if (which == 2) {
                            deleteeSceneChannenl(sceneID, datas.get(position).channel_id + "");

                        }
                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {
        if (!sceneName.equals(binding.sceneName.getText().toString())) {
            new AlertDialog.Builder(SceneDetailActivityV2.this)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            setResult(3, new Intent());

                        }
                    })
                    .setMessage(R.string.warning_not_save)
                    .create().show();
        } else {
            finish();
        }
    }

    public void rightSubTitleClicked(View view) {
        updateScene();
    }

    private void updateSceneChannenl(String status, String sceneID, String channelID) {
        Map<String, String> map = new HashMap<>();
        map.put("channel_id", channelID);
        map.put("scene_id", sceneID);
        map.put("status", status);
        addSubscription(api.updateSceneChannel(map), new BaseSubscriber<RespondBody>(this) {
            @Override
            public void onNext(RespondBody resultBody) {
                UiUtils.showToast(resultBody.msg);
                getData();
            }
        });
    }

    private void deleteeSceneChannenl(String sceneID, String channelID) {
        Map<String, String> map = new HashMap<>();
        map.put("channel_id", channelID);
        map.put("scene_id", sceneID);
        addSubscription(api.deleteSceneChannel(map), new BaseSubscriber<RespondBody>(this) {
            @Override
            public void onNext(RespondBody resultBody) {
                UiUtils.showToast(resultBody.msg);
                getData();
            }
        });
    }


    private void updateScene() {
        String name = binding.sceneName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            UiUtils.showToast("名称不能为空");
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("保存中...");
            dialog.setCancelable(false);
        }
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("id", sceneID);
        map.put("name", name);
        if (!"-1".equals(imgId)) {
            map.put("image", imgId);
        }

        addSubscription(api.updateSceneInfo(map), new BaseSubscriber<RespondBody>(getApplicationContext()) {
            @Override
            public void onNext(RespondBody resultBody) {
                super.onNext(resultBody);
                if (resultBody.code==200){
                    new IosAlertDialog(SceneDetailActivityV2.this)
                            .builder()
                            .setCancelable(false)
                            .setMsg("修改成功!")
                            .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).show();
                }else {
                    UiUtils.showToast(resultBody.msg);
                }
                sceneName = binding.sceneName.getText().toString();
                dialog.dismiss();
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dialog.dismiss();

            }
        });
    }

    private List<SceneItem> newDatas = new ArrayList<>();

    @Override
    public void onChannelSelected(List<ChannelInfo> items) {
        newDatas.clear();
        for (int i = 0; i < items.size(); i++) {
            SceneItem item = new SceneItem();
            item.channel_id = items.get(i).id;
            item.title = items.get(i).title;
            newDatas.add(item);
        }
        addSceneChannel();
    }

    @Override
    public void onChannelSelected(String ids) {

    }

    private void addSceneChannel() {
        Map<String, String> map = new HashMap<>();
//        map.put("ids", ids);
        map.put("scene_id", sceneID);
        if (newDatas.size() != 0) {
            try {
                JSONArray array = new JSONArray();
                for (SceneItem item :
                        newDatas) {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("id", item.channel_id);
                    jsonObject.put("status", item.status);
                    array.put(jsonObject);

                }
                map.put("channels", array.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mCompositeSubscription.add(api.addSceneChannel(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<RespondBody>(SceneDetailActivityV2.this) {
                    @Override
                    public void onNext(RespondBody resultBody) {
                        super.onNext(resultBody);
                        UiUtils.showToast(resultBody.msg);
                        getData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    public static class SceneItemAdapter extends BaseQuickAdapter<SceneItem, BaseViewHolder> {

        public SceneItemAdapter(int layoutResId, @Nullable List<SceneItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneItem item) {
            helper.setText(R.id.name, item.title)
                    .setText(R.id.status, item.status == 1 ? "打开" : "关闭")
                    .setTextColor(R.id.status, item.status == 1 ?
                            Color.parseColor("#0989fe") : Color.parseColor("#fb5629"));
        }
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        mCompositeSubscription.add(api.getSceneChannelV2(sceneID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<RespondBody<SceneItemlEntity>>(this.getApplicationContext()) {
                    @Override
                    public void onNext(RespondBody<SceneItemlEntity> channelEntityResultBody) {
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

    @Override
    public void onPicChoose(String id, String path) {
        if (fragment != null) {
            fragment.dismiss();
        }
        Glide.with(this)
                .load(path)
                .dontTransform()
                .crossFade()
                .override(UiUtils.dip2px(64), UiUtils.dip2px(64))
                .into(binding.sceneImg);
        imgId = id;
    }

}
