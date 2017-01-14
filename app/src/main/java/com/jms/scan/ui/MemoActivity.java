package com.jms.scan.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jms.scan.R;
import com.jms.scan.adapter.ItemMemoAdapter;
import com.jms.scan.dto.DockStockDto;
import com.jms.scan.engine.DockStockService;
import com.jms.scan.ui.base.BaseActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.ResourceUtil;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_memo)
public class MemoActivity extends BaseActivity {

    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.srl_memos)
    private SwipeRefreshLayout mSrlMemos;
    @ViewInject(R.id.lv_memos)
    private ListView mLvMemos;
    @ViewInject(R.id.bt_scan)
    private Button mBtScan;
    @ViewInject(R.id.bt_submit)
    private Button mBtSubmit;
    @ViewInject(R.id.bt_seal)
    private Button mBtSeal;
    @ViewInject(R.id.iv_left)
    private ImageView mIvLeft;


    private DockStockService dockStockService;
    private ItemMemoAdapter adapter;
    private List<DockStockDto> dsds=new ArrayList<DockStockDto>();
    String dockcode;

    @Override
    protected void injectView() {
        x.view().inject(this);
        mIvLeft.setVisibility(View.VISIBLE);
        mTvTitle.setText(ResourceUtil.getResourceById(this, R.string.label_memo));
    }

    @Override
    protected void getData() {
        mLvMemos.addHeaderView(View.inflate(this, R.layout.item_memo, null));
        adapter=new ItemMemoAdapter(this);
        adapter.setDsds(dsds);
        mLvMemos.setAdapter(adapter);
        dockStockService=new DockStockService();
        try {
            dockcode=dockStockService.getDockCode();
        } catch (DbException e) {
            e.printStackTrace();
        }
        mSrlMemos.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSrlMemos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mSrlMemos.setRefreshing(false);
                    }
                }, 6000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.bt_scan:
                Intent intent=new Intent();
                intent.setClass(MemoActivity.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("dockCode", dockcode);
                intent.putExtra(Constants.FLAG, Constants.FLAG_MEMO);
                startActivity(intent);
                break;
            case R.id.bt_seal:
                try {
                    dockStockService.updateDock(dockcode);
                    Toast.makeText(this, R.string.label_seal_success, Toast.LENGTH_SHORT).show();
                    dockcode=dockStockService.getDockCode();
                } catch (DbException e) {
                    Toast.makeText(this, R.string.label_seal_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_submit:

                break;
        }
    }

    public void reloadData(){
        try {
            dsds=dockStockService.findDockStockDto(Constants.FLAG_MEMO);
            adapter.setDsds(dsds);
            mLvMemos.setSelection(dsds.size() - 1);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    @Override
    protected void setListener() {
        mBtScan.setOnClickListener(this);
        mBtSubmit.setOnClickListener(this);
        mBtSeal.setOnClickListener(this);
        mIvLeft.setOnClickListener(this);
    }


}
