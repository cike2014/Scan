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
    @Column(name="box_code")
    private String boxCode;//箱码
    @Column(name="bar_code")
    private String barCode;// 基本单位|条码
    @Column(name="status")
    private int status;// 1：启用 2：停用
    @Column(name="ratio")
    private Integer ratio;//装箱数
    @Column(name="volume")
    private Double volume;//体积
    @Column(name="mass")
    private Double mass;//质量
    @Column(name="alloc")
    private String alloc;//货位


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

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode=boxCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode=barCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio=ratio;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume=volume;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass=mass;
    }

    public String getAlloc() {
        return alloc;
    }

    public void setAlloc(String alloc) {
        this.alloc=alloc;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", boxCode='" + boxCode + '\'' +
                ", barCode='" + barCode + '\'' +
                ", status=" + status +
                ", ratio=" + ratio +
                ", volume=" + volume +
                ", mass=" + mass +
                ", alloc='" + alloc + '\'' +
                '}';
    }
}
