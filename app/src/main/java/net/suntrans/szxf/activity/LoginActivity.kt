package net.suntrans.szxf.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.jpush.android.api.JPushInterface

import com.pgyersdk.update.PgyUpdateManager
import com.trello.rxlifecycle.android.ActivityEvent
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

import net.suntrans.szxf.App
import net.suntrans.szxf.MainActivity
import net.suntrans.szxf.R
import net.suntrans.szxf.api.RetrofitHelper
import net.suntrans.szxf.bean.LoginResult
import net.suntrans.szxf.utils.LogUtil
import net.suntrans.szxf.utils.UiUtils
import net.suntrans.szxf.views.EditView
import net.suntrans.szxf.views.LoadingDialog

import java.net.SocketTimeoutException
import java.net.UnknownHostException

import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import net.suntrans.szxf.BuildConfig.DEBUG
import net.suntrans.szxf.Config
import net.suntrans.szxf.Config.FILE_PROVIDER
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.utils.SharedPreferencesHepler

/**
 * Created by Looney on 2017/7/21.
 */

class LoginActivity : RxAppCompatActivity(), View.OnClickListener {


    private var account: EditView? = null
    private var password: EditView? = null
    private var dialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        account = findViewById(R.id.account) as EditView
        password = findViewById(R.id.password) as EditView
        val usernames = App.getSharedPreferences().getString("account", "")
        val passwords = App.getSharedPreferences().getString("password", "")

        account!!.setText(usernames)
        password!!.setText(passwords)
        findViewById(R.id.login).setOnClickListener(this)
        if (!DEBUG)
            PgyUpdateManager.register(this, FILE_PROVIDER)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.login) {
            login()
        }
    }

    private fun login() {
        var accounts = account!!.text.toString()
        var passwords = password!!.text.toString()
        if (TextUtils.isEmpty(accounts)) {
            UiUtils.showToast(getString(R.string.tips_account_empty))

            return
        }
        if (TextUtils.isEmpty(passwords)) {
            UiUtils.showToast(getString(R.string.tips_password_empty))
            return
        }
        if (dialog == null) {
            dialog = LoadingDialog(this)
            dialog!!.setWaitText(getString(R.string.tips_login))
        }
        dialog!!.show()
        accounts = accounts.replace(" ", "")
        passwords = passwords.replace(" ", "")
        val finalAccounts = accounts
        val finalPasswords = passwords
        RetrofitHelper.getLoginApi().login(accounts, passwords)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : BaseSubscriber<LoginResult>(this@LoginActivity) {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        e.printStackTrace()
                        dialog!!.dismiss()

                    }

                    override fun onNext(loginResult: LoginResult?) {
                        dialog!!.dismiss()
                        if (loginResult != null) {
                            if (loginResult.data.token.access_token != null) {
                                App.getSharedPreferences().edit().putString("access_token", loginResult.data.token.access_token)
                                        .putString("account", finalAccounts)
                                        .putString("password", finalPasswords)
                                        .putString("expires_in", loginResult.data.token.expires_in)
                                        .putLong("firsttime", System.currentTimeMillis())
                                        .putInt("role_id", loginResult.data.user.role_id)
                                        .commit()
                                JPushInterface.setAlias(this@LoginActivity,0,finalAccounts)
                                App.ROLE_ID = loginResult.data.user.role_id
                                val office = loginResult.data!!.rooms
                                        .filter {
                                            it.house_type == Config.OFFICE
                                        }
                                val dorm = loginResult.data!!.rooms
                                        .filter {
                                            it.house_type == Config.DORM
                                        }
                                if (office.isNotEmpty()) {
                                    SharedPreferencesHepler.setOfficeHouseId(office[0].house_id)
                                }
                                if (dorm.isNotEmpty()) {
                                    SharedPreferencesHepler.setDormHouseId(dorm[0].house_id)
                                }
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                UiUtils.showToast(getString(R.string.tips_server_error))
                            }

                        } else {

                            UiUtils.showToast(getString(R.string.tips_server_error))

                        }
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
        //        if (!DEBUG)
        //            PgyUpdateManager.unregister();

    }
}
