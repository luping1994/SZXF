package net.suntrans.szxf.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import net.suntrans.szxf.R;
import net.suntrans.szxf.api.RetrofitHelper;
import net.suntrans.szxf.bean.Ameter3Entity;
import net.suntrans.szxf.chart.DayAxisValueFormatter;
import net.suntrans.szxf.chart.MyAxisValueFormatter;
import net.suntrans.szxf.chart.MyMarkerView;
import net.suntrans.szxf.rx.BaseSubscriber;
import net.suntrans.szxf.uiv2.fragment.EnergyDetailFragment;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.UiUtils;
import net.suntrans.szxf.views.CompatDatePickerDialog;
import net.suntrans.szxf.views.SegmentedGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

/**
 * Created by Looney on 2017/7/13.
 */

public class Ammeter3Activity2 extends BasedActivity{

    private String sno;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ammeter3_2);
        initToolBar();

        findViewById(R.id.rightTitleMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        sno = getIntent().getStringExtra("sno");
        id = getIntent().getStringExtra("id");

        EnergyDetailFragment fragment = EnergyDetailFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment)
                .commit();
    }

    private void initToolBar() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("name") + "用电量");
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}

