package com.jms.scan.ui;

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

import com.jms.scan.R;
import com.jms.scan.adapter.ItemAdapter;
import com.jms.scan.bean.Stock;
import com.jms.scan.engine.DockStockService;
import com.jms.scan.engine.OrderService;
import com.jms.scan.engine.StockService;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.DockStockDto;
import com.jms.scan.ui.base.BaseActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.ResourceUtil;
import com.jms.scan.util.common.StringUtils;
import com.jms.scan.util.common.TestUtil;
import com.jms.scan.util.debug.LogUtil;
import com.jms.scan.util.notification.ToastUtils;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_fcl)
public class FclActivity extends BaseActivity {

    @ViewInject(R.id.iv_left)
    private ImageView mIvLeft;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.iv_right)
    private ImageView mIvRight;

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
    }

    @Override
    protected void getData() {
        orderService=ServiceFactory.getInstance().getOrderService();
        stockService=ServiceFactory.getInstance().getStockService();
        dockStockService=ServiceFactory.getInstance().getDockStockService();
        orderCode=getIntent().getStringExtra(Constants.FLAG_ORDER_CODE);

        mLvFcl.addHeaderView(View.inflate(this, R.layout.item_top, null));
        adapter=new ItemAdapter(this);
        adapter.setDsds(dsds);
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
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result=mEtBoxCode.getText().toString();
                if ("".equals(result.trim())) {
                    return;
                }
                try {
                    Stock stock=stockService.getByBoxCode(result);
                    if (stock == null) {
                        Toast.makeText(FclActivity.this, R.string.scan_box_notexist, Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.bt_save:
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
            dsds=dockStockService.findDockStockDto(orderCode);
            adapter.setDsds(dsds);
            mLvFcl.setSelection(dsds.size() - adapter.fillSize - 1);
        } catch (DbException e) {
            LogUtil.e(TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * 发生改变，保存时需要更新数据库
     */
    public void change() {
        change=Constants.FLAG_CHANGED;
    }


    @Override
    protected void setListener() {
        mBtSave.setOnClickListener(this);
        mIvLeft.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mTvTitle.setOnClickListener(this);
    }

}
