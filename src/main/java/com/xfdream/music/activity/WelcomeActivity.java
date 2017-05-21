package com.xfdream.music.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.xfdream.music.R;
import com.xfdream.music.dao.SongDao;
import com.xfdream.music.data.SystemSetting;
import com.xfdream.music.util.Constants;
import com.xfdream.music.util.HttpUtils;
import com.xfdream.music.util.MusicUtils;
import com.xfdream.music.util.XmlUtil;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;

import static com.xfdream.music.util.Constants.HELLO_STATE;
import static com.xfdream.music.util.Constants.HELLO_STATE_NO;
import static com.xfdream.music.util.Constants.TrafficOnline;

/**
 * 程序入口
 * Created by Dream on 16/6/6.
 */
public class WelcomeActivity extends BaseActivity{

    private static final String TAG = "WelcomeActivity-----";

    private SongDao mMusicDao;
    private Handler mHandler;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private OutputStream os;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //取消标题栏
        //setTitleVisable(false);

        //沉浸状态栏
        setImmersive(true);

        //准备主页数据
        /**
         * 准备主页数据：当数据准备完成以后进入主页
         *
         * 访问本地数据库  文件  网络都是耗时操作：非UI线程
         */
        mMusicDao = new SongDao(this);

        mHandler = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                //进入主页：添加一个动画

                // startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

                //第一步: 准备资源图片
                //第二步: 编写布局文件
                //第三步: 实现功能
                initWelComeAnimator();
                initLogoAnimator();
            }
        };

        initData();
    }

    /**
     * 检测从本地数据库查看是否有音乐数据：扫描  耗时
     */
    private void initData() {
        new Thread(new Runnable() {
            public void run() {
                if(mMusicDao.searchAll().size()>0){//本地有数据
                    //如果本地有数据，等待3秒跳转到主页
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(), 3000);
                }else{//本地数据库没有数据，那么就要到手机系统数据库中去数据
                    mHandler.sendEmptyMessage(1);
                }
                if (new SystemSetting(WelcomeActivity.this,false).getBooleanValue(TrafficOnline)){
                    Constants.WEB_SONG_DATA = HttpUtils.getWebSongs(Constants.WEB_KEYWORD);
                }else {
                    Constants.XML_SONG_DATA = XmlUtil.parseWebSongList(WelcomeActivity.this);
                }
                Constants.LOCAL_SONG_DATA = new SongDao(WelcomeActivity.this).searchAll();
                if (HELLO_STATE.equals(HELLO_STATE_NO)){
                    MediaPlayer lMediaPlayer = MediaPlayer.create(WelcomeActivity.this, R.raw.kg_hello_01);
                    if (lMediaPlayer!=null){
                        lMediaPlayer.start();
                    }
                }
            }
        }).start();
    }

    private void initWelComeAnimator(){
        ImageView iv_slogan = (ImageView) findViewById(R.id.iv_slogan);
        //属性动画实现
        //第一个参数---target:代表我们要给那个视图添加动画
        //第二个参数---propertyName:代表动画类型
        //第三个参数---动画开始的时候的位置,或者说状态
        //第四个参数---动画结束的时候的位置,或者说状态
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv_slogan,"alpha",0.0f,1.0f);
        //动画的时间
        objectAnimator.setDuration(5000);
        //启动动画
        objectAnimator.start();

        //给我们的动画添加监听
        //牵涉到架构设计中的: 适配器模式
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //启动新的页面---页码跳转(并判断是否是第一次登陆 若是则显示新手引导页)
                preferences = getSharedPreferences("count",MODE_WORLD_READABLE);
                int count = preferences.getInt("count", 0);
                //判断是不是首次登录
                //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
                if (count == 0) {
                    startActivity(new Intent(getApplicationContext(),GuideActivity.class));
                    overridePendingTransition(R.anim.tween_enter, R.anim.tween_exit);
                    finish();
                }
                editor = preferences.edit();
                //存入数据
                editor.putInt("count", ++count);
                //提交修改
                editor.commit();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(R.anim.tween_enter, R.anim.tween_exit);
                finish();
            }
        });
    }

    private void initLogoAnimator(){
        //给我们的Logo添加动画
        ImageView iv_logo = (ImageView) findViewById(R.id.iv_logo);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(iv_logo,"scaleX",1.0f,1.2f,1.0f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(iv_logo,"scaleY",1.0f,1.2f,1.0f);
        //通过动画集合组合并且执行动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        //这两个动画同时执行
        animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet.start();
    }

}
