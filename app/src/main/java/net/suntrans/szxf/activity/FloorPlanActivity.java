package net.suntrans.szxf.activity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;


import net.suntrans.looney.widgets.SegmentedGroup;
import net.suntrans.szxf.App;
import net.suntrans.szxf.R;
import net.suntrans.szxf.api.Api;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.ControlEntity;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.LoadingDialog;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/12/5.
 * Des:
 */

public class FloorPlanActivity extends BasedActivity {

    private String house_id;
    private String token;
    private WebView webview;
    private OrientationEventListener listener;

    private int mScreenProtrait = 1;
    private int mCurrentOrient = 1;
    private RelativeLayout toolbar;
    private View statusbar;
    private Configuration newConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }


        setContentView(R.layout.activity_plan);
//        StatusBarCompat.compat(findViewById(R.id.root));

        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        statusbar = findViewById(R.id.statusbar);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));

        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        house_id = "4";
        token = "Bearer " + App.getSharedPreferences().getString("access_token", "-1");
        webview = (WebView) findViewById(R.id.webview);
        setUpWebview(webview);
//        setOrientationEventListener();

//        listener.enable();


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadJs("token", house_id);
            }
        });


        webview.loadUrl("file:///android_asset/plan/floor_plan.html");
        webview.addJavascriptInterface(new AndroidtoJs(), "control");

        SegmentedGroup segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented_group);

        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio0) {
                    loadJs("token", "4");
                } else if (checkedId == R.id.radio1) {
                    loadJs("token", "5");

                }
            }
        });
    }

    private void loadJs(String token, String house_id) {
        String js = "";
        js += "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"./js/designer.js\";";
        js += "newscript.onload=function(){"
                + "init(\""
                + token + "\",\""
                + house_id + "\");};";
        js += "document.body.appendChild(newscript);";

//                System.out.println(js);
        webview.loadUrl("javascript:" + js);
    }

    private void setUpWebview(WebView webview) {
        WebSettings settings = webview.getSettings();

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setGeolocationEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        webview.setInitialScale(0);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebViewClient(new WebViewClient());
        webview.setVerticalScrollBarEnabled(false);

        WebSettings localWebSettings = webview.getSettings();
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        localWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        webview.setHorizontalScrollBarEnabled(false);//水平不显示


    }

    // 继承自Object类
    public class AndroidtoJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void switchChannel(String control) {
            String[] split = control.split(",");
            final String din = split[0];
            final String number = split[1];
            final String status = split[2];
            final String title = split[3];
            String cmds = "";
            if ("1".equals(status)) {
                cmds = "关闭";
            }else {
                cmds = "打开";
            }

            sendCmd(din, status, number);
//            new AlertDialog.Builder(FloorPlanActivity.this)
//                    .setMessage("是否" + cmds + title)
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .setNegativeButton("取消", null).create().show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        newConfig = getResources().getConfiguration();
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            toolbar.setVisibility(View.GONE);
//            statusbar.setVisibility(View.GONE);
//            //横屏
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            toolbar.setVisibility(View.VISIBLE);
//            statusbar.setVisibility(View.VISIBLE);
//            //竖屏
//        } else if (newConfig.hardKeyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
//            //键盘没关闭。屏幕方向为横屏
//        } else if (newConfig.hardKeyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
//            //键盘关闭。屏幕方向为竖屏
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        listener.disable();
        handler.removeCallbacksAndMessages(null);
        LogUtil.i("onDestroy");
        webview.destroy();
        super.onDestroy();
    }


    private void setOrientationEventListener() {
        listener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {

                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;  //手机平放时，检测不到有效的角度
                }
                //只检测是否有四个角度的改变
                if (orientation > 315 || orientation < 45) { //0度
                    orientation = 0;
                } else if (orientation > 45 && orientation < 135) { //90度
                    orientation = 90;
                } else if (orientation > 135 && orientation < 225) { //180度
                    orientation = 180;
                } else if (orientation > 225 && orientation < 315) { //270度
                    orientation = 270;
                } else {
                    return;
                }
                if (mCurrentOrient != orientation) {
                    orientationChanged(orientation);
                }
//                Log.i("MyOrientationDetector ", "onOrientationChanged:" + orientation);

            }
        };
    }

    private void reLoadJS() {
        String js = "";
        js += "var newscript = document.createElement(\"script\");";
        js += "newscript.text=\'initContainerByToken(\"%1$s\",\"%2$s\")\';";
        js += "document.body.appendChild(newscript);";

        js = String.format(js, token, house_id);
        System.out.println(js);
        webview.loadUrl("javascript:" + js);
    }

    private void orientationChanged(int orientaion) {
        if (orientaion == 270) {
            toolbar.setVisibility(View.GONE);
            statusbar.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }
        if (orientaion == 90) {
            toolbar.setVisibility(View.GONE);
            statusbar.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
        if (orientaion == 0) {
            toolbar.setVisibility(View.VISIBLE);
            statusbar.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
        if (orientaion == 180) {
            toolbar.setVisibility(View.VISIBLE);
            statusbar.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);

        }
        reLoadJS();
        mCurrentOrient = orientaion;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private Api api = RetrofitHelper.getApi();


    private Handler handler = new Handler();

    private void sendCmd(String din, String cmd, String number) {

        if (cmd.equals("1")) {
            cmd = "0";
        } else {
            cmd = "1";
        }
        api.switchChannel(din,
                cmd, number)
                .compose(this.<ControlEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ControlEntity>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ControlEntity data) {
                        if (data.code == 200) {

                            UiUtils.showToast(data.msg);
                        } else if (data.code == 102) {
                            UiUtils.showToast("您没有控制权限");
                        } else {
                            UiUtils.showToast(data.msg);
                        }

                    }
                });
    }

}
