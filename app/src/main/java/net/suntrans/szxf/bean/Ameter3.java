package net.suntrans.szxf.bean;

/**
 * Created by Looney on 2017/7/28.
 */

public class Ameter3 {
    public String name;
    public String value;
    public String nameCH;
    public String unit;
    public String field;

    public Ameter3(String name, String value, String nameCH,String field) {
        this.name = name;
        this.value = value;
        this.nameCH = nameCH;
        this.field = field;
    }

    public Ameter3() {
    }

    public Ameter3(String name, String value) {
        this.name = name;
        this.value = value;

    }
}
