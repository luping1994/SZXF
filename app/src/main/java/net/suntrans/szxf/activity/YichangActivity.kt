package net.suntrans.szxf.activity

import android.annotation.SuppressLint
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.graphics.Canvas
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.TextView

import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemSwipeListener

import net.suntrans.szxf.App
import net.suntrans.szxf.R
import net.suntrans.szxf.api.RetrofitHelper
import net.suntrans.szxf.bean.SampleResult
import net.suntrans.szxf.bean.YichangEntity
import net.suntrans.szxf.rx.BaseSubscriber
import net.suntrans.szxf.utils.ActivityUtils
import net.suntrans.szxf.utils.UiUtils
import net.suntrans.stateview.StateView
import net.suntrans.szxf.Config.UNIT_I

import java.util.ArrayList

/**
 * Created by Looney on 2017/8/17.
 */

class YichangActivity : BasedActivity() {

    private var datas: MutableList<YichangEntity.Yichang>? = null
    private var adapter: MyAdapter? = null
    private var stateView: StateView? = null
    private var recyclerView: RecyclerView? = null

    private var currentPage = 1
    private val fristLoad = 0
    private val loadMore = 2

    private val totalPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yichang)
        stateView = StateView.inject(findViewById(R.id.content))
        stateView!!.setOnRetryClickListener { getdata(fristLoad) }
        initToolBar()
        init()
    }

    private fun init() {
        datas = ArrayList<YichangEntity.Yichang>()
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        adapter = MyAdapter(R.layout.item_yicahng, datas)
        val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(adapter)
        val touchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
        touchHelper.attachToRecyclerView(recyclerView)
        adapter!!.enableSwipeItem()
        //        adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
        //            @Override
        //            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
        //
        //            }
        //
        //            @Override
        //            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
        //
        //            }
        //
        //            @Override
        //            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
        //                delete(datas.get(pos).log_id);
        //            }
        //
        //            @Override
        //            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        //
        //            }
        //        });
        adapter!!.setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener {
            if (currentPage > totalPage) {
                adapter!!.loadMoreEnd()
                return@RequestLoadMoreListener
            }
            getdata(loadMore)
        }, recyclerView)
        recyclerView!!.adapter = adapter

    }

    private fun delete(id: Int) {
        //        UiUtils.showToast("已经删除的条目id="+id);
        addSubscription(RetrofitHelper.getApi().deleteLog(id.toString() + ""), object : BaseSubscriber<SampleResult>(this) {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                super.onError(e)
                e.printStackTrace()
            }

            override fun onNext(o: SampleResult) {
                when {
                    o.getCode() == 200 -> UiUtils.showToast("删除成功!")
                    o.getCode() == 401 -> ActivityUtils.showLoginOutDialogFragmentToActivity(supportFragmentManager, "Alert")
                    else -> UiUtils.showToast("删除失败")
                }
            }
        })
    }

    //    @SuppressLint("RestrictedApi")
    private fun initToolBar() {
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        val title = intent.getStringExtra("title")
//        if (title != null) {
//            toolbar.title = title
//        } else {
//
//            toolbar.title = "用电安全"
//        }
//        toolbar.setNavigationIcon(R.drawable.ic_back)
//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayShowTitleEnabled(true)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        findViewById(R.id.back).setOnClickListener(View.OnClickListener { finish() })

        val txTitle = findViewById(R.id.title) as TextView
        txTitle.text = "用电安全"
    }

    internal inner class MyAdapter(@LayoutRes layoutResId: Int, data: List<YichangEntity.Yichang>?) : BaseItemDraggableAdapter<YichangEntity.Yichang, BaseViewHolder>(layoutResId, data) {

        override fun convert(helper: BaseViewHolder, item: YichangEntity.Yichang) {
            helper.setText(R.id.msg, item.house_number + "-" + item.name + "-" + item.message + "(" + item.value + UNIT_I + ")")
                    .setText(R.id.time, item.created_at)
        }
    }

    override fun onResume() {
        super.onResume()
        getdata(fristLoad)
    }

    private fun getdata(loadtype: Int) {
        if (loadtype == fristLoad) {
            recyclerView!!.visibility = View.INVISIBLE
            stateView!!.showLoading()
        }
        addSubscription(api.getYichang(currentPage.toString() + ""), object : BaseSubscriber<YichangEntity>(this) {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                super.onError(e)


                if (loadtype == loadMore)
                    adapter!!.loadMoreFail()
                else {
                    recyclerView!!.visibility = View.INVISIBLE
                    stateView!!.showRetry()
                }
            }

            override fun onNext(o: YichangEntity) {
                if (o.code == 200) {
                    val lists = o.data
                    if (lists == null || lists.size == 0) {
                        if (loadtype == fristLoad) {
                            stateView!!.showEmpty()
                            recyclerView!!.visibility = View.INVISIBLE
                        } else {
                            adapter!!.loadMoreFail()
                        }

                    } else {
                        if (loadtype == loadMore) {
                            adapter!!.loadMoreComplete()
                        } else {

                        }
                        currentPage++
                        stateView!!.showContent()
                        recyclerView!!.visibility = View.VISIBLE
                        datas!!.addAll(lists)
                        adapter!!.notifyDataSetChanged()
                    }

                } else {
                    UiUtils.showToast("获取数据失败")
                }
            }
        })
    }

    override fun onStop() {
        App.getSharedPreferences().edit().putInt("yichangCount", datas!!.size).commit()
        super.onStop()
    }
}
