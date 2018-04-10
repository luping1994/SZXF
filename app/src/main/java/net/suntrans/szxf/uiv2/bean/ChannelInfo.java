package net.suntrans.szxf.uiv2.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2017/11/13.
 * Des:
 */

public class ChannelInfo implements Parcelable {

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


    public String permission = "1";

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
    public int channel_type;
    public String device_group;
    public String updated_at;
    public String deleted_at;
    public String user_id;
    public String created_at;
    public String house_type;


    /**
     * dev_id : 471
     * house_id : null
     * max_i : 20
     * classification : null
     * number : 1
     * show_sort : 0
     * used : 1
     * channel_type : 2
     * device_group : 0
     * always_open : 0
     * din :
     */


    public int always_open;
    public String din;
    //以下属性仅用作选择通道时候V2版本用的字段
    public boolean checked =false;
    //以下属性仅用作选择通道时候V1版本用的字段
    public boolean isChecked = false;
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.datapoint_name);
        dest.writeString(this.number);
        dest.writeInt(this.status);
        dest.writeInt(this.device_type);
        dest.writeString(this.used);
        dest.writeString(this.title);
        dest.writeString(this.permission);
        dest.writeString(this.dev_id);
        dest.writeString(this.house_id);
        dest.writeString(this.max_i);
        dest.writeString(this.classification);
        dest.writeString(this.show_sort);
        dest.writeInt(this.channel_type);
        dest.writeString(this.device_group);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
        dest.writeString(this.user_id);
        dest.writeString(this.created_at);
        dest.writeString(this.house_type);
        dest.writeInt(this.always_open);
        dest.writeString(this.din);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    public ChannelInfo() {
    }

    protected ChannelInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.datapoint_name = in.readString();
        this.number = in.readString();
        this.status = in.readInt();
        this.device_type = in.readInt();
        this.used = in.readString();
        this.title = in.readString();
        this.permission = in.readString();
        this.dev_id = in.readString();
        this.house_id = in.readString();
        this.max_i = in.readString();
        this.classification = in.readString();
        this.show_sort = in.readString();
        this.channel_type = in.readInt();
        this.device_group = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
        this.user_id = in.readString();
        this.created_at = in.readString();
        this.house_type = in.readString();
        this.always_open = in.readInt();
        this.din = in.readString();
        this.checked = in.readByte() != 0;
        this.isChecked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ChannelInfo> CREATOR = new Parcelable.Creator<ChannelInfo>() {
        @Override
        public ChannelInfo createFromParcel(Parcel source) {
            return new ChannelInfo(source);
        }

        @Override
        public ChannelInfo[] newArray(int size) {
            return new ChannelInfo[size];
        }
    };
}
