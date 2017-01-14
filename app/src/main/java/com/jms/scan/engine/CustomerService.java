package com.jms.scan.engine;

import com.jms.scan.DataCenter;
import com.jms.scan.bean.Customer;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class CustomerService {
    private DbManager db ;
    public CustomerService(){
        db=x.getDb(DataCenter.get().getDaoConfig());
    }

    public Customer get(int id){
        try {
            return db.findById(Customer.class,id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUpdate(Customer customer){
        if(customer==get(customer.getId())){
            try {
                db.update(customer);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else{
            try {
                db.save(customer);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveUpdate(List<Customer> customerList){
        for(Customer customer:customerList){
            saveUpdate(customer);
        }
    }


}
