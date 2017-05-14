package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by alpha on 2017/1/12.
 */
@Table(name="t_dock_stock")
public class DockStock implements Serializable{

    @Column(name="id",isId = true,autoGen = true)
    private int id;
    //装箱号
    @Column(name="dcode")
    private String dcode;
    //产品编号
    @Column(name="scode")
    private String scode;
    //装箱单号
    @Column(name="ocode")
    private String ocode;
    //数量
    @Column(name="num")
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode=dcode;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode=scode;
    }

    public String getOcode() {
        return ocode;
    }

    public void setOcode(String ocode) {
        this.ocode=ocode;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num=num;
    }
}
