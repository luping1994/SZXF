package net.suntrans.szxf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.pgyersdk.update.PgyUpdateManager;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.suntrans.szxf.App;
import net.suntrans.szxf.MainActivity;
import net.suntrans.szxf.R;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.LoginResult;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.EditView;
import net.suntrans.szxf.views.LoadingDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.suntrans.szxf.BuildConfig.DEBUG;

/**
 * Created by Looney on 2017/7/21.
 */

public class LoginActivity extends RxAppCompatActivity implements View.OnClickListener {


    private EditView account;
    private EditView password;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = (EditView) findViewById(R.id.account);
        password = (EditView) findViewById(R.id.password);
        String usernames = App.getSharedPreferences().getString("account","");
        String passwords = App.getSharedPreferences().getString("password","");

        account.setText(usernames);
        password.setText(passwords);
        findViewById(R.id.login).setOnClickListener(this);
        if (!DEBUG)
            PgyUpdateManager.register(this, "net.suntrans.dachu.fileProvider");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            login();
        }
    }

    private void login() {
        String accounts = account.getText().toString();
        String passwords = password.getText().toString();
        if (TextUtils.isEmpty(accounts)) {
            UiUtils.showToast("请输入账号");

            return;
        }
        if (TextUtils.isEmpty(passwords)) {
            UiUtils.showToast("请输入密码");
            return;
        }
        if (dialog== null){
            dialog= new LoadingDialog(this);
            dialog.setWaitText("登录中...");
        }
        dialog.show();
        accounts = accounts.replace(" ","");
        passwords =passwords.replace(" ","");
        final String finalAccounts = accounts;
        final String finalPasswords = passwords;
        RetrofitHelper.getLoginApi().login(accounts, passwords)
                .compose(this.<LoginResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        if (e instanceof HttpException) {
                            if (e.getMessage() != null) {
                                if (e.getMessage().equals("HTTP 401 Unauthorized")) {
                                    UiUtils.showToast("账号或密码错误");
                                }
                            } else {
                                UiUtils.showToast("登录失败,请检查网络连接");
                            }
                        }else if (e instanceof SocketTimeoutException){
                            UiUtils.showToast("连接超时");
                        }else if (e instanceof UnknownHostException){
                            UiUtils.showToast(getString(R.string.tips_network_is_error));
                        }else {
                            UiUtils.showToast("连接服务器失败,请稍后再试");
                        }

                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        dialog.dismiss();
                        if (loginResult != null) {
                            LogUtil.i(loginResult.toString());
                            if (loginResult.data.token.access_token != null) {
                                App.getSharedPreferences().edit().putString("access_token", loginResult.data.token.access_token)
                                        .putString("account", finalAccounts)
                                        .putString("password", finalPasswords)
                                        .putString("expires_in", loginResult.data.token.expires_in)
                                        .putLong("firsttime", System.currentTimeMillis())
                                        .putInt("role_id",loginResult.data.user.role_id)
                                        .commit();
                                App.ROLE_ID = loginResult.data.user.role_id;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                            } else {
                                UiUtils.showToast("服务器错误!登录失败");
                            }

                        } else {

                            UiUtils.showToast("服务器错误!登录失败");

                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
        if (!DEBUG)
            PgyUpdateManager.unregister();

    }
}
