package net.suntrans.szxf.uiv2.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.InputType
import android.text.method.TransformationMethod
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

    private var dev_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensus_config)

        //        api.getEnvLog()
        binding!!.jiaquanYuzhi.setOnClickListener(this)
        binding!!.pm25Yuzhi.setOnClickListener(this)
        binding!!.zhendongYuzhi.setOnClickListener(this)
        binding!!.yanwuYuzhi.setOnClickListener(this)
        binding!!.wenduYuzhi.setOnClickListener(this)


        dev_id = intent.getStringExtra("dev_id")
        house_id = intent.getStringExtra("house_id")
        binding!!.title.text = intent.getStringExtra("title")

        binding!!.back.setOnClickListener({
            finish()
        })

        init()

    }

    fun init() {

        val map1 = HashMap<String, String>()
        map1["name"] = "甲醛"
        map1["max"] = "10.0"
        map1["min"] = "0.0"
        map1["field"] = "jiaquan"
        map1["unit"] = "ug/m³"
        binding!!.jiaquanYuzhi.setTag(R.id.jiaquanYuzhi, map1)

        val map2 = HashMap<String, String>()
        map2["name"] = "PM25"
        map2["max"] = "6000.0"
        map2["min"] = "0.0"
        map2["field"] = "pm25"
        map2["unit"] = "ug/m³"
        binding!!.pm25Yuzhi.setTag(R.id.pm25Yuzhi, map2)

        val map3 = HashMap<String, String>()
        map3["name"] = "振动"
        map3["max"] = "6.0"
        map3["min"] = "0.0"
        map3["field"] = "zhendong"
        map3["unit"] = "G"
        binding!!.zhendongYuzhi.setTag(R.id.zhendongYuzhi, map3)

        val map4 = HashMap<String, String>()
        map4["name"] = "烟雾"
        map4["max"] = "2000"
        map4["min"] = "0.0"
        map4["field"] = "yanwu"
        map4["unit"] = "ug/m³"

        binding!!.yanwuYuzhi.setTag(R.id.yanwuYuzhi, map4)

        val map5 = HashMap<String, String>()
        map5["name"] = "温度"
        map5["max"] = "120"
        map5["min"] = "-40"
        map5["field"] = "yanwu"
        map5["unit"] = "℃"
        binding!!.wenduYuzhi.setTag(R.id.wenduYuzhi, map5)

        binding!!.jiaquanSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            setSwitchParam(isChecked,"jiaquan")
        }

        binding!!.pm25Switch.setOnCheckedChangeListener { buttonView, isChecked ->
            setSwitchParam(isChecked,"pm25")

        }

        binding!!.zhendongSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            setSwitchParam(isChecked,"zhendong")
        }
        binding!!.yanwuSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            setSwitchParam(isChecked,"yanwu")

        }
        binding!!.wenduSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            setSwitchParam(isChecked,"wendu")

        }

        binding!!.refreshLayout
                .setOnRefreshListener { getConfig(dev_id) }
    }


    override fun onResume() {
        super.onResume()
        getConfig(dev_id)
    }

    private fun getConfig(house_id: String?) {
        addSubscription(api.getSensusConfig(house_id), object : BaseSubscriber<RespondBody<SensusConfig>>(this) {
            override fun onError(e: Throwable) {
                super.onError(e)
                e.printStackTrace()
                binding!!.refreshLayout.isRefreshing = false

            }

            override fun onNext(sensusConfigRespondBody: RespondBody<SensusConfig>) {
                super.onNext(sensusConfigRespondBody)
                initView(sensusConfigRespondBody.data)
                binding!!.refreshLayout.isRefreshing = false
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

        binding!!.jiaquanSwitch.isChecked = value.substring(0,1) == "1"
        binding!!.pm25Switch.isChecked =value.substring(1,2) == "1"
        binding!!.zhendongSwitch.isChecked = value.substring(2,3) == "1"
        binding!!.yanwuSwitch.isChecked = value.substring(3,4) == "1"
        binding!!.wenduSwitch.isChecked = value.substring(4,5) == "1"


    }

    override fun onClick(v: View) {
        val builder = QMUIDialog.EditTextDialogBuilder(this)
        val editText = builder.editText
        val tag = v!!.getTag(v!!.id) as HashMap<String, String>

        val field = tag["field"]
        val name = tag["name"]
        val min = tag["min"]!!.toFloat()
        val max = tag["max"]!!.toFloat()
        val unit = tag["unit"]
        builder.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)
        builder.setTitle("请输入$name 报警阈值")
        builder.setPlaceholder("$min~$max")
        builder.addAction(R.string.cancel) { dialog,
                                             index ->
            dialog.dismiss()
        }
        builder.addAction(R.string.ok) { dialog,
                                         index ->
            val s = editText.text.toString()

            if (s.toFloat()<min||s.toFloat()>max){
                UiUtils.showToast("$name 的阈值范围为:$min ~ $max")
                return@addAction
            }

            setParam(s, field!!)

            dialog.dismiss()
        }

        builder.create().show()

    }

    private fun setParam(s: String, type: String) {

        addSubscription(api.setSensusConfig(dev_id, s, type), object : BaseSubscriber<RespondBody<*>>(this) {
            override fun onError(e: Throwable?) {
                super.onError(e)
            }

            override fun onNext(t: RespondBody<*>?) {
                super.onNext(t)
                UiUtils.showToast(t!!.msg)
                getConfig(dev_id)
            }
        })

    }

    private fun setSwitchParam(isChecked:Boolean,field: String){
        var value ="1"
        if (isChecked){
            value = "1"
        }else{
            value = "0"
        }
        addSubscription(api.setVoiceConfig(dev_id, value, field), object : BaseSubscriber<RespondBody<*>>(this) {
            override fun onError(e: Throwable?) {
                super.onError(e)
            }

            override fun onNext(t: RespondBody<*>?) {
                super.onNext(t)
                UiUtils.showToast(t!!.msg)
                getConfig(dev_id)
            }
        })
    }
}
