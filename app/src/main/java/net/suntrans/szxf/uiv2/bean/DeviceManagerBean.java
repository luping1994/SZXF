package net.suntrans.szxf.uiv2.bean;

import java.util.List;

/**
 * Created by Looney on 2018/4/1.
 * Des:
 */
public class DeviceManagerBean {


    /**
     * sub_area : 四楼
     * title : 深圳消防
     * area_id : 4
     * device : [{"id":465,"din":"144115192400521659","company_id":1,"house_id":1,"area_id":4,"name":"六通道","device_img":null,"title":"401","vtype":"4300","addr":"2c010000","is_online":0,"show_sort":0,"sno":"171170040023","client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-04-01 10:26:57"},{"id":466,"din":"144115192400477993","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"402","vtype":"4300","addr":"00000313","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-30 08:17:36"},{"id":467,"din":"","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"403","vtype":"4300","addr":"00000264","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-29 16:54:17"},{"id":468,"din":"144115192400521659","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"404","vtype":"4300","addr":"00000299","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-31 10:44:54"},{"id":469,"din":"144115192400478859","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"405","vtype":"4300","addr":"00000302","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-31 15:38:57"},{"id":470,"din":"","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"406","vtype":"4300","addr":"00000265","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-29 16:55:17"},{"id":471,"din":"","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"407","vtype":"4300","addr":"00000266","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-29 16:55:31"},{"id":472,"din":"","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"408","vtype":"4300","addr":"00000277","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-29 16:55:43"},{"id":473,"din":"","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"409","vtype":"4300","addr":"00000332","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-29 16:55:49"},{"id":474,"din":"","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"410","vtype":"4300","addr":"00000263","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-29 16:55:59"},{"id":475,"din":"","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"411","vtype":"4300","addr":"00000311","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-29 16:56:12"},{"id":476,"din":"","company_id":1,"house_id":null,"area_id":4,"name":"六通道","device_img":null,"title":"412","vtype":"4300","addr":"00000306","is_online":0,"show_sort":0,"sno":null,"client_ip":null,"client_id":null,"port":null,"stat_connect":0,"stat_close":0,"status":1,"created_at":null,"updated_at":"2018-03-29 16:56:17"}]
     */

    public String sub_area;
    public String title;
    public String area_id;
    public List<DeviceBean> device;

    public static class DeviceBean {
        /**
         * id : 465
         * din : 144115192400521659
         * company_id : 1
         * house_id : 1
         * area_id : 4
         * name : 六通道
         * device_img : null
         * title : 401
         * vtype : 4300
         * addr : 2c010000
         * is_online : 0
         * show_sort : 0
         * sno : 171170040023
         * client_ip : null
         * client_id : null
         * port : null
         * stat_connect : 0
         * stat_close : 0
         * status : 1
         * created_at : null
         * updated_at : 2018-04-01 10:26:57
         */

        public String id;
        public String din;
        public String company_id;
        public String house_id;
        public String area_id;
        public String name;
        public String device_img;
        public String title;
        public String vtype;
        public String addr;
        public String is_online;
        public String show_sort;
        public String sno;
        public String client_ip;
        public String client_id;
        public String port;
        public String stat_connect;
        public String stat_close;
        public String status;
        public String created_at;
        public String updated_at;
    }
}
