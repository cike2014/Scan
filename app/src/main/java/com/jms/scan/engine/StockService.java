package com.jms.scan.engine;

import com.jms.scan.bean.Stock;
import com.jms.scan.engine.base.BaseService;
import com.jms.scan.param.CustomBean;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class StockService extends BaseService {

    /***
     * 查询所有货品集合
     * @return
     */
    public List<Stock> listAll() throws DbException{
        return db.findAll(Stock.class);
    }

    /**
     * 查询所有货品名称
     * @return
     */
    public List<CustomBean> listAllBeans() throws DbException {
        List<Stock> stocks=listAll();
        List<CustomBean> beans = new ArrayList<>();
        for(Stock stock:stocks){
            CustomBean bean = new CustomBean();
            bean.setId(stock.getId());
            bean.setCode(stock.getCode());//编号
            bean.setInfo(stock.getBarCode());//条码
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 根据id查询货品
     * @param id
     * @return
     */
    public Stock get(int id) throws DbException{
        return db.findById(Stock.class,id);
    }

    /**
     * 保存或者更新单个货品
     * @param stock
     */
    public void saveUpdate(Stock stock) throws DbException{
        if(stock==get(stock.getId())){
            db.update(stock);
        }else{
            db.save(stock);
        }
    }

    /**
     * 保存或者更新多个货品
     * @param stockList
     */
    public void saveUpdate(List<Stock> stockList) throws DbException {
        for(Stock stock:stockList){
            saveUpdate(stock);
        }
    }

    /**
     * 根据条码获得产品
     *
     * @param barCode
     * @return
     * @throws DbException
     */
    public Stock getByBarCode(String barCode) throws DbException {
        Stock stock=db.selector(Stock.class).where("bar_code", "=", barCode).findFirst();
        return stock;
    }

    /**
     * 根据箱码获得产品
     * @param boxCode
     * @return
     * @throws DbException
     */
    public Stock getByBoxCode(String boxCode) throws DbException {
        Stock stock=db.selector(Stock.class).where("box_code", "=", boxCode).findFirst();
        return stock;
    }


}
