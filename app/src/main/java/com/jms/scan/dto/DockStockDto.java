package com.jms.scan.dto;

/**
 * 装箱——产品数据传输层
 */
public class DockStockDto {

    private int sid;

    private int did;

    private String dcode;

    private String barcode;

    private String scode;

    private String sname;

    private int num;

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode=dcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode=barcode;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname=sname;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num=num;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode=scode;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid=sid;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did=did;
    }
}
