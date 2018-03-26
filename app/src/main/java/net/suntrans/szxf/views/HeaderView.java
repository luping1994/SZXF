package net.suntrans.szxf.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Looney on 2018/3/26.
 * Des:
 */

public class HeaderView extends FrameLayout {
    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);

    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
