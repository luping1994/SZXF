package net.suntrans.szxf.uiv2.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.qmuiteam.qmui.widget.dialog.QMUIDialog

import net.suntrans.szxf.R
import net.suntrans.szxf.activity.BasedActivity
import net.suntrans.szxf.bean.RespondBody
import net.suntrans.szxf.databinding.ActivitySensusConfigBinding
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.uiv2.bean.SensusConfig
import net.suntrans.szxf.utils.UiUtils

/**
 * Created by Looney on 2018/4/16.
 * Des:
 */
class SensusConfigActivity : BasedActivity(), View.OnClickListener {

    private var house_id: String? = null

    private var binding: ActivitySensusConfigBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensus_config)
        house_id = intent.getStringExtra("house_id")
        //        api.getEnvLog()
        binding!!.jiaquanYuzhi.setOnClickListener(this)
        binding!!.pm25Yuzhi.setOnClickListener(this)
        binding!!.zhendongYuzhi.setOnClickListener(this)
        binding!!.yanwuYuzhi.setOnClickListener(this)
        binding!!.wenduYuzhi.setOnClickListener(this)

    }


    override fun onResume() {
        super.onResume()
        getConfig(house_id)
    }

    private fun getConfig(house_id: String?) {
        addSubscription(api.getSensusConfig(house_id), object : BaseSubscriber<RespondBody<SensusConfig>>(this) {
            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNext(sensusConfigRespondBody: RespondBody<SensusConfig>) {
                super.onNext(sensusConfigRespondBody)
                initView(sensusConfigRespondBody.data)
            }
        })
    }

    private fun initView(config: SensusConfig) {
        val value = config.value
        val values = arrayOfNulls<String>(value.length)
        for (i in 0 until value.length) {
            values[i] = value.substring(0, i + 1)
        }
        binding!!.jiaquanYuzhi.text = config.jiaquan
        binding!!.pm25Yuzhi.text = config.pm25
        binding!!.zhendongYuzhi.text = config.zhendong
        binding!!.yanwuYuzhi.text = config.yanwu
        binding!!.wenduYuzhi.text = config.wendu

        binding!!.jiaquanSwitch.isChecked = values[0] == "1"
        binding!!.pm25Switch.isChecked = values[1] == "1"
        binding!!.zhendongSwitch.isChecked = values[2] == "1"
        binding!!.yanwuSwitch.isChecked = values[3] == "1"
        binding!!.wenduSwitch.isChecked = values[4] == "1"

    }

    override fun onClick(v: View) {
        val builder = QMUIDialog.EditTextDialogBuilder(this)
        val editText = builder.editText
        val tag  =  v!!.getTag(v!!.id) as Int

        editText.inputType = InputType.TYPE_CLASS_NUMBER
        builder.addAction(R.string.cancel) {
            dialog,
            index -> dialog.dismiss()
        }
        builder.addAction(R.string.ok){
            dialog,
            index ->
            val s = editText.text.toString()
            if (!s.matches("[0-9]+".toRegex())) {
                UiUtils.showToast(getString(R.string.tips_please_input_number))
                return@addAction
            }
            if (tag==7){
                if (s.toInt()<240||s.toInt()>280){
                    UiUtils.showToast(getString(R.string.tips_voltage_max))
                    return@addAction

                }
            }else if (tag==8){
                if (s.toInt()<150||s.toInt()>200){
                    UiUtils.showToast(getString(R.string.tips_voltage_min))
                    return@addAction

                }
            }else{
                if (s.toInt()<0||s.toInt()>40){
                    UiUtils.showToast(getString(R.string.tips_current_max))
                    return@addAction

                }
            }



            setParam(s,tag)
//            when (v!!.id) {
//                R.id.guoliu1 -> {
//                    setParam(s,1)
//                }
//                R.id.guoliu2 -> {
//                    setParam(s,2)
//
//                }
//                R.id.guoliu3 -> {
//                    setParam(s,3)
//
//                }
//                R.id.guoliu4 -> {
//                    setParam(s,4)
//
//                }
//                R.id.guoliu5 -> {
//                    setParam(s,5)
//
//                }
//                R.id.guoliu6 -> {
//                    setParam(s,6)
//
//                }
//            }
            dialog.dismiss()
        }

        builder.create().show()

    }

    private fun setParam(s:String,field :Int){


    }
}
