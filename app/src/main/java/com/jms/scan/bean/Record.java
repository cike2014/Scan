package com.jms.scan.bean;

import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class Record {

    private User user;

    private List<Box> boxes;

    private List<Stock> stocks;

    private List<Customer> customers;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes=boxes;
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

    @Override
    public String toString() {
        return "Record{" +
                "user=" + user +
                ", boxes=" + boxes +
                ", stocks=" + stocks +
                ", customers=" + customers +
                '}';
    }
}
