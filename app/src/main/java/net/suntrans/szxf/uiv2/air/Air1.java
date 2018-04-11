package net.suntrans.szxf.uiv2.air;

import net.suntrans.szxf.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2018/4/10.
 */

public final class Air1 implements Air {
    public static final int SWITCH_MODEL = 0;
    public static final int ADD_WENDU = 1;
    public static final int SUB_WENDU = 2;
    public static final int SWITCH_FENGSU = 3;
    public static final int SWITCH_FENGXIANG = 4;

    //模式
    public static final String[] MODELS = {"制冷", "制热", "除湿", "送风", "睡眠"};
    public static final int ZHILENG = 0;
    public static final int ZHIRE = 1;
    public static final int CHUSHI = 2;
    public static final int SONGFENG = 3;
    public static final int SHUIMING = 4;

    //风速
    public static final String[] FENGSUS = {"", "强劲风"};
    public static final int NORMAL_FENG = 0;
    public static final int QIANG_FENG = 1;

    //风向
    public static final String[] FENGXIANG = {"上下扫风", "左右扫风"};
    public static final int TOP_FENG = 0;
    public static final int RIGHT_FENG = 1;

    public static final String OFF_CMD_ID = "8";

    public static final Map<Integer, String> COLD_WENDU_CMD_ID = new HashMap<>();
    public static final Map<Integer, String> HOT_WENDU_CMD_ID = new HashMap<>();

    public static final Map<String, String> CHU_SHI = new HashMap<>();
    public static final Map<String, String> SONG_FENG = new HashMap<>();
    public static final Map<String, String> SHUI_MIN = new HashMap<>();


    public static final Map<String, String> DING_SHI = new HashMap<>();
    public static final List<Map<String, String>> FENG_SU = new ArrayList<>();
    public static final List<Map<String, String>> FENG_XIANG = new ArrayList<>();

    static {
        COLD_WENDU_CMD_ID.put(18, "2");
        COLD_WENDU_CMD_ID.put(19, "3");
        COLD_WENDU_CMD_ID.put(20, "4");
        COLD_WENDU_CMD_ID.put(21, "5");
        COLD_WENDU_CMD_ID.put(22, "6");
        COLD_WENDU_CMD_ID.put(23, "7");

        HOT_WENDU_CMD_ID.put(20, "9");
        HOT_WENDU_CMD_ID.put(21, "10");
        HOT_WENDU_CMD_ID.put(22, "11");
        HOT_WENDU_CMD_ID.put(23, "12");
        HOT_WENDU_CMD_ID.put(24, "13");
        HOT_WENDU_CMD_ID.put(25, "14");
        HOT_WENDU_CMD_ID.put(26, "15");
        HOT_WENDU_CMD_ID.put(27, "16");
        HOT_WENDU_CMD_ID.put(28, "17");
        HOT_WENDU_CMD_ID.put(29, "18");
        HOT_WENDU_CMD_ID.put(30, "19");

        CHU_SHI.put("id", "20");
        CHU_SHI.put("title", "除湿模式 26℃");

        SONG_FENG.put("id", "21");
        SONG_FENG.put("title", "送风模式");


        Map<String, String> map1 = new HashMap<>();
        map1.put("id", "22");
        map1.put("title", "上下扫风");
        FENG_SU.add(map1);

        Map<String, String> map2 = new HashMap<>();
        map2.put("id", "23");
        map2.put("title", "左右扫风");
        FENG_SU.add(map1);


        Map<String, String> map4 = new HashMap<>();
        map4.put("id", "25");
        map4.put("title", "");
        FENG_XIANG.add(map4);

        Map<String, String> map3 = new HashMap<>();
        map3.put("id", "24");
        map3.put("title", "强劲风开启");
        FENG_XIANG.add(map3);


        SHUI_MIN.put("id", "27");
        SHUI_MIN.put("title", "睡眠模式");


        DING_SHI.put("id", "26");
        DING_SHI.put("title", "定时0.5小时");

    }


    public int currentModel = ZHILENG;
    public int currentFengSuIndex = 0;
    public int currentFengXiangIndex = -1;

