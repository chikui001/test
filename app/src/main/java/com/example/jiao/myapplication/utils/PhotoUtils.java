package com.example.jiao.myapplication.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.example.jiao.myapplication.MyApplication;
import com.example.jiao.myapplication.R;
import com.extantfuture.util.CollectionUtil;
import com.extantfuture.util.JobCenter;
import com.extantfuture.util.Pair;
import com.extantfuture.util.StringUtil;

import java.io.*;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PhotoUtils {

	public static Map<String, SoftReference<Bitmap>> mPhotoThumbnailCache = new HashMap<String, SoftReference<Bitmap>>();
	private static final String PHOTO_THUMBNAIL_DIR = "photo/thumbnail/";

	/**
	 * 向服务器上传图片时，图片压缩到200kb以下
	 */
	public static final int IMAGE_MAX_SIZE = 204800;

	/**
	 * 向服务器上传图片时，如果图片大于IMAGE_MAX_SIZE，并且尺寸大于1280，先压缩尺寸
	 */
	public static final int IMAGE_MAX_WIDTH = 1280;

	public static final int IMAGE_MAX_HEIGHT = 1280;

	public static byte[] getBitmapByteArray(Bitmap bitmap) {
		if (null != bitmap && !bitmap.isRecycled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		}
		return null;
	}

	/**
	 * 图片压缩
	 *
	 * @param path
	 * @param type
	 * @return
	 */
	//	public static File needFile(String path, EfImageType type) {
	//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//		BitmapFactory.Options options = new BitmapFactory.Options();
	//		options.inJustDecodeBounds = true;
	//		BitmapFactory.decodeFile(path, options);
	//		options.inSampleSize = calculateInSampleSize(options, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
	//		options.inJustDecodeBounds = false;
	//		Bitmap after = BitmapFactory.decodeFile(path, options);
	//		if (null != after) {
	//			int degree = readPictureDegree(path);
	//			after = rotateBitmap(after, degree);
	//			after.compress(Bitmap.CompressFormat.JPEG, 80, baos);
	//			after.recycle();
	//
	//			File imageFileDir = MengdongFileUtil.getImageFilePath(MengdongApp.getInstance().getUserManager().getUserId(), type);
	//			File compressedFile = getRandomFile(imageFileDir, Const.JPG);
	//			if (null != compressedFile) {
	//				FileUtil.writeFile(compressedFile, baos.toByteArray());
	//				return compressedFile;
	//			}
	//		}
	//		return null;
	//	}

	/**
	 * 获取文件名是uuid产生的文件
	 *
	 * @param fileDir
	 * @param type
	 * @return
	 */
	public static File getRandomFile(File fileDir, String type) {
		if (null != fileDir && fileDir.exists()) {
			return new File(fileDir, StringUtil.concat(UUID.randomUUID().toString(), type));
		}
		return null;
	}

	/**
	 * 不加图片后缀防止别有用心的人,在相册也看不到
	 *
	 * @param fileDir
	 * @param url
	 * @return
	 */
	public static File getFileByMd5Url(File fileDir, String url) {
		if (null != fileDir && fileDir.exists() && StringUtil.isNotEmpty(url)) {
			Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
			return new File(fileDir, StringUtil.concat(md5FileNameGenerator.generate(url)));
		}
		return null;
	}

	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null) {
			return null;
		} else {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix mtx = new Matrix();
			mtx.postRotate((float) rotate);
			return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		}
	}

	private static int readPictureDegree(String path) {
		short degree = 0;

		try {
			ExifInterface e = new ExifInterface(path);
			int orientation = e.getAttributeInt("Orientation", 1);
			switch (orientation) {
				case 3:
					degree = 180;
				case 4:
				case 5:
				case 7:
				default:
					break;
				case 6:
					degree = 90;
					break;
				case 8:
					degree = 270;
			}
		} catch (IOException var4) {
			var4.printStackTrace();
		}

		return degree;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, float reqWidth, float reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if ((float) height > reqHeight || (float) width > reqWidth) {
			int heightRatio = (int) Math.ceil((double) ((float) height / reqHeight));
			int widthRatio = (int) Math.ceil((double) ((float) width / reqWidth));
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	/**
	 * 通过手机照相获取图片
	 *
	 * @param activity
	 * @return 照相后图片的路径
	 */
	@TargetApi(23)
	public static String takePicture(Activity activity, String filePath) {
		//权限检查
		if (ContextUtil.isAndroidM()) {
			if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
				return taskPictureImpl(activity, filePath);
			} else {
				MyApplication.getInstance().getPermissionManager().requestPermission(activity, Manifest.permission_group.CAMERA);
			}
		} else {
			return taskPictureImpl(activity, filePath);
		}

		return null;
	}

	private static String taskPictureImpl(Activity activity, String filePath) {
		File portraitFileCrop = new File(filePath);
		if (!portraitFileCrop.exists()) {
			try {
				portraitFileCrop.createNewFile();
			} catch (IOException e) {

			}
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(portraitFileCrop));
		activity.startActivityForResult(intent, ReqCodeKey.REQUEST_CODE_CAMERA);
		return portraitFileCrop.getPath();
	}

	@TargetApi(23)
	public static String takePicture(Fragment fragment, String filePath) {
		//权限检查
		if (ContextUtil.isAndroidM()) {
			if (fragment.getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
				return taskPictureImpl(fragment, filePath);
			} else {
				MyApplication.getInstance().getPermissionManager()
							 .requestPermission(fragment.getActivity(), Manifest.permission_group.CAMERA);
			}
		} else {
			return taskPictureImpl(fragment, filePath);
		}
		return null;
	}

	private static String taskPictureImpl(Fragment fragment, String filePath) {
		File portraitFileCrop = new File(filePath);
		if (!portraitFileCrop.exists()) {
			try {
				portraitFileCrop.createNewFile();
			} catch (IOException e) {

			}
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(portraitFileCrop));
		fragment.startActivityForResult(intent, ReqCodeKey.REQUEST_CODE_CAMERA);
		return portraitFileCrop.getPath();
	}

	/**
	 * 去系统相册
	 *
	 * @param activity
	 */
	public static void chooseImage(Activity activity) {
		if (ContextUtil.isAtLeastAndroidL()) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			activity.startActivityForResult(intent, ReqCodeKey.REQUEST_CODE_LOCATION_NEW);
		} else {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			activity.startActivityForResult(intent, ReqCodeKey.REQUEST_CODE_LOCATION_NEW);
		}
	}

	public static void chooseImage(Fragment fragment) {
		if (ContextUtil.isAtLeastAndroidL()) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			fragment.startActivityForResult(intent, ReqCodeKey.REQUEST_CODE_LOCATION_NEW);
		} else {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			fragment.startActivityForResult(intent, ReqCodeKey.REQUEST_CODE_LOCATION_NEW);
		}
	}

	/**
	 * 通过手机相册获取图片
	 *
	 * @param activity
	 * @deprecated
	 */
	public static void selectPhoto(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		activity.startActivityForResult(intent, ReqCodeKey.REQUEST_CODE_LOCATION);
	}

	/**
	 * 从文件中获取图片
	 *
	 * @param path 图片的路径
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path, float reqWidth, float reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getBitmapFromFile(String path, int sampleSize) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = sampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getBitmapFromFile(String path) {
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * 从Uri中获取图片
	 *
	 * @param cr  ContentResolver对象
	 * @param uri 图片的Uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
		try {
			return BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	/**
	 * 根据宽度和长度进行缩放图片
	 *
	 * @param path 图片的路径
	 * @param w    宽度
	 * @param h    长度
	 * @return
	 */
	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			if (w == 0 && h == 0) {
				return BitmapFactory.decodeFile(path, opts);
			}
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (w == 0) {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			} else if (h == 0) {
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else if (srcWidth > srcHeight) {
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static Bitmap generateCacheView(View view, int quality) {
		//低质量截图
		view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

		//开始截图
		view.setDrawingCacheEnabled(true);
		// this is the important code :)
		// Without it the view will have a dimension of 0,0 and the bitmap will be null
		view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
					 View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
		view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
		//截图
		view.buildDrawingCache(true);
		Bitmap cacheBitmap = view.getDrawingCache();
		if (null == cacheBitmap) {
			Log.d("PhotoUtil", "generateCacheView return null ");
			return null;
		}
		Bitmap newBitmap = Bitmap.createBitmap(cacheBitmap);
		//recycler原始bitmap
		view.setDrawingCacheEnabled(false);
		//查看需不需要压缩
		return PhotoUtils.compassQuality(newBitmap, quality);
	}

	public static byte[] compassQuality2Bytes(Bitmap image, int kb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		return baos.toByteArray();
	}

	public static Bitmap compassQuality(Bitmap image, int kb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		if (null != image && !image.isRecycled()) {
			image.recycle();
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap compassSize(Bitmap image, int ww, int hh) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 获取图片的长度和宽度
	 *
	 * @param bitmap 图片bitmap对象
	 * @return
	 */
	public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
		Bundle bundle = null;
		if (bitmap != null) {
			bundle = new Bundle();
			bundle.putInt("width", bitmap.getWidth());
			bundle.putInt("height", bitmap.getHeight());
			return bundle;
		}
		return null;
	}

	/**
	 * 判断图片高度和宽度是否过大
	 *
	 * @param bitmap 图片bitmap对象
	 * @return
	 */
	public static boolean bitmapIsLarge(Bitmap bitmap) {
		final int MAX_WIDTH = 60;
		final int MAX_HEIGHT = 60;
		Bundle bundle = getBitmapWidthAndHeight(bitmap);
		if (bundle != null) {
			int width = bundle.getInt("width");
			int height = bundle.getInt("height");
			if (width > MAX_WIDTH && height > MAX_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据比例缩放图片
	 *
	 * @param screenWidth 手机屏幕的宽度
	 * @param filePath    图片的路径
	 * @param ratio       缩放比例
	 * @return
	 */
	//	public static Bitmap CompressionPhoto(float screenWidth, String filePath, int ratio) {
	//		Bitmap bitmap = PhotoUtils.getBitmapFromFile(filePath);
	//		Bitmap compressionBitmap = null;
	//		float scaleWidth = screenWidth / (bitmap.getWidth() * ratio);
	//		float scaleHeight = screenWidth / (bitmap.getHeight() * ratio);
	//		Matrix matrix = new Matrix();
	//		matrix.postScale(scaleWidth, scaleHeight);
	//		try {
	//			compressionBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	//		} catch (Exception e) {
	//			return bitmap;
	//		}
	//		return compressionBitmap;
	//	}

	/**
	 * 滤镜效果--LOMO
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap lomoFilter(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int dst[] = new int[width * height];
		bitmap.getPixels(dst, 0, width, 0, 0, width, height);

		int ratio = width > height ? height * 32768 / width : width * 32768 / height;
		int cx = width >> 1;
		int cy = height >> 1;
		int max = cx * cx + cy * cy;
		int min = (int) (max * (1 - 0.8f));
		int diff = max - min;

		int ri, gi, bi;
		int dx, dy, distSq, v;

		int R, G, B;

		int value;
		int pos, pixColor;
		int newR, newG, newB;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;
				pixColor = dst[pos];
				R = Color.red(pixColor);
				G = Color.green(pixColor);
				B = Color.blue(pixColor);

				value = R < 128 ? R : 256 - R;
				newR = (value * value * value) / 64 / 256;
				newR = (R < 128 ? newR : 255 - newR);

				value = G < 128 ? G : 256 - G;
				newG = (value * value) / 128;
				newG = (G < 128 ? newG : 255 - newG);

				newB = B / 2 + 0x25;

				// ==========边缘黑暗==============//
				dx = cx - x;
				dy = cy - y;
				if (width > height)
					dx = (dx * ratio) >> 15;
				else
					dy = (dy * ratio) >> 15;

				distSq = dx * dx + dy * dy;
				if (distSq > min) {
					v = ((max - distSq) << 8) / diff;
					v *= v;

					ri = newR * v >> 16;
					gi = newG * v >> 16;
					bi = newB * v >> 16;

					newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				// ==========边缘黑暗end==============//

				dst[pos] = Color.rgb(newR, newG, newB);
			}
		}

		Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
		acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
		return acrossFlushBitmap;
	}

	/**
	 * 根据文字获取图片
	 *
	 * @param text
	 * @return
	 */
	public static Bitmap getIndustry(Context context, String text) {
		String color = "#ffefa600";
		if ("艺".equals(text)) {
			color = "#ffefa600";
		} else if ("学".equals(text)) {
			color = "#ffbe68c1";
		} else if ("商".equals(text)) {
			color = "#ffefa600";
		} else if ("医".equals(text)) {
			color = "#ff30c082";
		} else if ("IT".equals(text)) {
			color = "#ff27a5e3";
		}
		Bitmap src = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		int x = src.getWidth();
		int y = src.getHeight();
		Bitmap bmp = Bitmap.createBitmap(x, y, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(bmp);
		canvasTemp.drawColor(Color.parseColor(color));
		Paint p = new Paint(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.WHITE);
		p.setFilterBitmap(true);
		int size = (int) (13 * context.getResources().getDisplayMetrics().density);
		p.setTextSize(size);
		float tX = (x - getFontlength(p, text)) / 2;
		float tY = (y - getFontHeight(p)) / 2 + getFontLeading(p);
		canvasTemp.drawText(text, tX, tY, p);

		return toRoundCorner(bmp, 2);
	}

	/**
	 * @return 返回指定笔和指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}

	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}

	/**
	 * @return 返回指定笔离文字顶部的基准距离
	 */
	public static float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.leading - fm.ascent;
	}

	/**
	 * 获取圆角图片
	 *
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获取颜色的圆角bitmap
	 *
	 * @param context
	 * @param color
	 * @return
	 */
	public static Bitmap getRoundBitmap(Context context, int color) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics));
		int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0f, metrics));
		int round = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.0f, metrics));
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawRoundRect(new RectF(0.0F, 0.0F, width, height), round, round, paint);
		return bitmap;
	}

	public static Bitmap getPhotoThumbnail(Context context, String imageName) {
		if (mPhotoThumbnailCache.containsKey(imageName)) {
			Reference<Bitmap> reference = mPhotoThumbnailCache.get(imageName);
			if (reference.get() == null || reference.get().isRecycled()) {
				mPhotoThumbnailCache.remove(imageName);
			} else {
				return reference.get();
			}
		}
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = context.getAssets().open(PHOTO_THUMBNAIL_DIR + imageName);
			bitmap = BitmapFactory.decodeStream(is);
			if (bitmap == null) {
				throw new FileNotFoundException(imageName + "is not find");
			}
			mPhotoThumbnailCache.put(imageName, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {

			}
		}
	}

	/**
	 * 获取图片的类型信息
	 *
	 * @param in
	 * @return
	 * @see #getImageType(byte[])
	 */
	public static String getImageType(InputStream in) {
		if (in == null) {
			return null;
		}
		try {
			byte[] bytes = new byte[8];
			in.read(bytes);
			return getImageType(bytes);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 获取图片的类型信息
	 *
	 * @param bytes 2~8 byte at beginning of the image file
	 * @return image mimetype or null if the file is not image
	 */
	public static String getImageType(byte[] bytes) {
		if (isJPEG(bytes)) {
			return "image/jpeg";
		}
		if (isGIF(bytes)) {
			return "image/gif";
		}
		if (isPNG(bytes)) {
			return "image/png";
		}
		if (isBMP(bytes)) {
			return "application/x-bmp";
		}
		return null;
	}

	private static boolean isJPEG(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
	}

	private static boolean isGIF(byte[] b) {
		if (b.length < 6) {
			return false;
		}
		return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8' && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	private static boolean isPNG(byte[] b) {
		if (b.length < 8) {
			return false;
		}
		return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78 && b[3] == (byte) 71 && b[4] == (byte) 13
				&& b[5] == (byte) 10 && b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	private static boolean isBMP(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == 0x42) && (b[1] == 0x4d);
	}

	public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		//
		// Java Author: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com
		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.
		//
		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.
		//
		// If you are using this algorithm in your code please add
		// the following line:
		//
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
/*        if (VERSION.SDK_INT > 16) {
			Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap,
                            Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius  e.g. 3.f );
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }*/

		if (sentBitmap == null) {
			return (null);
		}

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
	}

	// Max read limit that we allow our input stream to mark/reset.
	private static final int MAX_READ_LIMIT_PER_IMG = 1024 * 1024;

	public static Bitmap scaleBitmap(Bitmap src, int maxWidth, int maxHeight) {
		double scaleFactor = Math.min(((double) maxWidth) / src.getWidth(), ((double) maxHeight) / src.getHeight());
		return Bitmap.createScaledBitmap(src, (int) (src.getWidth() * scaleFactor), (int) (src.getHeight() * scaleFactor), false);
	}

	public static Bitmap scaleBitmap(int scaleFactor, InputStream is) {
		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;

		return BitmapFactory.decodeStream(is, null, bmOptions);
	}

	public static int findScaleFactor(int targetW, int targetH, InputStream is) {
		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, bmOptions);
		int actualW = bmOptions.outWidth;
		int actualH = bmOptions.outHeight;

		// Determine how much to scale down the image
		return Math.min(actualW / targetW, actualH / targetH);
	}

	@SuppressWarnings("SameParameterValue")
	public static Bitmap fetchAndRescaleBitmap(String uri, int width, int height) throws IOException {
		URL url = new URL(uri);
		BufferedInputStream is = null;
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(urlConnection.getInputStream());
			is.mark(MAX_READ_LIMIT_PER_IMG);
			int scaleFactor = findScaleFactor(width, height, is);
			is.reset();
			return scaleBitmap(scaleFactor, is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	public static void fetchFileAndSave(String storePath, String uri) {
		new FileDownload(storePath, uri);
	}

	public static void fetchFileAndSaves(List<Pair<String, String>> needDownloadFileList) {
		if (CollectionUtil.isNotEmpty(needDownloadFileList)) {
			for (int i = 0; i < needDownloadFileList.size(); i++) {
				Pair<String, String> pair = needDownloadFileList.get(i);
				if (null != pair) {
					new FileDownload(pair.getFirst(), pair.getSecond()).executeOnExecutor(JobCenter.getExecutor(), "");
					;
				}
			}
		}
	}

	private static final String TEMP_IMAGE_POSTFIX = ".tmp";
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb

	public static class FileDownload extends AsyncTask<String, String, Boolean> {

		private String storePath;
		private String uri;

		public FileDownload(String storePath, String uri) {
			this.storePath = storePath;
			this.uri = uri;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			if (StringUtil.isNotEmpty(storePath) && StringUtil.isNotEmpty(uri)) {
				if (StringUtil.isNotEmpty(storePath) && StringUtil.isNotEmpty(uri)) {
					URL url = null;
					InputStream inputStream = null;
					try {
						url = new URL(uri);
						HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
						File file = new File(storePath);
						inputStream = urlConnection.getInputStream();
						save(file, inputStream, null);
					} catch (Exception e) {

					} finally {
						if (null != inputStream) {
							try {
								inputStream.close();
							} catch (IOException e) {
							}
						}
					}
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean aVoid) {
			super.onPostExecute(aVoid);
		}
	}

	public static boolean save(File file, InputStream imageStream, IoUtils.CopyListener listener) throws IOException {
		File imageFile = file;
		File tmpFile = new File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
		boolean loaded = false;
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), DEFAULT_BUFFER_SIZE);
			try {
				loaded = IoUtils.copyStream(imageStream, os, listener, DEFAULT_BUFFER_SIZE);
			} finally {
				IoUtils.closeSilently(os);
			}
		} finally {
			if (loaded && !tmpFile.renameTo(imageFile)) {
				loaded = false;
			}
			if (!loaded) {
				tmpFile.delete();
			}
		}
		return loaded;
	}
}
