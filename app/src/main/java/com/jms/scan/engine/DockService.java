package com.jms.scan.engine;

import android.database.Cursor;
import android.text.TextUtils;

import com.jms.scan.bean.Dock;
import com.jms.scan.engine.base.BaseService;
import com.jms.scan.util.common.Constants;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class DockService extends BaseService {

    private final static String FLAG_DOCK_CODE="APP_";

    /**
     * 根据装箱单号和装箱号确定一个装箱
     *
     * @param ocode
     * @param code
     * @return
     * @throws DbException
     */
    public Dock get(String ocode, String code) throws DbException {
        return db.selector(Dock.class).where("code", "=", code).and("ocode", "=", ocode).findFirst();
    }


    /**
     * 保存箱子
     *
     * @param dock
     * @throws DbException
     */
    public void save(Dock dock) throws DbException {
        db.save(dock);
    }

    /**
     * 获得某装箱单内可用的箱号（最大箱号已经封箱，创建新箱号；最大箱号没有封箱，返回该箱号）
     *
     * @return
     * @throws DbException
     */
    public String getDockCode(String ocode, int type) throws DbException {
        initDock();
        initOrder();
        StringBuilder sql=new StringBuilder("SELECT MAX(d.code) FROM t_dock d ");
        sql.append(" WHERE d.ocode = '").append(ocode).append("' AND d.type = ").append(type);
        Cursor cursor=db.execQuery(sql.toString());
        while (cursor.moveToNext()) {
            String maxCode=cursor.getString(0);
            int maxInt=0;
            if (!TextUtils.isEmpty(maxCode)) {
                Dock dock=get(ocode, maxCode);
                if (dock.getStatus() == 1) {
                    maxInt=Integer.parseInt(maxCode.split("_")[1]);
                    return FLAG_DOCK_CODE + String.format("%03d", maxInt + 1);
                } else {
                    return maxCode;
                }
            } else {
                return FLAG_DOCK_CODE + "001";
            }
        }
        return FLAG_DOCK_CODE + "001";
    }

    /**
     * 封箱
     */
    public void sealDock(String ocode, String dcode) throws DbException {
        db.executeUpdateDelete("UPDATE t_dock SET status = " + Constants.FLAG_SEAL + " WHERE code = '" + dcode + "' AND ocode = '" + ocode + "'");
    }

    /**
     * 根据装箱单号删除箱子
     *
     * @param ocode
     * @throws DbException
     */
    public void deleteDock(String ocode) throws DbException {
        StringBuilder sql=new StringBuilder("DELETE FROM t_dock WHERE ocode = '" + ocode + "'");
        db.executeUpdateDelete(sql.toString());
    }

    /**
     * 根据装箱单号获得所有装箱
     * @param ocode
     * @return
     * @throws DbException
     */
    public List<Dock> findDockByOcode(String ocode) throws DbException{
        return db.selector(Dock.class).where("ocode", "=", ocode).findAll();
    }
}
