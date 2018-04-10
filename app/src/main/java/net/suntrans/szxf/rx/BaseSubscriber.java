package net.suntrans.szxf.rx;

import net.suntrans.szxf.api.ApiErrorCode;
import net.suntrans.szxf.api.ApiErrorHelper;
import net.suntrans.szxf.api.ApiException;
import net.suntrans.szxf.utils.UiUtils;

import android.content.Context;

import rx.Subscriber;

/**
 * Created by Looney on 2017/9/6.
 */

public class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }


    @Override
    public void onStart() {
        if (!UiUtils.isNetworkAvailable()) {
            this.onError(new ApiException(ApiErrorCode.ERROR_NO_INTERNET, "network interrupt"));
            unsubscribe();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        ApiErrorHelper.handleCommonError(context, e);
    }

    @Override
    public void onNext(T t) {

    }
}
