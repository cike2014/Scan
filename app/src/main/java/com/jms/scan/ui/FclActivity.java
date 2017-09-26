package com.jms.scan.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jms.scan.R;
import com.jms.scan.adapter.ItemAdapter;
import com.jms.scan.bean.Dock;
import com.jms.scan.bean.DockStock;
import com.jms.scan.bean.Order;
import com.jms.scan.bean.Stock;
import com.jms.scan.engine.DockService;
import com.jms.scan.engine.DockStockService;
import com.jms.scan.engine.OrderService;
import com.jms.scan.engine.StockService;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.DockStockDto;
import com.jms.scan.param.OrderInfoParam;
import com.jms.scan.param.Result;
import com.jms.scan.ui.base.BaseActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.ResourceUtil;
import com.jms.scan.util.common.SoftInputUtil;
import com.jms.scan.util.common.StringUtils;
import com.jms.scan.util.common.TestUtil;
import com.jms.scan.util.debug.LogUtil;
import com.jms.scan.util.http.UrlContants;
import com.jms.scan.util.http.Xutils;
import com.jms.scan.util.json.ParseUtil;
import com.jms.scan.util.notification.ToastUtils;
import com.jms.scan.view.MyWindow;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_fcl)
public class FclActivity extends BaseActivity {

