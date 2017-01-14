package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by alpha on 2017/1/12.
 */
@Table(name="t_stock")
public class Stock implements Serializable {

    @Column(name="id",isId = true,autoGen = false)
    private int id;
    @Column(name="code")
    private String code; // 产品编码
    @Column(name="name")
    private String name;// 产品名称
    @Column(name="spec")
    private String spec;// 规格
    @Column(name="tid")
    private int tid;// 产品类别id
    @Column(name="barcode")
    private String barcode;// 基本单位|条码
    @Column(name="statud")
    private int status;// 1：启用 2：停用

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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec=spec;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid=tid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode=barcode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", spec='" + spec + '\'' +
                ", tid=" + tid +
                ", barcode='" + barcode + '\'' +
                ", status=" + status +
                '}';
    }
}
