package net.suntrans.szxf.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.szxf.R;

import java.util.List;

/**
 * Created by Looney on 2018/4/1.
 * Des:
 */
public abstract class RecyclerviewActivity<Data> extends BasedActivity {


    protected RecyclerView recyclerView;
    protected CommonAdapter adapter;

    protected List<Data> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(getLayoutManager());
        datas = getDatas();
        adapter = new CommonAdapter(getItemLayout(), datas);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fanhui)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getTitleText());
    }


    public class CommonAdapter extends BaseQuickAdapter<Data, BaseViewHolder> {

        public CommonAdapter(int layoutResId, @Nullable List<Data> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Data item) {

            setDataBySub(helper, item);
        }
    }

    protected abstract void setDataBySub(BaseViewHolder helper, Data item);

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract int getItemLayout();

    protected abstract List<Data> getDatas();

    protected abstract String getTitleText();

}
