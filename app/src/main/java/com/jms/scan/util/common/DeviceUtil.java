package com.jms.scan.util.common;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by alpha on 2017/5/4.
 */
public class DeviceUtil {

    public static Context context;

    public DeviceUtil(Context context) {
        this.context=context;
    }

    public static String getUnique() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
