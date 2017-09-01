package com.jms.scan.util.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jms.scan.DataCenter;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.CustomBean;
import com.jms.scan.util.debug.LogUtil;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by alpha on 2017/8/29.
 */
public class DataUtil {

    private static final String TAG = DataUtil.class.getSimpleName();

    private static DataUtil instance;

    private DataUtil(){

    }

    public static DataUtil getInstance(){
        if(instance==null){
            instance = new DataUtil();
        }
        return instance;
    }

    public List<CustomBean> getAllCustomers(){
        try {
            ACache aCache=DataCenter.get().getaCache();
            String customerJson = aCache.getAsString(Constants.KEY_CUSTOMER);
            if(StringUtils.isEmpty(customerJson)){
                List<CustomBean> customers=ServiceFactory.getInstance().getCustomerService().listAllBeans();
                customerJson = JSON.toJSONString(customers);
                aCache.put(Constants.KEY_CUSTOMER,customerJson,ACache.TIME_HOUR);
            }
            return JSON.parseArray(customerJson,CustomBean.class);
        } catch (DbException e) {
            LogUtil.e(TAG,e.getLocalizedMessage());
            return Lists.newArrayList();
        }
    }

    public List<CustomBean> getAllStocks(){
        try {
            ACache aCache=DataCenter.get().getaCache();
            String stockJson = aCache.getAsString(Constants.KEY_STOCK);
            if(StringUtils.isEmpty(stockJson)){
                List<CustomBean> stocks=ServiceFactory.getInstance().getStockService().listAllBeans();
                stockJson = JSON.toJSONString(stocks);
                aCache.put(Constants.KEY_STOCK,stockJson,ACache.TIME_HOUR);
            }
            return JSON.parseArray(stockJson,CustomBean.class);
        } catch (DbException e) {
            LogUtil.e(TAG,e.getLocalizedMessage());
            return Lists.newArrayList();
        }
    }


}
