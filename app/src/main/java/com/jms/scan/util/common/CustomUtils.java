package com.jms.scan.util.common;

import android.text.TextUtils;
import android.widget.EditText;

public class CustomUtils {
	/**
	 * 在URL中获得文件的名称
	 * 
	 * @param fileUrl
	 * @return
	 */
	public static String getDocTitle(String fileUrl) {
		return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
	}

	/**
	 * 根据文件后缀判断文件是否是图片类型
	 * 
	 * @param fileUrl
	 * @return
	 */
	public static boolean isImage(String fileUrl) {
		String imageSuffixs[] = new String[] { "BMP", "JPG", "JPEG", "PNG", "GIF" };
		String suffix = FileTypeUtils.getSuffix(fileUrl);

		for (int i = 0; i < imageSuffixs.length; i++) {
			if (imageSuffixs[i].toLowerCase().equals(suffix))
				return true;
		}
		return false;
	}

	/**
	 * 判断EditText是否为空
	 * 
	 * @param et
	 * @return
	 */
	public static boolean isEmpty(EditText et) {
		return TextUtils.isEmpty(et.getText().toString().trim());
	}

}