    @ViewInject(R.id.iv_left)
    private ImageView mIvLeft;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.iv_right)
    private ImageView mIvRight;
    @ViewInject(R.id.iv_search)
    private ImageView mIvSearch;

    @ViewInject(R.id.srl_fcls)
    private SwipeRefreshLayout mSrlFcls;
    @ViewInject(R.id.lv_fcl)
    private ListView mLvFcl;
    @ViewInject(R.id.et_boxcode)
    private EditText mEtBoxCode;
    @ViewInject(R.id.bt_save)
    private Button mBtSave;

    private OrderService orderService;
    private StockService stockService;

    private DockStockService dockStockService;
    private DockService dockService;
    private ItemAdapter adapter;
    private List<DockStockDto> dsds=new ArrayList<>();
    private String orderCode;

    private static final int REQUEST_CODE=1;

    private static final String TAG=FclActivity.class.getSimpleName();

    public static String[] datas=new String[]{"X-NA-6403-30", "X-NA-6421-18", "X-NA-6417-20"};

    private static int change=Constants.FLAG_NOT_CHANGED;//默认在当前界面没做修改


    @Override
    protected void injectView() {
        x.view().inject(this);
        mIvLeft.setVisibility(View.VISIBLE);
        mTvTitle.setText(ResourceUtil.getResourceById(this, R.string.label_fcl));
        mIvRight.setVisibility(View.VISIBLE);
        mIvSearch.setVisibility(View.VISIBLE);
    }

    @Override
    protected void getData() {
        orderService=ServiceFactory.getInstance().getOrderService();
        stockService=ServiceFactory.getInstance().getStockService();
        dockStockService=ServiceFactory.getInstance().getDockStockService();
        dockService=ServiceFactory.getInstance().getDockService();
        orderCode=getIntent().getStringExtra(Constants.FLAG_ORDER_CODE);

        mLvFcl.addHeaderView(View.inflate(this, R.layout.item_top, null));
        adapter=new ItemAdapter(this, Constants.FLAG_TYPE_FCL);
        adapter.setDockStocks(dsds);
        mLvFcl.setAdapter(adapter);

        if (StringUtils.isEmpty(orderCode)) {
            try {
                orderCode=orderService.getCode(Constants.FLAG_TYPE_FCL, FclActivity.this);
                LogUtil.d(TAG, "new:" + orderCode);
                Intent intent=new Intent(FclActivity.this, OrderActivity.class);
                intent.putExtra(Constants.FLAG_ORDER_CODE, orderCode);
                intent.putExtra(Constants.FLAG_STATUS, Constants.FLAG_STATUS_ADD);
                intent.putExtra(Constants.FLAG_TYPE, Constants.FLAG_TYPE_FCL);
                startActivityForResult(intent, REQUEST_CODE);
            } catch (DbException e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                ToastUtils.showShort(FclActivity.this, "无法获得当前装箱单单号，请重试...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FclActivity.this.finish();
                    }
                }, 1000);
            }
        } else {
            //直接加载数据
            initData();
            adapter.setOrderCode(orderCode);
            LogUtil.d(TAG, "old:" + orderCode);
        }

        dockStockService=new DockStockService();
        mSrlFcls.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSrlFcls.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mSrlFcls.setRefreshing(false);
                        initData();
                    }
                }, 2000);
            }
        });
        initScanListener();
    }

    /**
     * 监听条码文本框，获得条码后，保存
     */
    private void initScanListener() {
        mEtBoxCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String result=s.toString().trim();
                if (StringUtils.isEmpty(result)) {
                    return;
                }
                try {
                    Stock stock=stockService.getByBoxCode(result);
                    if (stock == null) {
//                        Toast.makeText(FclActivity.this, R.string.scan_box_notexist, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mEtBoxCode.setText("");
                            }
                        }, 200);
                    } else {
                        List<String> errors=new ArrayList<>();
                        dockStockService.executeFclScan(orderCode, result, errors);
                        if (errors.size() == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mEtBoxCode.setText("");
                                    initData();
                                }
                            }, 200);
                        } else {
                            Toast.makeText(FclActivity.this, errors.get(0), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (DbException e) {
                    LogUtil.e(TAG, Log.getStackTraceString(e));
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                //在OrderActivity界面修改了装箱单信息
                if (resultCode == Constants.FLAG_CHANGED) {
                    orderCode=data.getStringExtra(Constants.FLAG_ORDER_CODE);
                    adapter.setOrderCode(orderCode);
                }

                break;
            default:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_search:
                final EditText et = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入产品编码")
                        .setView(et)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String content = et.getText().toString();
                                if(StringUtils.isEmpty(content))return;
                                content = content.toUpperCase();
                                List<DockStockDto> dockStocks=adapter.getDockStocks();
                                int index = -1;
                                for(DockStockDto dockStock : dockStocks){
                                    index++;
                                    if(dockStock.getScode().equals(content)){
                                        break;
                                    }
                                }
                                if(index>-1){
                                    mLvFcl.setSelection(index + 1);
                                }

                            }
                        }).setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.bt_save:
                try {
                    orderService.saveOrder(orderCode);
                    if (change == Constants.FLAG_CHANGED) {
                        //清空该装箱单
                        dockStockService.deleteDockStockByOcode(orderCode);
                        //将list 数据一行行的保存到数据库
                        for (DockStockDto dto : dsds) {
                            dockStockService.addDockStock(orderCode, dto.getDcode(), dto.getScode(), dto.getNum());
                        }
                    }
                    final MyWindow mw=new MyWindow(FclActivity.this);
                    mw.setTitle("温馨提示");
                    mw.setMessage("保存成功！是否提交到服务器?");
                    mw.setPositiveButton("是", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                OrderInfoParam info=new OrderInfoParam();
                                Order order=orderService.getByCode(orderCode);
                                info.setCode(orderCode);
                                info.setCcode(order.getCcode());
                                info.setUid(order.getUid());
                                info.setDate(order.getDate());
                                info.setType(Constants.FLAG_TYPE_FCL);
                                List<Dock> dockList=dockService.findDockByOcode(orderCode);
                                List<OrderInfoParam.DockInfo> dockInfos=new ArrayList<>();
                                for (Dock dock : dockList) {
                                    OrderInfoParam.DockInfo dockInfo=info.new DockInfo();
                                    dockInfo.setCode(dock.getCode());
                                    dockInfo.setDate(dock.getDate());
                                    List<OrderInfoParam.StockInfo> stockInfos=new ArrayList<>();
                                    List<DockStock> dockStocks=dockStockService.findDockStockByDcodeAndOcode(orderCode, dock.getCode());
                                    for (DockStock dockStock : dockStocks) {
                                        OrderInfoParam.StockInfo stockInfo=info.new StockInfo();
                                        stockInfo.setCode(dockStock.getScode());
                                        stockInfo.setNum(dockStock.getNum());
                                        stockInfos.add(stockInfo);
                                    }
                                    dockInfo.setStockInfos(stockInfos);
                                    dockInfos.add(dockInfo);
                                }
                                info.setDockInfos(dockInfos);
                                String datas=JSONObject.toJSONString(info);
                                Map<String, String> params=new HashMap<String, String>(1);
                                params.put("datas", datas);
                                showLoading();
                                Xutils.get().post(UrlContants.getInstance().getSubmitUrl(), params, new Xutils.XCallBack() {
                                    @Override
                                    public void onSuccessResponse(String response) {
                                        hideLoading();
                                        Result result=ParseUtil.get().getResult(response);
                                        if (result.getCode() == 0) {
                                            ToastUtils.showShort(FclActivity.this, "提交成功");
                                            try {
                                                orderService.submitOrder(orderCode);
                                            } catch (DbException e) {
                                                LogUtil.e(TAG, Log.getStackTraceString(e));
                                            }
                                            startActivity(new Intent(FclActivity.this, ListActivity.class));
                                            FclActivity.this.finish();
                                        } else {
                                            ToastUtils.showShort(FclActivity.this, result.getInfo());
                                        }
                                    }

                                    @Override
                                    public void onError(String message) {
                                        hideLoading();
                                        ToastUtils.showShort(FclActivity.this, message);
                                    }
                                });

                            } catch (DbException e) {
                                LogUtil.e(TAG, Log.getStackTraceString(e));
                            }

                            mw.dismiss();
                        }
                    });
                    mw.setNegativeButton("否", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mw.dismiss();
                        }
                    });
                    mw.show();
                } catch (DbException e) {
                    LogUtil.e(TAG, Log.getStackTraceString(e));
                }


                break;
            case R.id.tv_title:
                mEtBoxCode.setText(TestUtil.getRandom(datas));
                break;
        }
    }


    /**
     * 初始化数据
     */
    public void initData() {
        try {
            boolean sorted=false;
            Order order=orderService.getByCode(orderCode);
            if (order.getSave().equals(Constants.FLAG_SAVE)) {
                sorted=true;
            }
            dsds=dockStockService.findDockStockDto(orderCode, sorted);
            adapter.setDockStocks(dsds);
            mLvFcl.setSelection(adapter.getCount() - adapter.fillSize - 1);
        } catch (DbException e) {
            LogUtil.e(TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * 发生改变，保存时需要更新数据库
     */
    public void change() {
        change=Constants.FLAG_CHANGED;
        initData();
    }

    /**
     * 根据焦点长度重新为条码框获得焦点
     */
    public void requestBoxCodeFocus() {
        mEtBoxCode.postDelayed(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(FclActivity.this,"定位扫描位置...");
                mEtBoxCode.requestFocus();
                SoftInputUtil.hideSoftInput(FclActivity.this);
            }
        }, 100);
    }

    @Override
    protected void setListener() {
        mBtSave.setOnClickListener(this);
        mIvLeft.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);
        mTvTitle.setOnClickListener(this);
    }

}
