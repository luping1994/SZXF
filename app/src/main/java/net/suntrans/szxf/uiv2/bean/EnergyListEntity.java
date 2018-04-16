package net.suntrans.szxf.uiv2.bean;

import net.suntrans.szxf.bean.RespondBody;

import java.util.List;

/**
 * Created by Looney on 2017/7/8.
 */

public class EnergyListEntity extends RespondBody<EnergyListEntity.EnergyListData>{


    public static class EnergyListData{

        /**
         * floor : [{"area_id":4,"floor_electricity":0,"name":"四楼","sub":[{"id":1,"name":"区消安办","electricity":null,"power":null},{"id":2,"name":"防火中队办公室","electricity":null,"power":null},{"id":3,"name":"区消安办","electricity":null,"power":null},{"id":4,"name":"区消安办","electricity":null,"power":null},{"id":5,"name":"建审科办公室","electricity":null,"power":null},{"id":6,"name":"防火中队办公室","electricity":null,"power":null},{"id":7,"name":"建审科办公室","electricity":null,"power":null},{"id":8,"name":"法制办公室","electricity":null,"power":null},{"id":9,"name":"建审科办公室","electricity":null,"power":null},{"id":10,"name":"案管中心","electricity":null,"power":null},{"id":11,"name":"证物室","electricity":null,"power":null},{"id":12,"name":"火调室","electricity":null,"power":null}]},{"area_id":5,"floor_electricity":0,"name":"五楼","sub":[{"id":13,"name":"文化活动室","electricity":null,"power":null},{"id":14,"name":"会议室","electricity":null,"power":null},{"id":15,"name":"区消安办主任室","electricity":null,"power":null},{"id":16,"name":"防火中队办公室","electricity":null,"power":null},{"id":17,"name":"大队长室","electricity":null,"power":null},{"id":18,"name":"情指室","electricity":null,"power":null},{"id":19,"name":"综合办公室","electricity":null,"power":null},{"id":20,"name":"副大队长室","electricity":null,"power":null},{"id":21,"name":"法制办公室","electricity":null,"power":null},{"id":22,"name":"副大队长室","electricity":null,"power":null},{"id":23,"name":"内勤室","electricity":null,"power":null},{"id":24,"name":"党员活动室","electricity":null,"power":null},{"id":25,"name":"政委室","electricity":null,"power":null},{"id":26,"name":"防火中队办公室","electricity":null,"power":null},{"id":27,"name":"副大队长室","electricity":null,"power":null},{"id":28,"name":"副大队长室","electricity":null,"power":null},{"id":29,"name":"防火中队办公室","electricity":null,"power":null}]}]
         * total_electricity : 13
         */

        public String total_electricity;
        public List<FloorBean> floor;


    }

    public static class FloorBean {
        /**
         * area_id : 4
         * floor_electricity : 0
         * name : 四楼
         * sub : [{"id":1,"name":"区消安办","electricity":null,"power":null},{"id":2,"name":"防火中队办公室","electricity":null,"power":null},{"id":3,"name":"区消安办","electricity":null,"power":null},{"id":4,"name":"区消安办","electricity":null,"power":null},{"id":5,"name":"建审科办公室","electricity":null,"power":null},{"id":6,"name":"防火中队办公室","electricity":null,"power":null},{"id":7,"name":"建审科办公室","electricity":null,"power":null},{"id":8,"name":"法制办公室","electricity":null,"power":null},{"id":9,"name":"建审科办公室","electricity":null,"power":null},{"id":10,"name":"案管中心","electricity":null,"power":null},{"id":11,"name":"证物室","electricity":null,"power":null},{"id":12,"name":"火调室","electricity":null,"power":null}]
         */
        public String area_id;
        public String floor_electricity;
        public String name;
        public List<SubBean> sub;

        public static class SubBean {

            /**
             * id : 1
             * name : 区消安办
             * electricity : null
             * power : null
             */
            public String id;
            public String name;
            public String house_number;
            public String electricity;
            public String power;
            public String sno;
        }
    }
}
