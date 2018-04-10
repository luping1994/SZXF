package net.suntrans.szxf

import net.suntrans.szxf.activity.BasedActivity
import net.suntrans.szxf.bean.RespondBody
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.uiv2.bean.MaxI

import java.util.ArrayList

/**
 * Created by Looney on 2018/4/8.
 * Des:
 */
class Test : BasedActivity() {
    private val datas = ArrayList<MaxI>()

    private fun dsa() {
        addSubscription(api.getMaxI(""), object : BaseSubscriber<RespondBody<*>>(this) {

            override fun onNext(respondBody: RespondBody<*>) {
                super.onNext(respondBody)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }
        })
    }
}
