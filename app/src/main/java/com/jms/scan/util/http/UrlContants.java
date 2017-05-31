package com.jms.scan.util.http;

import com.jms.scan.SysApplication;
import com.jms.scan.util.common.Constants;

import static com.jms.scan.util.setting.SettingUtils.getSharedPreferences;

/**
 * Created by alpha on 2017/1/11.
 */
public class UrlContants {

    private static UrlContants urlContants;

    public static UrlContants getInstance() {
        if (urlContants == null) {
            urlContants=new UrlContants();
        }
        return urlContants;
    }

    public String getLoginUrl() {
        return getServerAddress() + "/app/login";
    }


    public String getLogoutUrl() {
        return getServerAddress() + "/app/logout";
    }

    public String getSubmitUrl() {
        return getServerAddress() + "/app/submit";
    }


    public String getServerAddress() {
        return getSharedPreferences(SysApplication.getInstance().getApplicationContext(), Constants.SERVER_ADDRESS, "");
    }
}
