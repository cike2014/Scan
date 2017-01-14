package com.jms.scan.util.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 计步信息单独存储到xml文件中
 * 
 * @author liuyj
 *
 */
public class PedometerSettingUtils {
	private static SharedPreferences sharedPreferences = null;

	private static Editor editor = null;
	// 会自动加上xml后缀
	private static String xmlname = "PEDOMETER";

	private PedometerSettingUtils() {

	}

	private static SharedPreferences getSharedPreferencesObject(Context context) {
		if (sharedPreferences == null)
			sharedPreferences = context.getSharedPreferences(xmlname, Context.MODE_PRIVATE);
		return sharedPreferences;
	}

	private static Editor getEditorObject(Context context) {
		if (editor == null)
			editor = context.getSharedPreferences(xmlname, Context.MODE_PRIVATE).edit();
		return editor;
	}

	public static int getSharedPreferences(Context context, String paramString, int paramInt) {
		return getSharedPreferencesObject(context).getInt(paramString, paramInt);
	}

	public static long getSharedPreferences(Context context, String paramString, long paramLong) {
		return getSharedPreferencesObject(context).getLong(paramString, paramLong);
	}

	public static Boolean getSharedPreferences(Context context, String paramString, Boolean paramBoolean) {
		return getSharedPreferencesObject(context).getBoolean(paramString, paramBoolean);
	}

	public static String getSharedPreferences(Context context, String paramString1, String paramString2) {
		return getSharedPreferencesObject(context).getString(paramString1, paramString2);
	}

	public static void setEditor(Context context, String paramString, int paramInt) {
		getEditorObject(context).putInt(paramString, paramInt).commit();
	}

	public static void setEditor(Context context, String paramString, long paramLong) {
		getEditorObject(context).putLong(paramString, paramLong).commit();
	}

	public static void setEditor(Context context, String paramString, Boolean paramBoolean) {
		getEditorObject(context).putBoolean(paramString, paramBoolean).commit();
	}

	public static void setEditor(Context context, String paramString1, String paramString2) {
		getEditorObject(context).putString(paramString1, paramString2).commit();
	}

	// Delete
	public static void remove(Context context, String key) {
		getEditorObject(context).remove(key).commit();
	}

	public static void clear(Context context) {
		getEditorObject(context).clear().commit();
	}
}
