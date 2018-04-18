package net.suntrans.szxf.uiv2.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageView
import com.trello.rxlifecycle.android.FragmentEvent
import net.suntrans.szxf.R
import net.suntrans.szxf.activity.Ammeter3Activity2
import net.suntrans.szxf.api.Api
import net.suntrans.szxf.api.RetrofitHelper
import net.suntrans.szxf.bean.AreaEntity
import net.suntrans.szxf.bean.EnergyEntity
import net.suntrans.szxf.fragment.base.BasedFragment
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.uiv2.adapter.EnergyListAdapter
import net.suntrans.szxf.uiv2.bean.EnergyListEntity
import net.suntrans.szxf.utils.ActivityUtils
import net.suntrans.szxf.utils.UiUtils
import net.suntrans.szxf.views.ScrollChildSwipeRefreshLayout
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by Looney on 2017/7/20.
 *
 *
 */

class EnergyListFragment : BasedFragment() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_moni
    }

    private var datas: MutableList<EnergyListEntity.FloorBean>? = null
    private var adapter: EnergyListAdapter? = null
    private var expandableListView: ExpandableListView? = null
    private var add: ImageView? = null
    private var refreshLayout: ScrollChildSwipeRefreshLayout? = null
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        refreshLayout = view!!.findViewById(R.id.refreshlayout) as ScrollChildSwipeRefreshLayout
        refreshLayout?.setOnRefreshListener { getAreaData(1) }
        refreshLayout?.setColorSchemeColors(context.resources.getColor(R.color.colorPrimary))

        datas = ArrayList<EnergyListEntity.FloorBean>()
        expandableListView = view!!.findViewById(R.id.recyclerview) as ExpandableListView
        adapter = EnergyListAdapter(datas, context)
        expandableListView!!.setAdapter(adapter)
        expandableListView!!.divider = null


        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            val intent = Intent()
            intent.setClass(activity, Ammeter3Activity2::class.java)
            intent.putExtra("sno", datas!![groupPosition].sub[childPosition].id)
            intent.putExtra("id", datas!![groupPosition].sub[childPosition].id)
            intent.putExtra("name",datas!![groupPosition].sub[childPosition].house_number+"-"+ datas!![groupPosition].sub[childPosition].name)
            startActivity(intent)

            true
        }
        stateView.setEmptyResource(R.layout.base_empty)
        stateView.setLoadingResource(R.layout.base_loading)
        stateView.setRetryResource(R.layout.base_retry)

        super.onViewCreated(view, savedInstanceState)
    }

    private var api: Api? = null

    private fun getAreaData(a: Int) {
        if (a == 0) {
            stateView.showLoading()
            refreshLayout?.visibility = View.INVISIBLE
        }
        if (api==null){
          api =   RetrofitHelper.getApi()
        }
        api!!.energyIndexNewApi
                .compose(this.bindUntilEvent<EnergyListEntity>(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<EnergyListEntity>(activity) {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        e.printStackTrace()
                        if (a == 0) {
                            stateView.showRetry()
                        }
                        refreshLayout?.isRefreshing = false

                    }

                    override fun onNext(homeSceneResult: EnergyListEntity?) {
                        refreshLayout?.isRefreshing = false
                        if (homeSceneResult != null) {
                            if (homeSceneResult.code == 200) {
                                if (homeSceneResult.data == null || homeSceneResult.data.floor.size == 0) {
                                    stateView.showEmpty()
                                    return
                                }
                                datas!!.clear()
                                datas!!.addAll(homeSceneResult.data.floor)
                                adapter!!.notifyDataSetChanged()
//                                expandableListView!!.expandGroup(0, true)
                                if (a == 0) {
                                    refreshLayout?.visibility = View.VISIBLE;
                                    stateView.showContent()
                                }
                            } else if (homeSceneResult.code == 401) {
                                ActivityUtils.showLoginOutDialogFragmentToActivity(childFragmentManager, "Alert")

                            } else {
                                UiUtils.showToast(homeSceneResult.msg)
                                stateView.showRetry()
                            }
                        }
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        getAreaData(0)
    }

    override fun onFragmentFirstVisible() {

    }

    override fun onRetryClick() {
        getAreaData(0)
    }



}
