package net.suntrans.szxf.bean;

import java.util.List;

/**
 * Created by Looney on 2017/4/19.
 */

public class LoginResult {

    /**
     * code : 200
     * msg : ok
     * data : {"token":{"token_type":"Bearer","expires_in":518399,"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjFiNjViZDU2MGI3NTRjZjRkYmZiNjZmNjNmN2I5NzdlYjc4ZWJjNWFmNDcxYTA4MWIzMGMwNzE1MGZiOTA5YzFhOGJhYWZiNDU2ZDEyYmMwIn0.eyJhdWQiOiIyIiwianRpIjoiMWI2NWJkNTYwYjc1NGNmNGRiZmI2NmY2M2Y3Yjk3N2ViNzhlYmM1YWY0NzFhMDgxYjMwYzA3MTUwZmI5MDljMWE4YmFhZmI0NTZkMTJiYzAiLCJpYXQiOjE1MjE3MDIxNzEsIm5iZiI6MTUyMTcwMjE3MSwiZXhwIjoxNTIyMjIwNTcwLCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.gg25fLQgjIi0EMJ6qanxx-quWtUILB12Q4Sv9Gh1Ofi0uyytTIzZ6ncy4IGU8RI8NkjBuM0peYFhDlgRitqS5duSxSuIW4Q_W8GnK8IvIGF3bzVkARhaWIouT6ux5y8QmsY0FyQKUWv62hwD-YVgmNr8u1utbqPr48qNtBlUvaRm5HK6b61dauvYQIrDcinN3VRdrCCUzbta6MKUViVvaeLBsOssGJryNt0jYWL1MC1RPXTCzr-eBx-L8SGXqFV7SCQ2o_aAX9uF9b3B1Sm40dReUyNJstlwzZoIwYUHtG6miJat0Zy7IwVunmAf5TYVipH6K3jbc1H3_05ia8oc96lz2WDbTJ6Ss0-tnUSAFQ12xaT4_I5zMCyXjd40ty6me3EUqDL4AMDxYoo0e_6Sgyftp_DVv3mIscdJIh8ifZ2aK9KtqOxrwy7Qt_cZnT45yDhTRKXHVkXfEpjRmjxLVJ5EgNqhW6-gIFEDghU7oOiVv2hv4b_qbJq3eqN3X3w8YAeagDdj_4J6p0GB34jajEIgYBymtYipaMAn-ykmlfrHeXIofDgHjEFa7cOWie5CnU-BAUTv6mNw-52w0sAHGp0ZZEqk60Fhw1yU1db5zgg4M4DAAQHh_TOEOGqrhSGBjcSHJUfLRLYitf8hBx1AGPT2KmcCtC_fTVgNZNCPzcA","refresh_token":"wzUuhAtOJHR3NRtRzj7TnAskB3qjJe1mmt8zxfVpgOM87s0fJq5CO5tqMgGF7Y1xygv6Ls/vBUoFuUpM0FdodTQR8g0EO35y70fZNUNq/QvBtjkN04llZPX+svUm4bCYWSxRmFCfo+P4Ya6DaJNvhTOQYJvZAUXEptuTgIEPG9LDo69x1YZ6QPzI44trYqm7oBvVV7IuUeX7tCK4tH06O+Fsxg8y7L3KAJ5qq56GopjsnHm9PHjuKeXoiSVM63fSmxMkENclQpuwm4PBzzOBAuKrgxztoTfJSzjDL3McxwoSowweohU9+1ItBcXrjLowUSOgiP0H6NTLBwSq4eXRRc+BgMozDVaeqa2gMg245q4Oln4+5dyJguI6IxeCeS1Ytq0dOAh6IhT/9+cOOvMmMdVsLhY/9r7XX0JQlMg4+UHLWYQZjhfQQndmPZL8mY+LKB1Cr4Ev9CWG9rafq89E1vg4HvBDdpnRdNOz21ReUMdA/esIwNRCXAjEqTh+Dp5BElIcooNKklIWcj5+ILR5ggiS7C8mW5CylvvH0G2h35Rh9bEM1R+iP2AnYv6t8wTQ9tZOxSHnN2Zaub4ltqYQEshCaurnGjw/bm/af9k7/DFxV7VIvU4m5S96vqdmPlF5zmm4p+9iOFg7TjwlPSckijZv6JJZpsnfMCKjIsOPj/4="},"user":{"id":4,"company_id":1,"username":"admin01","nickname":"员工","created_at":"2017-06-11 17:26:44","updated_at":"2018-03-22 12:07:17","status":1,"title":"TIT餐厅智能配电","avatar_url":"/uploads/images/HXRLqA97w75ugpSfI4Q8mmzG3tRXnrWiiYH5GHgr.jpeg","img_url":["/uploads/images/4WXv3GFycRddj1xorg6U00rR159hx8SMBNkkgFgo.jpeg"],"app_id":"20037","app_secret":"5umc*Yo6AyYR","salt":"123121","role_id":3},"timer":{"sensus":60000,"ammeter":600000,"light":3000}}
     */
    public String code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * token : {"token_type":"Bearer","expires_in":518399,"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjFiNjViZDU2MGI3NTRjZjRkYmZiNjZmNjNmN2I5NzdlYjc4ZWJjNWFmNDcxYTA4MWIzMGMwNzE1MGZiOTA5YzFhOGJhYWZiNDU2ZDEyYmMwIn0.eyJhdWQiOiIyIiwianRpIjoiMWI2NWJkNTYwYjc1NGNmNGRiZmI2NmY2M2Y3Yjk3N2ViNzhlYmM1YWY0NzFhMDgxYjMwYzA3MTUwZmI5MDljMWE4YmFhZmI0NTZkMTJiYzAiLCJpYXQiOjE1MjE3MDIxNzEsIm5iZiI6MTUyMTcwMjE3MSwiZXhwIjoxNTIyMjIwNTcwLCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.gg25fLQgjIi0EMJ6qanxx-quWtUILB12Q4Sv9Gh1Ofi0uyytTIzZ6ncy4IGU8RI8NkjBuM0peYFhDlgRitqS5duSxSuIW4Q_W8GnK8IvIGF3bzVkARhaWIouT6ux5y8QmsY0FyQKUWv62hwD-YVgmNr8u1utbqPr48qNtBlUvaRm5HK6b61dauvYQIrDcinN3VRdrCCUzbta6MKUViVvaeLBsOssGJryNt0jYWL1MC1RPXTCzr-eBx-L8SGXqFV7SCQ2o_aAX9uF9b3B1Sm40dReUyNJstlwzZoIwYUHtG6miJat0Zy7IwVunmAf5TYVipH6K3jbc1H3_05ia8oc96lz2WDbTJ6Ss0-tnUSAFQ12xaT4_I5zMCyXjd40ty6me3EUqDL4AMDxYoo0e_6Sgyftp_DVv3mIscdJIh8ifZ2aK9KtqOxrwy7Qt_cZnT45yDhTRKXHVkXfEpjRmjxLVJ5EgNqhW6-gIFEDghU7oOiVv2hv4b_qbJq3eqN3X3w8YAeagDdj_4J6p0GB34jajEIgYBymtYipaMAn-ykmlfrHeXIofDgHjEFa7cOWie5CnU-BAUTv6mNw-52w0sAHGp0ZZEqk60Fhw1yU1db5zgg4M4DAAQHh_TOEOGqrhSGBjcSHJUfLRLYitf8hBx1AGPT2KmcCtC_fTVgNZNCPzcA","refresh_token":"wzUuhAtOJHR3NRtRzj7TnAskB3qjJe1mmt8zxfVpgOM87s0fJq5CO5tqMgGF7Y1xygv6Ls/vBUoFuUpM0FdodTQR8g0EO35y70fZNUNq/QvBtjkN04llZPX+svUm4bCYWSxRmFCfo+P4Ya6DaJNvhTOQYJvZAUXEptuTgIEPG9LDo69x1YZ6QPzI44trYqm7oBvVV7IuUeX7tCK4tH06O+Fsxg8y7L3KAJ5qq56GopjsnHm9PHjuKeXoiSVM63fSmxMkENclQpuwm4PBzzOBAuKrgxztoTfJSzjDL3McxwoSowweohU9+1ItBcXrjLowUSOgiP0H6NTLBwSq4eXRRc+BgMozDVaeqa2gMg245q4Oln4+5dyJguI6IxeCeS1Ytq0dOAh6IhT/9+cOOvMmMdVsLhY/9r7XX0JQlMg4+UHLWYQZjhfQQndmPZL8mY+LKB1Cr4Ev9CWG9rafq89E1vg4HvBDdpnRdNOz21ReUMdA/esIwNRCXAjEqTh+Dp5BElIcooNKklIWcj5+ILR5ggiS7C8mW5CylvvH0G2h35Rh9bEM1R+iP2AnYv6t8wTQ9tZOxSHnN2Zaub4ltqYQEshCaurnGjw/bm/af9k7/DFxV7VIvU4m5S96vqdmPlF5zmm4p+9iOFg7TjwlPSckijZv6JJZpsnfMCKjIsOPj/4="}
         * user : {"id":4,"company_id":1,"username":"admin01","nickname":"员工","created_at":"2017-06-11 17:26:44","updated_at":"2018-03-22 12:07:17","status":1,"title":"TIT餐厅智能配电","avatar_url":"/uploads/images/HXRLqA97w75ugpSfI4Q8mmzG3tRXnrWiiYH5GHgr.jpeg","img_url":["/uploads/images/4WXv3GFycRddj1xorg6U00rR159hx8SMBNkkgFgo.jpeg"],"app_id":"20037","app_secret":"5umc*Yo6AyYR","salt":"123121","role_id":3}
         * timer : {"sensus":60000,"ammeter":600000,"light":3000}
         */

