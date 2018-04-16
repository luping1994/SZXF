package net.suntrans.szxf.uiv2.bean;

import java.util.List;

/**
 * Created by Looney on 2018/4/8.
 * Des:
 */
public class ConLog {

    public List<ConLogItem> list;

    public static class ConLogItem{

        public String id;
        public String created_at;
        public String username;
        public String message;
        public String company_id;
        public Object vtype;
        public Object contents;
        public Object client_ip;
        public String timestamp;
        public String type;
        public String house_id;
    }


    /**
     * id : 3708
     * created_at : 2018-04-16 16:42:09
     * username : lixiang
     * message : 关闭三川测试房间2空调(第3通道)
     * company_id : 1
     * vtype : null
     * contents : null
     * client_ip : null
     * timestamp : 1523868129639
     * type : 0
     * house_id : 31
     */

}
