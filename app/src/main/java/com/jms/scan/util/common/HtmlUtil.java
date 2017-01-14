package com.jms.scan.util.common;

import android.text.TextUtils;

public class HtmlUtil {

	private static String getHtmlBody(String htmlContent) {
		return htmlContent.substring(htmlContent.indexOf("<body>") + 6, htmlContent.lastIndexOf("</body>"));
	}

	private static String getHtmlData(String bodyHTML) {
		String head = "<head>"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
				+ "<style>img{max-width: 100%; width:auto; height:auto;}</style>" + "</head>";
		return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
	}

	public static String autoMatchHtml(String html) {
		if (TextUtils.isEmpty(html)) {
			return "<div style='text-align:center;'>暂无内容</div>";
		}
		return getHtmlData(getHtmlBody(html));
	}
}
