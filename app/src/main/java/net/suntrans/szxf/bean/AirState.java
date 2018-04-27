package net.suntrans.szxf.bean;

/**
 * Created by Looney on 2018/4/27.
 * Des:
 */
public class AirState {

    /**
     * id : 1
     * user_id : 1
     * family_id : 1
     * name : 401
     * img_url : null
     * sort : 0
     * vtype : air
     * channel_id : 414
     * vod_url : 0
     * dev_id : 465
     * addr : 03040000
     * air_id : 2
     * created_at : null
     * updated_at : 2018-04-27 09:50:35
     * last_code : 110200
     * max_temp : 32
     * min_temp : 16
     */

    public String id;
    public String user_id;
    public String family_id;
    public String name;
    public String img_url;
    public String sort;
    public String vtype;
    public String channel_id;
    public String vod_url;
    public String dev_id;
    public String addr;
    public String air_id;
    public Object created_at;
    public String updated_at;
    public String last_code;
    public String max_temp;
    public String min_temp;

    @Override
    public String toString() {

        return "AirState{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", family_id='" + family_id + '\'' +
                ", name='" + name + '\'' +
                ", img_url='" + img_url + '\'' +
                ", sort='" + sort + '\'' +
                ", vtype='" + vtype + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", vod_url='" + vod_url + '\'' +
                ", dev_id='" + dev_id + '\'' +
                ", addr='" + addr + '\'' +
                ", air_id='" + air_id + '\'' +
                ", created_at=" + created_at +
                ", updated_at='" + updated_at + '\'' +
                ", last_code='" + last_code + '\'' +
                ", max_temp='" + max_temp + '\'' +
                ", min_temp='" + min_temp + '\'' +
                '}';
    }
}
