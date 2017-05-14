package com.jms.scan.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jms.scan.R;
import com.jms.scan.bean.Dock;
import com.jms.scan.engine.DockService;
import com.jms.scan.engine.DockStockService;
import com.jms.scan.engine.StockService;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.CustomBean;
import com.jms.scan.param.DockStockDto;
import com.jms.scan.ui.FclActivity;
import com.jms.scan.ui.MemoActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.StringUtils;
import com.jms.scan.util.debug.LogUtil;
import com.jms.scan.view.ListViewDialog;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<DockStockDto> dsds=new ArrayList<DockStockDto>();
    private DockStockService dockStockService;
    private StockService stockService;
    private List<CustomBean> beans=new ArrayList<>();
    private DockService dockService;

    public static final int fillSize=8;
    private static final String TAG=ItemAdapter.class.getSimpleName();

    public ItemAdapter(Context context) {
        this.dockStockService=ServiceFactory.getInstance().getDockStockService();
        this.stockService=ServiceFactory.getInstance().getStockService();
        this.dockService=ServiceFactory.getInstance().getDockService();
        try {
            beans=stockService.listAllBeans();
        } catch (DbException e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
        }
        this.context=context;
    }

    public void setDsds(List<DockStockDto> dsds) {
        this.dsds=dsds;
        for (int i=0; i < fillSize; i++) {
            DockStockDto dto=new DockStockDto();
            dsds.add(dto);
        }
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

    int mCurrentTouchedIndex=-1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DockStockDto dsd=dsds.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.item_memo_fcl, null);
            holder.mTvDockCode=(TextView) convertView.findViewById(R.id.tv_dockcode);
            holder.mTvStockCode=(TextView) convertView.findViewById(R.id.tv_stockcode);
            holder.mEtNum=(EditText) convertView.findViewById(R.id.et_num);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }

        //将当前bean绑定控件
        holder.mEtNum.setTag(dsd);

        holder.mTvStockCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String current=holder.mTvStockCode.getText().toString().trim();
                ListViewDialog dialog=new ListViewDialog(context) {
                    @Override
                    protected ArrayList<CustomBean> getListData() {
                        return (ArrayList<CustomBean>) beans;
                    }
                };
                dialog.onCreateDialog();
                dialog.setCallBack(new ListViewDialog.ItemCallback() {
                    @Override
                    public void itemClick(CustomBean result) {
                        if (StringUtils.isEmpty(result.getCode())) return;
                        if (result.equals(current)) return;
                        holder.mTvStockCode.setText(result.getCode());
                        changeStatus();
                    }
                });
            }
        });

        holder.mEtNum.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    LogUtil.d(TAG, "click:");
                    v.requestFocus();
                    changeStatus();
                }
                return false;
            }
        });

        holder.mEtNum.addTextChangedListener(new TextWatcher() {

            String cur="";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DockStockDto bean = (DockStockDto) holder.mEtNum.getTag();
                if(!StringUtils.isEmpty(s+"")){
                    bean.setNum(Integer.parseInt(s+""));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                cur=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*String text=holder.mEtNum.getText().toString();
                if (!StringUtils.isEmpty(text) && !text.equals(cur)) {
                    dsds.get(position).setNum(Integer.parseInt(text));
                }*/
            }
        });

        holder.mEtNum.setOnTouchListener(new OnEditTextTouched(position));
        holder.mEtNum.clearFocus();
        if (position == mCurrentTouchedIndex) {
            holder.mEtNum.requestFocus();
        }

        if (dsd.getNum()!=0) {
            holder.mEtNum.setText(dsd.getNum() + "");
        } else {
            holder.mEtNum.setText("");
        }

        if (!StringUtils.isEmpty(dsd.getScode())) {
            holder.mTvStockCode.setText(dsd.getScode());
        } else {
            holder.mTvStockCode.setText("");
        }
        if (!StringUtils.isEmpty(dsd.getDcode())) {
            holder.mTvDockCode.setText(dsd.getDcode());
        } else {
            holder.mTvDockCode.setText("");
        }
        //封箱加背景色
        Dock dock=null;
        try {
            dock=dockService.get(dsd.getOcode(), dsd.getDcode());
        } catch (DbException e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
        }
        if (dock != null && dock.getStatus() == Constants.FLAG_SEAL) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.exercise_green));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        return convertView;
    }

    public void changeStatus() {
        if (context instanceof MemoActivity) {
            ((MemoActivity) context).change();
        }
        if (context instanceof FclActivity) {
            ((FclActivity) context).change();
        }
    }


    public final class ViewHolder {
        TextView mTvDockCode;
        TextView mTvStockCode;
        EditText mEtNum;
    }

    private class OnEditTextTouched implements View.OnTouchListener {
        private int position;

        public OnEditTextTouched(int position) {
            this.position=position;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCurrentTouchedIndex=position;
            }
            return false;
        }
    }
}