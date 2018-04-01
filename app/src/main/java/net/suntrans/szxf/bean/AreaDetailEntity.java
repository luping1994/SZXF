package net.suntrans.szxf.bean;

import java.util.List;

/**
 * Created by Looney on 2017/4/24.
 */

public class AreaDetailEntity extends RespondBody<List<AreaDetailEntity.AreaDetailData>>{

    public static class AreaDetailData   {

        /**
         * id : 179
         * dev_id : 465
         * house_id : 1
         * max_i : 10
         * name : 通道1
         * title : 前台大厅
         * classification : 插座
         * number : 1
         * status : 1
         * show_sort : 1
         * used : 1
         * channel_type : 2
         * device_group : 0
         * din : 144115192400477993
         */

        public String id;
        public String dev_id;
        public String house_id;
        public String max_i;
        public String name;
        public String title;
        public String classification;
        public String number;
        public String status;
        public String show_sort;
        public String used;
        public String channel_type;
        public String device_group;
        public String din;
    }


}
