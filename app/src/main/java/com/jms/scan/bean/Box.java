package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by alpha on 2017/1/12.
 */
@Table(name="t_box")
public class Box implements Serializable {

    @Column(name="id",isId = true,autoGen = false)
    private int id;
    @Column(name="code")
    private String code;// 箱码
    @Column(name="num")
    private int num;// 数量
    @Column(name="sid")
    private int sid;// 产品id
    @Column(name="type")
    private int type;// 1：整箱单箱码 2：拼箱单箱码
    @Column(name="status")
    private int status;//1：启用 2：停用

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num=num;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid=sid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type=type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    @Override
    public String toString() {
        return "Box{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", num=" + num +
                ", sid=" + sid +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
