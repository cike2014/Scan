package com.jms.scan.util.debug;


import com.jms.scan.BuildConfig;

/**
 * log工具类：防止log信息泄露
 */
public class LogUtil {

	private static String defaultTag = "healthmanager";

	private LogUtil() {
	}

	public static void setTag(String tag) {
		defaultTag = tag;
	}

	/*********************** Log ***************************/
	public static int v(String tag, String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.v(tag, msg) : -1;
	}

	public static int d(String tag, String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.d(tag, msg) : -1;
	}

	public static int i(String tag, String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.i(tag, msg) : -1;
	}

	public static int w(String tag, String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.w(tag, msg) : -1;
	}

	public static int e(String tag, String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.e(tag, msg) : -1;
	}

	/*********************** Log with Throwable ***************************/
	public static int v(String tag, String msg, Throwable tr) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.v(tag, msg, tr) : -1;
	}

	public static int d(String tag, String msg, Throwable tr) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.d(tag, msg, tr) : -1;
	}

	public static int i(String tag, String msg, Throwable tr) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.i(tag, msg, tr) : -1;
	}

	public static int w(String tag, String msg, Throwable tr) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.w(tag, msg, tr) : -1;
	}

	public static int e(String tag, String msg, Throwable tr) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.e(tag, msg, tr) : -1;
	}

	/*********************** Log with defaultTag ***************************/
	public static int v(String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.v(defaultTag, buildMessage(msg)) : -1;
	}

	public static int d(String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.d(defaultTag, buildMessage(msg)) : -1;
	}

	public static int i(String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.i(defaultTag, buildMessage(msg)) : -1;
	}

	public static int e(String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.e(defaultTag, buildMessage(msg)) : -1;
	}

	public static int w(String msg) {
		return BuildConfig.DEBUG && msg != null ? android.util.Log.w(defaultTag, buildMessage(msg)) : -1;
	}

	protected static String buildMessage(String msg) {
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

		return new StringBuilder().append(caller.getClassName()).append(".").append(caller.getMethodName())
				.append("(): ").append(msg).toString();
	}
}