    public int currentColdWendu = 18;
    public int currentHotWendu = 20;


    private static int coldMin = 18;
    private static int coldMax = 23;
    private static int hotMin = 20;
    private static int hotMax = 30;

    public String getCmdID(int modelIndex, int control) {
        return null;
    }


    private boolean hasOn = false;

    public boolean isOn() {
        return hasOn;
    }

    public void close() {
        hasOn = false;
        listener.stateStringChanged("", "", "", "");
        listener.sendCmd(OFF_CMD_ID);
    }

    public void open() {
        hasOn = true;
        updateUI();
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


    }


    @Override
    public void addWendu() {
        if (!hasOn) {
            updateUI();
            hasOn = true;
            return;
        }
        if (currentModel == CHUSHI || currentModel == SONGFENG || currentModel == SHUIMING) {
            UiUtils.showToast(MODELS[currentModel] + "不支持调温度");
            return;
        }

        if (currentModel == ZHILENG) {
            currentColdWendu++;
            if (currentColdWendu > coldMax) {
                currentColdWendu = coldMax;
                UiUtils.showToast("已是最大温度值!");
                return;
            }
        } else if (currentModel == ZHIRE) {
            currentHotWendu++;
            if (currentColdWendu > hotMax) {
                currentHotWendu = hotMin;
                UiUtils.showToast("已是最大温度值!");
                return;
            }
        }

        updateUI();
    }

    @Override
    public void subWendu() {
        if (!hasOn) {
            updateUI();
            hasOn = true;
            return;

        }
        if (currentModel == CHUSHI || currentModel == SONGFENG || currentModel == SHUIMING) {
            UiUtils.showToast(MODELS[currentModel] + "不支持调温度");
            return;
        }
        if (currentModel == ZHILENG) {
            currentColdWendu--;
            if (currentColdWendu < coldMin) {
                currentColdWendu = coldMin;
                UiUtils.showToast("已是最小温度值!");
                return;
            }
        } else if (currentModel == ZHIRE) {
            currentHotWendu--;
            if (currentColdWendu < hotMin) {
                currentHotWendu = hotMin;
                UiUtils.showToast("已是最小温度值!");
                return;
            }
        }

        updateUI();
    }

    @Override
    public void switchFengsu() {
        if (!hasOn) {
            updateUI();
            hasOn = true;
            return;

        }
        currentFengSuIndex++;
        if (currentFengSuIndex == FENGSUS.length) {
            currentFengSuIndex = 0;
        }
        updateUI();
    }

    @Override
    public void switchFengXiang() {
        if (!hasOn) {
            updateUI();
            hasOn = true;
            return;
        }
        currentFengXiangIndex++;
        if (currentFengXiangIndex == FENGXIANG.length) {
            currentFengXiangIndex = 0;
        }
        updateUI();
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

        String id = "";
        String modelText = "";
        String wenText = "";
        String fengsuText = "";
        String fengxiangText = "";


        modelText = MODELS[currentModel];
        if (currentFengSuIndex == -1) {
            fengsuText = "";
        } else {
            fengsuText = FENGSUS[currentFengSuIndex];
        }

        if (currentFengXiangIndex == -1) {
            fengxiangText = "";
        } else {
            fengxiangText = FENGXIANG[currentFengXiangIndex];
        }


        if (currentModel == ZHILENG) {
            id = COLD_WENDU_CMD_ID.get(currentColdWendu);
            wenText = currentColdWendu + "℃";
        }

        if (currentModel == ZHIRE) {
            id = HOT_WENDU_CMD_ID.get(currentHotWendu);
            wenText = currentHotWendu + "℃";

        }

        if (currentModel == CHUSHI) {
            id = CHU_SHI.get("id");
            wenText = "";
        }

        if (currentModel == SONGFENG) {
            id = SONG_FENG.get("id");
            wenText = "";
        }


        if (currentModel == SHUIMING) {
            id = SHUI_MIN.get("id");
            wenText = "";
        }


        listener.stateStringChanged(modelText, wenText, fengsuText, fengxiangText);
        listener.sendCmd(id);
    }

}
