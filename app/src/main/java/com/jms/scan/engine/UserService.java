package com.jms.scan.engine;

import com.jms.scan.DataCenter;
import com.jms.scan.bean.User;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by alpha on 2017/1/12.
 */
public class UserService {
    private DbManager db;

    public UserService() {
        db=x.getDb(DataCenter.get().getDaoConfig());
    }

    public User get(int id)  {
        try {
            return db.findById(User.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveOrUpdate(User user) {
        if(user==get(user.getUid())){
            try {
                db.update(user);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else{
            try {
                db.save(user);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
}
