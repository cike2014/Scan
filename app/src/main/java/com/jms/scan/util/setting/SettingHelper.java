package com.jms.scan.util.setting;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class SettingHelper {

	private SettingHelper() {

	}

	/**
	 * 是否第一次启动应用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFirstStart(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			int curVersion = info.versionCode;
			int lastVersion = SettingUtils.getSharedPreferences(context, "version", 0);
			if (curVersion > lastVersion) {
				// 如果当前版本大于上次版本，该版本属于第一次启动
				// 将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
				return true;
			} else {
				return false;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 是否第一次安装应用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFirstInstall(Context context) {
		boolean install = SettingUtils.getSharedPreferences(context, "first_install", false);
		return install;
	}

	/**
	 * 应用已启动
	 * 
	 * @param context
	 */
	public static void setStarted(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			int curVersion = info.versionCode;
			SettingUtils.setEditor(context, "version", curVersion);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 应用已安装并启动
	 * 
	 * @param context
	 */
	public static void setInstalled(Context context) {
		SettingUtils.setEditor(context, "first_install", true);
	}

}
