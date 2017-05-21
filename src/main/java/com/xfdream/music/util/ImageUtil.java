package com.xfdream.music.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.xfdream.music.async.AsyncTaskHandler;

public class ImageUtil {
	public static final int TOP = 0;
	public static final int BOTTOM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int LEFT_TOP = 4;
	public static final int LEFT_BOTTOM = 5;
	public static final int RIGHT_TOP = 6;
	public static final int RIGHT_BOTTOM = 7;

	// 杞紩鐢ㄥ唴瀛樼紦瀛?
	public static Map<String, SoftReference<Bitmap>> sImageCache = new HashMap<String, SoftReference<Bitmap>>();

	/**
	 * 图锟斤拷糯锟斤拷锟叫?锟斤拷锟捷匡拷群透叨鹊谋锟斤拷锟较碉拷锟?
	 * */
	public static Bitmap zoomBitmap(Bitmap src, float scaleX, float scaleY) {
		Matrix matrix = new Matrix();
		matrix.setScale(scaleX, scaleY);
		Bitmap t_bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
				src.getHeight(), matrix, true);
		return t_bitmap;
	}

	/**
	 * 图锟斤拷糯锟斤拷锟叫?锟斤拷锟捷匡拷群透叨锟?
	 * */
	public static Bitmap zoomBitmap(Bitmap src, int width, int height) {
		return Bitmap.createScaledBitmap(src, width, height, true);
	}

	/**
	 * Drawable转Bitmap
	 * */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		return ((BitmapDrawable) drawable).getBitmap();
	}

	/**
	 * Bitmap转Drawable
	 * */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * Bitmap转byte[]
	 * */
	public static byte[] bitmapToByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		return out.toByteArray();
	}

	/**
	 * byte[]转Bitmap
	 * */
	public static Bitmap byteToBitmap(byte[] data) {
		if (data.length != 0) {
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		return null;
	}

	/**
	 * 锟斤拷圆锟角碉拷图锟斤拷
	 * */
	public static Bitmap createRoundedCornerBitmap(Bitmap src, int radius) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		// 锟斤拷锟斤拷锟斤拷32位图
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bitmap);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(0xff424242);
		// 锟斤拷止锟斤拷缘锟侥撅拷锟?
		paint.setAntiAlias(true);
		// 锟斤拷锟斤拷锟斤拷位图锟斤拷锟斤拷锟剿诧拷锟斤拷锟斤拷
		paint.setFilterBitmap(true);
		Rect rect = new Rect(0, 0, w, h);
		RectF rectf = new RectF(rect);
		// 锟斤拷锟狡达拷圆锟角的撅拷锟斤拷
		canvas.drawRoundRect(rectf, radius, radius, paint);

		// 取锟斤拷锟斤拷锟斤拷平锟斤拷锟斤拷锟斤拷锟绞撅拷喜锟?
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		// 锟斤拷锟斤拷图锟斤拷
		canvas.drawBitmap(src, rect, rect, paint);
		return bitmap;
	}

	/**
	 * 锟斤拷锟斤拷选锟叫达拷锟斤拷示图片
	 * */
	public static Drawable createSelectedTip(Context context,int srcId,int tipId){
		Bitmap src=BitmapFactory.decodeResource(context.getResources(), srcId);
		Bitmap tip=BitmapFactory.decodeResource(context.getResources(), tipId);
		final int w=src.getWidth();
		final int h=src.getHeight();
		Bitmap bitmap=Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bitmap);
		//锟斤拷锟斤拷原图
		canvas.drawBitmap(src, 0, 0, paint);
		//锟斤拷锟斤拷锟斤拷示图片
		canvas.drawBitmap(tip,(w-tip.getWidth()),0, paint);
		return bitmapToDrawable(bitmap);
	}
	
	/**
	 * 锟斤拷锟斤拷影锟斤拷图锟斤拷
	 * */
	public static Bitmap createReflectionBitmap(Bitmap src) {
		// 锟斤拷锟斤拷图锟斤拷锟侥匡拷隙
		final int spacing = 4;
		final int w = src.getWidth();
		final int h = src.getHeight();
		// 锟斤拷锟狡革拷锟斤拷锟斤拷32位图
		Bitmap bitmap = Bitmap.createBitmap(w, h + h / 2 + spacing,
				Config.ARGB_8888);
		// 锟斤拷锟斤拷锟斤拷X锟斤拷牡锟接巴硷拷锟?
		Matrix m = new Matrix();
		m.setScale(1, -1);
		Bitmap t_bitmap = Bitmap.createBitmap(src, 0, h / 2, w, h / 2, m, true);

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		// 锟斤拷锟斤拷原图锟斤拷
		canvas.drawBitmap(src, 0, 0, paint);
		// 锟斤拷锟狡碉拷影图锟斤拷
		canvas.drawBitmap(t_bitmap, 0, h + spacing, paint);
		// 锟斤拷锟斤拷锟斤拷染-锟斤拷Y锟斤拷叩锟斤拷锟斤拷锟饺?
		Shader shader = new LinearGradient(0, h + spacing, 0, h + spacing + h
				/ 2, 0x70ffffff, 0x00ffffff, Shader.TileMode.MIRROR);
		paint.setShader(shader);
		// 取锟斤拷锟斤拷锟斤拷平锟斤拷锟斤拷锟斤拷锟绞撅拷虏恪?
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// 锟斤拷锟斤拷锟斤拷染锟斤拷影锟侥撅拷锟斤拷
		canvas.drawRect(0, h + spacing, w, h + h / 2 + spacing, paint);
		return bitmap;
	}
	
	/**
	 * 锟斤拷锟斤拷锟侥碉拷影图锟斤拷
	 * */
	public static Bitmap createReflectionBitmapForSingle(Bitmap src) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		// 锟斤拷锟狡革拷锟斤拷锟斤拷32位图
		Bitmap bitmap = Bitmap.createBitmap(w, h / 2, Config.ARGB_8888);
		// 锟斤拷锟斤拷锟斤拷X锟斤拷牡锟接巴硷拷锟?
		Matrix m = new Matrix();
		m.setScale(1, -1);
		Bitmap t_bitmap = Bitmap.createBitmap(src, 0, h / 2, w, h / 2, m, true);

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		// 锟斤拷锟狡碉拷影图锟斤拷
		canvas.drawBitmap(t_bitmap, 0, 0, paint);
		// 锟斤拷锟斤拷锟斤拷染-锟斤拷Y锟斤拷叩锟斤拷锟斤拷锟饺?
		Shader shader = new LinearGradient(0, 0, 0, h / 2, 0x70ffffff,
				0x00ffffff, Shader.TileMode.MIRROR);
		paint.setShader(shader);
		// 取锟斤拷锟斤拷锟斤拷平锟斤拷锟斤拷锟斤拷锟绞撅拷虏恪?
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// 锟斤拷锟斤拷锟斤拷染锟斤拷影锟侥撅拷锟斤拷
		canvas.drawRect(0, 0, w, h / 2, paint);
		return bitmap;
	}

	/**
	 * 锟斤拷苫锟缴硷拷锟?
	 * */
	public static Bitmap createGrayBitmap(Bitmap src) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		// 锟斤拷色锟戒换锟侥撅拷锟斤拷
		ColorMatrix matrix = new ColorMatrix();
		// saturation 锟斤拷锟酵讹拷值锟斤拷锟斤拷小锟斤拷锟斤拷为0锟斤拷锟斤拷时锟斤拷应锟斤拷锟角灰讹拷图; 为1锟斤拷示锟斤拷锟酵度诧拷锟戒，锟斤拷锟矫达拷锟斤拷1锟斤拷锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷
		matrix.setSaturation(0);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		paint.setColorFilter(filter);
		canvas.drawBitmap(src, 0, 0, paint);
		return bitmap;
	}

	/**
	 * 锟斤拷锟斤拷图片[format:Bitmap.CompressFormat.PNG,Bitmap.CompressFormat.JPEG]
	 * */
	public static boolean saveImage(Bitmap src, String filepath,
			CompressFormat format) {
		boolean rs = false;
		File file = new File(filepath);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (src.compress(format, 100, out)) {
				out.flush();
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 锟斤拷锟剿⌒э拷锟?
	 * */
	public static Bitmap createWatermark(Bitmap src, Bitmap watermark,
			int direction, int spacing) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(src, 0, 0, null);
		if (direction == LEFT_TOP) {
			canvas.drawBitmap(watermark, spacing, spacing, null);
		} else if (direction == LEFT_BOTTOM) {
			canvas.drawBitmap(watermark, spacing, h - watermark.getHeight()
					- spacing, null);
		} else if (direction == RIGHT_TOP) {
			canvas.drawBitmap(watermark, w - watermark.getWidth() - spacing,
					spacing, null);
		} else if (direction == RIGHT_BOTTOM) {
			canvas.drawBitmap(watermark, w - watermark.getWidth() - spacing, h
					- watermark.getHeight() - spacing, null);
		}
		return bitmap;
	}

	/**
	 * 锟较筹拷图锟斤拷
	 * */
	public static Bitmap composeBitmap(int direction, Bitmap... bitmaps) {
		if (bitmaps.length < 2) {
			return null;
		}
		Bitmap firstBitmap = bitmaps[0];
		for (int i = 1; i < bitmaps.length; i++) {
			firstBitmap = composeBitmap(firstBitmap, bitmaps[i], direction);
		}
		return firstBitmap;
	}

	private static Bitmap composeBitmap(Bitmap firstBitmap,
			Bitmap secondBitmap, int direction) {
		if (firstBitmap == null) {
			return null;
		}
		if (secondBitmap == null) {
			return firstBitmap;
		}
		final int fw = firstBitmap.getWidth();
		final int fh = firstBitmap.getHeight();
		final int sw = secondBitmap.getWidth();
		final int sh = secondBitmap.getHeight();
		Bitmap bitmap = null;
		Canvas canvas = null;
		if (direction == TOP) {
			bitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			canvas = new Canvas(bitmap);
			canvas.drawBitmap(secondBitmap, 0, 0, null);
			canvas.drawBitmap(firstBitmap, 0, sh, null);
		} else if (direction == BOTTOM) {
			bitmap = Bitmap.createBitmap(fw > sw ? fw : sw, fh + sh,
					Config.ARGB_8888);
			canvas = new Canvas(bitmap);
			canvas.drawBitmap(firstBitmap, 0, 0, null);
			canvas.drawBitmap(secondBitmap, 0, fh, null);
		} else if (direction == LEFT) {
			bitmap = Bitmap.createBitmap(fw + sw, sh > fh ? sh : fh,
					Config.ARGB_8888);
			canvas = new Canvas(bitmap);
			canvas.drawBitmap(secondBitmap, 0, 0, null);
			canvas.drawBitmap(firstBitmap, sw, 0, null);
		} else if (direction == RIGHT) {
			bitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			canvas = new Canvas(bitmap);
			canvas.drawBitmap(firstBitmap, 0, 0, null);
			canvas.drawBitmap(secondBitmap, fw, 0, null);
		}
		return bitmap;
	}

	/**
	 * 閫氳繃璧勬簮id鑾峰彇璧勬簮鍥剧墖
	 *
	 * @param context
	 * @param id
	 *            璧勬簮id
	 * @return
	 */
	public static Bitmap readBitmap(Context context, int id) {
		Bitmap bm = null;
		if (sImageCache.containsKey(id + "")) {
			bm = sImageCache.get(id + "").get();
			if (bm == null) {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inPreferredConfig = Bitmap.Config.RGB_565;// 琛ㄧず16浣嶄綅鍥?
				// 565浠ｈ〃瀵瑰簲涓夊師鑹插崰鐨勪綅鏁?
				opt.inInputShareable = true;
				opt.inPurgeable = true;// 璁剧疆鍥剧墖鍙互琚洖鏀?
				InputStream is = context.getResources().openRawResource(id);
				bm = BitmapFactory.decodeStream(is, null, opt);
				sImageCache.put(id + "", new SoftReference<Bitmap>(bm));
			}
		} else {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;// 琛ㄧず16浣嶄綅鍥?
			// 565浠ｈ〃瀵瑰簲涓夊師鑹插崰鐨勪綅鏁?
			opt.inInputShareable = true;
			opt.inPurgeable = true;// 璁剧疆鍥剧墖鍙互琚洖鏀?
			InputStream is = context.getResources().openRawResource(id);
			bm = BitmapFactory.decodeStream(is, null, opt);
			sImageCache.put(id + "", new SoftReference<Bitmap>(bm));
		}
		return bm;
	}

	/**
	 * 鍔犺浇璧勬簮鍥剧墖
	 *
	 * @param context
	 * @param imageview
	 * @param resourceID
	 * @param defResourceID
	 */
	public static void loadResourceImage(final Context context,
										 final ImageView imageview, final int resourceID,
										 final int defResourceID) {
		imageview.setBackgroundResource(defResourceID);
		new AsyncTaskHandler() {

			@Override
			protected void onPostExecute(Object result) {
				Bitmap bm = (Bitmap) result;
				if (bm == null) {
					imageview.setBackgroundResource(defResourceID);
				} else {
					imageview.setBackgroundDrawable(new BitmapDrawable(bm));
				}
			}

			@Override
			protected Object doInBackground() throws Exception {
				return ImageUtil.readBitmap(context, resourceID);
			}
		}.execute();
	}
}
