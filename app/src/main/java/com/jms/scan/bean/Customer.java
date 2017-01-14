package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

@Table(name="t_customer")
public class Customer implements Serializable {


    @Column(name="id",isId = true,autoGen = false)
    private int id;
    /**
     * 单位编码
     */
    @Column(name="code")
    private String code;
    /**
     * 单位名称
     */
    @Column(name="name")
    private String name;
    /**
     * 状态 1 ： 启用 2 ： 停用
     */
    @Column(name="statuc")
    private int status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
