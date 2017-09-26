package com.jms.scan.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jms.scan.R;
import com.jms.scan.adapter.ItemListAdapter;
import com.jms.scan.engine.DockService;
import com.jms.scan.engine.DockStockService;
import com.jms.scan.engine.OrderDockService;
import com.jms.scan.engine.OrderService;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.OrderInfo;
import com.jms.scan.ui.base.BaseActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.debug.LogUtil;
import com.jms.scan.util.notification.ToastUtils;
import com.jms.scan.util.setting.SettingUtils;
import com.jms.scan.view.MyWindow;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_order_list)
public class ListActivity extends BaseActivity {

    @ViewInject(R.id.iv_left)
    private ImageView mIvLeft;
    @ViewInject(R.id.iv_right)
    private ImageView mIvRight;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.srl_order)
    private SwipeRefreshLayout mSrlOrder;
    @ViewInject(R.id.lv_orders)
    private ListView mLvOrders;

    private int uid;

    private OrderService orderService;
    private DockService dockService;
    private OrderDockService orderDockService;
    private DockStockService dockStockService;
    private List<OrderInfo> infoList=new ArrayList<>();
    private static final String TAG=ListActivity.class.getSimpleName();

    @Override
    protected void injectView() {
        x.view().inject(this);
        mIvLeft.setVisibility(View.VISIBLE);
        mTvTitle.setText(getResources().getText(R.string.label_list));
        mLvOrders.addHeaderView(View.inflate(ListActivity.this, R.layout.item_list, null));
    }

    @Override
    protected void getData() {
        uid=SettingUtils.getSharedPreferences(ListActivity.this, Constants.UID, -1);
        orderService=ServiceFactory.getInstance().getOrderService();
        dockService=ServiceFactory.getInstance().getDockService();
        dockStockService=ServiceFactory.getInstance().getDockStockService();
        orderDockService=ServiceFactory.getInstance().getOrderDockService();
        try {
            infoList=orderService.getOrderInfosByUid(uid);
        } catch (DbException e) {
            LogUtil.e(TAG, Log.getStackTraceString(e));
        }
        final ItemListAdapter adapter=new ItemListAdapter(ListActivity.this, infoList);
        mLvOrders.setAdapter(adapter);
        mSrlOrder.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSrlOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            infoList=orderService.getOrderInfosByUid(uid);
                            adapter.setInfoList(infoList);
                        } catch (DbException e) {
                            LogUtil.e(TAG, Log.getStackTraceString(e));
                        }
                        mSrlOrder.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mLvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderInfo info=infoList.get(position - 1);
                Intent intent=null;
                if (info.getType().equals(Constants.FLAG_TYPE_MEMO)) {
                    intent=new Intent(ListActivity.this, MemoActivity.class);
                }
                if (info.getType().equals(Constants.FLAG_TYPE_FCL)) {
                    intent=new Intent(ListActivity.this, FclActivity.class);
                }
                if (intent == null) {
                    return;
                }
                intent.putExtra(Constants.FLAG_ORDER_CODE, info.getCode());
                startActivity(intent);
            }
        });

        mLvOrders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final OrderInfo info=infoList.get(position - 1);
                final MyWindow myWindow=new MyWindow(ListActivity.this);
                myWindow.setTitle("温馨提示");
                myWindow.setMessage("您确定要删除该装箱数据吗?");
                myWindow.setPositiveButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String orderCode=info.getOrder().getCode();

                        try {
                            dockService.deleteDock(orderCode);
                            dockStockService.deleteDockStockByOcode(orderCode);
                            orderService.deleteByCode(orderCode);
                            infoList=orderService.getOrderInfosByUid(uid);
                            adapter.setInfoList(infoList);
                        } catch (DbException e) {
                            LogUtil.e(TAG, e.getLocalizedMessage());
                            ToastUtils.showShort(ListActivity.this, "删除失败，请重试");
                        }

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

                return false;
            }
        });
    }

    @Override
    protected void setListener() {
        mIvLeft.setOnClickListener(this);
    }

    @Override
    protected void onKeyEv(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
        }
    }
}
