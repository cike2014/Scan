package com.jms.scan.param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alpha on 2017/1/12.
 */
public class Result<T> {

    private List<T> dataList = new ArrayList<T>();

    private T dataObj;

    private String status;

    private String info;

    private int code;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList=dataList;
    }

    public T getDataObj() {
        return dataObj;
    }

    public void setDataObj(T dataObj) {
        this.dataObj=dataObj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info=info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code=code;
    }

    @Override
    public String toString() {
        return "Result{" +
                "dataList=" + dataList +
                ", dataObj=" + dataObj +
                ", status='" + status + '\'' +
                ", info='" + info + '\'' +
                ", code=" + code +
                '}';
    }
}
