package com.jms.scan.engine;

import com.jms.scan.bean.Customer;
import com.jms.scan.engine.base.BaseService;
import com.jms.scan.param.CustomBean;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class CustomerService extends BaseService{

    public Customer get(int id) throws DbException{
        return db.findById(Customer.class,id);
    }

    public void saveUpdate(Customer customer) throws DbException {
        if(customer==get(customer.getId())){
            db.update(customer);
        }else{
            db.save(customer);
        }
    }

    public void saveUpdate(List<Customer> customerList) throws DbException{
        for(Customer customer:customerList){
            saveUpdate(customer);
        }
    }

    public List<CustomBean> listAllBeans() throws DbException{
        List<Customer> customers = db.findAll(Customer.class);
        List<CustomBean> beans = new ArrayList<>();
        for(Customer customer : customers){
            CustomBean bean = new CustomBean();
            bean.setId(customer.getId());
            bean.setCode(customer.getCode());
            bean.setInfo(customer.getName());
            beans.add(bean);
        }
        return beans;

    }


}
