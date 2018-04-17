package net.suntrans.szxf.uiv2.activity

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.View
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

import net.suntrans.szxf.R
import net.suntrans.szxf.activity.BasedActivity
import net.suntrans.szxf.bean.RespondBody
import net.suntrans.szxf.databinding.ActivityParamSettingBinding
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.uiv2.bean.MaxI
import net.suntrans.szxf.utils.UiUtils
import java.util.ArrayList

/**
 * Created by Looney on 2018/4/8.
 * Des:
 */
class ParamSettingActivity : BasedActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        val builder = QMUIDialog.EditTextDialogBuilder(this)
        val editText = builder.editText
       val tag  =  v!!.getTag(v!!.id) as Int
        when (tag) {
            7 -> builder.setTitle("设置过压值(240~280V)")
            8 -> builder.setTitle("设置欠压值(150~200V)")
            else -> builder.setTitle("设置通道 $tag 过流值(0~40A)")
        }
        builder.setInputType(InputType.TYPE_CLASS_NUMBER )
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

    private var binding: ActivityParamSettingBinding? = null
    private var dev_id: String= "0"

    private val datas = ArrayList<MaxI>()
    private val datasU = ArrayList<MaxI>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_param_setting)
        binding!!.back
                .setOnClickListener { finish() }

        dev_id = intent.getStringExtra("id")
        binding!!.title
                .text = intent.getStringExtra("title")
        binding!!.refreshLayout.setOnRefreshListener {
            getData()
        }
        binding!!.refreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        binding!!.guoliu1.setTag(R.id.guoliu1, 1)
        binding!!.guoliu2.setTag(R.id.guoliu2, 2)
        binding!!.guoliu3.setTag(R.id.guoliu3, 3)
        binding!!.guoliu4.setTag(R.id.guoliu4, 4)
        binding!!.guoliu5.setTag(R.id.guoliu5, 5)
        binding!!.guoliu6.setTag(R.id.guoliu6, 6)
        binding!!.guoya.setTag(R.id.guoya, 7)
        binding!!.qianya.setTag(R.id.qianya, 8)

        binding!!.guoliu1.setOnClickListener(this)
        binding!!.guoliu2.setOnClickListener(this)
        binding!!.guoliu3.setOnClickListener(this)
        binding!!.guoliu4.setOnClickListener(this)
        binding!!.guoliu5.setOnClickListener(this)
        binding!!.guoliu6.setOnClickListener(this)
        binding!!.guoya.setOnClickListener(this)
        binding!!.qianya.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {


        addSubscription(api.getMaxI(dev_id), object : BaseSubscriber<RespondBody<List<MaxI>>>(this) {
            override fun onNext(listRespondBody: RespondBody<List<MaxI>>) {
                super.onNext(listRespondBody)
                datas.clear()

                datas.addAll(listRespondBody.data)

                updateView(listRespondBody)
                binding!!.refreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                binding!!.refreshLayout.isRefreshing = false
            }
        })

        addSubscription(api.getMaxU(dev_id), object : BaseSubscriber<RespondBody<List<MaxI>>>(this) {
            override fun onNext(listRespondBody: RespondBody<List<MaxI>>) {
                super.onNext(listRespondBody)
                datasU.clear()
                datasU.addAll(listRespondBody.data)
                updateViewU(listRespondBody)
                binding!!.refreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                e.printStackTrace()
                binding!!.refreshLayout.isRefreshing = false
            }
        })

    }

    private fun setParam(maxI: String, number: Int) {
        var map = HashMap<String,String>()


        if (number==7){
            map["dev_id"] = dev_id
            map["type"] = "2"
        }else if (number==8){
            map["dev_id"] = dev_id
            map["type"] = "3"
        }else{
            val id = filterData(number, datas)!!.id
            map["channel_id"] = id
            map["type"] = "4"

        }
        map["value"] = maxI

        addSubscription(api.setMaxI(map),
                object : BaseSubscriber<RespondBody<*>>(this) {
                    override fun onNext(t: RespondBody<*>?) {
                        super.onNext(t)
                        UiUtils.showToast(t!!.msg)
                        getData()
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                    }

                })


    }

    private fun updateView(listRespondBody: RespondBody<List<MaxI>>) {

        val data = listRespondBody.data

        binding!!.guoliu1.text = filterData(1, data)!!.max_i
        binding!!.guoliu2.text = filterData(2, data)!!.max_i
        binding!!.guoliu3.text = filterData(3, data)!!.max_i
        binding!!.guoliu4.text = filterData(4, data)!!.max_i
        binding!!.guoliu5.text = filterData(5, data)!!.max_i
        binding!!.guoliu6.text = filterData(6, data)!!.max_i
    }

    private fun updateViewU(listRespondBody: RespondBody<List<MaxI>>) {

        val data = listRespondBody.data

        binding!!.guoya.text = data[0].max_u
        binding!!.qianya.text = data[0].min_u
    }

    private fun filterData(number1: Int, data: List<MaxI>): MaxI? {
        return data.filter {
            it.number == number1
        }[0]
    }


}
