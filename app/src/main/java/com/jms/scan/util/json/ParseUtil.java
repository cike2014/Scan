package com.jms.scan.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jms.scan.param.Result;

/**
 * Created by alpha on 2017/1/12.
 */
public class ParseUtil {

    public static ParseUtil util;
    public static ParseUtil get(){
        if(util==null){
            util = new ParseUtil();
        }
        return util;
    }


    public Result getResult(String result, Class clazz) {
        Result r=new Result();
        JSONObject jsonObject=JSON.parseObject(result);
        r.setCode(jsonObject.getIntValue("code"));
        r.setStatus(jsonObject.getString("status"));
        r.setInfo(jsonObject.getString("info"));
        String data=jsonObject.getString("data");
        if (JsonUtils.JsonType.JSON_TYPE_OBJECT == JsonUtils.getJsonType(data)) {
            r.setDataObj(JSON.parseObject(data, clazz));
        }
        if (JsonUtils.JsonType.JSON_TYPE_ARRAY == JsonUtils.getJsonType(data)) {
            r.setDataList(JSON.parseArray(data, clazz));
        }
        return r;
    }

    public Result getResult(String result){
        Result r=new Result();
        JSONObject jsonObject=JSON.parseObject(result);
        r.setCode(jsonObject.getIntValue("code"));
        r.setStatus(jsonObject.getString("status"));
        r.setInfo(jsonObject.getString("info"));
        return r;
    }
}
