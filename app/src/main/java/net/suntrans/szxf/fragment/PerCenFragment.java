package net.suntrans.szxf.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.szxf.App;
import net.suntrans.szxf.MainActivity;
import net.suntrans.szxf.R;
import net.suntrans.szxf.ROLE;
import net.suntrans.szxf.activity.AboutActivity;
import net.suntrans.szxf.activity.ChangePassActivity;
import net.suntrans.szxf.activity.DeviceManagerActivity;
import net.suntrans.szxf.activity.FankuiActivity;
import net.suntrans.szxf.activity.LoginActivity;
import net.suntrans.szxf.activity.QuestionActivity;
import net.suntrans.szxf.activity.YichangActivity;
import net.suntrans.szxf.api.Api;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.SampleResult;
import net.suntrans.szxf.bean.UserInfo;
import net.suntrans.szxf.fragment.base.LazyLoadFragment;
import net.suntrans.szxf.fragment.din.ChangeNameDialogFragment;
import net.suntrans.szxf.fragment.din.UpLoadImageFragment;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.activity.ConLogsActivity;
import net.suntrans.szxf.uiv2.activity.ControlLogsActivity;
import net.suntrans.szxf.uiv2.activity.MessageActivity;
import net.suntrans.szxf.uiv2.bean.Image;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.StatusBarCompat;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.GlideRoundTransform;
import net.suntrans.szxf.views.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/20.
 */

