package net.suntrans.szxf.utils;

import net.suntrans.szxf.App;

/**
 * Created by Looney on 2018/4/3.
 * Des:
 */
public class SharedPreferencesHepler {

    //house_type 0是办公室 1是宿舍
    public static String getOfficeHouseId() {
        return App.getSharedPreferences().getString("office_id", "");
    }

    public static void setOfficeHouseId(String office_id) {
        App.getSharedPreferences().edit().putString("office_id", office_id).commit();
    }

    public static String getDormHouseId() {
        return App.getSharedPreferences().getString("dorm_id", "");
    }

    public static void setDormHouseId(String dorm_id) {
        App.getSharedPreferences().edit().putString("dorm_id", dorm_id).commit();
    }
}
