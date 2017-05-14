package com.jms.scan.engine;

import com.jms.scan.bean.User;
import com.jms.scan.engine.base.BaseService;

import org.xutils.ex.DbException;

/**
 * Created by alpha on 2017/1/12.
 */
public class UserService extends BaseService{


    public User get(int id) throws DbException{
        return db.findById(User.class, id);
    }

    public void saveOrUpdate(User user) throws DbException {
        if(get(user.getUid())!=null){
            deleteUser(user.getUid());
        }
        db.save(user);
    }

    public void deleteUser(int id) throws DbException{
        db.deleteById(User.class,id);
    }
}
