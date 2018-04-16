package net.suntrans.szxf.uiv2.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.TextView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog

import net.suntrans.szxf.R
import net.suntrans.szxf.activity.BasedActivity
import net.suntrans.szxf.bean.RespondBody
import net.suntrans.szxf.databinding.ActivityKtConfigBinding
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.utils.UiUtils

/**
 * Created by Looney on 2018/4/17.
 */

class KtAutoActivity : BasedActivity() {

    private var binding: ActivityKtConfigBinding? = null

    private val maxWendu = 18

    private var channel_id = "0";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kt_config)

        channel_id = intent.getStringExtra("channel_id")

        binding!!.rightSubTitle
                .setOnClickListener({

                })

        binding!!.wendu
                .setOnClickListener {
                    val builder = QMUIDialog.EditTextDialogBuilder(this)
                    val editText = builder.editText
                    builder.addAction(R.string.cancel) { dialog,
                                                         index ->
                        dialog.dismiss()
                    }
                    builder.addAction(R.string.ok) { dialog,
                                                     index ->
                        val s = editText.text.toString()

                        val status = binding!!.wenduSwitch.isChecked
                        var s2 = ""
                        if (status) {
                            s2 = "1"
                        } else {
                            s2 = "0"
                        }

                        val textView = it as  TextView
                        textView.text = s
                        setMaxWendu(channel_id, s, s2)
                        dialog.dismiss()
                    }

                    builder.create().show()
                }


    }


    override fun onResume() {
        super.onResume()
        getMaxWendu(channel_id)
    }

    private fun getMaxWendu(channel_id: String) {
        addSubscription(api.getMaxWendu(channel_id), object : BaseSubscriber<RespondBody<Map<String, String>>>(this) {
            override fun onNext(mapRespondBody: RespondBody<Map<String, String>>) {
                super.onNext(mapRespondBody)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }
        })
    }

    private fun setMaxWendu(channel_id: String, maxwendu: String, status: String) {
        addSubscription(api.setMaxWendu(channel_id, maxwendu, status), object : BaseSubscriber<RespondBody<*>>(this) {
            override fun onNext(mapRespondBody: RespondBody<*>) {
                super.onNext(mapRespondBody)
                UiUtils.showToast(mapRespondBody.msg)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }
        })
    }
}
