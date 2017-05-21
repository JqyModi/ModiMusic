package com.xfdream.music.service;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Modi on 2016/11/23.
 * 閭锛?294432350@qq.com
 */

public class DeskLrcText extends TextView {
    private static final String TAG = DeskLrcText.class.getSimpleName();
    public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private float startX;
    private float startY;
    private float one = 0.0f;
    private float two = 0.01f;
    private WindowManager wm;
    private String text;
    private int statusBarHeight;

    public static boolean isShow = false;

    public static boolean isShow() {
        return isShow;
    }

    public static void setShow(boolean show) {
        isShow = show;
    }

    public DeskLrcText(Context context) {
        super(context);
        // handler.post(update);
        wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        updateTextThread.start();
        statusBarHeight = getStatusBarHeight();

        isShow = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { // 瑙︽懜鐐圭浉瀵逛簬灞忓箷宸︿笂瑙掑潗鏍?
        float x = event.getRawX();
        float y = event.getRawY() - statusBarHeight;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*startX = event.getX();
                startY = event.getY();*/
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "x::" + startX + ",y::" + startY);
                Log.w(TAG, "rawx::" + x + ",rawy::" + y);
            case MotionEvent.ACTION_UP:
                updatePosition(x - startX, y - startY);
                break;
        }

/*        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                //getRawX/Y 鏄幏鍙栫浉瀵逛簬Device鐨勫潗鏍囦綅缃?娉ㄦ剰鍖哄埆getX/Y[鐩稿浜嶸iew]
                params.x = (int) event.getRawX();
                params.y = (int) event.getRawY();
                //鏇存柊"妗岄潰姝岃瘝"鐨勪綅缃?
                wm.updateViewLayout(this,params);
                //涓嬮潰鐨剅emoveView 鍙互鍘绘帀"妗岄潰姝岃瘝"
                //wm.removeView(myView);
                break;
            case MotionEvent.ACTION_MOVE:
                params.x = (int) event.getRawX();
                params.y = (int) event.getRawY();
                wm.updateViewLayout(this,params);
                break;
        }*/

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float len = getTextSize() * text.length();

       /* 娓叉煋姝岃瘝锛屽墠鍥涗釜鍙傛暟琛ㄧず浠庡摢閲屾覆鏌撳埌鍝噷
        绗?涓弬鏁颁负娓叉煋鐨勪袱绉嶄笉鍚岄鑹?
        绗?涓弬鏁拌〃绀烘覆鏌撶殑鐩稿浣嶇疆
        鑼冨洿浠?鍒? 绗?涓弬鏁拌〃绀烘ā寮?
        */
        Shader shader = new LinearGradient(0, 0, len, 0, new int[]{Color.YELLOW, Color.RED}, new float[]{one, two}, Shader.TileMode.CLAMP);
        //Shader lShader = new SweepGradient(0,0,new int[]{Color.YELLOW, Color.RED}, new float[]{one, two});
        Shader lShader = new RadialGradient(0, 0, len, new int[]{Color.YELLOW, Color.RED}, new float[]{one, two}, Shader.TileMode.CLAMP);
        Paint p = new Paint();
        p.setShader(lShader);
        p.setTextSize(getTextSize());
        canvas.drawText(text, (getWidth()-len)/2, (getTextSize()+this.getHeight())/2, p);

    }
    // 閫氳繃涓€涓紓姝ョ嚎绋嬫潵鎺у埗姝岃瘝娓叉煋鐨勯€熷害
    private Thread updateTextThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (true) {
                one += 0.001f;
                two += 0.001f;
                if (two > 1.0) {
                    one = 0.0f;
                    two = 0.01f;
                }
                postInvalidate();
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }
            }
        }
    };
    // 鏇存柊娴姩绐楀彛浣嶇疆鍙傛暟
    private void updatePosition(float x, float y) { // View鐨勫綋鍓嶄綅缃?
        params.x = (int) x;
        params.y = (int) y;
        wm.updateViewLayout(this, params);
    }
    // 鑾峰緱鐘舵€佹爮楂樺害
    private int getStatusBarHeight() {
        Class c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 75;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
