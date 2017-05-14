package com.jms.scan.util.common;

/**
 * Created by alpha on 2017/5/10.
 */
public class TestUtil {

    /**
     * 获得指定数组随机元素
     * @param datas
     * @return
     */
    public static String getRandom(String[] datas){
        int index = (int) (Math.random() * datas.length);
        return  datas[index];
    }
}
