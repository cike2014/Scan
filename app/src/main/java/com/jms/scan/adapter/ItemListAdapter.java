package com.jms.scan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jms.scan.R;
import com.jms.scan.param.OrderInfo;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/5/10.
 */
public class ItemListAdapter extends BaseAdapter {

    private Context context;
    private List<OrderInfo> infoList=new ArrayList<>();

    public ItemListAdapter(Context context, List<OrderInfo> infoList) {
        this.context=context;
        this.infoList=infoList;
    }

    public void setInfoList(List<OrderInfo> infoList){
        this.infoList = infoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return infoList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final OrderInfo info=infoList.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.item_list, null);
            holder.mTvCode=(TextView) convertView.findViewById(R.id.tv_code);
            holder.mTvCustomer=(TextView) convertView.findViewById(R.id.tv_customer);
            holder.mTvDate=(TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }

        if(info.getSubmit()== Constants.FLAG_SUBMIT){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.exercise_green));
        }else{
            if(info.getSave()==Constants.FLAG_SAVE){
                convertView.setBackgroundColor(context.getResources().getColor(R.color.exercise_blue));
            }else{
                convertView.setBackgroundColor(context.getResources().getColor(R.color.exercise_gray));
            }
        }

        holder.mTvCustomer.setText(info.getCname());
        holder.mTvCode.setText(info.getCode());
        holder.mTvDate.setText(DateUtils.getDate(info.getDate(), DateUtils.DEFAULT_FORMAT));
        return convertView;
    }

    public final class ViewHolder {
        TextView mTvCode;
        TextView mTvDate;
        TextView mTvCustomer;
    }
}
