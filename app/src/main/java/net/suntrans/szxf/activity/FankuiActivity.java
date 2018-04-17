package net.suntrans.szxf.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;

import net.suntrans.stateview.StateView;
import net.suntrans.szxf.App;
import net.suntrans.szxf.R;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.bean.SampleResult;
import net.suntrans.szxf.bean.YichangEntity;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.bean.Gustbook;
import net.suntrans.szxf.utils.ActivityUtils;
import net.suntrans.szxf.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/8/17.
 */

public class FankuiActivity extends BasedActivity {

    private List<Gustbook> datas;
    private MyAdapter adapter;
    private StateView stateView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichang);
        stateView = StateView.inject(findViewById(R.id.content));
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getdata(fristLoad);
            }
        });
        initToolBar();
        init();
    }

    private void init() {
        datas = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyAdapter(R.layout.item_yicahng, datas);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        adapter.enableSwipeItem();
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
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (currentPage>totalPage){
                    adapter.loadMoreEnd();
                    return;
                }
                getdata(loadMore);
            }
        }, recyclerView);
        recyclerView.setAdapter(adapter);

    }

    private void delete(int id) {
//        UiUtils.showToast("已经删除的条目id="+id);
        addSubscription(RetrofitHelper.getApi().deleteLog(id + ""), new BaseSubscriber<SampleResult>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onNext(SampleResult o) {
                if (o.getCode() == 200) {
                    UiUtils.showToast("删除成功!");
                } else if (o.getCode()==401){
                    ActivityUtils.showLoginOutDialogFragmentToActivity(getSupportFragmentManager(),"Alert");
                }else {
                    UiUtils.showToast("删除失败");
                }
            }
        });
    }

    private void initToolBar() {
      findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView txTitle = (TextView) findViewById(R.id.title);
        txTitle.setText("意见反馈");
    }

    class MyAdapter extends BaseItemDraggableAdapter<Gustbook, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<Gustbook> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Gustbook item) {
            helper.setText(R.id.msg, "" + item.contents )
                    .setText(R.id.time, item.username)
                    .setTextColor(R.id.msg, Color.parseColor("#333333"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata(fristLoad);
    }

    private int currentPage = 1;
    private int fristLoad = 0;
    private int loadMore = 2;

    private int totalPage=0;
    private void getdata(final int loadtype) {
        if (loadtype == fristLoad){
            recyclerView.setVisibility(View.INVISIBLE);
            stateView.showLoading();
        }
        addSubscription(api.getGustbook(currentPage + ""), new BaseSubscriber<RespondBody<List<Gustbook>>>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                super.onError(e);


                if (loadtype == loadMore)
                    adapter.loadMoreFail();
                else{
                    recyclerView.setVisibility(View.INVISIBLE);
                    stateView.showRetry();
                }
            }

            @Override
            public void onNext(RespondBody<List<Gustbook>> o) {
                if (o.code == 200) {
                    List<Gustbook> lists = o.data;
                    if (lists == null || lists.size() == 0) {
                        if (loadtype==fristLoad){
                            stateView.showEmpty();
                            recyclerView.setVisibility(View.INVISIBLE);
                        }else {
                            adapter.loadMoreFail();
                        }

                    } else {
                        if (loadtype == loadMore) {
                            adapter.loadMoreComplete();
                        } else {

                        }
                        currentPage++;
                        stateView.showContent();
                        recyclerView.setVisibility(View.VISIBLE);
                        datas.addAll(lists);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    UiUtils.showToast("获取数据失败");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        App.getSharedPreferences().edit().putInt("yichangCount", datas.size()).commit();
        super.onStop();
    }
}
