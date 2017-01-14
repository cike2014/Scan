package com.jms.scan.util.common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jms.scan.AppInfo;
import com.jms.scan.util.debug.LogUtil;

import java.io.File;
import java.util.Hashtable;


public class Utils {

	public static File initProductDir(AppInfo info) {
		String userDir = "";
		if (SdcardUtil.isSdCardExists()) {
			userDir = Environment.getExternalStorageDirectory().toString() + System.getProperty("file.separator");
		} else {
			userDir = System.getProperty("file.separator");
		}
		File file = new File(userDir + info.getProductName() + System.getProperty("file.separator") + ".data"
				+ System.getProperty("file.separator"));
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public static File initTempDir(AppInfo info) {
		String userDir = "";
		if (SdcardUtil.isSdCardExists()) {
			userDir = Environment.getExternalStorageDirectory().toString() + System.getProperty("file.separator");
		} else {
			userDir = System.getProperty("file.separator");
		}
		File file = new File(userDir + info.getProductName() + System.getProperty("file.separator") + "temp"
				+ System.getProperty("file.separator"));
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView,int height) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null ) { 
                return; 
        } 
        
        Log.e("", "listAdapter.getCount() = " + listAdapter.getCount());
  
        int totalHeight = 0; 
        int tmp = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { 
                View listItem = listAdapter.getView(i, null, listView); 
                listItem.measure(0, 0); 
                totalHeight += listItem.getMeasuredHeight();   
                tmp = listItem.getMeasuredHeight();
        } 
        totalHeight += height;
        ViewGroup.LayoutParams params = listView.getLayoutParams(); 
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
        listView.setLayoutParams(params);
    }

	public static Hashtable mtables;

	public static void setTables(Hashtable tables) {
		mtables = tables;
	}

	public static Hashtable getTables() {
		return mtables;
	}
	public static String getRealname(String name) {
		LogUtil.d("name ", name);
		String ret = "";
		if (mtables == null) {
			ret = null;
		} else {
			ret = (String) mtables.get(name);
		}
		if (ret == null) {
			return "";
		}
		return ret;
	}
	public static int getLayout(Context context, String name) {
		int layout = context.getResources().getIdentifier(name + "ll", "layout", context.getPackageName());
		if (layout == 0) {
			layout = context.getResources().getIdentifier(name, "layout", context.getPackageName());
		}
		return layout;
	}

	/**
	 * 
	 * <p>
	 * 男: kcal/day = 5 + 10*(体重kg) + 6.25*(身高厘米)– 5* (年龄) 女: kcal/day = -161 + 10* (体重) + 6.25* (身高)
	 * – 5 *(年龄)
	 * </p>
	 * 
	 * @author liuyujian
	 * @date 2015年10月22日 下午5:58:26
	 * @param sex
	 * @param weight
	 * @param height
	 * @param age
	 * @return
	 */
	public static int getBasicMetabolism(int sex, int weight, int height, int age) {
		int result = 0;
		if (2 == sex) {// 女
			result = (int) (10 * weight + 6.25 * height - 5 * age - 161);
		} else {
			result = (int) (10 * weight + 6.25 * height - 5 * age + 5);
		}
		if (result <= 0) {
			return 0;
		}
		return result;
	}
}
