package com.jms.scan.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.jms.scan.R;
import com.jms.scan.adapter.ItemBeanAdapter;
import com.jms.scan.param.CustomBean;
import com.jms.scan.util.common.DisplayUtil;

import java.util.List;

/**
 * Created by alpha on 2017/5/3.
 */
public abstract class ListViewDialog {
    private static final String TAG=ListViewDialog.class.getSimpleName();
    private Context context;
    ItemCallback callBack;
    private List<CustomBean> customers =Lists.newArrayList();
    private Dialog dialog;

    public ListViewDialog(Context context) {
        this.context=context;
    }


    public Dialog onCreateDialog() {
        final View view=View.inflate(context, R.layout.dialog_listview,
                null);

        dialog=new Dialog(context, R.style.Dialog);
        dialog.setContentView(view);
        //设置属性
        Window dialogWindow=dialog.getWindow();
        WindowManager.LayoutParams lp=dialogWindow.getAttributes();
        lp.gravity=Gravity.CENTER;
        lp.width=DisplayUtil.getScreenWidth(context)*4/5 ;
        lp.height=DisplayUtil.getScreenHeight(context)*4/5 ;
        dialogWindow.setAttributes(lp);

        //设置内容
        ListView listView=(ListView) view.findViewById(R.id.lv_beans);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);

        //设置数据
        List<CustomBean> beans = getListData();
        customers.clear();
        customers.addAll(beans);
        final ItemBeanAdapter adapter=new ItemBeanAdapter(context, customers);
        listView.setAdapter(adapter);
        //设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View convertView,
                                    int position, long index) {
                CustomBean result=customers.get(position);
                callBack.itemClick(result);
                dialog.dismiss();
                dialog = null;
            }
        });
        //设置过滤
        EditText etCondition=(EditText) view.findViewById(R.id.et_condition);
        etCondition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String result=s.toString().trim();
                customers = getListData();
                List<CustomBean> newDatas = Lists.newArrayList();
                for (CustomBean data : customers) {
                    if (data.getCode().toUpperCase().indexOf(result.toUpperCase()) > -1) {
                        newDatas.add(data);
                    }
                    if(data.getInfo().toUpperCase().indexOf(result.toUpperCase())>-1){
                        newDatas.add(data);
                    }
                }
                customers.clear();
                customers.addAll(newDatas);
                adapter.setData(customers);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //显示窗体
        dialog.show();
        return dialog;
    }



    protected abstract List<CustomBean> getListData();

    public void setCallBack(ItemCallback callBack) {
        this.callBack=callBack;
    }

    public interface ItemCallback {
        void itemClick(CustomBean result);
    }
}
