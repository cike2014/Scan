package com.jms.scan.param;

/**
 * 装箱——产品数据传输层
 */
public class DockStockDto {


    private String dcode;

    private String scode;

    private String ocode;

    private String barCode;

    private int num;

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode=dcode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode=barCode;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode=scode;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num=num;
    }

    public String getOcode() {
        return ocode;
    }

    public void setOcode(String ocode) {
        this.ocode=ocode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DockStockDto that=(DockStockDto) o;

        if (dcode != null ? !dcode.equals(that.dcode) : that.dcode != null)
            return false;
        if (scode != null ? !scode.equals(that.scode) : that.scode != null)
            return false;
        return ocode != null ? ocode.equals(that.ocode) : that.ocode == null;

    }

    @Override
    public int hashCode() {
        int result=dcode != null ? dcode.hashCode() : 0;
        result=31 * result + (scode != null ? scode.hashCode() : 0);
        result=31 * result + (ocode != null ? ocode.hashCode() : 0);
        return result;
    }
}