public class PerCenFragment extends LazyLoadFragment implements View.OnClickListener, ChangeNameDialogFragment.ChangeNameListener, UpLoadImageFragment.onUpLoadListener {
    TextView name;
    private ImageView avatar;
    private TextView bagde;
    private RequestManager glideRequest;
//    private List<YichangEntity.DataBean.ListsBean> lists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_percen, container, false);
        View statusBar = view.findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeight = StatusBarCompat.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            params.height = statusBarHeight;
            statusBar.setLayoutParams(params);
            statusBar.setVisibility(View.VISIBLE);
        } else {
            statusBar.setVisibility(View.GONE);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setListener(view);
        avatar = (ImageView) view.findViewById(R.id.img);
        bagde = (TextView) view.findViewById(R.id.bagde);
        glideRequest = Glide.with(this);
        if(App.ROLE_ID == ROLE.STAFF){
            view.findViewById(R.id.gonggao)
            .setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.gonggao)
                    .setVisibility(View.GONE);
        }

    }

    private void setListener(View view) {
        view.findViewById(R.id.RLAbout).setOnClickListener(this);
        view.findViewById(R.id.RLDevice).setOnClickListener(this);
//        view.findViewById(R.id.RLHelp).setOnClickListener(this);
        view.findViewById(R.id.RLModify).setOnClickListener(this);
        view.findViewById(R.id.RLQues).setOnClickListener(this);
        view.findViewById(R.id.loginOut).setOnClickListener(this);
        view.findViewById(R.id.titleHeader).setOnClickListener(this);
        view.findViewById(R.id.RLtishi).setOnClickListener(this);
        view.findViewById(R.id.conLogs).setOnClickListener(this);
        view.findViewById(R.id.gonggao).setOnClickListener(this);

        name = (TextView) view.findViewById(R.id.name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLQues:
                if (App.ROLE_ID == ROLE.STAFF) {
                    startActivity(new Intent(getActivity(), QuestionActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), FankuiActivity.class));

                }
                break;
            case R.id.RLModify:
                startActivity(new Intent(getActivity(), ChangePassActivity.class));

                break;
            case R.id.RLDevice:
                startActivity(new Intent(getActivity(), DeviceManagerActivity.class));
                break;
//            case R.id.RLHelp:
//                break;
            case R.id.RLAbout:
                startActivity(new Intent(getActivity(), AboutActivity.class));

                break;
            case R.id.RLtishi:
                Intent intent = new Intent(getActivity(), YichangActivity.class);
                intent.putExtra("title", "异常提示");
                startActivity(intent);

                break;

            case R.id.loginOut:
                new AlertDialog.Builder(getContext())
                        .setMessage("是否退出当前账号?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                App.getSharedPreferences().edit().clear().commit();
                                ((MainActivity) getActivity()).killAll();
                                JPushInterface.deleteAlias(getContext().getApplicationContext(),0);
                                startActivity(new Intent(getActivity(), LoginActivity.class));

                            }
                        }).setNegativeButton("取消", null).create().show();
                break;
            case R.id.titleHeader:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(items2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                showChangedNameDialog();
                                break;
                            case 1:
                                showBottomSheet();
                                break;
                        }
                    }
                });
                builder.create().show();
                break;
            case R.id.conLogs:
                Intent intent4 = new Intent(getActivity(), ControlLogsActivity.class);
                intent4.putExtra("title", "控制日志");
                startActivity(intent4);

                break;

            case R.id.gonggao:
                Intent intent5 = new Intent(getActivity(), MessageActivity.class);
                intent5.putExtra("title", "公告消息");
                startActivity(intent5);
                break;
        }
    }

    private String[] items2 = {"更改名称", "更换头像"};

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
        getBadgeCount();
    }

    private void checkBadge() {
//        int yichangCount = App.getSharedPreferences().getInt("yichangCount", 0);
//        if (lists==null){
//            bagde.setVisibility(View.INVISIBLE);
//
//        }else {
//            if (lists.size()!=yichangCount){
//                bagde.setVisibility(View.VISIBLE);
//            }else {
//                bagde.setVisibility(View.INVISIBLE);
//
//            }
//        }

    }

    private final Api api = RetrofitHelper.getApi();

    private void getInfo() {

        api.getUserInfo()
                .compose(this.<UserInfo>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UserInfo>(getActivity()) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }

                    @Override
                    public void onNext(UserInfo info) {
                        if (info != null) {
                            if (info.code == 200) {
                                name.setText(info.data.nickname);
                                App.getSharedPreferences().edit().putString("user_id", info.data.id)
                                        .putString("nikename", info.data.nickname)
                                        .putString("touxiang", info.data.avatar_url)
                                        .commit();
//                                LogUtil.i(RetrofitHelper.BASE_URL2 + info.data.avatar_url);
                                glideRequest
                                        .load(info.data.avatar_url)
                                        .asBitmap()
                                        .override(UiUtils.dip2px(33), UiUtils.dip2px(33))
                                        .transform(new GlideRoundTransform(getActivity(), UiUtils.dip2px(16)))
                                        .placeholder(R.drawable.user_white)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                                if (resource != null){

                                                    avatar.setImageBitmap(resource);
                                                }
                                                else {
                                                    avatar.setImageResource(R.drawable.user_white);
                                                }
                                            }
                                        });
                            } else {

                            }
                        } else {
                        }
                    }
                });
    }


    ChangeNameDialogFragment fragment2;

    private void showChangedNameDialog() {
        fragment2 = (ChangeNameDialogFragment) getChildFragmentManager().findFragmentByTag("ChangeNameDialogFragment");
        if (fragment2 == null) {
            fragment2 = ChangeNameDialogFragment.newInstance("更改昵称");
            fragment2.setCancelable(true);
            fragment2.setListener(this);
        }
        fragment2.show(getChildFragmentManager(), "ChangeNameDialogFragment");
    }

    UpLoadImageFragment fragment;

    private void showBottomSheet() {
        fragment = (UpLoadImageFragment) getChildFragmentManager().findFragmentByTag("bottomSheetDialog");
        if (fragment == null) {
            fragment = UpLoadImageFragment.newInstance("2");
            fragment.setCancelable(true);
            fragment.setLoadListener(this);
        }
        fragment.show(getChildFragmentManager(), "bottomSheetDialog");

    }

    @Override
    public void changeName(String name) {
        upDate(name, null);
    }

    @Override
    public void uploadImageSuccess(String path) {
        upDate(null, path);
    }

    private LoadingDialog dialog;

    private void upDate(String name, String path) {
        if (dialog == null) {
            dialog = new LoadingDialog(getContext());
            dialog.setWaitText("请稍后");
        }
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(name)) {
            map.put("nickname", name);
        }
        if (!TextUtils.isEmpty(path)) {
            map.put("avatar_url", path);
        }
        LogUtil.i("percenfragment:" + path);
        ((MainActivity) getActivity()).addSubscription(RetrofitHelper.getApi().updateProfile(map), new BaseSubscriber<SampleResult>(getActivity()) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                dialog.dismiss();
            }

            @Override
            public void onNext(SampleResult result) {
                dialog.dismiss();
                if (result.getCode() == 200) {
                    UiUtils.showToast("更新成功");
                    getInfo();
                } else {
                    UiUtils.showToast(result.getMsg());
                }
            }
        });
    }

    private void getBadgeCount() {
//        ((MainActivity) getActivity()).   addSubscription(RetrofitHelper.getApi().getYichang(), new Subscriber<YichangEntity>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(YichangEntity o) {
//                if (o.getCode()==200){
//                    lists = o.getData().getLists();
//                    int yichangCount = App.getSharedPreferences().getInt("yichangCount", 0);
////                    if (lists.size()!=yichangCount){
////                        bagde.setVisibility(View.VISIBLE);
////                    }else {
////                        bagde.setVisibility(View.INVISIBLE);
////                    }
//                    App.getSharedPreferences().edit().putInt("yichangCount",lists.size()).commit();
//                }else {
//                }
//            }
//        });
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        checkBadge();
    }
}