        public TokenBean token;
        public UserBean user;
        public TimerBean timer;
        public List<RoomBean> rooms;
        public static class RoomBean{
            public String house_id;
            public String house_type;
        }

        public static class TokenBean {
            /**
             * token_type : Bearer
             * expires_in : 518399
             * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjFiNjViZDU2MGI3NTRjZjRkYmZiNjZmNjNmN2I5NzdlYjc4ZWJjNWFmNDcxYTA4MWIzMGMwNzE1MGZiOTA5YzFhOGJhYWZiNDU2ZDEyYmMwIn0.eyJhdWQiOiIyIiwianRpIjoiMWI2NWJkNTYwYjc1NGNmNGRiZmI2NmY2M2Y3Yjk3N2ViNzhlYmM1YWY0NzFhMDgxYjMwYzA3MTUwZmI5MDljMWE4YmFhZmI0NTZkMTJiYzAiLCJpYXQiOjE1MjE3MDIxNzEsIm5iZiI6MTUyMTcwMjE3MSwiZXhwIjoxNTIyMjIwNTcwLCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.gg25fLQgjIi0EMJ6qanxx-quWtUILB12Q4Sv9Gh1Ofi0uyytTIzZ6ncy4IGU8RI8NkjBuM0peYFhDlgRitqS5duSxSuIW4Q_W8GnK8IvIGF3bzVkARhaWIouT6ux5y8QmsY0FyQKUWv62hwD-YVgmNr8u1utbqPr48qNtBlUvaRm5HK6b61dauvYQIrDcinN3VRdrCCUzbta6MKUViVvaeLBsOssGJryNt0jYWL1MC1RPXTCzr-eBx-L8SGXqFV7SCQ2o_aAX9uF9b3B1Sm40dReUyNJstlwzZoIwYUHtG6miJat0Zy7IwVunmAf5TYVipH6K3jbc1H3_05ia8oc96lz2WDbTJ6Ss0-tnUSAFQ12xaT4_I5zMCyXjd40ty6me3EUqDL4AMDxYoo0e_6Sgyftp_DVv3mIscdJIh8ifZ2aK9KtqOxrwy7Qt_cZnT45yDhTRKXHVkXfEpjRmjxLVJ5EgNqhW6-gIFEDghU7oOiVv2hv4b_qbJq3eqN3X3w8YAeagDdj_4J6p0GB34jajEIgYBymtYipaMAn-ykmlfrHeXIofDgHjEFa7cOWie5CnU-BAUTv6mNw-52w0sAHGp0ZZEqk60Fhw1yU1db5zgg4M4DAAQHh_TOEOGqrhSGBjcSHJUfLRLYitf8hBx1AGPT2KmcCtC_fTVgNZNCPzcA
             * refresh_token : wzUuhAtOJHR3NRtRzj7TnAskB3qjJe1mmt8zxfVpgOM87s0fJq5CO5tqMgGF7Y1xygv6Ls/vBUoFuUpM0FdodTQR8g0EO35y70fZNUNq/QvBtjkN04llZPX+svUm4bCYWSxRmFCfo+P4Ya6DaJNvhTOQYJvZAUXEptuTgIEPG9LDo69x1YZ6QPzI44trYqm7oBvVV7IuUeX7tCK4tH06O+Fsxg8y7L3KAJ5qq56GopjsnHm9PHjuKeXoiSVM63fSmxMkENclQpuwm4PBzzOBAuKrgxztoTfJSzjDL3McxwoSowweohU9+1ItBcXrjLowUSOgiP0H6NTLBwSq4eXRRc+BgMozDVaeqa2gMg245q4Oln4+5dyJguI6IxeCeS1Ytq0dOAh6IhT/9+cOOvMmMdVsLhY/9r7XX0JQlMg4+UHLWYQZjhfQQndmPZL8mY+LKB1Cr4Ev9CWG9rafq89E1vg4HvBDdpnRdNOz21ReUMdA/esIwNRCXAjEqTh+Dp5BElIcooNKklIWcj5+ILR5ggiS7C8mW5CylvvH0G2h35Rh9bEM1R+iP2AnYv6t8wTQ9tZOxSHnN2Zaub4ltqYQEshCaurnGjw/bm/af9k7/DFxV7VIvU4m5S96vqdmPlF5zmm4p+9iOFg7TjwlPSckijZv6JJZpsnfMCKjIsOPj/4=
             */

            public String token_type;
            public String expires_in;
            public String access_token;
            public String refresh_token;
        }

        public static class UserBean {
            /**
             * id : 4
             * company_id : 1
             * username : admin01
             * nickname : 员工
             * created_at : 2017-06-11 17:26:44
             * updated_at : 2018-03-22 12:07:17
             * status : 1
             * title : TIT餐厅智能配电
             * avatar_url : /uploads/images/HXRLqA97w75ugpSfI4Q8mmzG3tRXnrWiiYH5GHgr.jpeg
             * img_url : ["/uploads/images/4WXv3GFycRddj1xorg6U00rR159hx8SMBNkkgFgo.jpeg"]
             * app_id : 20037
             * app_secret : 5umc*Yo6AyYR
             * salt : 123121
             * role_id : 3
             */

            public String id;
            public String company_id;
            public String username;
            public String nickname;
            public String created_at;
            public String updated_at;
            public String status;
            public String title;
            public String avatar_url;
            public String app_id;
            public String app_secret;
            public String salt;
            public int role_id;
            public List<String> img_url;
        }

        public static class TimerBean {
            /**
             * sensus : 60000
             * ammeter : 600000
             * light : 3000
             */
            public String sensus;
            public String ammeter;
            public String light;
        }
    }
}
