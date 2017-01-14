package com.jms.scan.util.common;

import android.content.Context;

/**
 * Created by alpha on 2017/1/11.
 */
public class ResourceUtil {

    public static String getResourceById(Context context, int id){
        return context.getResources().getString(id);
    }

    
}
