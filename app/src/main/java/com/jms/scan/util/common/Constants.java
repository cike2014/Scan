package com.jms.scan.util.common;

public class Constants {


    /**
     * SharedPreference常量
     */
    public static final String UID="uid";
    public static final String LOGIN_TIME="login_time";
    public static final String SERVER_ADDRESS="server_address";


    /**
     * 业务常量
     */
    public static final long SESSION_TIME=1 * 24 * 60 * 60 * 1000;
    public static final String SAVE_FILE_PATH=SdcardUtil.getRootPath() + "/image/";

    public static final String DB_NAME="ScanClient.db";
    public static final int DB_VERSION=1;

    public static final int FLAG_MEMO = 1;//拼装箱常量
    public static final int FLAG_FCL = 2;//整装箱常量
    public static final String FLAG = "flag";


}
