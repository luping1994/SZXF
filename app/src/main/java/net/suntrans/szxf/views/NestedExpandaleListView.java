package net.suntrans.szxf.views;

import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.content.Context;
/**
 * Created by Looney on 2018/4/1.
 * Des:
 */
public class NestedExpandaleListView extends ExpandableListView {
    public NestedExpandaleListView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        //将重新计算的高度传递回去
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}