package net.suntrans.szxf.uiv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.szxf.R;
import net.suntrans.szxf.activity.FloorPlanActivity;
import net.suntrans.szxf.activity.RecyclerviewActivity;
import net.suntrans.szxf.uiv2.adapter.DividerItemDecoration;
import net.suntrans.szxf.uiv2.bean.Floor;
import net.suntrans.szxf.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2018/4/1.
 * Des:
 */
public class FloorActivity extends RecyclerviewActivity<Floor> {

    private List<Floor> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (position==2||position==3){
                    UiUtils.showToast("8,9楼智能配电尚未改造完成");
                    return;
                }
                Intent intent = new Intent(FloorActivity.this, FloorPlanActivity.class);
                intent.putExtra("house_id",data.get(position).id);
                startActivity(intent);
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void setDataBySub(BaseViewHolder helper, Floor item) {
        helper.setText(R.id.name,item.name);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_floor;
    }

    @Override
    protected List<Floor> getDatas() {
        data = new ArrayList<>();
        Floor floor = new Floor("4楼", "4");
        data.add(floor);
        Floor floor2 = new Floor("5楼", "5");
        data.add(floor2);
        Floor floor3 = new Floor("8楼", "8");
        data.add(floor3);
        Floor floor4 = new Floor("9楼", "9");
        data.add(floor4);
        return data;
    }

    @Override
    protected String getTitleText() {
        return "楼层";
    }
}
