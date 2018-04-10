package net.suntrans.szxf.fragment

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.trello.rxlifecycle.android.FragmentEvent
import net.suntrans.looney.widgets.IosAlertDialog
import net.suntrans.szxf.R
import net.suntrans.szxf.activity.AddAreaActivity
import net.suntrans.szxf.activity.AddFloorActivity
import net.suntrans.szxf.activity.AreaDetailActivity
import net.suntrans.szxf.adapter.AreaAdapter
import net.suntrans.szxf.api.RetrofitHelper
import net.suntrans.szxf.bean.AreaEntity
import net.suntrans.szxf.bean.RespondBody
import net.suntrans.szxf.bean.SampleResult
import net.suntrans.szxf.fragment.base.BasedFragment
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.utils.ActivityUtils
import net.suntrans.szxf.utils.LogUtil
import net.suntrans.szxf.utils.StatusBarCompat
import net.suntrans.szxf.utils.UiUtils
import net.suntrans.szxf.views.NestedExpandaleListView
import net.suntrans.szxf.views.ScrollChildSwipeRefreshLayout
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.ParsePosition
import java.util.*

/**
 * Created by Looney on 2017/7/20.
 */

class AreaFragment : BasedFragment(), View.OnClickListener {


    override fun getLayoutRes(): Int {
        return R.layout.fragment_area
    }

    private var datas: MutableList<AreaEntity.AreaFloor>? = null
    private var adapter: AreaAdapter? = null
    private var expandableListView: NestedExpandaleListView? = null
    private var add: ImageView? = null
    private var refreshLayout: ScrollChildSwipeRefreshLayout? = null
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val statusBar = view!!.findViewById(R.id.statusbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val statusBarHeight = StatusBarCompat.getStatusBarHeight(context)
            val params = statusBar.layoutParams as LinearLayout.LayoutParams
            params.height = statusBarHeight
            statusBar.layoutParams = params
            statusBar.visibility = View.VISIBLE
        } else {
            statusBar.visibility = View.GONE

        }

        view.findViewById(R.id.buildingOpen)
                .setOnClickListener(this)

        view.findViewById(R.id.buildingClose)
                .setOnClickListener(this)
        view.findViewById(R.id.officeClose)
                .setOnClickListener(this)
        view.findViewById(R.id.officeOpen)
                .setOnClickListener(this)

        view.findViewById(R.id.susheClose)
                .setOnClickListener(this)

        view.findViewById(R.id.susheOpen)
                .setOnClickListener(this)

        refreshLayout = view.findViewById(R.id.refreshlayout) as ScrollChildSwipeRefreshLayout
        refreshLayout?.setOnRefreshListener { getAreaData(1) }
        refreshLayout?.setColorSchemeColors(context.resources.getColor(R.color.colorPrimary))
        datas = ArrayList<AreaEntity.AreaFloor>() as MutableList<AreaEntity.AreaFloor>?
        expandableListView = view!!.findViewById(R.id.recyclerview) as NestedExpandaleListView
        adapter = AreaAdapter(datas, context)
        expandableListView!!.setAdapter(adapter)
        expandableListView!!.divider = null
        add = view!!.findViewById(R.id.add) as ImageView
        add!!.setOnClickListener { v -> showPopupMenu() }

