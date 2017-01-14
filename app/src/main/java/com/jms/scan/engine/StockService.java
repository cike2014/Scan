package com.jms.scan.engine;

import com.jms.scan.DataCenter;
import com.jms.scan.bean.Stock;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class StockService {
    private DbManager db ;
    public StockService(){
        db=x.getDb(DataCenter.get().getDaoConfig());
    }

    public Stock get(int id){
        try {
            return db.findById(Stock.class,id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUpdate(Stock stock){
        if(stock==get(stock.getId())){
            try {
                db.update(stock);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else{
            try {
                db.save(stock);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveUpdate(List<Stock> stockList){
        for(Stock stock:stockList){
            saveUpdate(stock);
        }
    }

}
