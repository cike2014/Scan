package com.jms.scan.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jms.scan.R;
import com.jms.scan.SysApplication;
import com.jms.scan.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.bt_memo)
    private Button mBtMemo;
    @ViewInject(R.id.bt_fcl)
    private Button mBtFcl;
    @ViewInject(R.id.bt_exit)
    private Button mBtExit;


    @Override
    protected void injectView() {
        x.view().inject(this);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void setListener() {
        mBtExit.setOnClickListener(this);
        mBtMemo.setOnClickListener(this);
        mBtFcl.setOnClickListener(this);
    }

    @Override
    protected void onKeyEv(View view) {
        switch (view.getId()){
            case R.id.bt_exit:
                finish();
                SysApplication.getInstance().exit();
                break;
            case R.id.bt_memo:
                startActivity(new Intent(this,MemoActivity.class));
                break;
            case R.id.bt_fcl:
                startActivity(new Intent(this,FclActivity.class));
                break;
        }
    }
}
