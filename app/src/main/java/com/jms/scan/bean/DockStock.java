package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by alpha on 2017/1/12.
 */
@Table(name="t_dock_stock")
public class DockStock {

    @Column(name="id",isId = true,autoGen = true)
    private int id;
    @Column(name="did")
    private int did;
    @Column(name="sid")
    private int sid;
    @Column(name="num")
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did=did;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid=sid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num=num;
    }
}
