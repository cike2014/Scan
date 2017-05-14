package com.jms.scan.engine.base;

import android.database.Cursor;

import com.jms.scan.DataCenter;
import com.jms.scan.bean.Dock;
import com.jms.scan.bean.DockStock;
import com.jms.scan.bean.Order;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by alpha on 2017/5/8.
 */
public class BaseService {

    protected DbManager db;

    public BaseService() {
        db=x.getDb(DataCenter.get().getDaoConfig());
    }

    /**
     * 判断表名是否存在
     *
     * @param tableName
     * @return
     */
    public boolean isTableExist(DbManager db, String tableName) {
        try {
            Cursor cursor=db.execQuery("select count(*) from `sqlite_master` where type = 'table' and tbl_name = '" + tableName + "'");
            while (cursor.moveToNext()) {
                return cursor.getInt(0) > 0;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void initDock() throws DbException{
        TableEntity<?> table=db.getTable(Dock.class);
        if (!isTableExist(db, table.getName())) {
            db.save(new Dock());
            db.executeUpdateDelete("DELETE FROM t_dock");
        }
    }

    protected void initDockStock() throws DbException{
        TableEntity<?> table=db.getTable(DockStock.class);
        if (!isTableExist(db, table.getName())) {
            db.save(new DockStock());
            db.executeUpdateDelete("DELETE FROM t_dock_stock");
        }
    }

    protected void initOrder() throws DbException{
        TableEntity<?> table=db.getTable(Order.class);
        if (!isTableExist(db, table.getName())) {
            db.save(new Order());
            db.executeUpdateDelete("DELETE FROM t_order");
        }
    }



}