        adapter!!.setOnItemClickListener(object : AreaAdapter.onClickListener {
            override fun onLongParentClick(parentPosition: Int) {

            }

            override fun onParentItemClick(id: Int, parentPosition: Int) {
                var cmd: String = "0"
                var msg = ""
                if (id == R.id.close) {

                    cmd = "0"
                    msg = "是否关闭" + datas!![parentPosition].name.toString() + "所有开关"
                } else if (id == R.id.open) {

                    cmd = "1"
                    msg = "是否打开" + datas!![parentPosition].name.toString() + "所有开关"

                }

                IosAlertDialog(activity)
                        .builder()
                        .setMsg(msg)
                        .setNegativeButton("取消", null)
                        .setPositiveButton(resources.getString(R.string.queding)) {
                            api.switchArea(datas!![parentPosition].area_id.toString(), cmd)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(object : BaseSubscriber<RespondBody<*>>(activity) {

                                        override fun onNext(t: RespondBody<*>?) {
                                            super.onNext(t)
                                            UiUtils.showToast(t!!.msg)

                                        }

                                        override fun onError(e: Throwable?) {
                                            super.onError(e)
                                        }
                                    })
                        }.show()


            }

            override fun onClildItemClick(id: Int, parentPosition: Int, childPosition: Int) {
                var cmd: String = "0"
                var msg = ""
                if (id == R.id.close) {

                    cmd = "0"
                    msg = "是否关闭" + datas!![parentPosition].sub[childPosition].name.toString() + "所有开关"
                } else if (id == R.id.open) {

                    cmd = "1"
                    msg = "是否打开" + datas!![parentPosition].sub[childPosition].name.toString() + "所有开关"

                }
                IosAlertDialog(activity)
                        .builder()
                        .setMsg(msg)
                        .setNegativeButton("取消", null)
                        .setPositiveButton(resources.getString(R.string.queding)) {
                            api.switchHouse(datas!![parentPosition].sub[childPosition].id.toString(), cmd)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(object : BaseSubscriber<RespondBody<*>>(activity) {

                                        override fun onNext(t: RespondBody<*>?) {
                                            super.onNext(t)
                                            UiUtils.showToast(t!!.msg)

                                        }

                                        override fun onError(e: Throwable?) {
                                            super.onError(e)
                                        }
                                    })
                        }.show()

            }

        })
        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val intent = Intent(activity, AreaDetailActivity::class.java)
            intent.putExtra("id", datas!![groupPosition].sub[childPosition].id.toString() + "")
            intent.putExtra("name", datas!![groupPosition].sub[childPosition].name)
            startActivity(intent)
//            activity.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter, android.support.v7.appcompat.R.anim.abc_popup_exit)
            true
        }
        stateView.setEmptyResource(R.layout.base_empty)
        stateView.setLoadingResource(R.layout.base_loading)
        stateView.setRetryResource(R.layout.base_retry)

        super.onViewCreated(view, savedInstanceState)
    }


    private val api = RetrofitHelper.getApi()
    private fun getAreaData(a: Int) {
        if (a == 0) {
            stateView.showLoading()
            refreshLayout?.visibility = View.INVISIBLE
        }

        api.homeHouse
                .compose(this.bindUntilEvent<AreaEntity>(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<AreaEntity>(activity) {
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

                    override fun onNext(homeSceneResult: AreaEntity?) {
                        refreshLayout?.isRefreshing = false
                        if (homeSceneResult != null) {
                            if (homeSceneResult.code == 200) {
                                if (homeSceneResult.data == null || homeSceneResult.data.size == 0) {
                                    stateView.showEmpty()
                                    return
                                }
                                datas!!.clear()
                                datas!!.addAll(homeSceneResult.data)
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

    private var mPopupWindow: PopupWindow? = null
    private fun showPopupMenu() {
        if (mPopupWindow == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_area_menu, null)
            val ll = view.findViewById(R.id.content)
            ViewCompat.setElevation(ll, 20f)
            view.findViewById(R.id.addFloor).setOnClickListener {
                val intent = Intent(activity, AddFloorActivity::class.java)
                intent.putExtra("type", "addFloor")
                startActivity(intent)
                mPopupWindow!!.dismiss()
            }
            view.findViewById(R.id.addArea).setOnClickListener {
                val intent = Intent(activity, AddAreaActivity::class.java)
                intent.putExtra("type", "addFloor")
                startActivity(intent)
                mPopupWindow!!.dismiss()
            }
            mPopupWindow = PopupWindow(context)
            mPopupWindow!!.contentView = view
            mPopupWindow!!.height = WindowManager.LayoutParams.WRAP_CONTENT
            mPopupWindow!!.width = WindowManager.LayoutParams.WRAP_CONTENT
            mPopupWindow!!.animationStyle = R.style.TRM_ANIM_STYLE
            mPopupWindow!!.isFocusable = true
            mPopupWindow!!.isOutsideTouchable = true
            mPopupWindow!!.setBackgroundDrawable(ColorDrawable())
            mPopupWindow!!.setOnDismissListener {
                //                    setBackgroundAlpha(0.75f, 1f, 300);
            }
        }

        if (!mPopupWindow!!.isShowing) {
            val width = UiUtils.getDisplaySize(context)[0]
            val offset = UiUtils.dip2px(38)
            mPopupWindow!!.showAtLocation(add, Gravity.NO_GRAVITY, (width - context.resources.getDimension(R.dimen.pouopwindon_offset)).toInt(), offset)

        }

    }

    private fun deleteFloor(id: Int) {
        AlertDialog.Builder(context)
                .setMessage("是否删除该楼层?")
                .setPositiveButton("确定") { dialog, which -> delete(id) }.setNegativeButton("取消", null).create().show()

    }

    private fun delete(id: Int) {
        RetrofitHelper.getApi().deleteFloor(id.toString() + "")
                .compose(this.bindToLifecycle<SampleResult>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<SampleResult>(activity) {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        super.onError(e)
                    }

                    override fun onNext(result: SampleResult) {
                        if (result.code == 200) {
                            AlertDialog.Builder(context)
                                    .setPositiveButton("确定") { dialog, which -> getAreaData(1) }.setMessage("删除成功")
                                    .create().show()

                        } else {
                            UiUtils.showToast(result.getMsg())
                        }
                    }
                })
    }


    override fun onClick(v: View?) {
        var msg = ""
        var observable: Observable<RespondBody<*>>?=null
        when (v!!.id) {
            R.id.buildingClose -> {
                msg = "是否关闭整栋楼的开关"
                observable = api.switchBuilding("0")
            }
            R.id.buildingOpen -> {
                msg = "是否打开整栋楼的开关"
                observable = api.switchBuilding("1")
            }
            R.id.officeOpen -> {
                msg = "是否打开办公区的开关"
                observable = api.switchOffice("1", "0")
            }
            R.id.officeClose -> {
                msg = "是否关闭办公区的开关"
                observable = api.switchOffice("0", "0")
            }

            R.id.susheOpen -> {
                msg = "是否打开宿舍区的所有的开关"
                observable = api.switchOffice("1", "1")
            }

            R.id.susheClose -> {
                msg = "是否关闭宿舍区的所有的开关"
                observable = api.switchOffice("0","1")
            }
        }

        IosAlertDialog(activity)
                .builder()
                .setMsg(msg)
                .setNegativeButton("取消", null)
                .setPositiveButton(resources.getString(R.string.queding)) {
                    observable!!.
                            observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : BaseSubscriber<RespondBody<*>>(activity) {

                                override fun onNext(t: RespondBody<*>?) {
                                    super.onNext(t)
                                    UiUtils.showToast(t!!.msg)

                                }

                                override fun onError(e: Throwable?) {
                                    super.onError(e)
                                }
                            })
                }.show()
    }
}
