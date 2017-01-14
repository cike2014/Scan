package com.jms.scan.engine;

import android.database.Cursor;
import android.os.SystemClock;
import android.text.TextUtils;

import com.jms.scan.DataCenter;
import com.jms.scan.bean.Box;
import com.jms.scan.bean.Dock;
import com.jms.scan.bean.DockStock;
import com.jms.scan.bean.Stock;
import com.jms.scan.dto.DockStockDto;
import com.jms.scan.util.common.Constants;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class DockStockService {

    private DbManager db;

    private final static String FLAG_DOCK_CODE="APP_";

    public DockStockService() {
        db=x.getDb(DataCenter.get().getDaoConfig());
    }

    /**
     * 获得未封箱可用箱号
     *
     * @return
     * @throws DbException
     */
    public String getDockCode() throws DbException {
        TableEntity<?> table=db.getTable(Dock.class);
        if (!isTableExist(table.getName())) {
            db.save(new Dock());
            db.executeUpdateDelete("DELETE FROM t_dock");
        }
        Cursor cursor=db.execQuery("SELECT MAX(code) FROM t_dock WHERE type = "+ Constants.FLAG_MEMO);
        while (cursor.moveToNext()) {
            String maxcode=cursor.getString(0);
            int maxInt=0;
            if (!TextUtils.isEmpty(maxcode)) {
                Dock dock=getDockByCode(maxcode);
                if (dock.getStatus() == 1) {
                    maxInt=Integer.parseInt(maxcode.split("_")[1]);
                    return FLAG_DOCK_CODE + String.format("%03d", maxInt + 1);
                } else {
                    return maxcode;
                }
            } else {
                return FLAG_DOCK_CODE + "001";
            }
        }
        return FLAG_DOCK_CODE + "001";
    }

    public boolean isTableExist(String tableName) {
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

    /**
     * 根据条码获得产品
     *
     * @param barcode
     * @return
     * @throws DbException
     */
    public Stock getStockByBarcode(String barcode) throws DbException {
        Stock stock=db.selector(Stock.class).where("barcode", "=", barcode).findFirst();
        return stock;
    }

    public Dock getDockByCode(String dockcode) throws DbException {
        Dock dock=db.selector(Dock.class).where("code", "=", dockcode).findFirst();
        return dock;
    }

    public void saveDock(Dock dock) throws DbException {
        db.save(dock);
    }

    public void addDockStock(int did,int sid) throws DbException{
        addDockStock(did,sid,1);
    }

    public void addDockStock(int did, int sid,int num) throws DbException {
        DockStock dockStock=new DockStock();
        dockStock.setDid(did);
        dockStock.setSid(sid);
        dockStock.setNum(num);
        db.save(dockStock);
    }

    public DockStock getDockStock(int did, int sid) throws DbException {
        TableEntity<?> table=db.getTable(DockStock.class);
        if (!isTableExist(table.getName())) {
            db.save(new DockStock());
            db.executeUpdateDelete("DELETE FROM t_dock_stock");
        }
        DockStock dockStock=db.selector(DockStock.class).where("did", "=", did).and("sid", "=", sid).findFirst();
        return dockStock;
    }

    public void increase(int did, int sid) throws DbException {
        increase(did, sid, 1);
    }

    public void increase(int did, int sid, int num) throws DbException {
        db.executeUpdateDelete("UPDATE t_dock_stock SET num = num+" + num + " WHERE did = " + did + " AND sid = " + sid);
    }

    public void update(int did,int sid,int num) throws DbException{
        db.executeUpdateDelete("UPDATE t_dock_stock SET num = " + num + " WHERE did = " + did + " AND sid = " + sid);
    }

    public void delete(int did,int sid) throws DbException{
        db.executeUpdateDelete("DELETE FROM t_dock_stock WHERE did = "+did + " AND sid = "+sid);
    }
    /**
     * 封箱
     */
    public void updateDock(String dockCode) throws DbException {
        db.executeUpdateDelete("UPDATE t_dock SET status = 1 WHERE code = '" + dockCode + "'");
    }

    public Box getBoxByCode(String code) throws DbException {
        Box box=db.selector(Box.class).where("code", "=", code).findFirst();
        return box;
    }

    /**
     * 扫码成功后，处理整箱逻辑
     * @param dockcode
     * @param errors
     */
    public void execute(String dockcode, List<String> errors) {
        try {
            Box box=getBoxByCode(dockcode);
            if (box == null) {
                errors.add("该条码不存在，请同步档案后重试");
                return;
            }
            Dock dock=getDockByCode(dockcode);
            if (dock == null) {
                dock=new Dock();
                dock.setCode(dockcode);
                dock.setStatus(1);
                dock.setType(2);
                dock.setDate(SystemClock.currentThreadTimeMillis());
                saveDock(dock);
                dock=getDockByCode(dockcode);
            }

            DockStock dockStock=getDockStock(dock.getId(), box.getSid());
            if (dockStock == null) {
                addDockStock(dock.getId(), box.getSid(),box.getNum());
            } else {
                increase(dock.getId(),box.getSid(),box.getNum());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫码成功后处理拼箱逻辑
     *
     * @param dockcode：箱码
     * @param barcode：产品条码
     * @param errors
     */
    public void execute(String dockcode, String barcode, List<String> errors) {
        try {
            Stock stock=getStockByBarcode(barcode);
            if (stock == null) {
                errors.add("该条码不存在，请同步档案后重试");
                return;
            }
            Dock dock=getDockByCode(dockcode);
            if (dock == null) {
                dock=new Dock();
                dock.setCode(dockcode);
                dock.setDate(SystemClock.currentThreadTimeMillis());
                dock.setStatus(2);
                dock.setType(1);
                saveDock(dock);
                dock=getDockByCode(dockcode);
            }
            DockStock dockStock=getDockStock(dock.getId(), stock.getId());
            if (dockStock == null) {
                addDockStock(dock.getId(), stock.getId());
            } else {
                increase(dock.getId(), stock.getId());
            }
        } catch (DbException ex) {

        }

    }

    /**
     * 查看箱子-产品对应关系
     * @param type 1：拼装箱 2：整装箱
     * @return
     * @throws DbException
     */
    public List<DockStockDto> findDockStockDto(int type) throws DbException {
        StringBuilder sql=new StringBuilder("SELECT d.id AS did,s.id AS sid ,d.code AS dcode,s.code AS scode,s.barcode AS barcode,s.name AS sname,ds.num AS num ");
        sql.append(" FROM t_dock_stock ds ");
        sql.append(" LEFT JOIN t_dock d ON  ds.did = d.id ");
        sql.append(" LEFT JOIN t_stock s ON ds.sid = s.id ");
        sql.append(" WHERE d.type = "+type);
        Cursor cursor=db.execQuery(sql.toString());
        List<DockStockDto> dsds=new ArrayList<DockStockDto>();
        while (cursor.moveToNext()) {
            DockStockDto dsd=new DockStockDto();
            dsd.setDid(cursor.getInt(cursor.getColumnIndex("did")));
            dsd.setSid(cursor.getInt(cursor.getColumnIndex("sid")));
            dsd.setDcode(cursor.getString(cursor.getColumnIndex("dcode")));
            dsd.setScode(cursor.getString(cursor.getColumnIndex("scode")));
            dsd.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
            dsd.setSname(cursor.getString(cursor.getColumnIndex("sname")));
            dsd.setNum(cursor.getInt(cursor.getColumnIndex("num")));
            dsds.add(dsd);
        }
        return dsds;
    }

}
