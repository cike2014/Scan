package com.jms.scan.param;

import com.jms.scan.bean.Customer;
import com.jms.scan.bean.Stock;
import com.jms.scan.bean.User;

import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class Record {

    private User user;

    private String flag;

    private List<Stock> stocks;

    private List<Customer> customers;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }


    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks=stocks;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers=customers;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag=flag;
    }

    @Override
    public String toString() {
        return "Record{" +
                "user=" + user +
                ", stocks=" + stocks +
                ", customers=" + customers +
                '}';
    }
}
