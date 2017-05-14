package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by alpha on 2017/1/11.
 */
@Table(name="t_user")
public class User {

    @Column(name="uid", isId=true, autoGen=false)
    private Integer uid;

    @Column(name="user_id")
    private String userid;

    @Column(name="username")
    private String username;

    @Column(name="name")
    private String name;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid=uid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid=userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
}
