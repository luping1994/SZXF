package net.suntrans.szxf.uiv2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.suntrans.szxf.Config;
import net.suntrans.szxf.R;
import net.suntrans.szxf.bean.AreaEntity;
import net.suntrans.szxf.uiv2.bean.DeviceManagerBean;

import java.util.List;

import static net.suntrans.szxf.Config.UNIT_ENERGY;
import static net.suntrans.szxf.Config.UNIT_P;

/**
 * Created by Looney on 2017/5/23.
 */

public class DeviceManagerAdapter extends BaseExpandableListAdapter {
    private List<DeviceManagerBean> datas;
    private Context mContext;

    public DeviceManagerAdapter(List<DeviceManagerBean> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;

    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).device.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).device.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = null;
        GroupHolder groupHolder = null;
        if (convertView != null) {
            view = convertView;
            groupHolder = (GroupHolder) view.getTag(R.id.name);
            view.setTag(R.id.root, groupPosition);
        } else {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_env, parent, false);
            groupHolder = new GroupHolder(view);
            view.setTag(R.id.name, groupHolder);
            view.setTag(R.id.root, groupPosition);
        }
        groupHolder.setData(groupPosition);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        ChildHolder holder = null;
        if (convertView != null) {
            view = convertView;
            holder = (ChildHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_device, parent, false);
            holder = new ChildHolder(view);
            view.setTag(holder);
        }
        holder.setData(groupPosition, childPosition);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public class GroupHolder {
        TextView mName;
        ImageView mImage;
        String[] colors = new String[]{"#f99e5b", "#d3e4ad", "#94c9d6"};
        TextView count;
        TextView status;
        private final View root;

        public GroupHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mImage = (ImageView) view.findViewById(R.id.imageView);

            root = view.findViewById(R.id.root);
            status = (TextView) view.findViewById(R.id.status);
        }

        public void setData(final int groupPosition) {

            mName.setText(datas.get(groupPosition).sub_area);
            List<DeviceManagerBean.DeviceBean> device = datas.get(groupPosition).device;
            int offLinecount = 0;
            int onLinecount = 0;
            for (DeviceManagerBean.DeviceBean d : device) {
                if ("1".equals(d.is_online)) {
                    onLinecount++;
                } else {
                    offLinecount++;
                }
            }


            status.setText("在线:" + onLinecount + ",离线:" + offLinecount);

//            mImage.setBackgroundColor(Color.parseColor(colors[groupPosition % 3]));
//            count.setText(datas.get(groupPosition).sub.size() + "");
        }
    }

    public class ChildHolder {


        private TextView name;
        private TextView des;
        private TextView title;
        private int onlineColor;
        private ImageView mImage;

        public ChildHolder(View itemView) {
            name = (TextView) itemView.findViewById(R.id.name);
            title = (TextView) itemView.findViewById(R.id.title);
            mImage = (ImageView) itemView.findViewById(R.id.image);
            des = (TextView) itemView.findViewById(R.id.des);
            onlineColor =mContext.getResources().getColor(R.color.online_color);

        }

        public void setData(final int groupPosition, final int childPosition) {
            name.setText(datas.get(groupPosition).device.get(childPosition).title);
            des.setText(datas.get(groupPosition).device.get(childPosition).name);
            title.setText("1".equals(datas.get(groupPosition).device.get(childPosition).is_online) ? "在线" : "不在线");
            title.setTextColor("1".equals(datas.get(groupPosition).device.get(childPosition).is_online) ? onlineColor : Color.RED);
            String device_type = datas.get(groupPosition).device.get(childPosition).vtype;
//            System.out.println(device_type);
            if ("6100".equals(device_type)){
                mImage.setImageResource(R.drawable.diliugan);
            }else if ("4300".equals(device_type)){
                mImage.setImageResource(R.drawable.ic_liutongdao);

            }
        }
    }

    public void setOnGroupLongClickListener(DeviceManagerAdapter.onParentLongClickListener listener) {
        this.listener = listener;
    }

    private onParentLongClickListener listener;

    public interface onParentLongClickListener {
        void onLongParentClick(int parentPosition);

    }


}
