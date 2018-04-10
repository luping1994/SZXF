package net.suntrans.szxf.api;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import net.suntrans.szxf.App;
import net.suntrans.szxf.activity.AlertActivity;
import net.suntrans.szxf.activity.BasedActivity;
import net.suntrans.szxf.activity.LoginActivity;
import net.suntrans.szxf.rx.RxBus;
import net.suntrans.szxf.utils.UiUtils;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Looney on 2017/9/6.
 */

public class ApiErrorHelper {
    public static void handleCommonError(final Context context, Throwable e) {
        if (e instanceof HttpException) {
            UiUtils.showToast("远程服务不可用");
        } else if (e instanceof IOException) {
//            UiUtils.showToast("连接服务器失败");
        } else if (e instanceof ApiException) {
            int code = ((ApiException) e).code;
            if (code == ApiErrorCode.UNAUTHORIZED) {
                UiUtils.showToast("您的身份已过期,请重新登录");
                Intent intent =new Intent(context, AlertActivity.class);
                RxBus.getInstance().post(intent);
            } else if (code == ApiErrorCode.ERROR_NO_INTERNET) {
                UiUtils.showToast("网络连接不可用");
            }else {
                UiUtils.showToast(((ApiException) e).msg);
            }

        } else {
//            UiUtils.showToast("部分数据加载失败!");
        }
    }

    private static  void startAlertActivity(Context context) {
        context.startActivity(new Intent(context, AlertActivity.class));
        ((BasedActivity)context).overridePendingTransition(0,0);
    }

    private static void showAlertDialog(final BasedActivity context){
        new AlertDialog.Builder(context)
                .setMessage("您的账号已从别处登录")
                .setCancelable(false)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit()
                                .putString("access_token","-1")
                                .putString("expires_in","-1")
                                .putLong("firsttime",-1l)
                                .commit();
                        context.killAll();
                    }
                })
                .setNegativeButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit()
                                .putString("access_token","-1")
                                .putString("expires_in","-1")
                                .putLong("firsttime",-1l)
                                .commit();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context. startActivity(intent);
                        context. overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_fade_out);
                        context.killAll();
                    }
                })
                .create().show();
    }
}
