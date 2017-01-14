package com.jms.scan.util.common;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.jms.scan.SysApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtils {

	private static Toast mToast;
	public static String sRegPhoneNumber = "^((13[0-9])|(15[0-9])|(14[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
	public static String sRegMatcherNumber = "^[A-Za-z0-9]+$";
	public static String sRegEx = "[`~!@#$%^&*()+=\\-\\s*|\t|\r|\n|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
	public static String sRegEx1 = "\\+|(?<=\\d)-|\\*|/|&|=|(>=)|(<=)";
	public static String sRegexMarcherURL = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
			+ "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
			+ "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
			+ "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
			+ "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
			+ "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
			+ "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
			+ "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
	public static String sRegNumber = "^[0-9]+.?[0-9]*$";

	private static void showToast(String str) {
		if (mToast == null) {
			mToast = Toast.makeText(SysApplication.getInstance(), str, Toast.LENGTH_LONG);
		}
		mToast.setText(str);
		mToast.show();
	}

	/** 判断字符串是否为数字 */
	public static boolean checkNumberRules(String inputNmber) {
		if (TextUtils.isEmpty(inputNmber)) {
			return false;
		}
		Pattern pattern = Pattern.compile(sRegNumber);
		Matcher isNum = pattern.matcher(inputNmber);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean checkPasswordRules(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword)) {
			showToast("密码不能为空");
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9A-Za-z]{6,20}");
		Matcher isNum = pattern.matcher(inputPassword);
		if (!isNum.matches()) {
			showToast("密码由6~20位数字或字母构成");
			return false;
		}
		return true;
	}

	public static boolean checkConfirmPasswordRules(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword)) {
			showToast("确认密码不能为空");
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9A-Za-z]{6,20}");
		Matcher isNum = pattern.matcher(inputPassword);
		if (!isNum.matches()) {
			showToast("确认密码由6~20位数字或字母构成");
			return false;
		}
		return true;
	}

	public static boolean checkPhoneRules(String phone) {
		if (TextUtils.isEmpty(phone)) {
			showToast("手机号不能为空");
			return false;
		}
		Pattern pattern = Pattern.compile(sRegPhoneNumber);
		Matcher isNum = pattern.matcher(phone);
		if (!isNum.matches()) {
			showToast("请输入正确的手机号");
			return false;
		}
		return true;
	}

	// 判断两个输入框密码是否一致
	public static boolean isPwdAgreement(EditText edt, EditText edtConfirm) {
		if (isContentEmpty(edt) || isContentEmpty(edtConfirm)) {
			return false;
		}
		if (!edt.getText().toString().equals(edtConfirm.getText().toString())) {
			return false;
		}
		return true;
	}

	private static boolean isContentEmpty(EditText edt) {
		return TextUtils.isEmpty(edt.getText().toString());
	}

	// 检查两次输入是否一致
	public static boolean checkInputEquals(String pwd, String pwdConfirm) {
		if (!checkPasswordRules(pwd)) {
			return false;
		}
		if (!checkConfirmPasswordRules(pwdConfirm)) {
			return false;
		}
		if (!pwd.equals(pwdConfirm)) {
			showToast("两次输入的密码不一致");
			return false;
		}
		return true;
	}

	public static boolean checkSms(String sms) {
		if (TextUtils.isEmpty(sms)) {
			showToast("验证码不能为空");
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]{6}");
		Matcher isNum = pattern.matcher(sms);
		if (!isNum.matches()) {
			showToast("请输入正确的验证码");
			return false;
		}
		return true;
	}

	// 二维码校验
	public static boolean checkQRCode(String qrcode) {
		if (TextUtils.isEmpty(qrcode)) {
			showToast("请输入数字和字母");
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9A-Za-z]+");
		Matcher isNum = pattern.matcher(qrcode);
		if (!isNum.matches()) {
			showToast("请输入数字和字母");
			return false;
		}
		return true;
	}

	public static boolean checkURL(String url) {
		if (TextUtils.isEmpty(url)) {
			return false;
		}
		Pattern pattern = Pattern.compile(sRegexMarcherURL);
		Matcher isURL = pattern.matcher(url);
		if (!isURL.matches()) {
			return false;
		}
		return true;
	}

}
