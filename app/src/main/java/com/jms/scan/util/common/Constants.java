package com.jms.scan.util.common;

public class Constants {


    /**
     * SharedPreference常量
     */
    public static final String UID="uid";
    public static final String LOGIN_TIME="login_time";
    public static final String SERVER_ADDRESS="server_address";
    public static final String CLIENT_FLAG = "client_flag";


    /**
     * 业务常量
     */
    public static final long SESSION_TIME=1 * 24 * 60 * 60 * 1000;
    public static final String SAVE_FILE_PATH=SdcardUtil.getRootPath() + "/image/";

    public static final String DB_NAME="ScanClient.db";
    public static final int DB_VERSION=1;

    public static final String FLAG_TYPE="flag_type";//类型标识
    public static final int FLAG_TYPE_MEMO= 1;//拼装箱常量
    public static final int FLAG_TYPE_FCL= 2;//整装箱常量

    public static final int FLAG_UNSEAL = 2;//没有封箱
    public static final int FLAG_SEAL = 1;//已经封箱

    public static final int FLAG_UNSAVE = 2;//没有保存
    public static final int FLAG_SAVE = 1;//已经保存

    public static final int FLAG_UNSUBMIT = 2;//没有提交
    public static final int FLAG_SUBMIT = 1;//已经提交



    public static final String FLAG_STATUS = "flag_status";//状态标识
    public static final int FLAG_STATUS_ADD= 1;//新增标识
    public static final int FLAG_STATUS_EDIT= 2;//编辑标识

    public static final String FLAG_ORDER_CODE = "orderCode";//装箱单单号

    public static final String FLAG = "flag";

    public static String STATUS_Y = "y";
    public static String STATUS_N = "n";

    public static final int FLAG_CHANGED = 1;
    public static final int FLAG_NOT_CHANGED = 2;

    public static final String KEY_CUSTOMER = "key_customer";
    public static final String KEY_STOCK = "key_stock";


}
