package com.xfdream.music.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xfdream.music.R;
import com.xfdream.music.adapter.MenuAdapter;
import com.xfdream.music.data.SystemSetting;

public class BaseActivity extends AppCompatActivity {
    public static final String BROADCASTRECEVIER_ACTON = "com.xfdream.music.recevier.commonrecevier";
    private CommonRecevier commonRecevier;
    public float brightnesslevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 竖屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        // 设置皮肤背景
        SystemSetting setting = new SystemSetting(this, false);
        String brightness = setting.getValue(SystemSetting.KEY_BRIGHTNESS);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        brightnesslevel = attributes.screenBrightness;
        if (brightness != null && brightness.equals("0")) {//夜间模式
            attributes.screenBrightness = SystemSetting.KEY_DARKNESS;
            getWindow().setAttributes(attributes);
        }
        this.getWindow().setBackgroundDrawableResource(
                setting.getCurrentSkinResId());
        commonRecevier = new CommonRecevier();
    }

    /**
     * 设置正常模式和夜间模式
     */
    public void setBrightness(View v) {
        SystemSetting setting = new SystemSetting(this, true);
        String brightness = setting.getValue(SystemSetting.KEY_BRIGHTNESS);
        //MenuAdapter.ViewHolder viewHolder = (MenuAdapter.ViewHolder) v.getTag();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        if (brightness != null && brightness.equals("0")) {//夜间模式
            /*viewHolder.tv_title.setText(getResources().getString(R.string.darkness_title));
            viewHolder.btn_menu.setBackgroundResource(R.drawable.btn_menu_darkness);*/
            attributes.screenBrightness = brightnesslevel;
            setting.setValue(SystemSetting.KEY_BRIGHTNESS, "1");
            getWindow().setAttributes(attributes);
        } else {//正常模式
            //viewHolder.tv_title.setText(getResources().getString(R.string.brightness_title));
            //viewHolder.btn_menu.setBackgroundResource(R.drawable.btn_menu_brightness);
            attributes.screenBrightness = SystemSetting.KEY_DARKNESS;
            setting.setValue(SystemSetting.KEY_BRIGHTNESS, "0");
            getWindow().setAttributes(attributes);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(commonRecevier, new IntentFilter(BROADCASTRECEVIER_ACTON));
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(commonRecevier);
    }


    public class CommonRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    /**
     * 是否设置沉浸式状态栏
     *
     * @param action
     */
    public void setImmersive(boolean action) {
        if (action) {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 是否设置全屏
     *
     * @param action
     */
    public void setFullScreen(boolean action) {
        if (action) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
