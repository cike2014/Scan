package com.jms.scan.engine;

import com.jms.scan.DataCenter;
import com.jms.scan.bean.Box;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class BoxService {
    private DbManager db ;
    public BoxService(){
        db=x.getDb(DataCenter.get().getDaoConfig());
    }

    public Box get(int id){
        try {
            return db.findById(Box.class,id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUpdate(Box box){
        if(box==get(box.getId())){
            try {
                db.update(box);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else{
            try {
                db.save(box);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveUpdate(List<Box> boxList){
        for(Box box:boxList){
            saveUpdate(box);
        }
    }

}
