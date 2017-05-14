package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 装箱单
 */
@Table(name="t_order")
public class Order {

    @Column(name = "id",isId = true,autoGen = true)
    private Integer id;

    @Column(name="code")
    private String code;

    @Column(name="date")
    private Long date ;

    @Column(name="type")
    private Integer type;//1拼装箱 2整装箱

    @Column(name="save")
    private Integer save;//1 已经保存 2 未保存

    @Column(name="uid")
    private Integer uid;//制单人id

    @Column(name="ccode")
    private String ccode;//客户编号

    @Column(name="submit")
    private Integer subit;//1 已经提交 2 未提交

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

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode=ccode;
    }

    public Integer getSubit() {
        return subit;
    }

    public void setSubit(Integer subit) {
        this.subit=subit;
    }
}
