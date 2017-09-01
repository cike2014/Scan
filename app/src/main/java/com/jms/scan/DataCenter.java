package com.jms.scan;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jms.scan.util.common.ACache;
import com.jms.scan.util.common.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.xutils.DbManager;

public class DataCenter {
    private static DataCenter instance;
    private SysApplication mApplication;
    private AppInfo appInfo;
    private DbManager.DaoConfig daoConfig;

    private String deviceToken;
    public static ImageLoader IMAGE_LOADER;
    private static DisplayImageOptions IMAGE_OPTIONS;
    private static ACache aCache;

    private static final String TAG = DataCenter.class.getSimpleName();

    private DataCenter(SysApplication mApplication) {
        this.mApplication=mApplication;
    }

    public  ACache getaCache(){
        if(aCache==null){
            aCache = ACache.get(mApplication.getBaseContext());
        }
        return aCache;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    public static void init(SysApplication hccApplication) {
        if (instance == null) {
            synchronized (DataCenter.class) {
                if (instance == null) {
                    instance=new DataCenter(hccApplication);
                    instance.init();
                }
            }
        }
    }

    private void init() {
        this.appInfo=new AppInfo(mApplication);
        initImageLoader();
        initDb();
    }

    private void initDb() {
        daoConfig=new DbManager.DaoConfig()
                .setDbName(Constants.DB_NAME)
                .setDbVersion(Constants.DB_VERSION)
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                });
    }

    private void initImageLoader() {
        UnlimitedDiscCache productCache=new UnlimitedDiscCache(appInfo.getProductPath());
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(mApplication)
                .defaultDisplayImageOptions(IMAGE_OPTIONS).diskCache(productCache).build();
        IMAGE_LOADER=ImageLoader.getInstance();
        IMAGE_LOADER.init(config);
    }

    public static void loadImage(String uri, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        if (null == IMAGE_OPTIONS) {
            IMAGE_OPTIONS=new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();
        }
        IMAGE_LOADER.displayImage(uri, imageView, IMAGE_OPTIONS, imageLoadingListener);
    }


    public void clearCache() {
        ImageLoader.getInstance().clearDiskCache();
    }

    public static DataCenter get() {
        if (instance == null) {
            throw new RuntimeException("Please invoke DataCenter.init(application) before use this!");
        }
        return instance;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo=appInfo;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken=deviceToken;
    }



}
