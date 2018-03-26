package net.suntrans.szxf.fragment;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.szxf.App;
import net.suntrans.szxf.MainActivity;
import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.AddSceneActivity;
import net.suntrans.szxf.activity.FloorPlanActivity;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.SampleResult;
import net.suntrans.szxf.bean.UserInfo;
import net.suntrans.szxf.fragment.din.ChangeNameDialogFragment;
import net.suntrans.szxf.fragment.din.SceneFragment;
import net.suntrans.szxf.fragment.din.UpLoadImageFragment;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.StatusBarCompat;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/20.
 */

public class AdminHomePageFragment extends RxFragment implements View.OnClickListener {

    private RelativeLayout menu;
    private ImageView banner;
    private String[] items2 = {"更换背景"};
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        setHasOptionsMenu(true);
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
        initView(view);
    }

    private void initView(View view) {


    }

    @Override
    public void onClick(View v) {

    }


    private PopupWindow mPopupWindow;

    private void showPopupMenu() {
        if (mPopupWindow == null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_dining_menu, null);

            view.findViewById(R.id.name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), AddSceneActivity.class));
                    mPopupWindow.dismiss();
                }
            });

            view.findViewById(R.id.floor1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FloorPlanActivity.class);
                    intent.putExtra("house_id", "1");
                    intent.putExtra("title", "1楼");
                    startActivity(intent);
                    mPopupWindow.dismiss();
                }
            });
            view.findViewById(R.id.floor2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FloorPlanActivity.class);
                    intent.putExtra("house_id", "2");
                    intent.putExtra("title", "2楼");
                    startActivity(intent);
                    mPopupWindow.dismiss();
                }
            });
            view.findViewById(R.id.floor3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FloorPlanActivity.class);
                    intent.putExtra("house_id", "3");
                    intent.putExtra("title", "2楼");
                    startActivity(intent);
                    mPopupWindow.dismiss();
                }
            });
            view.findViewById(R.id.floor4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FloorPlanActivity.class);
                    intent.putExtra("house_id", "4");
                    intent.putExtra("title", "2楼");
                    startActivity(intent);
                    mPopupWindow.dismiss();
                }
            });
            mPopupWindow = new PopupWindow(view);
            mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setAnimationStyle(R.style.TRM_ANIM_STYLE);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
//                    setBackgroundAlpha(0.75f, 1f, 300);
                }
            });
        }

        if (!mPopupWindow.isShowing()) {
            int width = UiUtils.getDisplaySize(getContext())[0];
            int offset = UiUtils.dip2px(38);

            mPopupWindow.showAtLocation(root, Gravity.NO_GRAVITY, width - (int) getContext().getResources().getDimension(R.dimen.pouopwindon_offset), offset);
//            mPopupWindow.showAtLocation(root, Gravity.NO_GRAVITY, 100,offset);
//            mPopupWindow.showAsDropDown(menu);
//            setBackgroundAlpha(1f, 0.75f, 240);
        }

    }

    private void setBackgroundAlpha(float from, float to, int duration) {
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                getActivity().getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }


}






