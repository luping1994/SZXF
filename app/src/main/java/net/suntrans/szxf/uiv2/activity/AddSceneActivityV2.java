package net.suntrans.szxf.uiv2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.szxf.App;
import net.suntrans.szxf.R;
import net.suntrans.szxf.ROLE;
import net.suntrans.szxf.activity.AddSceneChannelActivity;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.databinding.ActivityAddSceneV2Binding;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.bean.ChannelInfo;
import net.suntrans.szxf.uiv2.bean.SceneItem;
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

/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

public class AddSceneActivityV2 extends BasedActivity implements PicChooseFragment.onItemChooseListener, AllChannelFragment.onChannelSelectedListener {

    private ActivityAddSceneV2Binding binding;
    private LoadingDialog dialog;
    private PicChooseFragment fragment;
    private String imgId = "1";
    private AllChannelFragment allChannelFragment;

    private SceneDetailActivityV2.SceneItemAdapter adapter;
    private List<SceneItem> datas;

    private int requestCode = 101;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_scene_v2);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.addDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.ROLE_ID == ROLE.STAFF){
                    allChannelFragment = new AllChannelFragment();
                    allChannelFragment.setOnChannelSelectedListener(AddSceneActivityV2.this);
                    allChannelFragment.show(getSupportFragmentManager(), "allChannel");
                }else {
                    Intent intent = new Intent(AddSceneActivityV2.this, AddSceneChannelActivity.class);
                    intent.putExtra("showType",AddSceneChannelActivity.ADD);
                    startActivityForResult(intent,requestCode);
                }
            }
        });

        binding.bgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PicChooseFragment();
                fragment.setOnItemChooseListener(AddSceneActivityV2.this);
                fragment.show(getSupportFragmentManager(), "choosePic");
            }
        });

        datas = new ArrayList<>();
        adapter = new SceneDetailActivityV2.SceneItemAdapter(R.layout.item_scene_channel, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showModifyDialog(position);
            }
        });
    }


    public void rightSubTitleClicked(View view) {
        createScene();
    }

    private void createScene() {
        String name = binding.sceneName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            UiUtils.showToast(getString(R.string.warning_empty_name));
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText(getString(R.string.info_saving));
            dialog.setCancelable(false);
        }
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("image", imgId);
        if (datas.size()!=0){
            try {
                JSONArray array = new JSONArray();
                for (SceneItem item :
                        datas) {
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

//        System.out.println(name + "," + imgId);

        addSubscription(api.createScene(map), new BaseSubscriber<RespondBody>(getApplicationContext()) {
            @Override
            public void onNext(RespondBody resultBody) {
                super.onNext(resultBody);
                UiUtils.showToast(resultBody.msg);
                dialog.dismiss();
                new IosAlertDialog(AddSceneActivityV2.this)
                        .builder()
                        .setMsg(resultBody.msg)
                        .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).setCancelable(false).show();
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dialog.dismiss();

            }
        });
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


    @Override
    public void onChannelSelected(List<ChannelInfo> items) {
        datas.clear();
        for (int i = 0; i < items.size(); i++) {
            SceneItem item = new SceneItem();
            item.channel_id = items.get(i).id;
            item.title = items.get(i).title;
            datas.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChannelSelected(String ids) {
        //do nothing
    }

    private void showModifyDialog(final int position) {
        String[] items = {getString(R.string.open), getString(R.string.close), getString(R.string.delete)};
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            datas.get(position).status = 1;
                        } else if (which == 1) {
                            datas.get(position).status = 0;

                        } else if (which == 2) {
                            adapter.remove(position);
                        }
                        adapter.notifyDataSetChanged();
                        System.out.println(datas.size());
                    }
                }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode1, int resultCode, Intent data) {
        if (requestCode == requestCode1){
            if (resultCode == 102){
                ArrayList<ChannelInfo> infos = data.getParcelableArrayListExtra("datas");

                datas.clear();
                for (ChannelInfo info :
                    infos    ) {
                    SceneItem item = new SceneItem();
                    item.channel_id = info.id;
                    item.title = info.title;
                    item.name = info.name;
                    item.status = 0;
                    item.device_type = info.device_type;
                    datas.add(item);

                }
                adapter.notifyDataSetChanged();
                infos.clear();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
