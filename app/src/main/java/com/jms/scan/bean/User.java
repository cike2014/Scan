package com.jms.scan.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by alpha on 2017/1/11.
 */
@Table(name="t_user")
public class User {

    @Column(name = "uid",isId = true,autoGen = false)
    private int uid;

    @Column(name="user_id")
    private String userid;

    @Column(name = "username")
    private String username;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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



}
