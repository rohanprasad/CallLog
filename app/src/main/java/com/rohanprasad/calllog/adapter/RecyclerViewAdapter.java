package com.rohanprasad.calllog.adapter;

import android.provider.CallLog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohanprasad.calllog.HelperFunction;
import com.rohanprasad.calllog.R;
import com.rohanprasad.calllog.holder.CallLogItem;

import java.util.ArrayList;

/**
 * Created by Rohan on 11-06-2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<CallLogItem> mDataList;
    private static ClickListener clickListener;
    private static LongClickListener longClickListener;

    public RecyclerViewAdapter(ArrayList<CallLogItem> mDataList, ClickListener clickListener, LongClickListener longClickListener) {
        this.mDataList = mDataList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView tvName;
        public TextView tvTime;
        public TextView tvDuration;
        public ImageView ivCallStatus;
        public ImageView ivUserImage;

        public ViewHolder(View inflatedView) {
            super(inflatedView);
            inflatedView.setOnClickListener(this);
            inflatedView.setOnLongClickListener(this);
            tvName = (TextView) inflatedView.findViewById(R.id.tv_call_list_item_name);
            tvTime = (TextView) inflatedView.findViewById(R.id.tv_call_list_item_time);
            tvDuration = (TextView) inflatedView.findViewById(R.id.tv_call_list_item_duration);
            ivCallStatus = (ImageView) inflatedView.findViewById(R.id.iv_call_list_item_call_type);
            ivUserImage = (ImageView) inflatedView.findViewById(R.id.iv_call_list_item_picture);
        }

        @Override
        public void onClick(View v) {

            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                longClickListener.itemLongClicked(v, getPosition());
                return true;
            }
            return false;
        }
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_call_list, null);
        ViewHolder mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int i) {

        CallLogItem mItem = mDataList.get(i);
        viewHolder.tvName.setText(HelperFunction.getTitleCase(mItem.getName()));
        viewHolder.tvDuration.setText(mItem.getDurationString());
        viewHolder.tvTime.setText(mItem.getTime());

        if (mItem.getCallStatus() == CallLog.Calls.OUTGOING_TYPE) {
            viewHolder.ivCallStatus.setImageResource(R.mipmap.ic_call_created);
        } else if (mItem.getCallStatus() == CallLog.Calls.INCOMING_TYPE) {
            viewHolder.ivCallStatus.setImageResource(R.mipmap.ic_call_received);
        } else if (mItem.getCallStatus() == CallLog.Calls.MISSED_TYPE) {
            viewHolder.ivCallStatus.setImageResource(R.mipmap.ic_call_missed);
        }

        if (mItem.getUserImageId() == null) {
            viewHolder.ivUserImage.setImageResource(HelperFunction.getAlphabetTile(mItem.getName()));
        } else {
            viewHolder.ivUserImage.setImageURI(mItem.getUserImageId());
        }
    }

    @Override
    public int getItemCount() {

        if (mDataList != null) {
            return mDataList.size();
        }

        return 0;
    }

    public interface ClickListener {
        public void itemClicked(View view, int pos);
    }

    public interface LongClickListener {
        public void itemLongClicked(View view, int pos);
    }


}
