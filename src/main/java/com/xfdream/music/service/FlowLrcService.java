package com.xfdream.music.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.xfdream.music.R;
import com.xfdream.music.entity.Sentence;
import com.xfdream.music.lrc.WordView;
import com.xfdream.music.message.CommonMessage;
import com.xfdream.music.util.Constants;
import com.xfdream.music.util.LogUtil;
import com.xfdream.music.util.ToastUtil;
import com.xfdream.music.util.WindowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Modi on 2016/11/23.
 * 邮箱：1294432350@qq.com
 */

public class FlowLrcService extends Service {
    private WindowManager wm;
    private DeskLrcText tv;
    private FlowLrcService mContext;

    public class LocalBinder extends Binder {
        FlowLrcService getService() {
            return FlowLrcService.this;
        }
    }

    public FlowLrcService() {
        /*wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        showWindow();*/
        mContext = this;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        EventBus.getDefault().register(this);
        ToastUtil.show(getApplicationContext(),"服务创建成功");
        wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        showWindow();
    }

    // 显示浮动窗口
    private void showWindow() {
        ToastUtil.show(getApplicationContext(),"显示悬浮窗");
        LayoutParams params = DeskLrcText.params;
        params.type = LayoutParams.TYPE_SYSTEM_ALERT | LayoutParams.TYPE_SYSTEM_OVERLAY;
        //params.type = LayoutParams.TYPE_SYSTEM_OVERLAY;
        // 设置窗口类型为系统级:为了让其他控件获取焦点
        params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置窗口焦点
        params.width = (int) (wm.getDefaultDisplay().getWidth() / 1.2);
        params.height = wm.getDefaultDisplay().getHeight() / 8;
        params.alpha = 80;
        params.gravity = Gravity.CENTER;
        // 以屏幕左上角为原点，设置x、y初始值,将悬浮窗口设置在屏幕中间的位置
        /*params.x = wm.getDefaultDisplay().getWidth() / 2;
        params.y = wm.getDefaultDisplay().getHeight() / 2;*/
        tv = new DeskLrcText(FlowLrcService.this);
        tv.setTextSize(18);

        //获取到正在播放音乐的歌词解析出来 --待完善
        //一句一句更新歌词显示
        tv.setText("难以忘记初次见你，一双迷人的眼睛");
        tv.setTextColor(Color.RED);
        tv.setBackground(getResources().getDrawable(R.drawable.desk_lrc_bg));
        //设置回去
        tv.params = params;
        wm.addView(tv, params);

        //WindowUtils.showPopupWindow(this);
    }
    // service退出时关闭浮动窗口
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        ToastUtil.show(getApplicationContext(),"服务销毁成功");
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        if (tv != null && tv.isShown()) {
            wm.removeView(tv);
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * 用来接收时间总线发送过来
     * 的待处理时间消息
     * 刷新桌面歌词
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshDeskLrc(CommonMessage event) {
        if (event.getMsg().equals(Constants.MSG_SEND_SONG_TEXT)){
            //刷新歌词
            int currentIndex = (int) event.getObj();
            LogUtil.e("lCurrentIndex = "+currentIndex);
            Sentence lSentence = Constants.LRC_WORD_DATA.get(currentIndex);
            tv.setText(lSentence.getContent());
        }
    }
}
