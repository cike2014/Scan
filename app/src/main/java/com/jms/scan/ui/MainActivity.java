package com.jms.scan.ui;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jms.scan.R;
import com.jms.scan.SysApplication;
import com.jms.scan.param.Result;
import com.jms.scan.bean.User;
import com.jms.scan.engine.UserService;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.ui.base.BaseActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.debug.LogUtil;
import com.jms.scan.util.http.UrlContants;
import com.jms.scan.util.http.Xutils;
import com.jms.scan.util.json.ParseUtil;
import com.jms.scan.util.notification.ToastUtils;
import com.jms.scan.util.setting.SettingUtils;
import com.jms.scan.view.MyWindow;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.tv_name)
    private TextView mTvName;
    @ViewInject(R.id.bt_memo)
    private Button mBtMemo;
    @ViewInject(R.id.bt_fcl)
    private Button mBtFcl;
    @ViewInject(R.id.bt_exit)
    private Button mBtExit;
    @ViewInject(R.id.bt_list)
    private Button mBtList;

    private UserService userService;


    @Override
    protected void injectView() {
        x.view().inject(this);
    }

    @Override
    protected void getData() {
        userService =ServiceFactory.getInstance().getUserService();
        int uid=SettingUtils.getSharedPreferences(MainActivity.this, Constants.UID, -1);
        User user=null;
        try {
            user=userService.get(uid);
        } catch (DbException e) {
            LogUtil.e(TAG,e.getLocalizedMessage());
            ToastUtils.showShort(MainActivity.this,"无法获得当前操作员信息，请重试...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  MainActivity.this.finish();
                }
            },2000);
        }
        mTvName.setText(user.getName());

    }

    @Override
    protected void setListener() {
        mBtExit.setOnClickListener(this);
        mBtMemo.setOnClickListener(this);
        mBtFcl.setOnClickListener(this);
        mBtList.setOnClickListener(this);
    }

    @Override
    protected void onKeyEv(View view) {
        switch (view.getId()) {
            case R.id.bt_exit:
                final MyWindow myWindow = new MyWindow(this);
                myWindow.setTitle("温馨提示");
                myWindow.setMessage("您确定要退出吗?");
                myWindow.hideTitle();
                myWindow.setPositiveButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        exit();
                        myWindow.dismiss();
                    }

                });
                myWindow.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myWindow.dismiss();
                    }
                });
                myWindow.show();
                break;
            case R.id.bt_memo:
                startActivity(new Intent(this, MemoActivity.class));
                break;
            case R.id.bt_fcl:
                startActivity(new Intent(this, FclActivity.class));
                break;
            case R.id.bt_list:
                startActivity(new Intent(this, ListActivity.class));
                break;
        }
    }

    private void exit(){
        final int uid=SettingUtils.getSharedPreferences(MainActivity.this, Constants.UID, -1);
        if (uid > -1) {
            Map<String, String> params=new HashMap<>();
            params.put("uid", uid + "");
            Xutils.get().get(UrlContants.LOGOUT_URL, params, new Xutils.XCallBack() {
                @Override
                public void onSuccessResponse(String response) {
                    Result result=ParseUtil.get().getResult(response);
                    if (result.getStatus().equals(Constants.STATUS_Y)) {
                        try {
                            ServiceFactory.getInstance().getUserService().deleteUser(uid);
                        } catch (DbException e) {
                            LogUtil.e(TAG,"退出时删除用户出现异常:"+ Log.getStackTraceString(e));
                        }
                        SettingUtils.clear(MainActivity.this);
                        finish();
                        SysApplication.getInstance().exit();
                    } else {
                        ToastUtils.showShort(MainActivity.this, "网络故障，暂时无法退出");
                    }
                }

                @Override
                public void onError(String message) {
                    ToastUtils.showShort(MainActivity.this, message);
                }
            });
        }

    }

    private int backClickTime = 0;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (backClickTime == 1) {
                exit();
                backClickTime = 0;
            }
            if (backClickTime == 1) {
                backClickTime = 0;
            } else {
                ToastUtils.showShort(MainActivity.this,"再按一次退出程序");
                backClickTime = 1;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        backClickTime = 0;
                    }
                }, 3000);
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}
