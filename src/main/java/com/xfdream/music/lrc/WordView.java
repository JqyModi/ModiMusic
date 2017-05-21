package com.xfdream.music.lrc;

/**
 * Created by Modi on 2016/12/1.
 * 锟斤拷锟戒：1294432350@qq.com
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xfdream.music.R;
import com.xfdream.music.entity.Sentence;
import com.xfdream.music.util.Common;
import com.xfdream.music.util.Constants;

public class WordView extends TextView {
    private static List<Sentence> mWordsList = new ArrayList<Sentence>();
    private LrcHandle mLrcHandle;
    private static int mCurrentTime = 0;
    private Paint mLoseFocusPaint;
    private Paint mOnFocusePaint;
    private float mX = 0;
    private float mMiddleY = 0;
    private float mY = 0;
    private static final int DY = 50;
    private int mIndex = 0;

    public WordView(Context context) throws IOException {
        super(context);
        init();
    }

    public WordView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);
        init();
    }

    public WordView(Context context, AttributeSet attrs, int defStyle)
            throws IOException {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.TRANSPARENT);
        Paint p = mLoseFocusPaint;
        p.setTextAlign(Paint.Align.CENTER);
        Paint p2 = mOnFocusePaint;
        p2.setTextAlign(Paint.Align.CENTER);

        if (mWordsList != null && mWordsList.size() > 0) {
            canvas.drawText((String) mWordsList.get(mIndex).getContent(), mX, mMiddleY, p2);
            Constants.LRC_CURRENT_INDEX = mIndex;
            int alphaValue = 25;
            float tempY = mMiddleY;
            for (int i = mIndex - 1; i >= 0; i--) {
                tempY -= DY;
                if (tempY < 0) {
                    break;
                }
                p.setColor(Color.argb(255 - alphaValue, 245, 245, 245));
                canvas.drawText((String) mWordsList.get(i).getContent(), mX, tempY, p);
                alphaValue += 25;
            }
            alphaValue = 25;
            tempY = mMiddleY;
            for (int i = mIndex + 1, len = mWordsList.size(); i < len; i++) {
                tempY += DY;
                if (tempY > mY) {
                    break;
                }
                p.setColor(Color.argb(255 - alphaValue, 245, 245, 245));
                canvas.drawText((String) mWordsList.get(i).getContent(), mX, tempY, p);
                alphaValue += 25;
            }
            mIndex++;
        }else {
            canvas.drawText(getResources().getString(R.string.lrc_not_find), mX, mMiddleY, p);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);

        mX = w * 0.5f;
        mY = h;
        mMiddleY = h * 0.3f;
    }

    @SuppressLint("SdCardPath")
    private void init() throws IOException {
        setFocusable(true);

        mLoseFocusPaint = new Paint();
        mLoseFocusPaint.setAntiAlias(true);
        mLoseFocusPaint.setTextSize(22);
        mLoseFocusPaint.setColor(Color.WHITE);
        mLoseFocusPaint.setTypeface(Typeface.MONOSPACE);

        mOnFocusePaint = new Paint();
        mOnFocusePaint.setAntiAlias(true);
        mOnFocusePaint.setColor(Color.YELLOW);
        mOnFocusePaint.setTextSize(40);
        mOnFocusePaint.setTypeface(Typeface.SANS_SERIF);
    }

    public void setLrcHandler(LrcHandle lrcHandler) {
        this.mLrcHandle = lrcHandler;
        if (mLrcHandle!=null){
            mWordsList = mLrcHandle.getWords();
            mCurrentTime = mLrcHandle.getCurrentTime();
            this.mIndex = mLrcHandle.getCurrentIndex();
        }
    }
}