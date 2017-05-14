package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 装箱表
 */
@Table(name="t_dock")
public class Dock {
    @Column(name="id", isId=true, autoGen=true)
    private int id;
    @Column(name="code")
    private String code;
    @Column(name="type")
    private int type;//1 拼装箱 2 整装箱
    @Column(name="date")
    private Long date;//装箱时间
    @Column(name="status")
    private int status;// 1 已经封箱2还未封箱
    @Column(name="ocode")
    private String ocode;//订单号

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type=type;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date=date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public String getOcode() {
        return ocode;
    }

    public void setOcode(String ocode) {
        this.ocode=ocode;
    }
}
