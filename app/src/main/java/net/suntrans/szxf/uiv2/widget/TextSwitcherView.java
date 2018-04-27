package net.suntrans.szxf.uiv2.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import net.suntrans.szxf.R;
import net.suntrans.szxf.utils.LogUtil;
import net.suntrans.szxf.utils.UiUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Looney on 2018/4/24.
 * Des:
 */
public class TextSwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private ArrayList<String> reArrayList = new ArrayList<String>();
    public int resIndex = 0;
    private final int UPDATE_TEXTSWITCHER = 1;
    private int timerStartAgainCount = 0;
    private Context mContext;
    private Timer timer;
    private boolean isWarning = false;

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }


    public TextSwitcherView(Context context) {

        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        init();
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
        // TODO Auto-generated constructor stub
    }

    private void init() {
        this.setFactory(this);
        this.setInAnimation(getContext(), R.anim.vertical_in);
        this.setOutAnimation(getContext(), R.anim.vertical_out);
        Message msg = new Message();
        msg.what = UPDATE_TEXTSWITCHER;
        handler.sendMessage(msg);
    }

    public void start() {
        resIndex =0;
        setTextColorIfWarning();
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(UPDATE_TEXTSWITCHER);
    }

    public void startOnce() {
        resIndex =0;
        setTextColorIfWarning();
        handler.removeCallbacksAndMessages(null);
        this.setCurrentText(this.reArrayList.get(resIndex));
    }
    public void stop() {
//        if (timer != null) {
//            timer.cancel();
//        }
        handler.removeCallbacksAndMessages(null);
    }

    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {   //不能在这里创建任何UI的更新，toast也不行
            // TODO Auto-generated method stub
            Message msg = new Message();
            msg.what = UPDATE_TEXTSWITCHER;
            handler.sendMessage(msg);
        }
    };
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            updateTextSwitcher();
        }

    };

    /**
     * 需要传递的资源
     *
     * @param reArrayList
     */
    public void setResource(ArrayList<String> reArrayList) {
        this.reArrayList.clear();
        this.reArrayList.addAll(reArrayList);
    }


    public void updateTextSwitcher() {

        setTextColorIfWarning();
        if (this.reArrayList != null && this.reArrayList.size() > 0) {
            if (this.reArrayList.size() == 1) {
                this.setCurrentText(this.reArrayList.get(0));
            } else {
                this.setText(this.reArrayList.get(resIndex++));
                if (resIndex > this.reArrayList.size() - 1) {
                    resIndex = 0;
                }
            }
        }

        handler.sendEmptyMessageDelayed(UPDATE_TEXTSWITCHER,3000);
    }

    @Override
    public View makeView() {
        // TODO Auto-generated method stub
        TextView   tView = new TextView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, UiUtils.dip2px(30));
        tView.setLayoutParams(params);
        tView.setGravity(Gravity.CENTER);
        tView.setTextSize(12);
        tView.setTextColor(textColor);
        return tView;
    }

    private int textColor = Color.parseColor("#ff0000");

    public void setTextColor(int color) {
        this.textColor = color;

    }

    private void setTextColorIfWarning(){
        if (isWarning){
            ((TextView) getNextView()).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) getCurrentView()).setTextColor(Color.parseColor("#ff0000"));
        }else {
            ((TextView) getNextView()).setTextColor(Color.parseColor("#0b9d2f"));
            ((TextView) getCurrentView()).setTextColor(Color.parseColor("#0b9d2f"));
        }
    }

}
