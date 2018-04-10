package net.suntrans.szxf.bean;

import java.util.List;

/**
 * Created by Looney on 2017/8/17.
 */

public class YichangEntity extends RespondBody<List<YichangEntity.Yichang>>{

    public class  Yichang{

        /**
         * house_number : 405
         * name : 建审科办公室
         * message : 2通道过流
         * number : 2
         * value : 2.00
         * created_at : 2018-03-31 20:14:51
         */

        public String house_number;
        public String name;
        public String message;
        public int number;
        public String value;
        public String created_at;
    }
}
