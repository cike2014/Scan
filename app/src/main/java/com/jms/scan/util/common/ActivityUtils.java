package com.jms.scan.util.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.jms.scan.R;
import com.jms.scan.SysApplication;

/**
 * Activity通用帮助类
 * 
 * @date 2014年4月16日
 */
public class ActivityUtils {


	public static int[] getScreenSize() {
		int[] screens;
		DisplayMetrics dm = new DisplayMetrics();
		dm = SysApplication.getInstance().getResources().getDisplayMetrics();
		screens = new int[] { dm.widthPixels, dm.heightPixels };
		return screens;
	}

	/**
	 * 切换全屏状态。
	 * 
	 * @param activity
	 *            Activity
	 * @param isFull
	 *            设置为true则全屏，否则非全屏
	 */
	public static void toggleFullScreen(Activity activity, boolean isFull) {
		hideTitleBar(activity);
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (isFull) {
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	/**
	 * 设置为全屏
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void setFullScreen(Activity activity) {
		toggleFullScreen(activity, true);
	}

	/**
	 * 隐藏Activity的系统默认标题栏
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void hideTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 强制设置Actiity的显示方向为垂直方向。
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void setScreenVertical(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 强制设置Activity的显示方向为横向。
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void setScreenHorizontal(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * activity切换动画
	 * 
	 * @param m
	 * @param isEnd
	 */
	@SuppressLint("NewApi")
	public static void runActivityAnim(Activity m, boolean isEnd) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
			if (isEnd) {
				m.overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
			} else {
				m.overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
			}
		}
	}

	/**
	 * 快捷方式是否存在
	 * 
	 * @return
	 */
	public static boolean ifAddShortCut(Context context) {
		boolean isInstallShortCut = false;
		ContentResolver cr = context.getContentResolver();
		String authority = "com.android.launcher2.settings";
		Uri uri = Uri.parse("content://" + authority + "/favorites?notify=true");
		Cursor c = cr.query(uri, new String[] { "title", "iconResource" }, "title=?",
				new String[] { context.getString(R.string.app_name) }, null);
		if (null != c && c.getCount() > 0) {
			isInstallShortCut = true;
		}
		return isInstallShortCut;
	}

	public static int getScreenHeight(Activity paramActivity) {
		Display display = paramActivity.getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static int getScreenWidth(Activity paramActivity) {
		Display display = paramActivity.getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		return metrics.widthPixels;
	}

	/**
	 * 获取系统状态栏高度
	 * 
	 * @param activity
	 *            Activity
	 * @return 状态栏高度
	 */
	public static int getStatusBarHeight(Activity activity) {
		Rect localRect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		return localRect.top;

	}

	public static int getActionBarHeight(Activity paramActivity) {
		int contentViewTop = paramActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

		int[] attrs = new int[] { android.R.attr.actionBarSize };
		TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
		return ta.getDimensionPixelSize(0, DisplayUtil.dip2px(paramActivity, 48));
	}

	// below status bar,include actionbar, above softkeyboard
	public static int getAppHeight(Activity paramActivity) {
		Rect localRect = new Rect();
		paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		return localRect.height();
	}

	/**
	 * 隐藏软件输入法
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void hideSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
