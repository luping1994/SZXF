package net.suntrans.szxf.utils;

/**
 * Created by Looney on 2018/4/10.
 * Des:
 */
public class RemoteControl {
    //定义四种模式
    private static final String MOSHI_ZHILENG = "制冷模式";
    private static final String MOSHI_ZHIRE = "制热模式";
    private static final String MOSHI_SONGFENG = "送风模式";
    private static final String MOSHI_SLEEP = "睡眠模式";
    private static final String MOSHI_CHUSHI = "除湿模式";

    private static final String[] MODELS = {MOSHI_ZHILENG, MOSHI_ZHIRE, MOSHI_SONGFENG, MOSHI_SLEEP, MOSHI_CHUSHI};

    //制冷模式温度上限
    private static final int MAX_WENDU_COLD = 23;
    private static final int MIN_WENDU_COLD = 18;

    //制热模式温度上限
    private static final int MAX_WENDU_HOT = 30;
    private static final int MIN_WENDU_HOT = 20;

    private int currentMoshiIndex = 1;

    private int currentHotWendu = 18;
    private int currentColdWendu = 20;

    public void switchModel() {
        if (currentMoshiIndex == 4) {
            currentMoshiIndex = 1;
        }
        currentMoshiIndex++;
        listener.onMoshiChanged(MODELS[currentMoshiIndex]);
        switch (currentMoshiIndex) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }


    private RemoteControlListener listener;

    public interface RemoteControlListener {
        void onMoshiChanged(String moshi);

        void onWenduChanged(String wendu);

        void onFengsuChanged(String fengsu);

        void onFengxiangChanged(String fengxiang);
    }

}
