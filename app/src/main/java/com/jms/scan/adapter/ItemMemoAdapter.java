package com.jms.scan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jms.scan.R;
import com.jms.scan.dto.DockStockDto;
import com.jms.scan.engine.DockStockService;
import com.jms.scan.ui.FclActivity;
import com.jms.scan.ui.MemoActivity;
import com.jms.scan.view.CustomDialog;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class ItemMemoAdapter extends BaseAdapter {

    private Context context;
    private List<DockStockDto> dsds=new ArrayList<DockStockDto>();
    private DockStockService dockStockService;

    public ItemMemoAdapter(Context context) {
        this.dockStockService=new DockStockService();
        this.context=context;
    }

    public void setDsds(List<DockStockDto> dsds) {
        this.dsds=dsds;
        notifyDataSetChanged();
    }

    public void addDsd(DockStockDto dsd) {
        this.dsds.add(dsd);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dsds.size();
    }

    @Override
    public Object getItem(int position) {
        return dsds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dsds.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DockStockDto dsd=dsds.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.item_memo, null);
            holder.mTvDockCode=(TextView) convertView.findViewById(R.id.tv_dockcode);
            holder.mTvNum=(TextView) convertView.findViewById(R.id.tv_num);
            holder.mTvStockCode=(TextView) convertView.findViewById(R.id.tv_stockcode);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomDialog.Builder builder=new CustomDialog.Builder(context);
                builder.setTitle("温馨提示").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int num=Integer.parseInt(builder.getNum());
                        try {
                            dockStockService.update(dsd.getDid(), dsd.getSid(), num);
                            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                            refresh(context);
                            dialog.dismiss();
                        } catch (DbException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "保存失败，请重试", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setCenterButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dockStockService.delete(dsd.getDid(), dsd.getSid());
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            refresh(context);
                            dialog.dismiss();
                        } catch (DbException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "删除失败，请重试", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setName(dsd.getSname()).setNum(dsd.getNum() + "");
                builder.create().show();
            }
        });
        holder.mTvNum.setText(dsd.getNum() + "");
        holder.mTvStockCode.setText(dsd.getScode());
        holder.mTvDockCode.setText(dsd.getDcode());
        return convertView;
    }

    private void refresh(Context context) {
        Activity activity=(Activity) context;
        if (activity instanceof MemoActivity) {
            ((MemoActivity) activity).reloadData();
        }
        if (activity instanceof FclActivity) {
            ((FclActivity) activity).reloadData();
        }
    }

    public final class ViewHolder {
        TextView mTvDockCode;
        TextView mTvStockCode;
        TextView mTvNum;
    }
}
