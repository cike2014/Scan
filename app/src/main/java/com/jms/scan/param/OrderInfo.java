package com.jms.scan.param;

import com.jms.scan.bean.Order;

/**
 * Created by alpha on 2017/5/9.
 * 订单信息
 */
public class OrderInfo {

    private Integer id;

    private String code;

    private Long date ;

    private Integer type;//1拼装箱 2整装箱

    private Integer save;//1 已经保存 2 未保存

    private Integer submit;//是否提交

    private Integer uid;//制单人id

    private String uname;//制单人

    private String cname;//客户姓名

    private String ccode;//客户编号

    public Order getOrder(){
        Order order = new Order();
        order.setId(this.getId());
        order.setUid(this.getUid());
        order.setCcode(this.getCcode());
        order.setCode(this.getCode());
        order.setDate(this.getDate());
        order.setSave(this.getSave());
        order.setType(this.getType());
        order.setSubmit(this.getSubmit());
        return order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date=date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type=type;
    }

    public Integer getSave() {
        return save;
    }

    public void setSave(Integer save) {
        this.save=save;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid=uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname=uname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname=cname;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode=ccode;
    }

    public Integer getSubmit() {
        return submit;
    }

    public void setSubmit(Integer submit) {
        this.submit=submit;
    }
}
