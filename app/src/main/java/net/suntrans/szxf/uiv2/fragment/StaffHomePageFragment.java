package net.suntrans.szxf.uiv2.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import net.suntrans.szxf.Config;
import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.AddSceneActivity;
import net.suntrans.szxf.activity.FloorPlanActivity;
import net.suntrans.szxf.uiv2.BasedFragment2;
import net.suntrans.szxf.uiv2.activity.SceneActivity;
import net.suntrans.szxf.uiv2.bean.SceneInfo;
import net.suntrans.szxf.utils.UiUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Looney on 2018/4/2.
 * Des:
 */
public class StaffHomePageFragment extends BasedFragment2 {
    public List<SceneInfo> datas = new CopyOnWriteArrayList<>();
    private SceneV2Fragment fragment;
    private HouseFragment fragment2;
    private HouseFragment fragment3;
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_staff_home, container, false);
        return inflate;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        root = view.findViewById(R.id.root);
        fragment =  SceneV2Fragment.newInstance(SceneV2Fragment.LINEARLAYOUT);
        fragment2 = HouseFragment.newInstance(Config.OFFICE);
        fragment3 = HouseFragment.newInstance(Config.DORM);

        getChildFragmentManager()
                .beginTransaction().replace(R.id.sceneContent, fragment)
                .replace(R.id.officeContent, fragment2)
                .replace(R.id.dormContent, fragment3)
                .commit();

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (fragment != null)
                    fragment.getData();
                if (fragment2 != null)
                    fragment2.getData();
                if (fragment3 != null)
                    fragment3.getData();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        view.findViewById(R.id.menu)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupMenu();
                    }
                });
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private Handler mHandler = new Handler();

    private PopupWindow mPopupWindow;

    private void showPopupMenu() {
        if (mPopupWindow == null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_menu, null);

            view.findViewById(R.id.model).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), SceneActivity.class));
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
}
