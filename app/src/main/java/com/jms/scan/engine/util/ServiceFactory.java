package com.jms.scan.engine.util;

import com.jms.scan.engine.CustomerService;
import com.jms.scan.engine.DockService;
import com.jms.scan.engine.DockStockService;
import com.jms.scan.engine.OrderDockService;
import com.jms.scan.engine.OrderService;
import com.jms.scan.engine.StockService;
import com.jms.scan.engine.UserService;

/**
 * Created by alpha on 2017/5/8.
 */
public class ServiceFactory {
    private static ServiceFactory instance;

    private UserService userService;
    private DockService dockService;
    private OrderService orderService;
    private DockStockService dockStockService;
    private StockService stockService;
    private CustomerService customerService;
    private OrderDockService orderDockService;

    private ServiceFactory(){

    }

    public static ServiceFactory getInstance(){
        if(instance==null){
            instance = new ServiceFactory();
        }
        return instance;
    }

    public UserService getUserService(){
        if(userService==null){
            userService = new UserService();
        }
        return userService;
    }

    public DockService getDockService(){
        if(dockService==null){
            dockService = new DockService();
        }
        return dockService;
    }

    public OrderService getOrderService(){
        if(orderService==null){
            orderService = new OrderService();
        }
        return orderService;
    }

    public DockStockService getDockStockService(){
        if(dockStockService==null){
            dockStockService = new DockStockService();
        }
        return dockStockService;
    }

    public StockService getStockService(){
        if(stockService==null){
            stockService = new StockService();
        }
        return stockService;
    }

    public CustomerService getCustomerService(){
        if(customerService==null){
            customerService = new CustomerService();
        }
        return customerService;
    }


    public OrderDockService getOrderDockService(){
        if(orderDockService==null){
            orderDockService = new OrderDockService();
        }
        return orderDockService;
    }


}
