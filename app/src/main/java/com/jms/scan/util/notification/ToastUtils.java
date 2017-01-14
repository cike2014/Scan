package com.jms.scan.util.notification;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.text.MessageFormat;

/**
 * 展示通知帮助类 {@link Toast}
 * 
 * @date 2014年4月16日
 */
public class ToastUtils {

	private static Activity activity;

	private static void show(final Activity activity, final int resId, final int duration) {
		if (activity == null)
			return;

		final Context context = activity.getApplication();
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (ToastUtils.activity != activity) {
					// 解决Toast重复出现多次
					ToastUtils.activity = activity;
					Toast.makeText(context, resId, duration).show();
				}

			}
		});
	}

	private static void show(final Activity activity, final String message, final int duration) {
		if (activity == null)
			return;
		if (TextUtils.isEmpty(message))
			return;

		final Context context = activity.getApplication();
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// if(ToastUtils.activity != activity) {
				// // 解决Toast重复出现多次
				// ToastUtils.activity=activity;
				Toast.makeText(context, message, duration).show();
				// }
			}
		});
	}

	/**
	 * 长展示message{@link Toast}{@link Toast#LENGTH_LONG}
	 * 
	 * @param activity
	 * @param resId
	 */
	public static void showLong(final Activity activity, int resId) {
		show(activity, resId, LENGTH_LONG);
	}

	/**
	 * 短展示message{@link Toast}{@link Toast#LENGTH_SHORT}
	 * 
	 * @param activity
	 * @param resId
	 */
	public static void showShort(final Activity activity, final int resId) {
		show(activity, resId, LENGTH_SHORT);
	}

	/**
	 * 长展示message{@link Toast}{@link Toast#LENGTH_LONG}
	 * 
	 * @param activity
	 * @param message
	 */
	public static void showLong(final Activity activity, final String message) {
		show(activity, message, LENGTH_LONG);
	}

	/**
	 * 短展示message{@link Toast}{@link Toast#LENGTH_SHORT}
	 * 
	 * @param activity
	 * @param message
	 */
	public static void showShort(final Activity activity, final String message) {
		show(activity, message, LENGTH_SHORT);
	}

	/**
	 * 长展示message{@link Toast}{@link Toast#LENGTH_LONG}
	 * 
	 * @param activity
	 * @param message
	 * @param args
	 */
	public static void showLong(final Activity activity, final String message, final Object... args) {
		String formatted = MessageFormat.format(message, args);
		show(activity, formatted, LENGTH_LONG);
	}

	/**
	 * 短展示message{@link Toast}{@link Toast#LENGTH_SHORT}
	 * 
	 * @param activity
	 * @param message
	 * @param args
	 */
	public static void showShort(final Activity activity, final String message, final Object... args) {
		String formatted = MessageFormat.format(message, args);
		show(activity, formatted, LENGTH_SHORT);
	}

	/**
	 * 长展示message{@link Toast}{@link Toast#LENGTH_LONG}
	 * 
	 * @param activity
	 * @param resId
	 * @param args
	 */
	public static void showLong(final Activity activity, final int resId, final Object... args) {
		if (activity == null)
			return;

		String message = activity.getString(resId);
		showLong(activity, message, args);
	}

	/**
	 * 短展示message{@link Toast}{@link Toast#LENGTH_SHORT}
	 * 
	 * @param activity
	 * @param resId
	 * @param args
	 */
	public static void showShort(final Activity activity, final int resId, final Object... args) {
		if (activity == null)
			return;

		String message = activity.getString(resId);
		showShort(activity, message, args);
	}
}
