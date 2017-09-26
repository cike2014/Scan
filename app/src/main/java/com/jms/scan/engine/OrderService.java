package com.jms.scan.engine;

import android.content.Context;
import android.database.Cursor;

import com.jms.scan.bean.Order;
import com.jms.scan.engine.base.BaseService;
import com.jms.scan.param.OrderInfo;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.common.DateUtils;
import com.jms.scan.util.common.StringUtils;
import com.jms.scan.util.setting.SettingUtils;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/5/8.
 */
public class OrderService extends BaseService {

    private static final String TAG=OrderService.class.getSimpleName();

    public String getCode(int type, Context context) throws DbException {
        initOrder();
        long today=DateUtils.getToday();
        long tomorrow=DateUtils.getTommrow();
        String maxCode="";
        String sql="SELECT MAX(code) FROM t_order WHERE type = " + type + " AND date > " + today + " AND date <" + tomorrow;
        final Cursor cursor=db.execQuery(sql);
        while (cursor.moveToNext()) {
            maxCode=cursor.getString(0);
        }
        String todayStr=DateUtils.getDate(today, "yyyyMMdd");
        String deviceCode=SettingUtils.getSharedPreferences(context, Constants.CLIENT_FLAG, "");
        String flag=(type == 1 ? "P" : "Z");
        if (!StringUtils.isEmpty(maxCode)) {
            maxCode=maxCode.substring(11);
            int i=Integer.parseInt(maxCode);
            String ucode=String.format("%03d", i + 1);
            return deviceCode + flag + todayStr + ucode;
        } else {
            return deviceCode + flag + todayStr + "001";
        }

    }

    /**
     * 根据装箱单号获得装箱单信息
     *
     * @param ocode
     * @return
     * @throws DbException
     */
    public OrderInfo getOrderInfoByOcode(String ocode) throws DbException {
        initOrder();
        StringBuilder sql=new StringBuilder("SELECT ");
        sql.append("o.id,o.code,o.date,o.type,o.save,o.uid,u.name AS uname,c.name AS cname,o.ccode AS ccode ");
        sql.append("FROM t_order o LEFT JOIN t_user u ON o.uid = u.uid ");
        sql.append("LEFT JOIN t_customer c ON o.ccode = c.code WHERE o.code = '");
        sql.append(ocode).append("'");
        Cursor cursor=db.execQuery(sql.toString());
        OrderInfo info=null;
        while (cursor.moveToNext()) {
            info=new OrderInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setCode(cursor.getString(cursor.getColumnIndex("code")));
            info.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
            info.setSave(cursor.getInt(cursor.getColumnIndex("save")));
            info.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
            info.setUname(cursor.getString(cursor.getColumnIndex("uname")));
            info.setCname(cursor.getString(cursor.getColumnIndex("cname")));
            info.setCcode(cursor.getString(cursor.getColumnIndex("ccode")));
        }
        return info;
    }

    /**
     * 根据制单人查询所有装箱单
     *
     * @param uid
     * @return
     * @throws DbException
     */
    public List<OrderInfo> getOrderInfosByUid(int uid) throws DbException {
        initOrder();
        StringBuilder sql=new StringBuilder("SELECT ");
        sql.append("o.id,o.code,o.date,o.type,o.save,o.submit,o.uid,u.name AS uname,c.name AS cname,o.ccode AS ccode ");
        sql.append("FROM t_order o LEFT JOIN t_user u ON o.uid = u.uid ");
        sql.append("LEFT JOIN t_customer c ON o.ccode = c.code WHERE o.uid = ").append(uid).append(" ORDER BY o.date DESC ");
        Cursor cursor=db.execQuery(sql.toString());
        List<OrderInfo> infoList=new ArrayList<>();
        while (cursor.moveToNext()) {
            OrderInfo info=new OrderInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setCode(cursor.getString(cursor.getColumnIndex("code")));
            info.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
            info.setSave(cursor.getInt(cursor.getColumnIndex("save")));
            info.setSubmit(cursor.getInt(cursor.getColumnIndex("submit")));
            info.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
            info.setUname(cursor.getString(cursor.getColumnIndex("uname")));
            info.setCname(cursor.getString(cursor.getColumnIndex("cname")));
            info.setCcode(cursor.getString(cursor.getColumnIndex("ccode")));
            infoList.add(info);
        }
        return infoList;
    }

    /**
     * 保存装箱单
     *
     * @param order
     * @throws DbException
     */
    public void save(Order order) throws DbException {
        db.save(order);
    }

    /**
     * 根据装箱单号获得装箱单
     *
     * @param ocode
     * @return
     * @throws DbException
     */
    public Order getByCode(String ocode) throws DbException {
        return db.selector(Order.class).where("code", "=", ocode).findFirst();
    }

    /**
     * 根据装箱单号修改客户
     *
     * @param order
     * @throws DbException
     */
    public void updateCustomer(Order order) throws DbException {
        db.executeUpdateDelete("UPDATE t_order SET ccode = '" +
                order.getCcode() + "' WHERE code = '" + order.getCode() + "'");
    }

    /**
     * 根据单号将装箱单客户编号改成ccode
     *
     * @param ocode
     * @param ccode
     * @throws DbException
     */
    public void updateCustomer(String ocode, String ccode) throws DbException {
        db.executeUpdateDelete("UPDATE t_order SET ccode = '" +
                ccode + "' WHERE code = '" + ocode + "'");
    }

    /**
     * 根据单号将装箱单改成保存状态
     *
     * @param ocode
     * @throws DbException
     */
    public void saveOrder(String ocode) throws DbException {
        db.executeUpdateDelete("UPDATE t_order SET save = " + Constants.FLAG_SAVE + " WHERE code = '" + ocode + "'");
    }


    /**
     * 根据单号提交装箱单
     * @param ocode
     * @throws DbException
     */
    public void submitOrder(String ocode) throws DbException{
        db.executeUpdateDelete("UPDATE t_order SET submit = "+Constants.FLAG_SUBMIT+" WHERE code = '"+ocode+"'");
    }

    /**
     * 根据单号删除装箱单
     * @param ocode
     * @throws DbException
     */
    public void deleteByCode(String ocode) throws DbException{
        db.executeUpdateDelete("DELETE FROM t_order WHERE code = '"+ocode+"'");
    }



}
