package net.suntrans.szxf.uiv2.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.databinding.ActivitySceneBinding;
import net.suntrans.szxf.fragment.din.SceneFragment;
import net.suntrans.szxf.uiv2.fragment.SceneV2Fragment;
import net.suntrans.szxf.uiv2.fragment.SceneV2ManagerFragment;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */
public class SceneActivity extends BasedActivity {

    private ActivitySceneBinding binding;
    private SceneV2ManagerFragment sceneManagerFragment;
    private SceneV2Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scene);
        fragment = SceneV2Fragment.newInstance(SceneV2Fragment.GRIDELAYOUT);
        getSupportFragmentManager().beginTransaction().replace(R.id.sceneContent, fragment).commit();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    finish();
                }
            }
        });

    }


    public void rightSubTitleClicked(View view) {
        popupFragment();
    }

    private void popupFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            navitiveToNextFragment();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
           super.onBackPressed();
        }
    }

    private void navitiveToNextFragment() {
        sceneManagerFragment = (SceneV2ManagerFragment) getSupportFragmentManager().findFragmentByTag("SceneManagerFragment");
        if (sceneManagerFragment == null) {
            sceneManagerFragment = new SceneV2ManagerFragment();
        }
        if (sceneManagerFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.sceneContent, sceneManagerFragment, "SceneManagerFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private final FragmentManager.OnBackStackChangedListener mBackStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    updateSubButton();
                }
            };

    private void updateSubButton() {
        boolean isRoot = getSupportFragmentManager().getBackStackEntryCount() == 0;
        if (isRoot){
            fragment.getData();
            binding.rightSubTitle.setText(R.string.subtitle_manager);
        }else {
            binding.rightSubTitle.setText(R.string.subtitle_finish);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);

    }
}
