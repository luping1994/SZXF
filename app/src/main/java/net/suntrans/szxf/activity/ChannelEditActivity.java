package net.suntrans.szxf.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.szxf.R;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.ChannelEditorInfo;
import net.suntrans.szxf.bean.SampleResult;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.SwitchButton;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

public class ChannelEditActivity extends BasedActivity {

    private String channel_id;
    private String channel_type;
    private Spinner spinner;
    private EditText nameTx;
    private String name;
    private SwitchButton usedCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_editor);
        channel_id = getIntent().getStringExtra("id");
        channel_type = getIntent().getStringExtra("channel_type");
        spinner = (Spinner) findViewById(R.id.type);
        nameTx = (EditText) findViewById(R.id.name);
        name = getIntent().getStringExtra("title");
        nameTx.setText(name);
        nameTx.setSelection(name.length() > 10 ? 10 : name.length());

        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));

        if ("1".equals(channel_type)) {
            spinner.setSelection(0);
        } else if ("2".equals(channel_type)) {
            spinner.setSelection(1, true);
        }   else if ("3".equals(channel_type)){
            spinner.setSelection(2);
        }

        usedCB = (SwitchButton) findViewById(R.id.isUsed);

        String checked = getIntent().getStringExtra("used");
        usedCB.setChecked("1".equals(checked));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getData();
    }

    private void getData() {
        if (channel_id == null)
            return;
        RetrofitHelper.getApi()
                .getChannelEditor(channel_id)
                .compose(this.<ChannelEditorInfo>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ChannelEditorInfo>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ChannelEditorInfo info) {
                        super.onNext(info);
                        if (info.data != null) {
                            if ("1".equals(info.data.channel_type)) {
                                spinner.setSelection(0);
                            } else if ("2".equals(info.data.channel_type)) {
                                spinner.setSelection(1, true);

                            } else {
                                spinner.setSelection(0);
                            }
                        }
                    }
                });
    }

    boolean committing = false;

    private void upDate(String id, String name, String channel_type) {

        if (committing) {
            UiUtils.showToast("正在修改请稍后...");
            return;
        }

        String used = usedCB.isChecked() ? "1" : "0";
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("title", name);
        map.put("vtype", channel_type);
        map.put("used", used);
        committing = true;
        addSubscription(RetrofitHelper.getApi().updateChannel(map), new BaseSubscriber<SampleResult>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                committing = false;
            }

            @Override
            public void onNext(SampleResult result) {
                committing = false;
                UiUtils.showToast(result.getMsg());

                if (result.isOk()) {
                    finish();
                }
            }
        });
    }

    public void commit(View view) {
        String name = nameTx.getText().toString();
        String type = spinner.getSelectedItemPosition() + 1 + "";
        if (TextUtils.isEmpty(name)) {
            UiUtils.showToast("名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(type)) {
            UiUtils.showToast("类型不能为空");
            return;
        }

        upDate(channel_id, name, type);
    }
}
