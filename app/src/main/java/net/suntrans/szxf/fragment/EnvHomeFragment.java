package net.suntrans.szxf.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.szxf.R;
import net.suntrans.szxf.ROLE;
import net.suntrans.szxf.fragment.din.EnvFragment;
import net.suntrans.szxf.uiv2.fragment.EnvListFragment;
import net.suntrans.szxf.utils.StatusBarCompat;

/**
 * Created by Administrator on 2017/8/14.
 */

public class EnvHomeFragment extends RxFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_env_home,container,false);
        View statusBar = view.findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeight = StatusBarCompat.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            params.height = statusBarHeight;
            statusBar.setLayoutParams(params);
            statusBar.setVisibility(View.VISIBLE);
        }else {
            statusBar.setVisibility(View.GONE);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Fragment  fragment = new EnvListFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.content,fragment).commit();
    }
}
