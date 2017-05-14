package com.jms.scan.param;

/**
 * Created by alpha on 2017/5/8.
 */
public class CustomBean {

    private int id;

    private String code;//对应产品编号 客户编号

    private String info;//对应产品条码、客户名称等

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info=info;
    }
}
