package net.suntrans.szxf.activity

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.graphics.Canvas
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.DatePicker
import android.widget.RadioGroup
import android.widget.TextView

import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import net.suntrans.looney.widgets.CompatDatePickerDialog

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
import net.suntrans.szxf.databinding.ActivityYichangTotalBinding
import net.suntrans.szxf.utils.UiUtils.pad
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Looney on 2017/8/17.
 */

class YichangActivityTotal : BasedActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        println("onclick")
        checkedId = v!!.id
        pickerDialog = CompatDatePickerDialog(this, mDateSetListener, mYear, mMonth - 1, mDay)
        val datePicker = pickerDialog!!.getDatePicker()
        datePicker.maxDate = System.currentTimeMillis()

        pickerDialog!!.show()
    }

    private var datas: ArrayList<YichangEntity.Yichang>? = null
    private var adapter: MyAdapter? = null
    private var stateView: StateView? = null
    private var recyclerView: RecyclerView? = null

    private var currentPage = 1
    private val fristLoad = 0
    private val loadMore = 2

    private val totalPage = 0

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var checkedId: Int = 0
    private var pickerDialog: CompatDatePickerDialog? = null

    private var binding: ActivityYichangTotalBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_yichang_total)
        stateView = StateView.inject(findViewById(R.id.content))
        stateView!!.setOnRetryClickListener { getdata(fristLoad, currentShowType, binding!!.startTime.text.toString(), binding!!.endTime.text.toString()) }
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


            getdata(loadMore, currentShowType, binding!!.startTime.text.toString(), binding!!.endTime.text.toString())

        }, recyclerView)
        recyclerView!!.adapter = adapter


        binding!!.startTime.setOnClickListener(this)
        binding!!.endTime.setOnClickListener(this)

        binding!!.radio0.isChecked = true
        binding!!.segmentedGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio0 -> {
                    currentShowType =1
                    (datas as ArrayList<YichangEntity.Yichang>).clear()
                }
                R.id.radio1 -> {
                    currentShowType = 2
                    (datas as ArrayList<YichangEntity.Yichang>).clear()

                }
            }
            currentPage =1
            adapter!!.notifyDataSetChanged()
            getdata(loadMore, currentShowType, binding!!.startTime.text.toString(), binding!!.endTime.text.toString())
            // setData(data);
        }
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH) + 1
        mDay = c.get(Calendar.DAY_OF_MONTH)




        binding!!.endTime.text = mYear.toString() + "-" + pad(mMonth) + "-" + pad(mDay)


        c.add(Calendar.DAY_OF_MONTH, -1)
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH) + 1
        mDay = c.get(Calendar.DAY_OF_MONTH)


        binding!!.startTime.text = mYear.toString() + "-" + pad(mMonth) + "-" + pad(mDay)

        getdata(fristLoad, currentShowType, binding!!.startTime.text.toString(), binding!!.endTime.text.toString())

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


    }

    internal inner class MyAdapter(@LayoutRes layoutResId: Int, data: List<YichangEntity.Yichang>?) : BaseItemDraggableAdapter<YichangEntity.Yichang, BaseViewHolder>(layoutResId, data) {

        override fun convert(helper: BaseViewHolder, item: YichangEntity.Yichang) {
            helper.setText(R.id.msg, item.house_number + "-" + item.name + "-" + item.message )
                    .setText(R.id.time, item.created_at)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getdata(loadtype: Int, showType: Int, startTime: String, endTime: String) {
        if (loadtype == fristLoad) {
            recyclerView!!.visibility = View.INVISIBLE
            stateView!!.showLoading()
        }
        addSubscription(api.getYichangTotal(showType.toString(),currentPage.toString(),startTime,endTime), object : BaseSubscriber<YichangEntity>(this) {
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
                            adapter!!.loadMoreEnd(false)
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
        super.onStop()
    }

    private val mDateSetListener = CompatDatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        mYear = year
        mMonth = monthOfYear + 1
        mDay = dayOfMonth


        if (checkedId == R.id.startTime) {
            binding!!.startTime.text = StringBuilder()
                    .append(mYear).append("-")
                    .append(pad(mMonth)).append("-")
                    .append(pad(mDay))
        } else if (checkedId == R.id.endTime) {
            binding!!.endTime.text = StringBuilder()
                    .append(mYear).append("-")
                    .append(pad(mMonth)).append("-")
                    .append(pad(mDay))
        }


        val startTime = binding!!.startTime.text.toString()
        val endTime = binding!!.endTime.text.toString()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var startTimeLong: Long = 0
        var endTimeLong: Long = 0
        try {
            startTimeLong = sdf.parse(startTime).time
            endTimeLong = sdf.parse(endTime).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (startTimeLong > endTimeLong) {
            UiUtils.showToast("结束时间必须大于起始时间")
            datas!!.clear()
            adapter!!.notifyDataSetChanged()
            return@OnDateSetListener
        }

        currentPage =1
        datas!!.clear()
        adapter!!.notifyDataSetChanged()
        getdata(fristLoad,currentShowType,startTime,endTime)
    }

    private var currentShowType = 1
}
