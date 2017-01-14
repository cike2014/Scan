package com.jms.scan.util.http;

import com.jms.scan.SysApplication;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.setting.SettingUtils;

/**
 * Created by alpha on 2017/1/11.
 */
public class UrlContants {

    private static String SERVER_ADDRESS;

    static {
        SERVER_ADDRESS=SettingUtils.getSharedPreferences(SysApplication.getInstance().getApplicationContext(), Constants.SERVER_ADDRESS, "");
    }

    /**登录url*/
    public static final String LOGIN_URL = SERVER_ADDRESS+"/app/login";
}
