package net.suntrans.szxf.uiv2.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.TextView

import net.suntrans.looney.widgets.IosAlertDialog
import net.suntrans.szxf.R
import net.suntrans.szxf.activity.BasedActivity
import net.suntrans.szxf.bean.RespondBody
import net.suntrans.szxf.databinding.ActivityEnergyMoniBinding
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.uiv2.bean.AirCmd
import net.suntrans.szxf.uiv2.fragment.KongtiaoFragment

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

class KongtiaoActivity : BasedActivity() {

    private var binding: ActivityEnergyMoniBinding? = null
    private var channel_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_energy_moni)
        binding!!.back.setOnClickListener { finish() }

        val txTitle = findViewById(R.id.title) as TextView
        val title = intent.getStringExtra("title") + "空调"
        txTitle.text = title

        findViewById(R.id.back)
                .setOnClickListener { finish() }

        channel_id = intent.getStringExtra("channel_id")
        println("channel_id:"+channel_id)

        binding!!.rightSubTitle.visibility = View.VISIBLE
        binding!!.rightSubTitle
                .setText(R.string.sub_title_liandong)

        binding!!.rightSubTitle
                .setOnClickListener {
                    val intent = Intent(this@KongtiaoActivity, KtAutoActivity::class.java)
                    intent.putExtra("channel_id", channel_id)
                    intent.putExtra("title", title)
                    startActivity(intent)
                }


        val fragment = KongtiaoFragment.newInstance(channel_id)
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit()

    }


    private fun getData(channel_ids: String?) {
        addSubscription(api.getTotalCmd(channel_ids), object : BaseSubscriber<RespondBody<List<AirCmd>>>(this) {
            override fun onNext(body: RespondBody<List<AirCmd>>) {

                if (body.data != null && body.data.size > 1) {


                } else {
                    IosAlertDialog(this@KongtiaoActivity)
                            .builder()
                            .setCancelable(false)
                            .setMsg(getString(R.string.tips_kt_unsported))
                            .setPositiveButton(resources.getString(R.string.close)) { finish() }.show()
                }

            }

            override fun onError(e: Throwable) {

                e.printStackTrace()

                super.onError(e)
            }
        })
    }


}
