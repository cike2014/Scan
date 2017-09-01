package com.jms.scan.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jms.scan.DataCenter;
import com.jms.scan.R;
import com.jms.scan.bean.Customer;
import com.jms.scan.bean.Stock;
import com.jms.scan.bean.User;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.Record;
import com.jms.scan.param.Result;
import com.jms.scan.ui.base.BaseActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.DeviceUtil;
import com.jms.scan.util.common.ResourceUtil;
import com.jms.scan.util.common.StringUtils;
import com.jms.scan.util.debug.LogUtil;
import com.jms.scan.util.http.UrlContants;
import com.jms.scan.util.http.Xutils;
import com.jms.scan.util.json.ParseUtil;
import com.jms.scan.util.notification.ToastUtils;
import com.jms.scan.util.setting.SettingUtils;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.et_account)
    private EditText mEtAccount;
    @ViewInject(R.id.et_password)
    private EditText mEtPassword;
    @ViewInject(R.id.et_server)
    private EditText mEtServer;
    @ViewInject(R.id.bt_login)
    private Button mBtLogin;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;



    private final static String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void injectView() {
        x.view().inject(this);
        mTvTitle.setText(ResourceUtil.getResourceById(this, R.string.label_login));
    }

    @Override
    protected void setListener() {
        mBtLogin.setOnClickListener(this);
    }

    @Override
    protected void onKeyEv(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                prepareLogin();
                break;
        }
    }

    private void prepareLogin() {
        if (StringUtils.isEmpty(mEtAccount.getText().toString())) {
            ToastUtils.showShort(this, ResourceUtil.getResourceById(this, R.string.tip_account_notnull));
            return;
        }
        if (StringUtils.isEmpty(mEtPassword.getText().toString())) {
            ToastUtils.showShort(this, ResourceUtil.getResourceById(this, R.string.tip_password_notnull));
            return;
        }
        if (StringUtils.isEmpty(mEtServer.getText().toString())) {
            ToastUtils.showShort(this, ResourceUtil.getResourceById(this, R.string.tip_server_notnull));
            return;
        }

        SettingUtils.setEditor(this, Constants.SERVER_ADDRESS, StringUtils.getViewText(mEtServer));

        doLogin();

    }

    private void doLogin() {
        showLoading();
        Map<String, String> params=new HashMap<>();
        params.put("username", StringUtils.getViewText(mEtAccount));
        params.put("password",StringUtils.getViewText(mEtPassword));
        params.put("mac", new DeviceUtil(LoginActivity.this).getUnique());
        Xutils.get().post(UrlContants.getInstance().getLoginUrl(), params, new Xutils.XCallBack() {

            @Override
            public void onSuccessResponse(String response) {
                Result result=ParseUtil.get().getResult(response, Record.class);
                if(result.getStatus().equals(Constants.STATUS_N)){
                    mEtAccount.setFocusable(true);
                    ToastUtils.showShort(LoginActivity.this,result.getInfo());
                }else{
                    Record record =(Record) result.getDataObj();
                    User user = record.getUser();
                    List<Customer> customers = record.getCustomers();
                    List<Stock> stocks = record.getStocks();
                    try {
                        ServiceFactory.getInstance().getUserService().saveOrUpdate(user);
                        ServiceFactory.getInstance().getCustomerService().saveUpdate(customers);
                        ServiceFactory.getInstance().getStockService().saveUpdate(stocks);
                        //清空ACache缓存
                        DataCenter.get().getaCache().clear();
                    } catch (DbException e) {
                        LogUtil.e(TAG, Log.getStackTraceString(e));
                        ToastUtils.showShort(LoginActivity.this,"同步档案失败，正在关闭...");
                    }
                    //维护终端编号
                    SettingUtils.setEditor(LoginActivity.this,Constants.CLIENT_FLAG,record.getFlag());
                    //维护当前操作员
                    SettingUtils.setEditor(LoginActivity.this,Constants.UID,user.getUid());
                    hideLoading();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    LoginActivity.this.finish();
                }
            }

            @Override
            public void onError(String message) {
                hideLoading();
                LogUtil.e(TAG,"error:"+message);
                ToastUtils.showShort(LoginActivity.this, "网络异常，请检查网络...");
            }

        });
    }

    @Override
    protected void getData() {

    }
}
