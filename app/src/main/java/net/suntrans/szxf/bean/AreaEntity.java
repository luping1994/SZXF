package net.suntrans.szxf.bean;

import java.util.List;

/**
 * Created by Looney on 2017/7/8.
 */

public class AreaEntity extends RespondBody<List<AreaEntity.AreaFloor>>{



    public static class AreaFloor{
        public int id;
        public String user_id;
        public String name;
        public String img_url;
        public String show_sort;
        public List<FloorRoom> sub;
        public boolean isCheck = false;
    }

    public static class FloorRoom{
        public int id;
        public String house_id;
        public String name;
        public String img_url;
        public String show_sort;

        public List<Channel> lists;
        public boolean isChecked = false;

        //能耗电表列表要用的字段
        public String yesterday;
        public String today;
        public String allPower;

        //能耗首页列表字段
        public String electricity;
        public String sno; //电表表号



        //能耗监测要用的字段~
        public String power;
        public String current;

        //环境要用的字段
        public String pm25;
        public String jiquan;
        public String guanzhao;
        public String shidu;
        public String wendu;
        public String yanwu;
        public String isOnline;


        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }

    public static class Channel{
        public String area_id;
        public String channel_id;
        public String id;
        public String name;
        public String permission;
        public boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
