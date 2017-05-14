package com.jms.scan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jms.scan.R;
import com.jms.scan.param.CustomBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/5/3.
 */
public class ItemBeanAdapter extends BaseAdapter {

    private Context context;
    private List<CustomBean> beans = new ArrayList<>();

    public ItemBeanAdapter(Context context,List<CustomBean> beans){
        this.context = context;
        this.beans = beans;
    }

    public void setData(List<CustomBean> beans){
        this.beans = beans;
        notifyDataSetChanged();;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public CustomBean getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CustomBean bean = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.item_beans, null);
            holder.mTvName=(TextView) convertView.findViewById(R.id.tv_name);
            holder.mTvCode =(TextView) convertView.findViewById(R.id.tv_code);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(bean.getInfo());
        holder.mTvCode.setText(bean.getCode());
        return convertView;
    }

    public final class ViewHolder {
        TextView mTvName;
        TextView mTvCode;
    }
}
