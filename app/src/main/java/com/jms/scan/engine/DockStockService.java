package com.jms.scan.engine;

import android.database.Cursor;

import com.jms.scan.bean.Dock;
import com.jms.scan.bean.DockStock;
import com.jms.scan.bean.Stock;
import com.jms.scan.engine.base.BaseService;
import com.jms.scan.engine.util.ServiceFactory;
import com.jms.scan.param.DockStockDto;
import com.jms.scan.util.common.Constants;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class DockStockService extends BaseService {
    private DockService dockService;
    private StockService stockService;

    public DockStockService() {
        super();
        dockService=ServiceFactory.getInstance().getDockService();
        stockService=ServiceFactory.getInstance().getStockService();
    }

    /**
     * 增加箱子、产品关联，数量为1
     *
     * @param ocode
     * @param dcode
     * @param scode
     * @throws DbException
     */
    public void addDockStock(String ocode, String dcode, String scode) throws DbException {
        addDockStock(ocode, dcode, scode, 1);
    }

    /**
     * 增加箱子、产品关联，数量为num
     *
     * @param ocode
     * @param dcode
     * @param scode
     * @param num
     * @throws DbException
     */
    public void addDockStock(String ocode, String dcode, String scode, int num) throws DbException {
        DockStock dockStock=new DockStock();
        dockStock.setOcode(ocode);
        dockStock.setDcode(dcode);
        dockStock.setScode(scode);
        dockStock.setNum(num);
        db.save(dockStock);
    }

    /**
     * 根据箱子id，产品id获得箱子产品关联关系
     *
     * @param dcode
     * @param scode
     * @return
     * @throws DbException
     */
    public DockStock getDockStock(String ocode, String dcode, String scode) throws DbException {
        initDockStock();
        DockStock dockStock=db.selector(DockStock.class).where("ocode", "=", ocode).and("dcode", "=", dcode).and("scode", "=", scode).findFirst();
        return dockStock;
    }

    /**
     * 箱子产品关联，递增步数为1
     *
     * @param ocode
     * @param dcode
     * @param scode
     * @throws DbException
     */
    public void increase(String ocode, String dcode, String scode) throws DbException {
        increase(ocode, dcode, scode, 1);
    }

    /**
     * 修改箱子产品关联，递增部署为num
     *
     * @param dcode
     * @param scode
     * @param num
     * @throws DbException
     */
    public void increase(String ocode, String dcode, String scode, int num) throws DbException {
        db.executeUpdateDelete("UPDATE t_dock_stock SET num = num+" + num + " WHERE dcode = '" + dcode + "' AND scode = '" + scode + "' AND ocode = '" + ocode + "'");
    }

    public void update(String ocode, String dcode, String scode, int num) throws DbException {
        db.executeUpdateDelete("UPDATE t_dock_stock SET num = " + num + " WHERE dcode = '" + dcode + "' AND scode = '" + scode + "' AND ocode = '" + ocode + "'");
    }

    public void delete(String ocode, String dcode, String scode) throws DbException {
        db.executeUpdateDelete("DELETE FROM t_dock_stock WHERE ocode = '" + ocode + "' AND dcode = '" + dcode + "' AND scode = '" + scode + "'");
    }


    /**
     * 扫码成功后，处理整箱逻辑
     *
     * @param ocode
     * @param dcode
     * @param errors
     */
    public void executeFclScan(String ocode, String dcode, List<String> errors) throws DbException {
        Stock stock=stockService.getByBoxCode(dcode);
        if (stock == null) {
            errors.add("该条码不存在，请同步档案后重试");
            return;
        }
        Dock dock=dockService.get(ocode, dcode);
        if (dock == null) {
            dock=new Dock();
            dock.setCode(dcode);
            dock.setOcode(ocode);
            dock.setStatus(Constants.FLAG_UNSEAL);
            dock.setType(Constants.FLAG_TYPE_FCL);
            dock.setDate(System.currentTimeMillis());
            dockService.save(dock);
            dock=dockService.get(ocode, dcode);
        }
        DockStock dockStock=getDockStock(ocode, dcode, stock.getCode());
        if (dockStock == null) {
            addDockStock(ocode, dcode, stock.getCode(), stock.getRatio());
        } else {
            increase(ocode, dcode, stock.getCode(), stock.getRatio());
        }

    }

    /**
     * 扫码成功后处理拼箱逻辑
     *
     * @param ocode:装箱单号
     * @param dcode：箱码
     * @param barcode：产品条码
     * @param errors
     */
    public void excecuteMemoScan(String ocode, String dcode, String barcode, List<String> errors) throws DbException {
        Stock stock=stockService.getByBarCode(barcode);
        if (stock == null) {
            errors.add("该条码不存在，请同步档案后重试");
            return;
        }
        Dock dock=dockService.get(ocode, dcode);
        if (dock == null) {
            dock=new Dock();
            dock.setCode(dcode);
            dock.setDate(System.currentTimeMillis());
            dock.setStatus(Constants.FLAG_UNSEAL);
            dock.setType(Constants.FLAG_TYPE_MEMO);
            dock.setOcode(ocode);
            dockService.save(dock);
            dock=dockService.get(ocode, dcode);
        }
        DockStock dockStock=getDockStock(ocode, dcode, stock.getCode());
        if (dockStock == null) {
            addDockStock(ocode, dcode, stock.getCode());
        } else {
            increase(ocode, dcode, stock.getCode());
        }


    }

    /**
     * 根据装箱单号获得该箱内装箱与产品对应关系
     *
     * @param ocode
     * @return
     * @throws DbException
     */
    public List<DockStockDto> findDockStockDto(String ocode,boolean sorted) throws DbException {
        initDock();
        initDockStock();
        initOrder();
        StringBuilder sql=new StringBuilder("SELECT");
        sql.append(" d.code AS dcode,s.code AS scode,ds.num AS num,ds.ocode AS ocode,s.bar_code AS barCode");
        sql.append(" FROM t_dock_stock ds ");
        sql.append(" LEFT JOIN t_dock d ON  ds.dcode = d.code ");
        sql.append(" LEFT JOIN t_stock s ON ds.scode = s.code ");
        sql.append(" WHERE ds.ocode = '").append(ocode).append("'");
        if(sorted){
            sql.append(" ORDER BY d.code ASC,s.code ASC ");
        }
        Cursor cursor=db.execQuery(sql.toString());
        List<DockStockDto> dockStockDtoList=new ArrayList<>();
        while (cursor.moveToNext()) {
            DockStockDto dsd=new DockStockDto();
            dsd.setDcode(cursor.getString(cursor.getColumnIndex("dcode")));
            dsd.setScode(cursor.getString(cursor.getColumnIndex("scode")));
            dsd.setBarCode(cursor.getString(cursor.getColumnIndex("barCode")));
            dsd.setNum(cursor.getInt(cursor.getColumnIndex("num")));
            dsd.setOcode(cursor.getString(cursor.getColumnIndex("ocode")));
            if (dockStockDtoList.indexOf(dsd) == -1) {
                dockStockDtoList.add(dsd);
            }
        }
        return dockStockDtoList;
    }

    /**
     * 获得某装箱单中某装箱中产品数量，确定是否可以封箱
     *
     * @param ocode
     * @param dcode
     * @return
     */
    public int getOrderDockStockCount(String ocode, String dcode) throws DbException {
        initDock();
        StringBuilder sql=new StringBuilder("SELECT SUM(num)");
        sql.append(" FROM t_dock_stock WHERE ocode = '").append(ocode).append("' AND dcode = '").append(dcode).append("'");
        Cursor cursor=db.execQuery(sql.toString());
        while (cursor.moveToNext()) {
            return cursor.getInt(0);
        }
        return 0;
    }

    /**
     * 根据装箱单号删除装箱-产品关系
     *
     * @param ocode
     * @throws DbException
     */
    public void deleteDockStockByOcode(String ocode) throws DbException {
        StringBuilder sql=new StringBuilder("DELETE from t_dock_stock WHERE ocode = '" + ocode + "'");
        db.executeUpdateDelete(sql.toString());
    }

    /**
     * 根据装箱单号获得所有装箱
     * @param ocode
     * @return
     * @throws DbException
     */
    public List<DockStock> findDockStockByDcodeAndOcode(String ocode,String dcode) throws DbException{
        return db.selector(DockStock.class).where("ocode", "=", ocode).and("dcode","=",dcode).findAll();
    }

}
