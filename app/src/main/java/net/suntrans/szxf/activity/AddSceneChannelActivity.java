package net.suntrans.szxf.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.suntrans.szxf.R;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.AddSceneChannelResult;
import net.suntrans.szxf.bean.AreaEntity;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.fragment.din.AddSceneChannelFragment;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.activity.SceneDetailActivityV2;
import net.suntrans.szxf.uiv2.bean.ChannelInfo;
import net.suntrans.szxf.uiv2.bean.SceneItem;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class AddSceneChannelActivity extends BasedActivity implements DialogInterface.OnDismissListener {
    public static final String ADD="addScene";
    public static final String MODIFY="modifyScene";


    private AddSceneChannelFragment fragment;
    private LoadingDialog dialog;
    private Subscription subscribe;
    private String id;

    private String showType = MODIFY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scene_channel);
        setUpToolBar();
        init();
        id = getIntent().getStringExtra("scene_id");
        showType =   getIntent().getStringExtra("showType");

    }


    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("选择要添加的设备");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        fragment = new AddSceneChannelFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tijiao, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.commit) {
            List<AreaEntity.AreaFloor> datas = fragment.getDatas();

            ArrayList<ChannelInfo> infos = new ArrayList<>();
            int count = 0;
            final JSONArray array = new JSONArray();
            for (int i = 0; i < datas.size(); i++) {
                for (int j = 0; j < datas.get(i).rooms.size(); j++) {
                    for (int k = 0; k < datas.get(i).rooms.get(j).channels.size(); k++) {
                        ChannelInfo channel = datas.get(i).rooms.get(j).channels.get(k);
                        if (channel.isChecked) {
                            infos.add(channel);
                            count++;
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("id", channel.id+"");
                                jsonObject.put("status", "0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            array.put(jsonObject);

                        }
                    }
                }
            }
            if (count == 0) {
                UiUtils.showToast("您没有选择任何设备");
                return true;
            }
            if (MODIFY.equals(showType)){
                new AlertDialog.Builder(this)
                        .setMessage("是否添加" + count + "个设备")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                addSceneChannel(id,array.toString());
                            }
                        }).setNegativeButton("取消",null).create().show();
            }else if (ADD.equals(showType)){
                Intent intent = new Intent();
                intent.putExtra("datas",infos);
                setResult(102,intent);
                finish();
            }


            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addSceneChannel(String sceneID,String channels) {
        Map<String, String> map = new HashMap<>();
//        map.put("ids", ids);
        map.put("scene_id", sceneID);
        map.put("channels", channels);

        mCompositeSubscription.add(api.addSceneChannel(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<RespondBody>(this) {
                    @Override
                    public void onNext(RespondBody resultBody) {
                        super.onNext(resultBody);
                        UiUtils.showToast(resultBody.msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }


    private void add(String id, String channel_id, String cmd) {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后");
            dialog.setOnDismissListener(this);
        }
        dialog.show();
        subscribe = api.addChannel(id, channel_id, cmd)
                .compose(this.<AddSceneChannelResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddSceneChannelResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        UiUtils.showToast("服务器错误");
                    }

                    @Override
                    public void onNext(AddSceneChannelResult result) {
                        dialog.dismiss();
                        if (result.getMsg() != null) {
                            if (result.getCode() == 200) {
                                int count = 0;
                                for (String s :
                                        result.getMsg()) {
                                    if (s.equals("ok"))
                                        count++;
                                }
                                UiUtils.showToast("添加" + count + "个设备成功");
                                finish();
                            } else if (result.getCode() == 102) {
                                UiUtils.showToast("您没有权限进行该操作");
                            } else if (result.getCode() == 250){
                                int count = 0;
                                for (String s :
                                        result.getMsg()) {
                                    if (s.equals("ok"))
                                        count++;
                                }
                                UiUtils.showToast("添加" + count + "个设备成功");
                            }
                        } else {
                            UiUtils.showToast("服务器错误");
                        }
                    }
                });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        subscribe.unsubscribe();
    }
}
