package net.suntrans.szxf.uiv2.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2017/11/13.
 * Des:
 */

public class ChannelInfo {

    /**
     * id : 1054
     * name : 通道2
     * number : 2
     * status : 0
     * device_type : 1
     * used : 1
     */
    public int id;
    public String name;
    public String datapoint_name;
    public String number;
    public int status;
    public int device_type;
    public String used;
    public String title;
    //以下属性仅用作选择通道时候
    public boolean checked =false;


    /**
     * dev_id : 445
     * house_id : 5
     * max_i : 10
     * classification : 插座
     * number : 1
     * show_sort : 1
     * used : 1
     * channel_type : 2
     * device_group : 0
     * updated_at : null
     * deleted_at : null
     * user_id : 5057
     * created_at : 2018-03-22 14:33:52
     * house_type : 0
     */

    public String dev_id;
    public String house_id;
    public String max_i;
    public String classification;
    public String show_sort;
    public String channel_type;
    public String device_group;
    public String updated_at;
    public String deleted_at;
    public String user_id;
    public String created_at;
    public String house_type;
}
