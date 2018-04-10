package net.suntrans.szxf.uiv2.bean;

import net.suntrans.szxf.bean.RespondBody;

import java.util.List;

/**
 * Created by Looney on 2018/4/1.
 * Des:
 */
public class SensusAbnormal extends RespondBody<List<SensusAbnormal.Abnormal>> {
    public  static  class Abnormal{

        /**
         * id : 209
         * dev_id : 489
         * created_at : 2018-03-30 19:00:53
         * message : 甲醛超标,当前甲醛值:1.51
         * status : 1
         * vtype : 5
         * updated_at : 2018-03-30 19:00:53
         * code : null
         * type : null
         * sub_type : null
         */

        public int id;
        public int dev_id;
        public String created_at;
        public String message;
        public String house_number;
        public int status;
        public int vtype;
        public String updated_at;
        public String code;
        public String type;
        public String sub_type;
    }
}
