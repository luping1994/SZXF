package net.suntrans.szxf.uiv2.air;

import com.github.mikephil.charting.formatter.IFillFormatter;

import net.suntrans.szxf.uiv2.fragment.EnvListFragment;
import net.suntrans.szxf.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2018/4/10.
 */

public final class Air1 implements Air {


    //模式
    public static final String[] MODELS = {"制冷", "送风", "除湿", "自动模式下睡眠模式", "制冷模式下睡眠模式", "除温模式下睡眠模式"};
    public static final int ZHILENG = 0;
    public static final int SONGFENG = 1;
    public static final int CHUSHI = 2;

    public static final int SLEEP_AUTO = 3;
    public static final int SLEEP_ZHILENG = 4;
    public static final int SLEEP_CHUWEN = 5;


    public static final String OFF_CMD_ID = "29";

    public static final Map<Integer, String> COLD_WENDU_CMD_ID = new HashMap<>();

    public static final Map<String, String> SONG_FENG = new HashMap<>();
    public static final Map<String, String> CHU_SHI = new HashMap<>();

    public static final Map<String, String> SHUI_MING_AUTO = new HashMap<>();
    public static final Map<String, String> SHUI_MING_ZHILENG = new HashMap<>();
    public static final Map<String, String> SHUI_MING_CHUSHI = new HashMap<>();


    public static final Map<String, String> DING_SHI = new HashMap<>();


    static {
        COLD_WENDU_CMD_ID.put(16, "30");
        COLD_WENDU_CMD_ID.put(17, "31");
        COLD_WENDU_CMD_ID.put(18, "32");
        COLD_WENDU_CMD_ID.put(19, "33");
        COLD_WENDU_CMD_ID.put(20, "34");
        COLD_WENDU_CMD_ID.put(21, "35");
        COLD_WENDU_CMD_ID.put(22, "36");
        COLD_WENDU_CMD_ID.put(23, "37");


//        CHU_SHI.put("id", "20");
//        CHU_SHI.put("title", "除湿模式 26℃");

        SONG_FENG.put("id", "39");
        SONG_FENG.put("title", "送风模式");

        SHUI_MING_AUTO.put("id", "40");
        SHUI_MING_AUTO.put("title", "自动模式下睡眠模式");


        SHUI_MING_ZHILENG.put("id", "41");
        SHUI_MING_ZHILENG.put("title", "制冷模式下睡眠模式");

        SHUI_MING_CHUSHI.put("id", "42");
        SHUI_MING_CHUSHI.put("title", "除湿模式下睡眠模式");


        CHU_SHI.put("id", "38");
        CHU_SHI.put("title", "除湿模式");


        DING_SHI.put("id", "26");
        DING_SHI.put("title", "定时0.5小时");

    }


    public int currentModel = ZHILENG;
    public int currentFengSuIndex = 0;
    public int currentFengXiangIndex = -1;

    public int currentColdWendu = 18;
    public int currentHotWendu = 20;


    private static int coldMin = 16;
    private static int coldMax = 23;


    public String getCmdID(int modelIndex, int control) {
        return null;
    }


    private boolean hasOn = false;

    public boolean isOn() {
        return hasOn;
    }

    public void setOn(boolean a) {
        hasOn = a;
    }


    public void close() {
        hasOn = false;
        listener.stateStringChanged("", "", "", "");
        listener.sendCmd(OFF_CMD_ID);
    }

    public void open() {
        hasOn = true;
        updateUI();
        sendModelCmd();
    }

    @Override
    public void switchModel() {
        if (!hasOn) {
            updateUI();
            hasOn = true;
            return;

        }
        currentModel++;
        if (currentModel == MODELS.length) {
            currentModel = 0;
        }

        updateUI();
        sendModelCmd();
    }

    private void sendModelCmd() {
        String id = "";
        if (currentModel==ZHILENG){
            id = COLD_WENDU_CMD_ID.get(currentColdWendu);
        }
        if (currentModel ==SONGFENG){
            id = SONG_FENG.get("id");
        }

        if (currentModel ==CHUSHI){
            id = CHU_SHI.get("id");
        }

        if (currentModel ==SLEEP_AUTO){
            id = SHUI_MING_AUTO.get("id");
        }
        if (currentModel ==SLEEP_ZHILENG){
            id = SHUI_MING_ZHILENG.get("id");
        }

        if (currentModel ==SLEEP_CHUWEN){
            id = SHUI_MING_CHUSHI.get("id");
        }
        listener.sendCmd(id);
    }


    @Override
    public void addWendu() {
        if (!hasOn) {
            updateUI();
            hasOn = true;
            return;
        }
        if (currentModel != ZHILENG) {
            UiUtils.showToast(MODELS[currentModel] + "不支持调温度");
            return;
        }
        currentColdWendu++;
        if (currentColdWendu > coldMax) {
            currentColdWendu = coldMax;
            UiUtils.showToast("已是最大温度值!");
            return;
        }

        updateUI();

        sendWenCmd();
    }

    private void sendWenCmd() {
        String id = "";
        id = COLD_WENDU_CMD_ID.get(currentColdWendu);
        listener.sendCmd(id);

//        if (currentModel == ZHIRE) {
//            id = HOT_WENDU_CMD_ID.get(currentHotWendu);
//        }

    }

    @Override
    public void subWendu() {
        if (!hasOn) {
            updateUI();
            hasOn = true;
            return;
        }

        if (currentModel != ZHILENG) {
            UiUtils.showToast(MODELS[currentModel] + "不支持调温度");
            return;
        }

        currentColdWendu--;
        if (currentColdWendu < coldMin) {
            currentColdWendu = coldMin;
            UiUtils.showToast("已是最小温度值!");
            return;
        }
        updateUI();
        sendWenCmd();
    }

    @Override
    public void switchFengsu() {
        UiUtils.showToast("此空调不支持风速调节");

    }

    @Override
    public void switchFengXiang() {
        UiUtils.showToast("此空调不支持风向调节");
    }


    public void setListener(AirStateChangedListener listener) {
        this.listener = listener;
    }

    private AirStateChangedListener listener;

    public interface AirStateChangedListener {
        void sendCmd(String id);

        void stateStringChanged(String modelText, String wenduText, String fengsuText, String fengxiangText);
    }


    private void updateUI() {

        String modelText = "";
        String wenText = "";
        String fengsuText = "";
        String fengxiangText = "";

        modelText = MODELS[currentModel];
        String id = "";
        if (currentModel == ZHILENG) {
            id = COLD_WENDU_CMD_ID.get(currentColdWendu);
            wenText = currentColdWendu + "℃";
        }
//
        if (currentModel == SONGFENG) {


        }
        if (currentModel == CHUSHI) {
            wenText = "25℃";

        }

//
//
//        if (currentModel == SONGFENG) {
//            id = SONG_FENG.get("id");
//            wenText = "";
//        }
//
//
//        if (currentModel == SHUIMING) {
//            id = SHUI_MIN.get("id");
//            wenText = "";
//        }


        listener.stateStringChanged(modelText, wenText, fengsuText, fengxiangText);

    }

}
