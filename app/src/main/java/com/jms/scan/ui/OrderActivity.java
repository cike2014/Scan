package com.jms.scan.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jms.scan.R;
import com.jms.scan.bean.Order;
import com.jms.scan.bean.User;
import com.jms.scan.engine.CustomerService;
import com.jms.scan.engine.OrderService;
import com.jms.scan.engine.UserService;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.CustomBean;
import com.jms.scan.param.OrderInfo;
import com.jms.scan.ui.base.BaseActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.DateUtils;
import com.jms.scan.util.common.StringUtils;
import com.jms.scan.util.debug.LogUtil;
import com.jms.scan.util.notification.ToastUtils;
import com.jms.scan.util.setting.SettingUtils;
import com.jms.scan.view.ListViewDialog;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alpha on 2017/5/8.
 */
@ContentView(R.layout.activity_order)
public class OrderActivity extends BaseActivity {
    @ViewInject(R.id.iv_left)
    private ImageView mIvLeft;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;

    @ViewInject(R.id.tv_code)
    private TextView mTvCode;
    @ViewInject(R.id.tv_date)
    private TextView mTvDate;
    @ViewInject(R.id.tv_user)
    private TextView mTvUser;
    @ViewInject(R.id.tv_customer)
    private TextView mTvCustomer;
    @ViewInject(R.id.bt_confirm)
    private Button mBtConfirm;


    private UserService userService;
    private OrderService orderService;
    private CustomerService customerService;

    //装箱单号
    private String orderCode;
    //新增或编辑
    private int flag_status;
    //装箱单类型
    private int flag_type;

    private Order order;
    private String ccode;//客户编号


    private List<CustomBean> beans=new ArrayList<>();
    private static final String TAG=OrderActivity.class.getSimpleName();
    @Override
    protected void injectView() {
        x.view().inject(this);
        mTvTitle.setText(getResources().getString(R.string.order));
        mIvLeft.setVisibility(View.VISIBLE);
    }

    @Override
    protected void getData() {

        userService=ServiceFactory.getInstance().getUserService();
        orderService=ServiceFactory.getInstance().getOrderService();
        customerService=ServiceFactory.getInstance().getCustomerService();
        int uid=SettingUtils.getSharedPreferences(OrderActivity.this, Constants.UID, -1);
        User user=null;
        try {
            beans=customerService.listAllBeans();
            user=userService.get(uid);
        } catch (DbException e) {
            LogUtil.e(TAG, Log.getStackTraceString(e));
        }
        orderCode=getIntent().getStringExtra(Constants.FLAG_ORDER_CODE);
        flag_status=getIntent().getIntExtra(Constants.FLAG_STATUS, -1);
        flag_type=getIntent().getIntExtra(Constants.FLAG_TYPE, -1);
        if (flag_status == Constants.FLAG_STATUS_ADD) {//新增装箱单
            mTvCode.setText(orderCode);
            Date now=new Date();
            mTvDate.setText(DateUtils.Date2FormatString(now));
            mTvUser.setText(user.getName());
            //设置订单信息
            order=new Order();
            order.setCode(orderCode);
            order.setDate(now.getTime());
            order.setSave(Constants.FLAG_UNSAVE);
            order.setType(flag_type);
            order.setUid(uid);
        } else {//编辑装箱单
            try {
                OrderInfo info=orderService.getOrderInfoByOcode(orderCode);
                mTvCode.setText(info.getCode());
                mTvDate.setText(DateUtils.getDate(info.getDate(), DateUtils.DEFAULT_FORMAT));
                mTvUser.setText(info.getUname());
                mTvCustomer.setText(info.getCname());
                ccode = info.getCcode();//设置客户编号
                order=info.getOrder();//设置当前装箱单
            } catch (DbException e) {
                LogUtil.e(TAG, Log.getStackTraceString(e));
            }
        }
        mBtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setCcode(ccode);
                if (StringUtils.isEmpty(order.getCcode())) {
                    ToastUtils.showShort(OrderActivity.this, "请先选择客户");
                    return;
                }
                if (flag_status == Constants.FLAG_STATUS_ADD) {//新增
                    try {
                        orderService.save(order);
                    } catch (DbException e) {
                        LogUtil.e(TAG, Log.getStackTraceString(e));
                        ToastUtils.showShort(OrderActivity.this, "设置失败，请重试...");
                    }
                } else {//修改
                    try {
                       orderService.updateCustomer(orderCode,ccode);
                    } catch (DbException e) {
                        LogUtil.e(TAG, Log.getStackTraceString(e));
                        ToastUtils.showShort(OrderActivity.this, "设置失败，请重试...");
                    }
                }
                Intent data=new Intent();
                data.putExtra(Constants.FLAG_ORDER_CODE, orderCode);
                setResult(Constants.FLAG_CHANGED, data);
                finish();
            }
        });

        mTvCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewDialog dialog=new ListViewDialog(OrderActivity.this) {
                    @Override
                    protected ArrayList<CustomBean> getListData() {
                        return (ArrayList<CustomBean>) beans;
                    }
                };
                dialog.onCreateDialog();
                dialog.setCallBack(new ListViewDialog.ItemCallback() {
                    @Override
                    public void itemClick(CustomBean result) {
                        mTvCustomer.setText(result.getInfo());
                        ccode=result.getCode();
                    }
                });
            }
        });

        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Constants.FLAG_NOT_CHANGED);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(Constants.FLAG_NOT_CHANGED);
        super.onBackPressed();
    }
}
