package com.jms.scan.util.common;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SoftInputUtil {
	/**
	 * 显示软键盘
	 * 
	 * @param context
	 * @param view
	 * 
	 */
	public static void showInputMethod(final Context context, final View view) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(view, 0);
			}
		}, 500);
	}

	/**
	 * @param context
	 * @since 隐藏输入法
	 */
	public static void hideInputMethod(Activity context) {
		try {
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
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

	/**
	 * 关闭已经显示的输入法窗口。
	 * 
	 * @param context
	 *            上下文对象，一般为Activity
	 * @param focusingView
	 *            输入法所在焦点的View
	 */
	public static void closeSoftInput(Context context, View focusingView) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(focusingView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	/**
	 * 使UI适配输入法
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void adjustSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}
}
