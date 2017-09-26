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

import com.google.common.collect.Lists;
import com.jms.scan.R;
import com.jms.scan.bean.Dock;
import com.jms.scan.bean.DockStock;
import com.jms.scan.bean.Stock;
import com.jms.scan.engine.DockService;
import com.jms.scan.engine.DockStockService;
import com.jms.scan.engine.StockService;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.CustomBean;
import com.jms.scan.param.DockStockDto;
import com.jms.scan.ui.FclActivity;
import com.jms.scan.ui.MemoActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.DataUtil;
import com.jms.scan.util.common.StringUtils;
import com.jms.scan.util.debug.LogUtil;
import com.jms.scan.view.ListViewDialog;
import com.jms.scan.view.MyWindow;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * 装箱。单号-箱号-产品号列表
 */
public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<DockStockDto> dockStocks=Lists.newArrayList();
    private StockService stockService;
    private DockService dockService;
    private DockStockService dockStockService;

    //当前ListView维护的订单号和箱码
    private String orderCode;
    private String dockCode;

    //是否需要重新定位
    private boolean relocate = false;

    //数量规定的长度限制
    private final int NUM_LENGTH = 5;

    public static final int fillSize=8;
    private int type;
    private static final String TAG=ItemAdapter.class.getSimpleName();

    public ItemAdapter(Context context,int type) {
        this.stockService=ServiceFactory.getInstance().getStockService();
        this.dockService=ServiceFactory.getInstance().getDockService();
        this.dockStockService=ServiceFactory.getInstance().getDockStockService();
        this.context=context;
        this.type = type;
    }

    public void setOrderCode(String orderCode){
        this.orderCode = orderCode;
    }

    public void setDockCode(String dockCode){
        this.dockCode = dockCode;
    }


    public void setDockStocks(List<DockStockDto> dockStocks) {
        this.dockStocks=dockStocks;
        for (int i=0; i < fillSize; i++) {
            DockStockDto dto=new DockStockDto();
            dockStocks.add(dto);
        }
        notifyDataSetChanged();
    }

    public List<DockStockDto> getDockStocks(){
        return this.dockStocks;
    }

    @Override
    public int getCount() {
        return dockStocks.size();
    }

    @Override
    public Object getItem(int position) {
        return dockStocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dockStocks.size();
    }

    int mCurrentTouchedIndex = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DockStockDto dockStock=dockStocks.get(position);
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
        holder.mEtNum.setTag(dockStock);

        holder.mTvDockCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final MyWindow wm = new MyWindow(context);
                wm.setTitle("温馨提示");
                wm.setMessage("是否删除该条目?");
                wm.setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            wm.dismiss();
                            dockStockService.delete(dockStock.getOcode(),dockStock.getDcode(),dockStock.getScode());
                            changeStatus();
                        } catch (DbException e) {
                            LogUtil.e(TAG,"删除失败，请重试");
                        }
                    }
                });
                wm.setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wm.dismiss();
                    }
                });
                wm.show();
                return false;
            }
        });

        holder.mTvStockCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String current=holder.mTvStockCode.getText().toString().trim();
                ListViewDialog dialog=new ListViewDialog(context) {
                    @Override
                    protected List<CustomBean> getListData() {
                        return DataUtil.getInstance().getAllStocks();
                    }
                };
                dialog.setCallBack(new ListViewDialog.ItemCallback() {
                    @Override
                    public void itemClick(CustomBean result) {
                        if (StringUtils.isEmpty(result.getCode())) return;
                        if (current.equals(result.getCode())) return;
                        if (StringUtils.isEmpty(current)) {//如果空白行，新增记录，并通知activity更新
                            try {
                                Stock stock=stockService.getByCode(result.getCode());
                                if(type==Constants.FLAG_TYPE_MEMO){
                                    dockStockService.excecuteMemoScan(orderCode,dockCode,stock.getBarCode(),Lists.<String>newArrayList());
                                }else{
                                    dockStockService.executeFclScan(orderCode,stock.getBoxCode(),Lists.<String>newArrayList());
                                }
                                changeStatus();
                            } catch (DbException e) {
                                LogUtil.e(TAG, e.getLocalizedMessage());
                            } finally {
                                return;
                            }
                        }
                        //改变货品后，直接更新到数据库中
                        try {
                            //删除旧产品
                            dockStockService.delete(dockStock.getOcode(), dockStock.getDcode(), current);
                            //查询新产品是否在数据库中存在
                            DockStock dbDockStock=dockStockService.getDockStock(dockStock.getOcode(), dockStock.getDcode(), result.getCode());
                            if (dbDockStock != null) {
                                //存在的话，直接更新
                                dockStockService.update(dockStock.getOcode(), dockStock.getDcode(), result.getCode(), dockStock.getNum());
                            } else {
                                //不存在的话，保存
                                dockStockService.addDockStock(dockStock.getOcode(), dockStock.getDcode(), result.getCode(), dockStock.getNum());
                            }
                            //通知activity，加载修改后的结果
                            mCurrentTouchedIndex = -1;
                            changeStatus();
                        } catch (DbException ex) {
                            LogUtil.e(TAG, ex.getLocalizedMessage());
                        }
                    }
                });
                dialog.onCreateDialog();
            }
        });


        holder.mEtNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO 默认添加的数量小于5
                if(!StringUtils.isEmpty(s.toString()) && s.length()>=NUM_LENGTH){
                    LogUtil.d(TAG,"输入长度："+s.length());
                    holder.mEtNum.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.mEtNum.clearFocus();
                            //如果在数量框直接扫码，则重新定位
                            requestFocus();
                        }
                    },10);
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.mEtNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            String cur="";
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(StringUtils.isEmpty(holder.mTvStockCode.getText().toString())){
                        v.clearFocus();
                    }else{
                        LogUtil.d(TAG,"mEtNum获得焦点，记录value:"+cur);
                        cur = ((EditText)v).getText().toString();
                    }
                }else{
                    final String s = ((EditText)v).getText().toString();
                    DockStockDto bean=(DockStockDto) holder.mEtNum.getTag();

                    if(s.length()>=NUM_LENGTH &&!StringUtils.isEmpty(cur)){
                        LogUtil.d(TAG,"mEtNum失去焦点，设置value:"+cur);
                        holder.mEtNum.setText(cur);
                        return;
                    }

                    if (StringUtils.isEmpty(s) || (bean.getNum() + "").equals(s)) {
                        return;
                    }

                    try {
                        //数量修改，直接保存到数据库
                        dockStockService.update(bean.getOcode(), bean.getDcode(), bean.getScode(), Integer.parseInt(s));
                        //通知activity更新
                        holder.mEtNum.setText(s);
                        holder.mEtNum.clearFocus();
                        //修改完成后，设置触摸位置为-1
                        mCurrentTouchedIndex = -1;
                        changeStatus();
                    } catch (DbException e) {
                        LogUtil.e(TAG, e.getLocalizedMessage());
                    }
                }
            }
        });

        if (dockStock.getNum() != 0) {
            holder.mEtNum.setText(dockStock.getNum() + "");
        } else {
            holder.mEtNum.setText("");
        }

        holder.mEtNum.setOnTouchListener(new OnEditTextTouched(position));
        //一开始失去焦点
        holder.mEtNum.clearFocus();
        LogUtil.d(TAG,position+";"+dockStocks.size());
        //如果刷新的话，当前位置==上次触摸mEtNum保存的位置，则获得焦点
        LogUtil.d(TAG,"刷新咯：mCurrentTouchedIndex="+mCurrentTouchedIndex+";postion="+position);
        LogUtil.d(TAG,"LIST中:dockCode="+dockStock.getDcode()+";stockCode="+dockStock.getScode());

        LogUtil.d(TAG,"====================================================");
        if(position==mCurrentTouchedIndex){
            holder.mEtNum.requestFocus();
            //将光标定位到最后
            String m =  holder.mEtNum.getText().toString();
            holder.mEtNum.setSelection(m.length());
        }

        if (!StringUtils.isEmpty(dockStock.getScode())) {
            holder.mTvStockCode.setText(dockStock.getScode());
        } else {
            holder.mTvStockCode.setText("");
        }
        if (!StringUtils.isEmpty(dockStock.getDcode())) {
            holder.mTvDockCode.setText(dockStock.getDcode());
        } else {
            holder.mTvDockCode.setText("");
        }
        //封箱加背景色
        Dock dock=null;
        try {
            dock=dockService.get(dockStock.getOcode(), dockStock.getDcode());
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

    public void requestFocus(){
        if(context instanceof MemoActivity){
            ((MemoActivity)context).requestBarCodeFocus();
        }
        if(context instanceof FclActivity){
            ((FclActivity)context).requestBoxCodeFocus();
        }
    }


    public final class ViewHolder {
        TextView mTvDockCode;
        TextView mTvStockCode;
        EditText mEtNum;
    }

    private class OnEditTextTouched implements View.OnTouchListener {

        private int position ;

        public OnEditTextTouched(int position){
            this.position = position;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //修改mEtNum，保存修改位置
                if(mCurrentTouchedIndex==-1){
                    mCurrentTouchedIndex = position;
                }

            }
            return false;
        }
    }
}