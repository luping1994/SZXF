package net.suntrans.szxf.uiv2.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.szxf.R;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.uiv2.BasedFragment2;
import net.suntrans.szxf.uiv2.activity.MessageDetailActivity;
import net.suntrans.szxf.uiv2.bean.NoticeEntity;
import net.suntrans.szxf.databinding.FragmentMessageBinding;
import net.suntrans.szxf.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import static net.suntrans.szxf.MessageType.messageColor;
import static net.suntrans.szxf.MessageType.messageTips;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class MessageFragment extends BasedFragment2 {
    private List<NoticeEntity.NoticeItem> datas;
    private FragmentMessageBinding binding;
    private Myadapter myadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();

        myadapter = new Myadapter(R.layout.item_message, datas);
        myadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("url", datas.get(position).url);
                startActivity(intent);
            }
        });
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.recyclerView.setAdapter(myadapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }

    class Myadapter extends BaseQuickAdapter<NoticeEntity.NoticeItem, BaseViewHolder> {

        private int usedColor;
        private int unusedColor;

        public Myadapter(int layoutResId, @Nullable List<NoticeEntity.NoticeItem> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, NoticeEntity.NoticeItem item) {
            TextView status = helper.getView(R.id.status);
            helper.setText(R.id.time, item.created_at)
                    .setText(R.id.title, item.title);
                status.setBackgroundColor(messageColor.get(item.vtype));
                status.setText(messageTips.get(item.vtype));

        }
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        addSubscription(api.loadNoticeList(), new BaseSubscriber<RespondBody<NoticeEntity>>(getContext()) {
            @Override
            public void onNext(RespondBody<NoticeEntity> body) {
                datas.clear();
                datas.addAll(body.data.lists);
                myadapter.notifyDataSetChanged();
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }
}
