package net.suntrans.szxf.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.AddSceneActivity;
import net.suntrans.szxf.activity.FloorPlanActivity;
import net.suntrans.szxf.uiv2.activity.EnergyHomeActivity;
import net.suntrans.szxf.uiv2.activity.EnergyMoniActivity;
import net.suntrans.szxf.uiv2.activity.MessageActivity;
import net.suntrans.szxf.uiv2.activity.SceneActivity;
import net.suntrans.szxf.utils.StatusBarCompat;
import net.suntrans.szxf.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/7/20.
 */

public class AdminHomePageFragment extends RxFragment implements View.OnClickListener {

    private RelativeLayout menu;
    private ImageView banner;
    private String[] items2 = {"更换背景"};
    private View root;
    private RecyclerView recyclerView;

    private List<Map<String, Object>> maps;
    private AdminAdapter adminAdapter;

    private View headerView;

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
        initData();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adminAdapter = new AdminAdapter(R.layout.item_admin_home, maps);
        recyclerView.setAdapter(adminAdapter);
        adminAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position>=maps.size()){
                    UiUtils.showToast(getString(R.string.tip_code_error));
                    return;
                }
                Class<?> aClass = (Class<?>) maps.get(position).get("class");
                if (aClass==null){
                    return;
                }
                Intent intent = new Intent(getActivity(), aClass);
                startActivity(intent);
            }
        });

        headerView = LayoutInflater.from(getContext().getApplicationContext())
                .inflate(R.layout.header_view,recyclerView,false);
        adminAdapter.addHeaderView(headerView);
    }

    private void initData() {
        maps = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "用电状态");
        map1.put("class", EnergyMoniActivity.class);
        map1.put("drawable", getActivity().getResources().getDrawable(R.drawable.ic_ydstate));
        maps.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "能耗");
        map2.put("class", EnergyHomeActivity.class);
        map2.put("drawable", getActivity().getResources().getDrawable(R.drawable.icon_energy));
        maps.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "模式");
        map3.put("class", SceneActivity.class);
        map3.put("drawable", getActivity().getResources().getDrawable(R.drawable.ic_scene));
        maps.add(map3);

        Map<String, Object> map4 = new HashMap<>();
        map4.put("name", "公告");
        map4.put("class", MessageActivity.class);
        map4.put("drawable", getActivity().getResources().getDrawable(R.drawable.ic_gonggao));
        maps.add(map4);

        Map<String, Object> map5 = new HashMap<>();
        map5.put("name", "设备状态");
        map5.put("drawable", getActivity().getResources().getDrawable(R.drawable.ic_yichang));
        maps.add(map5);

        Map<String, Object> map6 = new HashMap<>();
        map6.put("name", "平面图");
        map6.put("drawable", getActivity().getResources().getDrawable(R.drawable.ic_plan));
        maps.add(map6);

        Map<String, Object> map7 = new HashMap<>();
        map7.put("name", "用电安全");
        map7.put("drawable", getActivity().getResources().getDrawable(R.drawable.ic_safe));
        maps.add(map7);

        Map<String, Object> map8 = new HashMap<>();
        map8.put("name", "环境监测");
        map8.put("drawable", getActivity().getResources().getDrawable(R.drawable.ic_env));
        maps.add(map8);


    }

    @Override
    public void onClick(View v) {

    }


    public class AdminAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {

        public AdminAdapter(int layoutResId, @Nullable List<Map<String, Object>> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Map<String, Object> item) {
            TextView name = helper.getView(R.id.name);
            name.setText((String) item.get("name"));
            Drawable drawable = (Drawable) item.get("drawable");
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            name.setCompoundDrawables(null, drawable, null, null);
        }
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






