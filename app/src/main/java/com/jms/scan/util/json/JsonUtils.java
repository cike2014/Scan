package com.jms.scan.util.json;

import android.text.TextUtils;

public class JsonUtils<T> {
	
	public enum JsonType {
		JSON_TYPE_OBJECT, JSON_TYPE_ARRAY, JSON_TYPE_ERROR;
	}


	/**获取Json字符串的类型*/
	public static JsonType getJsonType(String json) {
		if (TextUtils.isEmpty(json)) {
			return JsonType.JSON_TYPE_ERROR;
		}

		final char[] strChar = json.substring(0, 1).toCharArray();
		final char firstChar = strChar[0];

		if (firstChar == '{') {
			return JsonType.JSON_TYPE_OBJECT;
		} else if (firstChar == '[') {
			return JsonType.JSON_TYPE_ARRAY;
		} else {
			return JsonType.JSON_TYPE_ERROR;
		}
	}
}
