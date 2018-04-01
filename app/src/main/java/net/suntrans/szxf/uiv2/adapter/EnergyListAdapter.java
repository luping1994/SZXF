package net.suntrans.szxf.uiv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.suntrans.szxf.R;
import net.suntrans.szxf.bean.AreaEntity;

import java.util.List;

import static net.suntrans.szxf.Config.UNIT_ENERGY;
import static net.suntrans.szxf.Config.UNIT_P;

/**
 * Created by Looney on 2017/5/23.
 */

public class EnergyListAdapter extends BaseExpandableListAdapter {
    private List<AreaEntity.AreaFloor> datas;
    private Context mContext;

    public EnergyListAdapter(List<AreaEntity.AreaFloor> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;

    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).sub.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).sub.get(childPosition);
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
            view.setTag(R.id.root,groupPosition);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_area, parent, false);
            groupHolder = new GroupHolder(view);
            view.setTag(R.id.name,groupHolder);
            view.setTag(R.id.root,groupPosition);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_energy2, parent, false);
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
        private final View root;

        public GroupHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mImage = (ImageView) view.findViewById(R.id.imageView);

            root = view.findViewById(R.id.root);
        }

        public void setData(final int groupPosition) {

            mName.setText(datas.get(groupPosition).name);
//            mImage.setBackgroundColor(Color.parseColor(colors[groupPosition % 3]));
//            count.setText(datas.get(groupPosition).sub.size() + "");
        }
    }

    public class ChildHolder {


        private TextView allPower;
        private TextView yesterday;
        private TextView today;
        private TextView name;

        public ChildHolder(View itemView) {
            allPower = (TextView) itemView.findViewById(R.id.allPower);
            yesterday = (TextView) itemView.findViewById(R.id.yesterday);
            today = (TextView) itemView.findViewById(R.id.today);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void setData(final int groupPosition, final int childPosition) {
            allPower.setText(datas.get(groupPosition).sub.get(childPosition).electricity+UNIT_ENERGY);
            yesterday.setText(datas.get(groupPosition).sub.get(childPosition).power+UNIT_P);
            today.setText(datas.get(groupPosition).sub.get(childPosition).electricity+UNIT_ENERGY);
            name.setText(datas.get(groupPosition).sub.get(childPosition).name);
//            Glide.with(mContext)
//                    .load(datas.get(groupPosition).sub.get(childPosition).img_url)
//                    .centerCrop()
//                    .crossFade()
//                    .into(mImage);

        }
    }

    public void setOnGroupLongClickListener(EnergyListAdapter.onParentLongClickListener listener) {
        this.listener = listener;
    }

    private onParentLongClickListener listener;

    public interface onParentLongClickListener {
        void onLongParentClick(int parentPosition);

    }


}
