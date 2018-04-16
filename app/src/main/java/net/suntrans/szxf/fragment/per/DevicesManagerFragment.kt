package net.suntrans.szxf.fragment.per

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import com.trello.rxlifecycle.android.FragmentEvent
import com.trello.rxlifecycle.components.support.RxFragment
import net.suntrans.szxf.R
import net.suntrans.szxf.activity.EnvDetailActivity
import net.suntrans.szxf.api.RetrofitHelper
import net.suntrans.szxf.bean.RespondBody
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.uiv2.activity.DeviceDetailActivity
import net.suntrans.szxf.uiv2.adapter.DeviceManagerAdapter
import net.suntrans.szxf.uiv2.bean.DeviceManagerBean
import net.suntrans.szxf.utils.ActivityUtils
import net.suntrans.szxf.utils.UiUtils
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by Looney on 2017/4/20.
 */

class DevicesManagerFragment : RxFragment() {
    private var refreshLayout: SwipeRefreshLayout? = null
    private var adapter: DeviceManagerAdapter? = null
    private var expandableListView: ExpandableListView? = null
    private val datas = ArrayList<DeviceManagerBean>()
    private var status: TextView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_devicesmanager, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        view!!.findViewById(R.id.fanhui).setOnClickListener { activity.finish() }
        status = view.findViewById(R.id.status) as TextView
        expandableListView = view.findViewById(R.id.recyclerView) as ExpandableListView

        adapter = DeviceManagerAdapter(datas, context)
        expandableListView!!.setAdapter(adapter)
        expandableListView!!.divider = null
        refreshLayout = view.findViewById(R.id.refreshlayout) as SwipeRefreshLayout
        refreshLayout!!.setOnRefreshListener { getData() }

        refreshLayout!!.setOnRefreshListener { getData() }
        refreshLayout!!.setColorSchemeColors(activity.resources.getColor(R.color.colorPrimary))

        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val vtype = datas!![groupPosition].device[childPosition].vtype
            if ("4300" == vtype){
                val intent = Intent(activity, DeviceDetailActivity::class.java)
                intent.putExtra("id", datas!![groupPosition].device[childPosition].id.toString() + "")
                intent.putExtra("name", datas!![groupPosition].device[childPosition].title)
                startActivity(intent)
            }else if ("6100" == vtype){

                val intent = Intent(activity, EnvDetailActivity::class.java)
                intent.putExtra("id", datas!![groupPosition].device[childPosition].house_id.toString() + "")
                intent.putExtra("name", datas!![groupPosition].device[childPosition].title)
                intent.putExtra("sno", datas!![groupPosition].device[childPosition].house_id)
                intent.putExtra("source", "manager")
                startActivity(intent)
            }

            true
        }
    }


    val api = RetrofitHelper.getApi();
    fun getData() {
        api.deviceManagerList
                .compose(this.bindUntilEvent<RespondBody<List<DeviceManagerBean>>>(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<RespondBody<List<DeviceManagerBean>>>(activity) {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        e.printStackTrace()

                        refreshLayout?.isRefreshing = false

                    }

                    override fun onNext(homeSceneResult: RespondBody<List<DeviceManagerBean>>?) {
                        refreshLayout?.isRefreshing = false
                        if (homeSceneResult != null) {
                            if (homeSceneResult.code == 200) {
                                if (homeSceneResult.data == null || homeSceneResult.data.isEmpty()) {
                                    return
                                }
                                datas!!.clear()
                                datas!!.addAll(homeSceneResult.data)
                                adapter!!.notifyDataSetChanged()
//                                expandableListView!!.expandGroup(0, true)
                                var isOnlieCount = 0
                                var totalCount = 0
                                homeSceneResult.data.forEach({
                                    totalCount += it.device.size
                                    isOnlieCount += it.device.filter { it.is_online == "1" }.size
                                })

                                var offLine = totalCount - isOnlieCount
                                status!!.text = "在线:$isOnlieCount,离线:$offLine"

                            } else if (homeSceneResult.code == 401) {
                                ActivityUtils.showLoginOutDialogFragmentToActivity(childFragmentManager, "Alert")

                            } else {
                                UiUtils.showToast(homeSceneResult.msg)
                            }
                        }
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        getData()
    }


}
