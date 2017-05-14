package com.jms.scan.util.http;

import com.jms.scan.SysApplication;
import com.jms.scan.util.common.Constants;

import static com.jms.scan.util.setting.SettingUtils.getSharedPreferences;

/**
 * Created by alpha on 2017/1/11.
 */
public class UrlContants {

    /**登录url*/
    public static final String LOGIN_URL = getServerAddress()+"/app/login";

    /**退出url*/
    public static final String LOGOUT_URL = getServerAddress()+"/app/logout";

    public static String getServerAddress(){
        return getSharedPreferences(SysApplication.getInstance().getApplicationContext(), Constants.SERVER_ADDRESS, "");
    }
}
