package com.jms.scan.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.jms.scan.R;
import com.jms.scan.adapter.ItemBeanAdapter;
import com.jms.scan.param.CustomBean;
import com.jms.scan.util.common.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/5/3.
 */
public abstract class ListViewDialog {
    static DialogFragment dialog=null;
    private Context context;
    ItemCallback callBack;
    private static final String TAG=ListViewDialog.class.getSimpleName();

    public ListViewDialog(Context mcontext) {
        this.context=mcontext;
    }


    public Dialog onCreateDialog() {
        final View view=View.inflate(context, R.layout.dialog_listview,
                null);

        final Dialog builder=new Dialog(context, R.style.Dialog);
        builder.setContentView(view);
        //设置属性
        Window dialogWindow=builder.getWindow();
        WindowManager.LayoutParams lp=dialogWindow.getAttributes();
        lp.gravity=Gravity.CENTER;
        lp.width=DisplayUtil.getScreenWidth(context)*2/3 ;
        lp.height=DisplayUtil.getScreenHeight(context)*2/3 ;
        dialogWindow.setAttributes(lp);

        //设置内容
        ListView listView=(ListView) view.findViewById(R.id.lv_beans);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        final ItemBeanAdapter adapter=new ItemBeanAdapter(context, getListData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View convertView,
                                    int position, long index) {
                CustomBean result=getListData().get(position);
                callBack.itemClick(result);
                builder.dismiss();
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
                ArrayList<CustomBean> datas=getListData();
                List<CustomBean> newDatas=new ArrayList<>();
                for (CustomBean data : datas) {
                    if (data.getCode().toUpperCase().indexOf(result.toUpperCase()) > -1) {
                        newDatas.add(data);
                    }
                    if(data.getInfo().toUpperCase().indexOf(result.toUpperCase())>-1){
                        newDatas.add(data);
                    }
                }
                adapter.setData(newDatas);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.show();
        return builder;
    }


    protected abstract ArrayList<CustomBean> getListData();

    public ItemCallback getCallBack() {
        return callBack;
    }

    public void setCallBack(ItemCallback callBack) {
        this.callBack=callBack;
    }

    public interface ItemCallback {
        public void itemClick(CustomBean result);
    }
}
