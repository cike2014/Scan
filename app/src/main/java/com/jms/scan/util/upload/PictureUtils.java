package com.jms.scan.util.upload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jms.scan.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/** 图片上传工具类 */
public class PictureUtils {

	private Activity activity;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int GALLERY_REQUEST_CODE = 2;
	private static final int CROP_REQUEST_CODE = 3;
	private static PictureUtils sInstance;
	private TextView tv_camera;
	private TextView tv_gallery;
	private TextView tv_cancel;
	private LinearLayout ll;

	private PictureUtils() {
	}

	public static PictureUtils newInstance() {
		if (sInstance == null) {
			synchronized (PictureUtils.class) {
				if (sInstance == null) {
					sInstance = new PictureUtils();
				}
			}
		}
		return sInstance;
	}

	/** 图片进行Base64编码 */
	public String encodeBase64(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
		byte[] bytes = stream.toByteArray();
		String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
		return img;
	}

	/** 图片进行Base64解码 */
	public Bitmap decodeBase64(String bitmap) {
		byte[] buf = Base64.decode(bitmap, Base64.DEFAULT);
		Bitmap bm = BitmapFactory.decodeByteArray(buf, 0, buf.length);
		return bm;
	}

	/** 显示图片上传对话框进行图片上传 */
	@SuppressLint("InflateParams")
	public void showUploadDialog(final Activity activity) {
		this.activity = activity;
		final Dialog customDialog = new Dialog(activity, R.style.flashmode_DialogTheme);

		setDialogLayout();

		// 相机
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
				customDialog.dismiss();
			}

		});
		// 相册
		tv_gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				activity.startActivityForResult(intent, GALLERY_REQUEST_CODE);
				customDialog.dismiss();
			}
		});

		// 取消
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customDialog.dismiss();
			}

		});

		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customDialog.setContentView(ll);

		Window window = customDialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.getDecorView().setPadding(0, 0, 0, 0);

		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);

		customDialog.setCanceledOnTouchOutside(true);
		customDialog.setCancelable(true);

		if (activity instanceof Activity) {
			Activity mActivity = activity;
			if (!mActivity.isFinishing()) {
				customDialog.show();
			}
		}

	}

	/** 裁剪需要上传的图片 ，在onActivityResult()中使用 */
	public Bitmap handlePictureData(int requestCode, Intent data) {
		if (requestCode == CAMERA_REQUEST_CODE) {// 相机返回结果
			if (data == null) {
				return null;
			} else {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bm = extras.getParcelable("data");
					Uri uri = saveBitmap(bm);
					startImageZoom(uri);
				}
			}
		} else if (requestCode == GALLERY_REQUEST_CODE) {// 图库返回结果
			if (data == null) {
				return null;
			}
			Uri uri;
			uri = data.getData();
			Uri fileUri = convertUri(uri);
			startImageZoom(fileUri);
		} else if (requestCode == CROP_REQUEST_CODE) {// 裁剪返回结果
			if (data == null) {
				return null;
			}
			Bundle extras = data.getExtras();
			if (extras == null) {
				return null;
			}
			Bitmap bm = extras.getParcelable("data");
			return bm;
		}
		return null;
	}

	/** 保存图片，返回存储路径 */
	public Uri saveBitmap(Bitmap bm) {
		File tmpDir = new File(Environment.getExternalStorageDirectory() + "/upload");
		Log.e("SD卡路径", tmpDir.getAbsolutePath());
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}

		File img = new File(tmpDir.getAbsolutePath(), "portrait.png");
		try {
			FileOutputStream fos = new FileOutputStream(img);
			// 进行图片压缩，压缩率85%
			bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 压缩图片，size标识缩放比例 */
	@SuppressWarnings("deprecation")
	private Bitmap compressPic(Uri uri) {
		try {
			InputStream input = activity.getContentResolver().openInputStream(uri);
			BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
			onlyBoundsOptions.inJustDecodeBounds = true;
			onlyBoundsOptions.inDither = true;// optional
			onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_4444;// optional
			BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
			input.close();
			if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
				return null;

			// 获取图片分别率
			int picwidth = onlyBoundsOptions.outWidth;
			int picheight = onlyBoundsOptions.outHeight;
			// 获取手机分辨率
			WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
			Display disp = wm.getDefaultDisplay();
			// disp.getSize(outSize);高版本方法
			int swidth = disp.getWidth();
			int sheight = disp.getHeight();
			// 计算缩放比
			int wr = picwidth / swidth;
			int hr = picheight / sheight;

			int r = 1;
			if (wr >= 1 || hr >= 1) {
				r = wr >= hr ? wr : hr;
			}

			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

			bitmapOptions.inSampleSize = r;// 图片长宽方向缩小倍数

			bitmapOptions.inDither = true;// 不进行图片抖动处理
			bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_4444;// 设置解码方式
			input = activity.getContentResolver().openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);

			input.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 图片由图库保存到SD卡中 */
	private Uri convertUri(Uri uri) {
		Bitmap bitmap = compressPic(uri);
		return saveBitmap(bitmap);
	}

	/** 调用系统相机的本地裁剪功能 */
	private void startImageZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 设置宽高比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 设置裁剪后宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, CROP_REQUEST_CODE);
	}

	/** 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	private int dip2px(float dpValue) {
		final float scale = activity.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** 设置Dialog的布局 */
	private void setDialogLayout() {
		ll = new LinearLayout(activity);
		LayoutParams llp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(llp);

		// 设置文本布局
		LayoutParams tvp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, dip2px(54));

		tv_camera = new TextView(activity);
		tv_camera.setText("拍照");
		tv_camera.setTextColor(Color.parseColor("#333333"));
		tv_camera.setTextSize(16);
		tv_camera.setClickable(false);
		tv_camera.setGravity(Gravity.CENTER);
		tv_camera.setLayoutParams(tvp);

		tv_gallery = new TextView(activity);
		tv_gallery.setText("从相册选择");
		tv_gallery.setTextColor(Color.parseColor("#333333"));
		tv_gallery.setTextSize(16);
		tv_gallery.setClickable(false);
		tv_gallery.setGravity(Gravity.CENTER);
		tv_gallery.setLayoutParams(tvp);

		tv_cancel = new TextView(activity);
		tv_cancel.setText("取消");
		tv_cancel.setTextColor(Color.parseColor("#00BC9C"));
		tv_cancel.setTextSize(16);
		tv_cancel.setClickable(false);
		tv_cancel.setGravity(Gravity.CENTER);
		tv_cancel.setLayoutParams(tvp);

		// 设置分隔线
		LayoutParams vp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, dip2px(1));
		vp.setMargins(dip2px(20), 0, dip2px(20), 0);
		View view1 = new View(activity);
		view1.setBackgroundColor(Color.parseColor("#EBEBEB"));
		view1.setLayoutParams(vp);

		View view2 = new View(activity);
		view2.setBackgroundColor(Color.parseColor("#EBEBEB"));
		view2.setLayoutParams(vp);

		ll.addView(tv_camera, 0);
		ll.addView(view1, 1);
		ll.addView(tv_gallery, 2);
		ll.addView(view2, 3);
		ll.addView(tv_cancel, 4);
	}
}
