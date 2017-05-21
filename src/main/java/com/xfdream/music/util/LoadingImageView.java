package com.xfdream.music.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;


import com.xfdream.music.R;
import com.xfdream.music.entity.SkinMessage;
import com.xfdream.music.observable.ObserverManage;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class LoadingImageView extends ImageView implements Observer {
	private Canvas pCanvas;
	private Bitmap defBitmap;
	private Paint paint;
	private Bitmap baseBitmap;
	private boolean isLoadImage = false;
	private Map<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();

	public LoadingImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LoadingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadingImageView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		ObserverManage.getObserver().addObserver(this);
		paint = new Paint();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {

		defBitmap = bitmaps.get("isTouchIsFalse");
		if (defBitmap == null) {
			baseBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.comm_ic_loading_icon);
			defBitmap = Bitmap.createBitmap(baseBitmap.getWidth(),
					baseBitmap.getHeight(), baseBitmap.getConfig());
			pCanvas = new Canvas(defBitmap);

			int color = Constants.BLACK_GROUND[Constants.DEF_COLOR_INDEX];
			float progressR = Color.red(color) / 255f;
			float progressG = Color.green(color) / 255f;
			float progressB = Color.blue(color) / 255f;
			float progressA = Color.alpha(color) / 255f;

			// 鏍规嵁SeekBar瀹氫箟RGBA鐨勭煩闃?
			float[] src = new float[] { progressR, 0, 0, 0, 0, 0, progressG, 0,
					0, 0, 0, 0, progressB, 0, 0, 0, 0, 0, progressA, 0 };
			// 瀹氫箟ColorMatrix锛屽苟鎸囧畾RGBA鐭╅樀
			ColorMatrix colorMatrix = new ColorMatrix();
			colorMatrix.set(src);
			// 璁剧疆Paint鐨勯鑹?
			paint.setColorFilter(new ColorMatrixColorFilter(src));
			// 閫氳繃鎸囧畾浜哛GBA鐭╅樀鐨凱aint鎶婂師鍥剧敾鍒扮┖鐧藉浘鐗囦笂
			pCanvas.drawBitmap(baseBitmap, new Matrix(), paint);
			bitmaps.put("isTouchIsFalse", defBitmap);
		}
		if (!isLoadImage) {
			setBackgroundDrawable(new BitmapDrawable(defBitmap));
			isLoadImage = true;
		}
		super.dispatchDraw(canvas);
	}

	@Override
	public void update(Observable arg0, Object data) {
		if (data instanceof SkinMessage) {
			SkinMessage msg = (SkinMessage) data;
			if (msg.type == SkinMessage.COLOR) {
				isLoadImage = false;
				bitmaps = new HashMap<String, Bitmap>();
				invalidate();
			}
		}
	}
}
