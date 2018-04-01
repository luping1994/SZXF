package net.suntrans.szxf.uiv2.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.suntrans.szxf.App;
import net.suntrans.szxf.R;
import net.suntrans.szxf.ROLE;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.adapter.FragmentAdapter;
import net.suntrans.szxf.databinding.ActivityMessageBinding;
import net.suntrans.szxf.uiv2.fragment.MessageFragment;


/**
 * Created by Looney on 2017/11/22.
 * Des:消息中心页面
 */

public class MessageActivity extends BasedActivity {

    private ActivityMessageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);

        int role_id = App.ROLE_ID;
        if (role_id == ROLE.LEADER || role_id == ROLE.SUPERVISOR) {
            binding.add.setVisibility(View.VISIBLE);
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MessageActivity.this, AddMessageActivity.class);
                    startActivity(intent);
                }
            });
        } else {
           binding. add.setVisibility(View.GONE);
        }

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        MessageFragment fragment1 = new MessageFragment();
        adapter.addFragment(fragment1, "公告");
        binding.viewPager.setAdapter(adapter);


        binding.fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
