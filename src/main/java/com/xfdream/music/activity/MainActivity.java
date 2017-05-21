package com.xfdream.music.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.xfdream.music.R;
import com.xfdream.music.adapter.DownLoadListAdapter;
import com.xfdream.music.adapter.DownLoadingListAdapter;
import com.xfdream.music.adapter.ListItemAdapter;
import com.xfdream.music.adapter.MenuAdapter;
import com.xfdream.music.adapter.SongItemAdapter;
import com.xfdream.music.adapter.SongItemWebAdapter;
import com.xfdream.music.custom.CircleImgView;
import com.xfdream.music.custom.FlingGalleryView;
import com.xfdream.music.custom.XfDialog;
import com.xfdream.music.custom.XfMenu;
import com.xfdream.music.dao.AlbumDao;
import com.xfdream.music.dao.ArtistDao;
import com.xfdream.music.dao.DownLoadInfoDao;
import com.xfdream.music.dao.PlayerListDao;
import com.xfdream.music.dao.SongDao;
import com.xfdream.music.data.SystemSetting;
import com.xfdream.music.entity.PlayerList;
import com.xfdream.music.entity.Song;
import com.xfdream.music.lrc.LrcHandle;
import com.xfdream.music.message.CommonMessage;
import com.xfdream.music.recevier.AutoShutdownRecevier;
import com.xfdream.music.service.DownLoadManager;
import com.xfdream.music.service.FlowLrcService;
import com.xfdream.music.service.MediaPlayerManager;
import com.xfdream.music.util.Common;
import com.xfdream.music.util.Constants;
import com.xfdream.music.util.HttpUtils;
import com.xfdream.music.util.LogUtil;
import com.xfdream.music.util.MusicUtils;
import com.xfdream.music.util.StringHelper;
import com.xfdream.music.util.ToastUtil;
import com.xfdream.music.util.XmlUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.xfdream.music.data.SystemSetting.KEY_GENERAL_SCANNERTIP;
import static com.xfdream.music.data.SystemSetting.KEY_ISSCANNERTIP;
import static com.xfdream.music.service.MediaPlayerManager.PLAYERFLAG_WEB;
import static com.xfdream.music.util.Constants.AutoUpdate;
import static com.xfdream.music.util.Constants.BG_CURRENT_PATH;
import static com.xfdream.music.util.Constants.CURRENT_LRC_HANDLER;
import static com.xfdream.music.util.Constants.HELLO_STATE;
import static com.xfdream.music.util.Constants.HELLO_STATE_NO;
import static com.xfdream.music.util.Constants.HELLO_STATE_OFF;
import static com.xfdream.music.util.Constants.LOCAL_SONG_DATA;
import static com.xfdream.music.util.Constants.LRC_CURRENT_INDEX;
import static com.xfdream.music.util.Constants.LRC_WORD_DATA;
import static com.xfdream.music.util.Constants.LongLight;
import static com.xfdream.music.util.Constants.MSG_SEND_SONG_TEXT;
import static com.xfdream.music.util.Constants.MSG_SONG_PLAY_OVER;
import static com.xfdream.music.util.Constants.MemoryExit;
import static com.xfdream.music.util.Constants.Shade;
import static com.xfdream.music.util.Constants.ShowDeskLrc;
import static com.xfdream.music.util.Constants.ShowFlowTag;
import static com.xfdream.music.util.Constants.ShowLockLrc;
import static com.xfdream.music.util.Constants.ShowLrc;
import static com.xfdream.music.util.Constants.ShowPlayLrc;
import static com.xfdream.music.util.Constants.TrafficLrc;
import static com.xfdream.music.util.Constants.TrafficOnline;
import static com.xfdream.music.util.Constants.TrafficPhoto;
import static com.xfdream.music.util.Constants.TryListener;
import static com.xfdream.music.util.Constants.WEB_SONG_DATA;
import static com.xfdream.music.util.Constants.WifiNet;
import static com.xfdream.music.util.Constants.XML_SONG_DATA;
import static com.xfdream.music.util.Constants.isAutoUpdate;
import static com.xfdream.music.util.Constants.isLongLight;
import static com.xfdream.music.util.Constants.isMemoryExit;
import static com.xfdream.music.util.Constants.isShade;
import static com.xfdream.music.util.Constants.isShowDeskLrc;
import static com.xfdream.music.util.Constants.isShowFlowTag;
import static com.xfdream.music.util.Constants.isShowLockLrc;
import static com.xfdream.music.util.Constants.isShowLrc;
import static com.xfdream.music.util.Constants.isShowPlayLrc;
import static com.xfdream.music.util.Constants.isTrafficLrc;
import static com.xfdream.music.util.Constants.isTrafficOnline;
import static com.xfdream.music.util.Constants.isTrafficPhoto;
import static com.xfdream.music.util.Constants.isTryListener;
import static com.xfdream.music.util.Constants.isWifiNet;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int XF_SOUND_CODE = 3;
    public float brightnesslevel = 0;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final int EFFECTS_PANEL = 0;

    private RelativeLayout mRlParentContent;

    //导航栏选项卡布局数组
    private ViewGroup[] vg_list_tab_item = new ViewGroup[3];
    private FlingGalleryView fgv_list_main;

    //当前屏幕的下标
    private int screenIndex = 0;
    //导航栏的内容
    private String[] list_item_items;
    //导航栏的icon
    private int[] list_item_icons = new int[] { R.drawable.list_music_icon,
            R.drawable.list_web_icon, R.drawable.list_download_icon };

    //本地列表
    private ViewGroup list_main_music;
    //网络音乐
    private ViewGroup list_main_web;
    //下载管理
    private ViewGroup list_main_download;

    //主屏幕内容布局
    private ViewGroup rl_list_main_content;
    //切换内容布局
    private ViewGroup rl_list_content;

    //本地音乐和下载管理的二三级布局
    private ImageButton ibtn_list_content_icon;//左边图标
    private ImageButton ibtn_list_content_do_icon;//右边图标
    private TextView tv_list_content_title;//标题
    private ListView lv_list_change_content;//替换ListView
    private Button btn_list_random_music2;//随机播放

    //本地音乐随机播放
    private Button btn_list_random_music_local;
    //网络音乐随机播放
    private Button btn_list_random_music_web;

    //网络音乐播放列表
    private ListView lv_list_web;

    //底部工具栏
    private CircleImgView ibtn_player_albumart;//专辑封面
    private ImageButton ibtn_player_control;//播放/暂停
    private TextView tv_player_title;//播放歌曲 歌手-标题
    //private ProgressBar pb_player_progress;//播放进度条
    private SeekBar pb_player_progress;//播放进度条
    private TextView tv_player_currentPosition;//当前播放的进度
    private TextView tv_player_duration;//歌曲播放时长

    /**
     * 默认页：0
     * 1.全部歌曲 2.歌手 3.专辑 4.文件夹 5.播放列表 6.我最爱听 7.最近播放 8.正在下载 9.下载完成
     * 22.歌手二级 33.专辑二级 44.文件夹二级 55.播放列表二级
     * */
    private int pageNumber=0;

    private SongDao songDao;
    private ArtistDao artistDao;
    private AlbumDao albumDao;
    private PlayerListDao playerListDao;
    private Toast toast;
    private ViewGroup.LayoutParams params;
    private LayoutInflater inflater;
    private DownLoadInfoDao downLoadInfoDao;

    private DownLoadManager downLoadManager;
    private DownLoadBroadcastRecevier downLoadBroadcastRecevier;

    public static MediaPlayerManager mediaPlayerManager;
    private MediaPlayerBroadcastReceiver mediaPlayerBroadcastReceiver;

    private XfMenu xfMenu;

    private ImageButton btn_header_change_skin;
    private static int drawerState = 0;
    private static Boolean isDrawerOpenState = false;

    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private Handler mHandler = null;

    private int mCurrentPosition = 1;
    public static MainActivity mContext;
    private SystemSetting mSetting;

    public static View rootView;
    private List<Song> mWebSongData;
    private SongItemWebAdapter mSongItemWebAdapter;

    public static int mFlowTagCount = 5;

    private Song mCurrentSong;
    private int currentTime = 0;
    private String mLocalPath;
    public static LrcHandle lrcHandler;

    private AudioManager audioManager;// 获取系统音频对象
    private boolean isSeekDrag = false;//进度是否在拖动
    private List<Song> mLocalSongData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        //沉浸状态栏
        //setImmersive(true);

        mHandler = new Handler();

        //全屏
        setFullScreen(true);

        rootView = findViewById(R.id.rl_root);
        if (savedInstanceState == null) {
            rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    rootView.getViewTreeObserver().removeOnPreDrawListener(this);
                    startRootAnimation();
                    return true;
                }
            });
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        //动画
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpenState = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpenState = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING && isDrawerOpenState){
                    openMenuAnim();
                }
            }
        });

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mContext = MainActivity.this;

        //initSlideView();

        //初始化主页
        initListMainContent();

        //initLrc();

        initMenu();

    }

    private void openMenuAnim() {
        int lScreenWidth = ScreenUtils.getScreenWidth(MainActivity.this);
        int lScreenHeight = ScreenUtils.getScreenHeight(MainActivity.this);

                /*RotateAnimation lRotateAnimation = new RotateAnimation(0, 180,lScreenWidth/2,lScreenHeight/2);
                lRotateAnimation.setDuration(1000);*/
        ScaleAnimation lScaleAnimation = new ScaleAnimation(0, 1, 0, 1, 0, lScreenHeight / 2);
        lScaleAnimation.setDuration(1000);

        AlphaAnimation lAlphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        lAlphaAnimation.setDuration(1000);

        AnimationSet lAnimationSet = new AnimationSet(getApplicationContext(), null);
        lAnimationSet.addAnimation(lScaleAnimation);
        lAnimationSet.addAnimation(lAlphaAnimation);
        lAnimationSet.setDuration(1000);
        //动画集
        mRlParentContent.setAnimation(lAnimationSet);
    }

    @Override
    protected void onResume() {
        super.onResume();

/*        //播放器管理
        if (mediaPlayerManager == null){
            mediaPlayerManager=new MediaPlayerManager(this);
        }
        mediaPlayerManager.setConnectionListener(mConnectionListener);*/

        updatePlayPannel();

        //mRlParentContent.setBackgroundResource(SystemSetting.SKIN_RESOURCES[new SystemSetting(this,true).getCurrentSkinId()]);
        initLrc();
        initMenu();

    }

    /**
     * 初始化歌词部分
     */
    public void initLrc() {
        int lSongId = mediaPlayerManager.getSongId();
        int lPlayerFlag = mediaPlayerManager.getPlayerFlag();
        if (lPlayerFlag == PLAYERFLAG_WEB){
            mCurrentSong = HttpUtils.querySongBySongId(lSongId);
        }else {
            mCurrentSong = new SongDao(MainActivity.this).searchById(lSongId);
        }
        String lLyricPath = null;
        if (mCurrentSong!=null){
            lLyricPath = mCurrentSong.getLyricPath();
        }
        lrcHandler = new LrcHandle();
        currentTime = mediaPlayerManager.getPlayerProgress();
        if (mCurrentSong!=null){
            mLocalPath = Constants.LRC_DIRS_PATH + File.separator + StringHelper.getPingYin(mCurrentSong.getName()) + ".lrc";
            initLrchandler(mCurrentSong, lLyricPath);
            LRC_CURRENT_INDEX = Common.getCurrentIndex(currentTime);
        }
    }

    private void initLrchandler(final Song song, String lyricPath) {
        File lFile = new File(mLocalPath);
        if (lyricPath != null && !lyricPath.equals("")) {
            lrcHandler.readLRC(lyricPath);
            //tv_player_lyric_info.setText("");
        } else if (lFile.exists()) {
            lrcHandler.readLRC(mLocalPath);
            //tv_player_lyric_info.setText("");
        } else {
            //network download lrc
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpUtils.netLoadLrcByDuration(song.getName(), song.getDurationTime());
                }
            }).start();
        }
        if (lrcHandler!=null){
            lrcHandler.setCurrentTime(currentTime);
            LRC_WORD_DATA = lrcHandler.getWords();
            CURRENT_LRC_HANDLER = lrcHandler;
        }
    }


    /**
     * 菜单功能实现
     */
    private void initMenu() {
        //网络运营商
        if (mSetting.getBooleanValue(TrafficPhoto)){    //流量下载图片
            ToastUtil.show(MainActivity.this,"允许流量下载图片");
        }
        if (mSetting.getBooleanValue(TrafficLrc)){      //流量下载歌词
            ToastUtil.show(MainActivity.this,"允许流量下载歌词");
        }
        if (mSetting.getBooleanValue(TrafficOnline)){   //在线听歌
            ToastUtil.show(MainActivity.this,"允许流量在线听歌");
        }
        /*if (mSetting.getBooleanValue(WifiNet)){

        }else{
            ToastUtil.show(MainActivity.this,"只能在WiFi状态下联网");
        }*/
/*        if (mSetting.getBooleanValue(ShowFlowTag)){     //播放器显示--显示悬浮便签
            Constants.MAX_WORLDS = mFlowTagCount;
            LogUtil.e("mFlowTagCount = "+mFlowTagCount);
        }else {
            mFlowTagCount = Constants.MAX_WORLDS;
            Constants.MAX_WORLDS = 0;
        }*/
        //歌词设置
        if (mSetting.getBooleanValue(ShowPlayLrc)){    //显示播放界面歌词

        }

        Intent serviceIntent = new Intent(mContext, FlowLrcService.class);
        if (mSetting.getBooleanValue(ShowDeskLrc)){      //显示桌面界面歌词
            mContext.startService(serviceIntent);
        }else {
            mContext.stopService(serviceIntent);
        }

        if (mSetting.getBooleanValue(ShowLockLrc)){   //显示锁屏界面歌词

        }
        /*if (mSetting.getBooleanValue(ShowLrc)){
        }*/
        //其他设置
        if (mSetting.getBooleanValue(TryListener)){    //试听模式
            int lPlayerProgress = mediaPlayerManager.getPlayerProgress()/1000;
            if (lPlayerProgress>10){
                mediaPlayerManager.nextPlayer();
            }
        }
        /*if (mSetting.getBooleanValue(LongLight)){      //屏幕背光常亮

        }*/
        if (mSetting.getBooleanValue(MemoryExit)){   //记忆退出位置

        }
        /*if (mSetting.getBooleanValue(Shade)){   //切换音乐渐变

        }*/
        //更新设置
        if (mSetting.getBooleanValue(AutoUpdate)){  //自动检测更新
            String lVersion = Common.getVersion(this);
            ToastUtil.show(this,"您已开启自动检测新版本功能，当前版本为："+lVersion);
        }
    }

    private void startRootAnimation() {
        rootView.setScaleY(0.3f);
        rootView.setPivotY(rootView.getY()+rootView.getHeight()/2);
        rootView.animate()
                .scaleY(1)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {   //设置
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }else if (id == R.id.action_share){      //分享
            shareCurrentTrack();
        }else if (id == R.id.action_search){      //搜索
            searchLocalMusic();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * search 本地歌曲
     */
    private void searchLocalMusic() {
        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_message_text) {
            // Handle the camera action
            messageCenter();
        } else */
        if (id == R.id.nav_sleep_text) {
            timeClose();
        } else if (id == R.id.nav_brightness_text) {
            setBrightness(item.getActionView());
        } else if (id == R.id.nav_about) {
            about();
        } else if (id == R.id.nav_sound_text) {
            Intent i = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
            //i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, MusicUtils.getCurrentAudioId());
            i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayerManager.getSongId());
            startActivityForResult(i, EFFECTS_PANEL);
        } else if (id == R.id.nav_listen_song_text) {
            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            //Intent intent=new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
            startActivityForResult(intent, XF_SOUND_CODE);
        } else if (id == R.id.nav_hello_text) {
            helloModi();
        } else if (id == R.id.nav_flow_tag_text) {
            flowTag();
        } else if (id == R.id.nav_scanning_text) {
            scanner();
        }
        /*else if (id == R.id.nav_share) {
            shareCurrentTrack();
        } */
        else if (id == R.id.nav_setting_text) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            //startActivity(new Intent(MainActivity.this,ScrollingActivity.class));
        } else if (id == R.id.nav_exit_title) {
            exit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 开启魔笛音乐问候音
     */
    private void helloModi() {
        if (HELLO_STATE.equals(HELLO_STATE_NO)){
            HELLO_STATE = HELLO_STATE_OFF;
            ToastUtil.show(MainActivity.this,"提示音已关闭");
        }else {
            HELLO_STATE = HELLO_STATE_NO;
            ToastUtil.show(MainActivity.this,"提示音已开启");
        }
    }

    private void exit() {
        cancelAutoShutdown();
        mediaPlayerManager.stop();
        downLoadManager.stop();
        finish();
    }

    private void flowTag() {
        final EditText et_alarmTime = new EditText(MainActivity.this);
        et_alarmTime.setKeyListener(new DigitsKeyListener());
        et_alarmTime.setHint(R.string.flow_tag_count);
        et_alarmTime.setLayoutParams(params);
        et_alarmTime.setTextSize(12);
        new XfDialog.Builder(MainActivity.this).setTitle(R.string.flow_tag)
                .setView(et_alarmTime).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
                String str = et_alarmTime.getText().toString();

                if (!TextUtils.isEmpty(str)) {
                    int flowTagCount = Integer.valueOf(str);
                    if (flowTagCount != 0) {
                        Constants.MAX_WORLDS = flowTagCount;
                    }
                }
            }
        }).setNegativeButton(getResources().getString(R.string.cancel), null).create().show();
    }

    //about
    private void about() {
        startActivity(new Intent(getApplicationContext(),ScrollingActivity.class));

    }

    private void timeClose() {
        final SystemSetting setting = new SystemSetting(MainActivity.this, true);
        final String autotime = setting.getValue(SystemSetting.KEY_AUTO_SLEEP);

        final EditText et_alarmTime = new EditText(MainActivity.this);
        et_alarmTime.setKeyListener(new DigitsKeyListener());
        et_alarmTime.setHint(R.string.time_close_hint);

        if (!TextUtils.isEmpty(autotime)) {
            et_alarmTime.setText(autotime);
        }
        et_alarmTime.setLayoutParams(params);
        et_alarmTime.setTextSize(12);
        new XfDialog.Builder(MainActivity.this).setTitle(R.string.time_close)
                .setView(et_alarmTime).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent operation = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(MainActivity.this, AutoShutdownRecevier.class), PendingIntent.FLAG_UPDATE_CURRENT);
                String str = et_alarmTime.getText().toString();

                if (!TextUtils.isEmpty(autotime)) {
                    alarmManager.cancel(operation);
                }
                if (!TextUtils.isEmpty(str)) {
                    int autotime = Integer.valueOf(str);
                    if (autotime != 0) {
                        setting.setValue(SystemSetting.KEY_AUTO_SLEEP, String.valueOf(autotime));
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + autotime * 60 * 1000, operation);
                    }
                } else {
                    setting.setValue(SystemSetting.KEY_AUTO_SLEEP, "");
                }
            }
        }).setNegativeButton(getResources().getString(R.string.cancel), null).create().show();
    }


    private void messageCenter() {

    }

    /**
     * 切换壁纸
     *
     * @param v
     */
    public void ibtnChangeBg(View v) {
        startActivity(new Intent(MainActivity.this, SkinSettingActivity.class));
        //startActivity(new Intent(MainActivity.this,SkinPicActivity.class));
    }

    /**
     * @return Share intent
     * @throws RemoteException
     */
    private String shareCurrentTrack() {
        Song lSong = songDao.searchById(mediaPlayerManager.getSongId());

        Intent shareIntent = new Intent();
        String currentTrackMessage = getResources().getString(R.string.now_listening_to) + " "
                + lSong.getName() + getResources().getString(R.string.by) + " "
                + lSong.getArtist().getName();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, currentTrackMessage);

        startActivity(Intent.createChooser(shareIntent,
                getResources().getString(R.string.share_track_using)));
        return currentTrackMessage;
    }

    // ----------------------------------ListMainContent初始化--------------------------------------

    /**
     * 初始化ListMainContent
     */
    public void initListMainContent() {

        mRlParentContent = (RelativeLayout) this.findViewById(R.id.rl_parent_content);

        mSetting = new SystemSetting(this, false);
        mSetting.setValue(KEY_GENERAL_SCANNERTIP,"DEFAULT");
        //String isStartup = mSetting.getValue(SystemSetting.KEY_ISSTARTUP);

        //启动新的页面---页码跳转(并判断是否是第一次登陆 若是则显示新手引导页)
        preferences = getSharedPreferences("isStartup",MODE_WORLD_READABLE);
        boolean isStartup = preferences.getBoolean("isStartup",true);
        //判断是不是首次登录
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        //检查Loading加载页面
        //if (isStartup == null || isStartup.equals("true")) {
        if (isStartup) {
            startActivity(new Intent(this, GuideActivity.class));
            this.finish();
            editor = preferences.edit();
            //存入数据
            editor.putBoolean("isStartup",false);
            //提交修改
            editor.commit();
        } else {
            checkScannerTip(mSetting);
            //初始化默认设置
            initDefaultSetting();
        }

        songDao = new SongDao(this);
        artistDao = new ArtistDao(this);
        albumDao = new AlbumDao(this);
        playerListDao = new PlayerListDao(this);
        downLoadInfoDao = new DownLoadInfoDao(this);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        //导航栏选项卡数组 实例化
        vg_list_tab_item[0] = (ViewGroup) this
                .findViewById(R.id.list_tab_item_music);
        vg_list_tab_item[1] = (ViewGroup) this
                .findViewById(R.id.list_tab_item_web);
        vg_list_tab_item[2] = (ViewGroup) this
                .findViewById(R.id.list_tab_item_download);

        //主屏幕内容布局选项 实例化
        list_main_music = (ViewGroup) this.findViewById(R.id.list_main_music);
        list_main_web = (ViewGroup) this.findViewById(R.id.list_main_web);
        list_main_download = (ViewGroup) this.findViewById(R.id.list_main_download);

        //随机播放音乐
        btn_list_random_music_local=(Button)list_main_music.findViewById(R.id.btn_list_random_music);
        btn_list_random_music_web=(Button)list_main_web.findViewById(R.id.btn_list_random_web);
        btn_list_random_music_local.setOnClickListener(btn_randomPlayerListener);
        btn_list_random_music_web.setOnClickListener(btn_randomPlayerListener);

        //主屏幕内容布局和切换内容布局 实例化
        rl_list_main_content=(ViewGroup)this.findViewById(R.id.rl_list_main_content);
        rl_list_content=(ViewGroup)this.findViewById(R.id.rl_list_content);

        //本地音乐和下载管理的二三级布局-公共 标题和内容区域
        ibtn_list_content_icon=(ImageButton)rl_list_content.findViewById(R.id.ibtn_list_content_icon);
        ibtn_list_content_do_icon=(ImageButton)rl_list_content.findViewById(R.id.ibtn_list_content_do_icon);
        tv_list_content_title=(TextView)rl_list_content.findViewById(R.id.tv_list_content_title);
        lv_list_change_content=(ListView)rl_list_content.findViewById(R.id.lv_list_change_content);
        ibtn_list_content_icon.setOnClickListener(imageButton_listener);
        ibtn_list_content_do_icon.setOnClickListener(imageButton_listener);
        lv_list_change_content.setOnItemClickListener(list_change_content_listener);
        lv_list_change_content.setOnItemLongClickListener(list_change_content_looglistener);
        btn_list_random_music2=(Button)rl_list_content.findViewById(R.id.btn_list_random_music2);
        btn_list_random_music2.setOnClickListener(btn_randomPlayerListener);

        //底部工具栏
        ibtn_player_albumart=(CircleImgView) this.findViewById(R.id.ibtn_player_albumart);
        ibtn_player_control=(ImageButton)this.findViewById(R.id.ibtn_player_control);
        tv_player_title=(TextView)this.findViewById(R.id.tv_player_title);
        //pb_player_progress=(ProgressBar)this.findViewById(R.id.pb_player_progress);
        pb_player_progress=(SeekBar)this.findViewById(R.id.pb_player_progress);
        pb_player_progress.setOnSeekBarChangeListener(seekBarChangeListener);

        tv_player_currentPosition=(TextView)this.findViewById(R.id.tv_player_currentPosition);
        tv_player_duration=(TextView)this.findViewById(R.id.tv_player_duration);

        ibtn_player_albumart.setOnClickListener(imageButton_listener);
        ibtn_player_control.setOnClickListener(imageButton_listener);

        //切换主屏幕内容容器
        fgv_list_main = (FlingGalleryView) rl_list_main_content
                .findViewById(R.id.fgv_list_main);
        fgv_list_main.setDefaultScreen(screenIndex);
        fgv_list_main.setOnScrollToScreenListener(scrollToScreenListener);

        //从资源文件中获取导航栏选项卡标题
        list_item_items = getResources().getStringArray(R.array.list_tab_items);
        //初始化导航栏
        initTabItem();
        //初始化本地音乐内容区域
        initListMusicItem();
        //初始化网络音乐
        lv_list_web=(ListView)list_main_web.findViewById(R.id.lv_list_web);
        //设置网络歌曲列表数据源
        final String keyword = Constants.WEB_KEYWORD;
        mWebSongData = WEB_SONG_DATA;
        mLocalSongData = XML_SONG_DATA;
        if (mLocalSongData==null){
            mLocalSongData = XmlUtil.parseWebSongList(this);
        }
        /*if (isWifiNet){
            if (WEB_SONG_DATA!=null&&WEB_SONG_DATA.size()>0){
                mWebSongData = WEB_SONG_DATA;
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebSongData = HttpUtils.getWebSongs(keyword);
                    }
                });
            }
        }else {
            ToastUtil.show(this,"WiFi状态下可以在线听歌哦");
        }*/
        if (mWebSongData !=null&& mWebSongData.size()>0){
            mSongItemWebAdapter = new SongItemWebAdapter(this, mWebSongData).setItemListener(mItemListener);
        }else {
            mSongItemWebAdapter = new SongItemWebAdapter(this, mLocalSongData).setItemListener(mItemListener);
        }
        lv_list_web.setAdapter(mSongItemWebAdapter);
        lv_list_web.setOnItemClickListener(webItemClickListener);

        //初始化下载管理
        initDownLoad();

        //下载管理
        downLoadManager=new DownLoadManager(this);
        downLoadManager.startAndBindService();

        //播放器管理
        /*if (mediaPlayerManager == null){
            mediaPlayerManager=new MediaPlayerManager(this);
        }
        mediaPlayerManager.setConnectionListener(mConnectionListener);*/

        createMenu();
    }

    /**
     * 初始化默认设置
     */
    private void initDefaultSetting() {
        mSetting.setValue(WifiNet, isWifiNet);
        mSetting.setValue(TrafficPhoto, isTrafficPhoto);
        mSetting.setValue(TrafficLrc, isTrafficLrc);
        mSetting.setValue(TrafficOnline, isTrafficOnline);
        mSetting.setValue(ShowFlowTag, isShowFlowTag);
        mSetting.setValue(ShowLrc, isShowLrc);
        mSetting.setValue(ShowPlayLrc, isShowPlayLrc);
        mSetting.setValue(ShowDeskLrc, isShowDeskLrc);
        mSetting.setValue(ShowLockLrc, isShowLockLrc);
        mSetting.setValue(TryListener, isTryListener);
        mSetting.setValue(LongLight, isLongLight);
        mSetting.setValue(MemoryExit, isMemoryExit);
        mSetting.setValue(Shade, isShade);
        mSetting.setValue(AutoUpdate, isAutoUpdate);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (seekBar.getId() == R.id.sb_player_playprogress) {
                isSeekDrag = false;
                mediaPlayerManager.seekTo(seekBar.getProgress());
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (seekBar.getId() == R.id.sb_player_playprogress) {
                isSeekDrag = true;
                tv_player_currentPosition.setText(Common.formatSecondTime(seekBar.getProgress()));
            }
        }

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (seekBar.getId() == R.id.sb_player_playprogress) {
                if (isSeekDrag) {
                    tv_player_currentPosition.setText(Common.formatSecondTime(progress));
                }
            }

            if (mSetting.getBooleanValue(TryListener)){
                int millions = progress / 1000;
                if (millions>10){
                    mediaPlayerManager.nextPlayer();
                }
            }

            currentTime = progress;
            LRC_CURRENT_INDEX = Common.getCurrentIndex(currentTime);
            //给桌面歌词发送数据
            LogUtil.e("currentTime = "+currentTime);
            EventBus.getDefault().post(new CommonMessage(MSG_SEND_SONG_TEXT,LRC_CURRENT_INDEX));
        }
    };

    /**
     * 检查显示扫描歌曲提示(第一次进入软件界面)
     * */
    private void checkScannerTip(SystemSetting setting){
        if(setting.getValue(KEY_ISSCANNERTIP)==null){
            scanner();
            setting.setValue(KEY_ISSCANNERTIP, "OK");
        }
    }

    private void scanner() {
        new XfDialog.Builder(this).setTitle("扫描提示").setMessage("是否要扫描本地歌曲入库？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
                Intent it=new Intent(MainActivity.this,ScanMusicActivity.class);
                startActivityForResult(it, 1);
            }
        }).setNegativeButton("取消", null).create().show();
    }

    /**
     * 随机播放
     * */
    private View.OnClickListener btn_randomPlayerListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //初始化歌词
            initLrc();
            if(mediaPlayerManager.getPlayerFlag()==MediaPlayerManager.PLAYERFLAG_WEB){
                int[] playerInfo=new int[]{-1,-1};
                ((SongItemWebAdapter)lv_list_web.getAdapter()).setPlayerInfo(playerInfo);
            }
            if(v.getId()==R.id.btn_list_random_music){
                mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_ALL, "");
            }else if(v.getId()==R.id.btn_list_random_web){
                if(!Common.getNetIsAvailable(MainActivity.this)){
                    toast=Common.showMessage(toast, MainActivity.this, "当前网络不可用");
                    return;
                }
                mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_WEB, "");
            }else if(v.getId()==R.id.btn_list_random_music2){
                if(Integer.valueOf(v.getTag().toString())==0){
                    return;
                }
                if(pageNumber==1){
                    mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_ALL, "");
                }else if(pageNumber==6){
                    mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_LIKE, "");
                }else if(pageNumber==7){
                    mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_LATELY, "");
                }else if(pageNumber==9){
                    mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_DOWNLOAD, "");
                }else if(pageNumber==22){
                    mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_ARTIST, condition);
                }else if(pageNumber==33){
                    mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_ALBUM, condition);
                }else if(pageNumber==44){
                    mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_FOLDER, condition);
                }else if(pageNumber==55){
                    mediaPlayerManager.randomPlayer(MediaPlayerManager.PLAYERFLAG_PLAYERLIST, condition);
                }
            }
        }
    };

    //网络音乐，点击播放/暂停
    private AdapterView.OnItemClickListener webItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if(!Common.getNetIsAvailable(MainActivity.this)){
                toast=Common.showMessage(toast, MainActivity.this, "当前网络不可用");
                return;
            }
            int songId=Integer.valueOf(((SongItemWebAdapter.ViewHolder)view.getTag()).tv_web_list_item_top.getTag().toString());
            if(songId==mediaPlayerManager.getSongId()){
                PlayerOrPause(view);
            }else {
                ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_pause);
                mediaPlayerManager.player(songId,MediaPlayerManager.PLAYERFLAG_WEB, null);
                int[] playerInfo=new int[]{songId,mediaPlayerManager.getPlayerState()};
                ((SongItemWebAdapter)lv_list_web.getAdapter()).setPlayerInfo(playerInfo);
            }
        }
    };

    private MediaPlayerManager.ServiceConnectionListener mConnectionListener=new MediaPlayerManager.ServiceConnectionListener() {
        @Override
        public void onServiceDisconnected() {
        }
        @Override
        public void onServiceConnected() {
            updatePlayPannel();
        }
    };

    /**
     * 更新播放控制面板信息
     */
    private void updatePlayPannel() {
        //每次进入activity时都要重新拿数据
        mediaPlayerManager.initPlayerMain_SongInfo();   //modi 更新播放控制面板信息
        updateSongItemList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //播放器管理
        if (mediaPlayerManager == null){
            mediaPlayerManager=new MediaPlayerManager(this);
        }
        mediaPlayerManager.setConnectionListener(mConnectionListener);

        //注册播放器-广播接收器
        mediaPlayerBroadcastReceiver=new MediaPlayerBroadcastReceiver();
        registerReceiver(mediaPlayerBroadcastReceiver, new IntentFilter(MediaPlayerManager.BROADCASTRECEVIER_ACTON));
        //注册下载任务-广播接收器
        downLoadBroadcastRecevier=new DownLoadBroadcastRecevier();
        registerReceiver(downLoadBroadcastRecevier, new IntentFilter(DownLoadManager.BROADCASTRECEVIER_ACTON));
        mediaPlayerManager.startAndBindService();
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayerManager!=null){
            unregisterReceiver(mediaPlayerBroadcastReceiver);
            unregisterReceiver(downLoadBroadcastRecevier);
            mediaPlayerManager.unbindService();
            //mediaPlayerManager = null;
        }
    }

    @Override
    protected void onDestroy() {
        downLoadManager.unbindService();
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }


    /**
     * 播放器-广播接收器
     * */
    private class MediaPlayerBroadcastReceiver extends BroadcastReceiver{

        private Bitmap mCachedArtwork;
        private Bitmap mDefaultArtWork;

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag=intent.getIntExtra("flag", -1);
            int lAlbum_id = intent.getIntExtra("album_id", -1);

            mDefaultArtWork = BitmapFactory.decodeResource(getResources(), R.drawable.zhangliangyin);
            if (lAlbum_id == -1){
                mCachedArtwork = mDefaultArtWork;
            }else {
                mCachedArtwork = MusicUtils.getCachedArtwork(MainActivity.this, lAlbum_id, mDefaultArtWork);
            }

            if(flag==MediaPlayerManager.FLAG_CHANGED){
                if (!isSeekDrag) {
                    int currentPosition = intent.getIntExtra("currentPosition", 0);
                    int duration = intent.getIntExtra("duration", 0);
                    tv_player_currentPosition.setText(Common.formatSecondTime(currentPosition));
                    tv_player_duration.setText(Common.formatSecondTime(duration));
                    pb_player_progress.setProgress(currentPosition);

                    //progress change
                    currentTime = currentPosition;

                    pb_player_progress.setMax(duration);
                }
                int currentPosition=intent.getIntExtra("currentPosition", 0);
                int duration=intent.getIntExtra("duration", 0);
                tv_player_currentPosition.setText(Common.formatSecondTime(currentPosition));
                tv_player_duration.setText(Common.formatSecondTime(duration));
                pb_player_progress.setProgress(currentPosition);
                pb_player_progress.setMax(duration);

                //modi 发送更新歌词界面消息
                //EventBus.getDefault().post(new CommonMessage(MSG_SONG_PLAY_OVER,null));
            }else if(flag==MediaPlayerManager.FLAG_PREPARE){
                String albumPic=intent.getStringExtra("albumPic");
                tv_player_title.setText(intent.getStringExtra("title"));
				/*if(TextUtils.isEmpty(albumPic)){
					ibtn_player_albumart.setImageResource(R.drawable.zhangliangyin);
				}else{
                    Bitmap bitmap=BitmapFactory.decodeFile(albumPic);
					//判断SD图片是否存在
					if(bitmap!=null){
						ibtn_player_albumart.setImageBitmap(mCachedArtwork);
					}else{
						ibtn_player_albumart.setImageResource(R.drawable.zhangliangyin);
					}
				}*/
                ibtn_player_albumart.setImageBitmap(mCachedArtwork);
                int duration=intent.getIntExtra("duration", 0);
                int currentPosition=intent.getIntExtra("currentPosition", 0);
                tv_player_currentPosition.setText(Common.formatSecondTime(currentPosition));
                tv_player_duration.setText(Common.formatSecondTime(duration));
                pb_player_progress.setMax(duration);
                pb_player_progress.setProgress(currentPosition);
                pb_player_progress.setSecondaryProgress(0);

                //更新播放列表状态
                updateSongItemList();
            }else if(flag==MediaPlayerManager.FLAG_INIT){//初始化播放信息
                int currentPosition=intent.getIntExtra("currentPosition", 0);
                int duration=intent.getIntExtra("duration", 0);
                pb_player_progress.setMax(duration);
                pb_player_progress.setProgress(currentPosition);
                tv_player_currentPosition.setText(Common.formatSecondTime(currentPosition));
                tv_player_duration.setText(Common.formatSecondTime(duration));
                tv_player_title.setText(intent.getStringExtra("title"));
                String albumPic=intent.getStringExtra("albumPic");
				/*if(TextUtils.isEmpty(albumPic)){
					ibtn_player_albumart.setImageResource(R.drawable.zhangliangyin);
				}else{
					Bitmap bitmap=BitmapFactory.decodeFile(albumPic);
					//判断SD卡图片是否存在
					if(bitmap!=null){
						ibtn_player_albumart.setImageBitmap(mCachedArtwork);
					}else{
						ibtn_player_albumart.setImageResource(R.drawable.zhangliangyin);
					}
				}*/
                ibtn_player_albumart.setImageBitmap(mCachedArtwork);
                int playerState=intent.getIntExtra("playerState", 0);
                if(playerState==MediaPlayerManager.STATE_PLAYER||playerState==MediaPlayerManager.STATE_PREPARE){//播放
                    ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_pause);
                    ibtn_player_albumart.startRotation();
                    initLrc();
                }else{
                    ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_player);
                    ibtn_player_albumart.stopRotation();
                }

                if(mediaPlayerManager.getPlayerState()==MediaPlayerManager.STATE_OVER){
                    if(pageNumber==1||pageNumber==6||pageNumber==7||pageNumber==22||pageNumber==33||pageNumber==44||pageNumber==55){
                        ((SongItemAdapter)lv_list_change_content.getAdapter()).setPlayerState(mediaPlayerManager.getPlayerState());
                    }
                    if(pageNumber==9){
                        ((DownLoadListAdapter)lv_list_change_content.getAdapter()).setPlayerState(mediaPlayerManager.getPlayerState());
                    }
                    ibtn_player_albumart.stopRotation();
                }

                //modi 发送更新歌词界面消息
                EventBus.getDefault().post(new CommonMessage(MSG_SONG_PLAY_OVER,null));
            }else if(flag==MediaPlayerManager.FLAG_LIST){
                //自动切歌播放，更新前台歌曲列表
                updateSongItemList();
                //modi 发送更新歌词界面消息
                EventBus.getDefault().post(new CommonMessage(MSG_SONG_PLAY_OVER,null));
            }else if(flag==MediaPlayerManager.FLAG_BUFFERING){
                int percent=intent.getIntExtra("percent", 0);
                percent=(int)(pb_player_progress.getMax()/100f)*percent;
                pb_player_progress.setSecondaryProgress(percent);
            }
        }
    }

    //自动切歌播放，更新前台歌曲列表
    private void updateSongItemList(){
        ibtn_player_albumart.startRotation();
        int[] playerInfo=new int[]{mediaPlayerManager.getSongId(),mediaPlayerManager.getPlayerState()};
        if(mediaPlayerManager.getPlayerFlag()==MediaPlayerManager.PLAYERFLAG_WEB){
            ((SongItemWebAdapter)lv_list_web.getAdapter()).setPlayerInfo(playerInfo);
        }else{
            if(pageNumber==1||pageNumber==6||pageNumber==7||pageNumber==22||pageNumber==33||pageNumber==44||pageNumber==55){
                ((SongItemAdapter)lv_list_change_content.getAdapter()).setPlayerInfo(playerInfo);
            }
            if(pageNumber==9){
                ((DownLoadListAdapter)lv_list_change_content.getAdapter()).setPlayerInfo(playerInfo);
            }
        }
        int state=mediaPlayerManager.getPlayerState();
        if(state==MediaPlayerManager.STATE_PLAYER||state==MediaPlayerManager.STATE_PREPARE){//播放
            ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_pause);
        }else if(state==MediaPlayerManager.STATE_PAUSE){//暂停
            ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_player);
        }
    }

    /**
     * 初始化下载管理信息
     * */
    private void initDownLoad(){
        List<HashMap<String, Object>> data=Common.getListDownLoadData();
        SimpleAdapter download_adapter = new SimpleAdapter(this, data,
                R.layout.list_item, new String[] { "icon", "title", "icon2" },
                new int[] { R.id.iv_list_item_icon, R.id.tv_list_item_title,
                        R.id.iv_list_item_icon2 });

        ListView lv_list_download = (ListView) list_main_download
                .findViewById(R.id.lv_list_download);
        lv_list_download.setAdapter(download_adapter);
        lv_list_download.setOnItemClickListener(list_download_listener);
    }

    //网络音乐项事件
    private SongItemWebAdapter.ItemListener mItemListener=new SongItemWebAdapter.ItemListener() {
        @Override
        public void onDownLoad(Song	song) {
            if(!Common.getNetIsAvailable(MainActivity.this)){
                toast=Common.showMessage(toast, MainActivity.this, "当前网络不可用");
                return;
            }
            if(!Common.isExistSdCard()){
                toast=Common.showMessage(toast, MainActivity.this, "请先插入SD卡");
                return;
            }
            //判断是否在下载列表中
            if(downLoadInfoDao.isExist(song.getNetUrl())){
                toast=Common.showMessage(toast, MainActivity.this, "此歌曲已经在下载列表中");
                return;
            }
            //判断是否已经下载过
            if(songDao.isExist(song.getNetUrl())){
                toast=Common.showMessage(toast, MainActivity.this, "此歌曲已经在下载过了");
                return;
            }
            //添加到下载列表中
            downLoadManager.add(song);
        }

    };

    //ImageButton click
    private View.OnClickListener imageButton_listener=new View.OnClickListener() {

        public void onClick(View v) {
            if(v.getId()==R.id.ibtn_list_content_icon){
                rl_list_content.setVisibility(View.GONE);
                rl_list_main_content.setVisibility(View.VISIBLE);
                pageNumber=0;
            }else if(v.getId()==R.id.ibtn_list_content_do_icon){
                if(pageNumber==5){//播放列表时，弹出菜单
                    doPlayList(0,0,null);
                }
            }else if(v.getId()==R.id.ibtn_player_control){
                PlayerOrPause(null);
            }else if(v.getId()==R.id.ibtn_player_albumart){
                startActivity(new Intent(MainActivity.this,PlayerMainActivity.class));
            }
        }
    };

    /**
     * 播放或暂停歌曲
     * */
    private void PlayerOrPause(View v){
        if(mediaPlayerManager.getPlayerState()==MediaPlayerManager.STATE_NULL){
            toast=Common.showMessage(toast, MainActivity.this, "请先添加歌曲...");
            return;
        }
        if(v==null){
            //当前列表播放结束
            if(mediaPlayerManager.getPlayerState()==MediaPlayerManager.STATE_OVER){
                toast=Common.showMessage(toast, MainActivity.this, "当前列表已经播放完毕！");
                ibtn_player_albumart.stopRotation();
                return;
            }
        }
        mediaPlayerManager.pauseOrPlayer();
        final int state=mediaPlayerManager.getPlayerState();
        LogUtil.e("播放状态："+state);
        int itemRsId=0;
        if(state==MediaPlayerManager.STATE_PLAYER||state==MediaPlayerManager.STATE_PREPARE){//播放
            ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_pause);
            itemRsId=R.drawable.music_list_item_player;
            initLrc();
            ibtn_player_albumart.startRotation();
        }else if(state==MediaPlayerManager.STATE_PAUSE){//暂停
            ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_player);
            itemRsId=R.drawable.music_list_item_pause;
            ibtn_player_albumart.stopRotation();
        }
        if(mediaPlayerManager.getPlayerFlag()==MediaPlayerManager.PLAYERFLAG_WEB){
            if(v==null){
                ((SongItemWebAdapter)lv_list_web.getAdapter()).setPlayerState(mediaPlayerManager.getPlayerState());
            }else{
                ((SongItemWebAdapter.ViewHolder)v.getTag()).tv_web_list_item_number.setBackgroundResource(itemRsId);
            }
        }else{
            if(pageNumber==1||pageNumber==6||pageNumber==7||pageNumber==22||pageNumber==33||pageNumber==44||pageNumber==55){
                if(v==null){
                    ((SongItemAdapter)lv_list_change_content.getAdapter()).setPlayerState(mediaPlayerManager.getPlayerState());
                }else{
                    ((SongItemAdapter.ViewHolder)v.getTag()).tv_song_list_item_number.setBackgroundResource(itemRsId);
                }
            }
            if(pageNumber==9){
                if(v==null){
                    ((DownLoadListAdapter)lv_list_change_content.getAdapter()).setPlayerState(mediaPlayerManager.getPlayerState());
                }else{
                    ((DownLoadListAdapter.ViewHolder)v.getTag()).tv_download_list_item_number.setBackgroundResource(itemRsId);
                }
            }
        }
    }

    /**
     * 添加或更新播放列表
     * */
    private void doPlayList(final int flag,final int id,String text){
        String actionmsg = null;
        final EditText et_newPlayList=new EditText(MainActivity.this);
        et_newPlayList.setLayoutParams(params);
        et_newPlayList.setTextSize(15);
        if(flag==0){//新建
            actionmsg="创建";
            et_newPlayList.setHint("请输入播放列表名称");
        }else if(flag==1){//更新
            actionmsg="更新";
            et_newPlayList.setText(text);
            et_newPlayList.selectAll();
        }
        final String actionmsg2=actionmsg;

        new XfDialog.Builder(MainActivity.this).setTitle(actionmsg+"播放列表")
                .setView(et_newPlayList,5,10,5,10).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String text=et_newPlayList.getText().toString().trim();
                if(!TextUtils.isEmpty(text)){
                    if(playerListDao.isExists(text)){
                        toast=Common.showMessage(toast, MainActivity.this, "此名称已经存在！");
                    }else{
                        PlayerList playerList=new PlayerList();
                        playerList.setName(text);

                        int rowId=-1;
                        if(flag==0){//创建播放列表
                            rowId=(int) playerListDao.add(playerList);
                        }else if(flag==1){//更新播放列表
                            playerList.setId(id);
                            rowId=playerListDao.update(playerList);
                        }
                        if(rowId>0){//判断是否成功
                            toast=Common.showMessage(toast, MainActivity.this, actionmsg2+"成功！");
                            lv_list_change_content.setAdapter(new ListItemAdapter(MainActivity.this,
                                    playerListDao.searchAll(),R.drawable.local_custom));
                            dialog.cancel();
                            dialog.dismiss();
                        }else{
                            toast=Common.showMessage(toast, MainActivity.this, actionmsg2+"失败！");
                        }
                    }
                }
            }
        }).setNegativeButton("取消",null).create().show();
    }

    /**
     * 创建底部菜单
     * */
    private void createMenu(){
        xfMenu=new XfMenu(MainActivity.this);

        List<int[]> data1=new ArrayList<int[]>();
        data1.add(new int[]{R.drawable.btn_menu_scanner,R.string.scan_title});
        data1.add(new int[]{R.drawable.btn_menu_skin,R.string.skinsetting_title});
        data1.add(new int[]{R.drawable.btn_menu_exit,R.string.exit_title});
        xfMenu.addItem("常用", data1, new MenuAdapter.ItemListener() {
            @Override
            public void onClickListener(int position, View view) {
                xfMenu.cancel();
                if(position==0){
                    Intent it=new Intent(MainActivity.this,ScanMusicActivity.class);
                    startActivityForResult(it, 1);
                }else if(position==1){
                    Intent it=new Intent(MainActivity.this,SkinSettingActivity.class);
                    startActivityForResult(it,2);
                }else if(position==2){
                    cancelAutoShutdown();
                    mediaPlayerManager.stop();
                    downLoadManager.stop();
                    finish();
                }
            }
        });

        List<int[]> data2=new ArrayList<int[]>();
        data2.add(new int[]{R.drawable.btn_menu_sleep,R.string.sleep_title});

        SystemSetting setting = new SystemSetting(this, false);
        String brightness=setting.getValue(SystemSetting.KEY_BRIGHTNESS);
        if(brightness!=null&&brightness.equals("0")){//夜间模式
            data2.add(new int[]{R.drawable.btn_menu_brightness,R.string.brightness_title});
        }else{
            data2.add(new int[]{R.drawable.btn_menu_darkness,R.string.darkness_title});
        }
        data2.add(new int[]{R.drawable.btn_menu_setting,R.string.systemsetting_title});
        xfMenu.addItem("工具", data2, new MenuAdapter.ItemListener() {
            @Override
            public void onClickListener(int position, View view) {
                xfMenu.cancel();
                if(position==0){
                    final SystemSetting setting=new SystemSetting(MainActivity.this, true);
                    final String autotime=setting.getValue(SystemSetting.KEY_AUTO_SLEEP);

                    final EditText et_alarmTime=new EditText(MainActivity.this);
                    et_alarmTime.setKeyListener(new DigitsKeyListener());
                    et_alarmTime.setHint("单位：分钟");

                    if(!TextUtils.isEmpty(autotime)){
                        et_alarmTime.setText(autotime);
                    }
                    et_alarmTime.setLayoutParams(params);
                    et_alarmTime.setTextSize(12);
                    new XfDialog.Builder(MainActivity.this).setTitle("设置定时关闭时间")
                            .setView(et_alarmTime).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                            PendingIntent operation=PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(MainActivity.this,AutoShutdownRecevier.class), PendingIntent.FLAG_UPDATE_CURRENT);
                            String str=et_alarmTime.getText().toString();

                            //取消定时关机
                            if(!TextUtils.isEmpty(autotime)){
                                alarmManager.cancel(operation);
                            }
                            //启动定时关机
                            if(!TextUtils.isEmpty(str)){
                                int autotime=Integer.valueOf(str);
                                if(autotime!=0){
                                    setting.setValue(SystemSetting.KEY_AUTO_SLEEP,String.valueOf(autotime));
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+autotime*60*1000, operation);
                                }
                                toast=Common.showMessage(toast, MainActivity.this, "音乐一点通将在"+et_alarmTime.getText().toString()+"分钟之后自动关闭");
                            }else{
                                setting.setValue(SystemSetting.KEY_AUTO_SLEEP,"");
                            }
                        }
                    }).setNegativeButton("取消", null).create().show();
                }else if(position==1){
                    setBrightness(view);
                }else if(position==2){
                    Intent it=new Intent(MainActivity.this,SystemSettingActivity.class);
                    startActivity(it);
                }
            }
        });

        List<int[]> data3=new ArrayList<int[]>();
        data3.add(new int[]{R.drawable.btn_menu_about,R.string.about_title});
        xfMenu.addItem("帮助", data3, new MenuAdapter.ItemListener() {
            @Override
            public void onClickListener(int position, View view) {
                xfMenu.cancel();
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
            }
        });

        xfMenu.create();
    }

    /**
     * 取消定时关闭
     * */
    private void cancelAutoShutdown(){
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent operation=PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(MainActivity.this,AutoShutdownRecevier.class), PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(operation);
        new SystemSetting(MainActivity.this, true).setValue(SystemSetting.KEY_AUTO_SLEEP,"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            mediaPlayerManager.initScanner_SongInfo();
            updateListAdapterData();
        }else if(requestCode==2&&resultCode==2){
            SystemSetting setting = new SystemSetting(this, false);
            this.getWindow().setBackgroundDrawableResource(
                    setting.getCurrentSkinResId());
        }else if(requestCode==XF_SOUND_CODE&&resultCode==RESULT_OK){ //判断是否是我们执行的语音识别
            // 取得语音的字符
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //设置视图更新
            String resultsString = "";
            for (int i = 0; i < results.size(); i++){
                resultsString += results.get(i);
            }
            LogUtil.e("resultsString = "+ resultsString);
            char lCharAt = resultsString.charAt(resultsString.length()-1);
            int rs = checkChineseAndEnglish(lCharAt);
            String lSubstring = "";
            if (rs == 0){
                //lSubstring = resultsString.substring(0, resultsString.lastIndexOf("。"));
                char lCharAt1 = resultsString.charAt(resultsString.length()-1);
                if (lCharAt1 == '。'){
                    lSubstring = resultsString.substring(0, resultsString.lastIndexOf("。"));
                }else if (lCharAt1 == '？'){
                    lSubstring = resultsString.substring(0, resultsString.lastIndexOf("？"));
                }else if (lCharAt1 == '！'){
                    lSubstring = resultsString.substring(0, resultsString.lastIndexOf("！"));
                }
                /*String rex = "，,?？！!。.";
                lSubstring = resultsString.replaceAll(rex,"");*/
            }else {
                lSubstring = resultsString.substring(0, resultsString.lastIndexOf("."));
            }
            ToastUtil.show(MainActivity.this,"语音识别结果："+lSubstring);
            Song lSongBySoundRec = getSongBySoundRec(lSubstring);
            if (lSongBySoundRec!=null){
                mediaPlayerManager.player(lSongBySoundRec.getId(),MediaPlayerManager.PLAYERFLAG_ALL,null);
            }else {
                ToastUtil.show(MainActivity.this,"本地数据库没有匹配的歌曲");
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 检查是否含有中文
     * @param charAt
     * @return
     */
    private int checkChineseAndEnglish(char charAt) {
        if ((char)(byte)charAt!=charAt){
            return 0;
        }else {
            return 1;
        }
    }
    /**
     * 检查是否含有中文
     * @param str
     * @return
     */
    private boolean isHaveChinese(String str) {
        if (str == null){
            return false;
        }
        return (str.length() == str.getBytes().length) ? true : false;
    }

    /**
     * 语音识别播放歌曲
     * @param resultsString 语音识别出字符串
     */
    private Song getSongBySoundRec(String resultsString) {
        for (int i=0;i<LOCAL_SONG_DATA.size();i++){
            Song lSong = LOCAL_SONG_DATA.get(i);
            if (lSong.getDisplayName().contains(resultsString)){
                return lSong;
            }
        }
        return null;
    }

    //重写返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if(keyCode==KeyEvent.KEYCODE_BACK){   //modi  去掉返回时结束播放服务
            if(pageNumber==0){
                int state=mediaPlayerManager.getPlayerState();
                if(state==MediaPlayerManager.STATE_NULL||state==MediaPlayerManager.STATE_OVER||state==MediaPlayerManager.STATE_PAUSE){
                    cancelAutoShutdown();
                    mediaPlayerManager.stop();
                    downLoadManager.stop();
                }
                finish();
                return true;
            }
            return backPage();
        }else */
        if(keyCode==KeyEvent.KEYCODE_MENU&&!xfMenu.isShowing()){
            //xfMenu.showAtLocation(findViewById(R.id.rl_parent_cotent), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回键事件
     * */
    private boolean backPage(){
        if(pageNumber<10){
            rl_list_content.setVisibility(View.GONE);
            rl_list_main_content.setVisibility(View.VISIBLE);
            pageNumber=0;
            return true;
        }else{
            if(pageNumber==22){
                jumpPage(1, 2,null);
            }else if(pageNumber==33){
                jumpPage(1, 3,null);
            }else if(pageNumber==44){
                jumpPage(1, 4,null);
            }else if(pageNumber==55){
                jumpPage(1, 5,null);
            }
        }
        return false;
    }

    //初始化本地音乐
    private void initListMusicItem() {
        List<HashMap<String, Object>> data=Common.getListMusicData();
        SimpleAdapter music_adapter = new SimpleAdapter(this, data,
                R.layout.list_item, new String[] { "icon", "title", "icon2" },
                new int[] { R.id.iv_list_item_icon, R.id.tv_list_item_title,
                        R.id.iv_list_item_icon2 });

        ListView lv_list_music = (ListView) list_main_music
                .findViewById(R.id.lv_list_music);
        lv_list_music.setAdapter(music_adapter);
        lv_list_music.setOnItemClickListener(list_music_listener);
    }

    //音乐列表项的点击事件
    private AdapterView.OnItemClickListener list_music_listener=new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            jumpPage(1, position+1,null);
        }
    };

    //下载管理列表项的点击事件
    private AdapterView.OnItemClickListener list_download_listener=new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            jumpPage(1, position+8,null);
        }
    };

    /**
     * 当前歌曲列表的播放列表的查询条件
     * */
    private String condition=null;

    /**
     * 跳转某页面事件
     * */
    private void jumpPage(int classIndex,int flag,Object obj){
        int[] playerInfo=new int[]{mediaPlayerManager.getSongId(),mediaPlayerManager.getPlayerState()};
        if(classIndex==1){
            rl_list_main_content.setVisibility(View.GONE);
            rl_list_content.setVisibility(View.VISIBLE);
            ibtn_list_content_icon.setBackgroundResource(R.drawable.player_btn_list);
            btn_list_random_music2.setVisibility(View.GONE);
            ibtn_list_content_do_icon.setVisibility(View.GONE);

            if(flag==1){//全部歌曲
                tv_list_content_title.setText(getString(R.string.title_all_music));
                List<String[]> data=songDao.searchByAll();
                lv_list_change_content.setAdapter(new SongItemAdapter(MainActivity.this, data,playerInfo).setItemListener(songItemListener));
                btn_list_random_music2.setVisibility(View.VISIBLE);
                btn_list_random_music2.setText(getString(R.string.title_total)+data.size()+getString(R.string.title_random_play));
                btn_list_random_music2.setTag(data.size());
            }else if(flag==2){//歌手
                tv_list_content_title.setText(getString(R.string.title_songer));
                lv_list_change_content.setAdapter(new ListItemAdapter(MainActivity.this,
                        artistDao.searchAll(),R.drawable.default_list_singer));
            }else if(flag==3){//专辑
                tv_list_content_title.setText(getString(R.string.title_album));
                lv_list_change_content.setAdapter(new ListItemAdapter(MainActivity.this,
                        albumDao.searchAll(),R.drawable.default_list_album));
            }else if(flag==4){//文件夹
                tv_list_content_title.setText(getString(R.string.title_folder));
                lv_list_change_content.setAdapter(new ListItemAdapter(MainActivity.this,
                        songDao.searchByDirectory(), R.drawable.local_file));
            }else if(flag==5){//播放列表
                tv_list_content_title.setText(getString(R.string.title_play_list));
                lv_list_change_content.setAdapter(new ListItemAdapter(MainActivity.this,
                        playerListDao.searchAll(),R.drawable.local_custom));
                ibtn_list_content_do_icon.setVisibility(View.VISIBLE);
            }else if(flag==6){//我最爱听
                tv_list_content_title.setText(getString(R.string.title_my_love));
                btn_list_random_music2.setVisibility(View.VISIBLE);
                List<String[]> data=songDao.searchByIsLike();
                lv_list_change_content.setAdapter(new SongItemAdapter(MainActivity.this, data,playerInfo).setItemListener(songItemListener));
                btn_list_random_music2.setVisibility(View.VISIBLE);
                btn_list_random_music2.setText(getString(R.string.title_total)+data.size()+getString(R.string.title_random_play));
                btn_list_random_music2.setTag(data.size());
            }else if(flag==7){//最近播放
                tv_list_content_title.setText(getString(R.string.title_play_recent));
                btn_list_random_music2.setVisibility(View.VISIBLE);
                List<String[]> data=songDao.searchByLately(mediaPlayerManager.getLatelyStr());
                lv_list_change_content.setAdapter(new SongItemAdapter(MainActivity.this, data,playerInfo).setItemListener(songItemListener));
                btn_list_random_music2.setVisibility(View.VISIBLE);
                btn_list_random_music2.setText(getString(R.string.title_total)+data.size()+getString(R.string.title_random_play));
                btn_list_random_music2.setTag(data.size());
            }else if(flag==8){//正在下载
                tv_list_content_title.setText(getString(R.string.title_downloading));
                lv_list_change_content.setAdapter(new DownLoadingListAdapter(MainActivity.this,
                        downLoadManager.getDownLoadData()).setItemListener(downLoadingListItemListener));
            }else if(flag==9){//下载完成
                tv_list_content_title.setText(getString(R.string.title_downloaded));
                List<Song> data=songDao.searchByDownLoad();
                lv_list_change_content.setAdapter(new DownLoadListAdapter(MainActivity.this,
                        data,playerInfo).setItemListener(downLoadListItemListener));
                btn_list_random_music2.setVisibility(View.VISIBLE);
                btn_list_random_music2.setText(getString(R.string.title_total)+data.size()+getString(R.string.title_random_play));
                btn_list_random_music2.setTag(data.size());
            }
            pageNumber=flag;
        }else if(classIndex==2){
            btn_list_random_music2.setVisibility(View.VISIBLE);
            TextView textView=((ListItemAdapter.ViewHolder)obj).textView;
            condition=textView.getTag().toString().trim();
            tv_list_content_title.setText(textView.getText().toString());
            List<String[]> data=null;
            if(flag==22){//某歌手歌曲
                data=songDao.searchByArtist(condition);
            }else if(flag==33){//某专辑歌曲
                data=songDao.searchByAlbum(condition);
            }else if(flag==44){//某文件夹歌曲
                data=songDao.searchByDirectory(condition);
            }else if(flag==55){//某播放列表的歌曲
                data=songDao.searchByPlayerList("$"+condition+"$");
            }
            lv_list_change_content.setAdapter(new SongItemAdapter(MainActivity.this, data,playerInfo).setItemListener(songItemListener));
            btn_list_random_music2.setText(getString(R.string.title_total)+data.size()+getString(R.string.title_random_play));
            btn_list_random_music2.setTag(data.size());
            pageNumber=flag;
        }
    }

    /**
     * 更新本地列表的数据展示（扫描后）
     * */
    private void updateListAdapterData(){
        int[] playerInfo=new int[]{mediaPlayerManager.getSongId(),mediaPlayerManager.getPlayerState()};
        if(pageNumber==1){
            List<String[]> data=songDao.searchByAll();
            lv_list_change_content.setAdapter(new SongItemAdapter(MainActivity.this, data,playerInfo).setItemListener(songItemListener));
            btn_list_random_music2.setText("(共"+data.size()+getString(R.string.title_random_play));
            btn_list_random_music2.setTag(data.size());
        }else if(pageNumber==22||pageNumber==33||pageNumber==44||pageNumber==55){
            List<String[]> data=null;
            if(pageNumber==22){
                data=songDao.searchByArtist(condition);
            }else if(pageNumber==33){
                data=songDao.searchByAlbum(condition);
            }else if(pageNumber==44){
                data=songDao.searchByDirectory(condition);
            }else if(pageNumber==55){
                data=songDao.searchByPlayerList("$"+condition+"$");
            }
            lv_list_change_content.setAdapter(new SongItemAdapter(MainActivity.this, data,playerInfo).setItemListener(songItemListener));
            btn_list_random_music2.setText("(共"+data.size()+getString(R.string.title_random_play));
            btn_list_random_music2.setTag(data.size());
        }
    }

    /**
     * 下载任务-广播接收器
     * */
    private class DownLoadBroadcastRecevier extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag=intent.getIntExtra("flag", -1);
            if(flag==DownLoadManager.FLAG_CHANGED){
                if(pageNumber==8){
                    ((DownLoadingListAdapter)lv_list_change_content.getAdapter()).setData(downLoadManager.getDownLoadData());
                }
            }else if(flag==DownLoadManager.FLAG_WAIT){
                toast=Common.showMessage(toast, MainActivity.this,"\""+intent.getStringExtra("displayname")+"\""+"添加到了下载列表中!");
            }else if(flag==DownLoadManager.FLAG_COMPLETED){
                if(pageNumber==8){
                    ((DownLoadingListAdapter)lv_list_change_content.getAdapter()).setData(downLoadManager.getDownLoadData());
                }
                toast=Common.showMessage(toast, MainActivity.this,"\""+intent.getStringExtra("displayname")+"\""+"下载完成!");
            }else if(flag==DownLoadManager.FLAG_FAILED){
                if(pageNumber==8){
                    ((DownLoadingListAdapter)lv_list_change_content.getAdapter()).setData(downLoadManager.getDownLoadData());
                }
                toast=Common.showMessage(toast, MainActivity.this,"\""+intent.getStringExtra("displayname")+"\""+"服务器找不到文件!");
            }else if(flag==DownLoadManager.FLAG_TIMEOUT){
                if(pageNumber==8){
                    ((DownLoadingListAdapter)lv_list_change_content.getAdapter()).setData(downLoadManager.getDownLoadData());
                }
                toast=Common.showMessage(toast, MainActivity.this,"\""+intent.getStringExtra("displayname")+"\""+"连接已经超时!");
            }else if(flag==DownLoadManager.FLAG_ERROR){
                if(pageNumber==8){
                    ((DownLoadingListAdapter)lv_list_change_content.getAdapter()).setData(downLoadManager.getDownLoadData());
                }
                toast=Common.showMessage(toast, MainActivity.this,"\""+intent.getStringExtra("displayname")+"\""+"发生错误!");
            }else if(flag==DownLoadManager.FLAG_COMMON){
                if(pageNumber==8){
                    ((DownLoadingListAdapter)lv_list_change_content.getAdapter()).setData(downLoadManager.getDownLoadData());
                }
            }
        }

    }

    /**
     * 删除歌曲，重置播放列表
     * */
    private void deleteForResetPlayerList(int songId,int flag,String parameter){
        final int state=mediaPlayerManager.getPlayerState();
        if(state==MediaPlayerManager.STATE_NULL||state==MediaPlayerManager.STATE_OVER){
            return;
        }
        if(mediaPlayerManager.getPlayerFlag()==MediaPlayerManager.PLAYERFLAG_WEB){
            return;
        }
        String t_parameter=mediaPlayerManager.getParameter();
        if(t_parameter==null) t_parameter="";
        if(flag==MediaPlayerManager.PLAYERFLAG_ALL||(mediaPlayerManager.getPlayerFlag()==flag&&parameter.equals(t_parameter))){
            //删除'播放列表'，就播放全部歌曲
            if(songId==-1){
                mediaPlayerManager.delete(-1);
                return;
            }else{
                //如果是当前播放歌曲，就要切换下一首
                if(songId==mediaPlayerManager.getSongId()){
                    mediaPlayerManager.delete(songId);
                }
            }
            mediaPlayerManager.resetPlayerList();
        }
    }

    /**
     * 正在下载-开始下载/暂停下载，删除
     * */
    private DownLoadingListAdapter.ItemListener downLoadingListItemListener=new DownLoadingListAdapter.ItemListener() {

        @Override
        public void onDelete(String url) {
            downLoadManager.delete(url);
        }

        @Override
        public void onPause(String url,int state) {
            if(state==DownLoadManager.STATE_PAUSE||state==DownLoadManager.STATE_ERROR||state==DownLoadManager.STATE_FAILED){
                downLoadManager.start(url);
            }else if(state==DownLoadManager.STATE_DOWNLOADING||state==DownLoadManager.STATE_CONNECTION||state==DownLoadManager.STATE_WAIT){
                downLoadManager.pause(url);
            }
        }
    };

    /**
     * 下载完成-删除
     * */
    private DownLoadListAdapter.ItemListener downLoadListItemListener=new DownLoadListAdapter.ItemListener(){
        @Override
        public void onDelete(int id, String path, int position) {
            createDeleteSongDialog(id, path, position,false);
        }
    };

    //本地音乐和下载管理的二三级布局-替换的ListView ItemClick事件
    private AdapterView.OnItemClickListener list_change_content_listener=new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if(pageNumber==2){//歌手种类列表
                jumpPage(2, 22,view.getTag());
            }else if(pageNumber==3){//专辑种类列表
                jumpPage(2, 33,view.getTag());
            }else if(pageNumber==4){//文件夹种类列表
                jumpPage(2, 44,view.getTag());
            }else if(pageNumber==5){//播放种类列表
                ibtn_list_content_do_icon.setVisibility(View.GONE);
                jumpPage(2, 55,view.getTag());
            }else if(pageNumber==8){//正在下载列表
                DownLoadingListAdapter.ViewHolder viewHolder=(DownLoadingListAdapter.ViewHolder)view.getTag();
                int state=Integer.valueOf(viewHolder.tv_download_list_item_top.getTag().toString());
                String url=viewHolder.tv_download_list_item_number.getTag().toString();
                downLoadingListItemListener.onPause(url, state);
            }else if(pageNumber==9){//下载完成列表
                if(mediaPlayerManager.getPlayerFlag()==MediaPlayerManager.PLAYERFLAG_WEB){
                    int[] playerInfo=new int[]{-1,-1};
                    ((SongItemWebAdapter)lv_list_web.getAdapter()).setPlayerInfo(playerInfo);
                }
                int songId=Integer.valueOf(((DownLoadListAdapter.ViewHolder)view.getTag()).ibtn_download_list_item_menu.getTag().toString());
                if(songId==mediaPlayerManager.getSongId()){
                    PlayerOrPause(view);
                }else {
                    ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_pause);
                    mediaPlayerManager.player(songId,MediaPlayerManager.PLAYERFLAG_DOWNLOAD, null);
                    int[] playerInfo=new int[]{songId,mediaPlayerManager.getPlayerState()};
                    ((DownLoadListAdapter)lv_list_change_content.getAdapter()).setPlayerInfo(playerInfo);
                }
            }else if(pageNumber==1){//全部歌曲列表
                playerMusicByItem(view,MediaPlayerManager.PLAYERFLAG_ALL,null);
            }else if(pageNumber==6){//我最爱听列表
                playerMusicByItem(view,MediaPlayerManager.PLAYERFLAG_LIKE,null);
            }else if(pageNumber==7){//最近播放列表
                playerMusicByItem(view,MediaPlayerManager.PLAYERFLAG_LATELY,null);
            }else if(pageNumber==22){//某歌手歌曲列表
                playerMusicByItem(view,MediaPlayerManager.PLAYERFLAG_ARTIST,condition);
            }else if(pageNumber==33){//某专辑歌曲列表
                playerMusicByItem(view,MediaPlayerManager.PLAYERFLAG_ALBUM,condition);
            }else if(pageNumber==44){//某文件夹歌曲列表
                playerMusicByItem(view,MediaPlayerManager.PLAYERFLAG_FOLDER,condition);
            }else if(pageNumber==55){//某播放列表歌曲列表
                playerMusicByItem(view,MediaPlayerManager.PLAYERFLAG_PLAYERLIST,condition);
            }
        }
    };

    private void playerMusicByItem(View view,int flag,String condition){
        if(mediaPlayerManager.getPlayerFlag()==MediaPlayerManager.PLAYERFLAG_WEB){
            int[] playerInfo=new int[]{-1,-1};
            ((SongItemWebAdapter)lv_list_web.getAdapter()).setPlayerInfo(playerInfo);
        }
        int songId=Integer.valueOf(((SongItemAdapter.ViewHolder)view.getTag()).tv_song_list_item_bottom.getTag().toString());
        if(songId==mediaPlayerManager.getSongId()){
            PlayerOrPause(view);
        }else {
            ibtn_player_control.setBackgroundResource(R.drawable.player_btn_mini_pause);
            mediaPlayerManager.player(songId,flag, condition);
            int[] playerInfo=new int[]{songId,mediaPlayerManager.getPlayerState()};
            ((SongItemAdapter)lv_list_change_content.getAdapter()).setPlayerInfo(playerInfo);
        }
    }

    //本地音乐和下载管理的二三级布局-替换的ListView ItemLoogClick事件
    private AdapterView.OnItemLongClickListener list_change_content_looglistener=new AdapterView.OnItemLongClickListener() {

        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            if(pageNumber==5){//播放列表时，弹出菜单
                if(position!=0){
                    doPlayListLoogItemDialog(view);
                    return true;
                }
            }else{
                if(!(pageNumber==2||pageNumber==3||pageNumber==4||pageNumber==5||pageNumber==8||pageNumber==9)){
                    final SongItemAdapter.ViewHolder viewHolder=(SongItemAdapter.ViewHolder)view.getTag();
                    final String path=viewHolder.tv_song_list_item_top.getTag().toString();//歌曲路径
                    final int sid=Integer.parseInt(viewHolder.tv_song_list_item_bottom.getTag().toString());//歌曲id
                    final String text=viewHolder.tv_song_list_item_top.getText().toString();

                    doListSongLoogItemDialog(sid,text,path,position);
                }
            }
            return false;
        }

    };

    //歌曲列表项事件
    private SongItemAdapter.ItemListener songItemListener=new SongItemAdapter.ItemListener() {
        @Override
        public void onLikeClick(int id, View view, int position) {
            //排除我最爱听歌曲列表
            if(pageNumber==6){
                songDao.updateByLike(id, 0);
                //更新歌曲列表
                ((SongItemAdapter)lv_list_change_content.getAdapter()).deleteItem(position);
                btn_list_random_music2.setText("(共"+lv_list_change_content.getCount()+"首)随机播放");
                btn_list_random_music2.setTag(lv_list_change_content.getCount());
                deleteForResetPlayerList(id,MediaPlayerManager.PLAYERFLAG_LIKE,"");
                return;
            }
            if(view.getTag().equals("1")){
                view.setTag("0");
                view.setBackgroundResource(R.drawable.dislike);
                songDao.updateByLike(id, 0);
            }else{
                view.setTag("1");
                view.setBackgroundResource(R.drawable.like);
                songDao.updateByLike(id, 1);
            }
        }
        @Override
        public void onMenuClick(int id, String text, String path, int position) {
            doListSongLoogItemDialog(id,text,path,position);
        }
    };

    /**
     * 创建歌曲列表菜单对话框
     * */
    private void doListSongLoogItemDialog(final int sid,String text,final String path,final int parentposition){
        String delete_title="移除歌曲";
        if(pageNumber==9){
            delete_title="清除任务";
        }

        String[] menustring=new String[]{"添加到列表","设为铃声",delete_title,"查看详情"};
        ListView menulist=new ListView(MainActivity.this);
        menulist.setCacheColorHint(Color.TRANSPARENT);
        menulist.setDividerHeight(1);
        menulist.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.dialog_menu_item, R.id.text1, menustring));
        menulist.setLayoutParams(new ViewGroup.LayoutParams(Common.getScreen(MainActivity.this)[0]/2, ViewGroup.LayoutParams.WRAP_CONTENT));

        final XfDialog xfdialog=new XfDialog.Builder(MainActivity.this).setTitle(text).setView(menulist).create();
        xfdialog.show();

        menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                xfdialog.cancel();
                xfdialog.dismiss();
                if(position==0){//添加到列表
                    createPlayerListDialog(sid);
                }else if(position==1){//设为铃声
                    createRingDialog(path);
                }else if(position==2){//移除歌曲
                    createDeleteSongDialog(sid,path,parentposition,true);
                }else if(position==3){//查看详情
                    createSongDetailDialog(sid);
                }
            }
        });
    }

    /**
     * 歌曲详细对话框
     * */
    private void createSongDetailDialog(int id){
        Song song=songDao.searchById(id);
        File file=new File(song.getFilePath());
        //歌曲不存在
        if(!file.exists()){
            toast=Common.showMessage(toast, MainActivity.this, "歌曲已经不存在，请删除歌曲！");
            return;
        }
        if(song.getSize()==-1){
            song.setSize((int)file.length());
            songDao.updateBySize(id, song.getSize());
        }
        //表示当时扫描时，是在媒体库中不存在的歌曲
        int duration=song.getDurationTime();
        if(duration==-1){
            //获取播放时长
            MediaPlayer t_MediaPlayer=new MediaPlayer();
            try {
                t_MediaPlayer.setDataSource(song.getFilePath());
                t_MediaPlayer.prepare();
                duration=t_MediaPlayer.getDuration();
            } catch (IllegalArgumentException e) {
            } catch (IllegalStateException e) {
            } catch (IOException e) {
            }finally{
                t_MediaPlayer.release();
                t_MediaPlayer=null;
            }
            if(duration!=-1){
                song.setDurationTime(duration);
                //更新数据库
                songDao.updateByDuration(id,duration);
            }
        }

        View view=inflater.inflate(R.layout.song_detail, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(Common.getScreen(MainActivity.this)[0]/2, ViewGroup.LayoutParams.WRAP_CONTENT));

        ((TextView)view.findViewById(R.id.tv_song_title)).setText(song.getName());
        ((TextView)view.findViewById(R.id.tv_song_album)).setText(song.getAlbum().getName());
        ((TextView)view.findViewById(R.id.tv_song_artist)).setText(song.getArtist().getName());
        ((TextView)view.findViewById(R.id.tv_song_duration)).setText(Common.formatSecondTime(duration));
        ((TextView)view.findViewById(R.id.tv_song_filepath)).setText(song.getFilePath());
        ((TextView)view.findViewById(R.id.tv_song_format)).setText(Common.getSuffix(song.getDisplayName()));
        ((TextView)view.findViewById(R.id.tv_song_size)).setText(Common.formatByteToMB(song.getSize())+"MB");

        new XfDialog.Builder(MainActivity.this).setTitle("歌曲详细信息").setNeutralButton("确定", null).setView(view).create().show();
    }

    /**
     * 添加到列表对话框
     * */
    private void createPlayerListDialog(final int id){
        List<String[]> pList=playerListDao.searchAll();

        RadioGroup rg_pl=new RadioGroup(MainActivity.this);
        rg_pl.setLayoutParams(params);
        final List<RadioButton> rbtns=new ArrayList<RadioButton>();

        for (int i = 0; i < pList.size();i++) {
            String[] str_temp=pList.get(i);
            RadioButton rbtn_temp=new RadioButton(MainActivity.this);
            rbtn_temp.setText(str_temp[1]);
            rbtn_temp.setTag(str_temp[0]);
            rg_pl.addView(rbtn_temp, params);
            rbtns.add(rbtn_temp);
        }

        new XfDialog.Builder(MainActivity.this).setTitle("播放列表").setView(rg_pl).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
                int selectedIndex=-1;
                for (int i = 0; i < rbtns.size(); i++) {
                    if(rbtns.get(i).isChecked()){
                        selectedIndex=i;
                        break;
                    }
                }
                if(selectedIndex!=-1){
                    songDao.updateByPlayerList(id,Integer.valueOf(rbtns.get(selectedIndex).getTag().toString()));
                    toast=Common.showMessage(toast, MainActivity.this, "添加成功");
                }else{
                    toast=Common.showMessage(toast, MainActivity.this, "请选择要添加到的播放列表");
                }
            }
        }).setNegativeButton("取消", null).create().show();
    }

    /**
     * 移除歌曲对话框:flag是否是本地歌曲列表删除
     * */
    private void createDeleteSongDialog(final int sid,final String filepath,final int position,final boolean flag){
        String t_title="移除歌曲";
        if(pageNumber==9){
            t_title="清除任务";
        }
        final String title=t_title;
        final CheckBox cb_deletesong=new CheckBox(MainActivity.this);
        cb_deletesong.setLayoutParams(params);
        cb_deletesong.setText("同时删除本地文件");

        XfDialog.Builder builder=new XfDialog.Builder(MainActivity.this).setView(cb_deletesong)
                .setTitle(title).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(cb_deletesong.isChecked()){
                            Common.deleteFile(MainActivity.this, filepath);
                        }
                        int rs=0;
                        //从播放列表中移除
                        if(!cb_deletesong.isChecked()&&pageNumber==55){
                            rs=songDao.deleteByPlayerList(sid, Integer.valueOf(condition));
                        }else{
                            //没有选中并且是下载完成删除
                            if(!cb_deletesong.isChecked()&&!flag){
                                rs=songDao.updateByDownLoadState(sid);
                            }else{
                                rs=songDao.delete(sid);
                            }
                        }
                        if(rs>0){
                            toast=Common.showMessage(toast, MainActivity.this, title+"成功");
                            dialog.cancel();
                            dialog.dismiss();

                            //更新歌曲列表
                            if(flag){
                                ((SongItemAdapter)lv_list_change_content.getAdapter()).deleteItem(position);
                            }else{
                                ((DownLoadListAdapter)lv_list_change_content.getAdapter()).deleteItem(position);
                            }
                            if(pageNumber==1){
                                deleteForResetPlayerList(sid,MediaPlayerManager.PLAYERFLAG_ALL,"");
                            }else if(pageNumber==6){
                                deleteForResetPlayerList(sid,MediaPlayerManager.PLAYERFLAG_LIKE,"");
                            }else if(pageNumber==7){
                                deleteForResetPlayerList(sid,MediaPlayerManager.PLAYERFLAG_LATELY,"");
                            }else if(pageNumber==9){
                                deleteForResetPlayerList(sid,MediaPlayerManager.PLAYERFLAG_DOWNLOAD,"");
                            }else if(pageNumber==22){
                                deleteForResetPlayerList(sid,MediaPlayerManager.PLAYERFLAG_ARTIST,condition);
                            }else if(pageNumber==33){
                                deleteForResetPlayerList(sid,MediaPlayerManager.PLAYERFLAG_ALBUM,condition);
                            }else if(pageNumber==44){
                                deleteForResetPlayerList(sid,MediaPlayerManager.PLAYERFLAG_FOLDER,condition);
                            }else if(pageNumber==55){
                                deleteForResetPlayerList(sid,MediaPlayerManager.PLAYERFLAG_PLAYERLIST,condition);
                            }
                            btn_list_random_music2.setText("(共"+lv_list_change_content.getCount()+"首)随机播放");
                            btn_list_random_music2.setTag(lv_list_change_content.getCount());
                        }else{
                            toast=Common.showMessage(toast, MainActivity.this, title+"失败");
                        }
                    }
                }).setNegativeButton("取消", null);

        builder.create().show();
    }

    /**
     * 设置铃声对话框
     * */
    private void createRingDialog(final String filepath){
        RadioGroup rg_ring=new RadioGroup(MainActivity.this);
        rg_ring.setLayoutParams(params);
        final RadioButton rbtn_ringtones=new RadioButton(MainActivity.this);
        rbtn_ringtones.setText("来电铃声");
        rg_ring.addView(rbtn_ringtones, params);
        final RadioButton rbtn_alarms=new RadioButton(MainActivity.this);
        rbtn_alarms.setText("闹铃铃声");
        rg_ring.addView(rbtn_alarms, params);
        final RadioButton rbtn_notifications=new RadioButton(MainActivity.this);
        rbtn_notifications.setText("通知铃声");
        rg_ring.addView(rbtn_notifications, params);
        final RadioButton rbtn_all=new RadioButton(MainActivity.this);
        rbtn_all.setText("全部铃声");
        rg_ring.addView(rbtn_all, params);

        new XfDialog.Builder(MainActivity.this).setTitle("设置铃声").setView(rg_ring).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ContentValues cv=new ContentValues();
                int type=-1;
                if(rbtn_ringtones.isChecked()){
                    type=RingtoneManager.TYPE_RINGTONE;
                    cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                }else if(rbtn_alarms.isChecked()){
                    type=RingtoneManager.TYPE_ALARM;
                    cv.put(MediaStore.Audio.Media.IS_ALARM, true);
                }else if(rbtn_notifications.isChecked()){
                    type=RingtoneManager.TYPE_NOTIFICATION;
                    cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                }else if(rbtn_all.isChecked()){
                    type=RingtoneManager.TYPE_ALL;
                    cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                    cv.put(MediaStore.Audio.Media.IS_ALARM, true);
                    cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                }
                if(type==-1){
                    toast=Common.showMessage(toast, MainActivity.this, "请选择铃声类型");
                }else{
                    Uri uri = MediaStore.Audio.Media.getContentUriForPath(filepath);
                    Uri ringtoneUri =null;
                    Cursor cursor = MainActivity.this.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[] { filepath },null);
                    //查询媒体库中存在的
                    if (cursor.getCount() > 0&&cursor.moveToFirst()) {
                        String _id = cursor.getString(0);
                        //更新媒体库
                        getContentResolver().update(uri, cv, MediaStore.MediaColumns.DATA + "=?",new String[] { filepath });
                        ringtoneUri= Uri.withAppendedPath(uri, _id);
                    }else{//不存在就添加
                        cv.put(MediaStore.MediaColumns.DATA,filepath);
                        ringtoneUri= MainActivity.this.getContentResolver().insert(uri, cv);
                    }
                    try {
                        RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this, type, ringtoneUri);
                        toast=Common.showMessage(toast, MainActivity.this, "铃声设置成功");
                    } catch (Exception e) {
                        toast=Common.showMessage(toast, MainActivity.this, "铃声设置失败");
                    }
                    dialog.cancel();
                    dialog.dismiss();
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    /**
     * 创建播放列表菜单对话框
     * */
    private void doPlayListLoogItemDialog(View view){
        final TextView textView=((ListItemAdapter.ViewHolder)view.getTag()).textView;
        final String text=textView.getText().toString();//播放列表名称
        final int plid=Integer.parseInt(textView.getTag().toString());//播放列表id

        String[] menustring=new String[]{"重命名","删除"};
        ListView menulist=new ListView(MainActivity.this);
        menulist.setCacheColorHint(Color.TRANSPARENT);
        menulist.setDividerHeight(1);
        menulist.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.dialog_menu_item, R.id.text1, menustring));
        menulist.setLayoutParams(new ViewGroup.LayoutParams(Common.getScreen(MainActivity.this)[0]/2, ViewGroup.LayoutParams.WRAP_CONTENT));

        final XfDialog xfdialog=new XfDialog.Builder(MainActivity.this).setTitle(text).setView(menulist).create();
        xfdialog.show();

        menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0){//重命名
                    xfdialog.cancel();
                    xfdialog.dismiss();
                    doPlayList(1, plid,text);
                }else if(position==1){//删除
                    xfdialog.cancel();
                    xfdialog.dismiss();
                    new XfDialog.Builder(MainActivity.this).setTitle("删除提示").setMessage("是否要删除这个播放列表？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(playerListDao.delete(plid)>0){
                                        toast=Common.showMessage(toast, MainActivity.this, "删除成功！");
                                        lv_list_change_content.setAdapter(new ListItemAdapter(MainActivity.this,
                                                playerListDao.searchAll(),R.drawable.local_custom));

                                        //更新正在播放列表
                                        deleteForResetPlayerList(-1, MediaPlayerManager.PLAYERFLAG_PLAYERLIST, String.valueOf(plid));
                                    }else{
                                        toast=Common.showMessage(toast, MainActivity.this, "删除失败！");
                                    }
                                    dialog.cancel();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("取消",null).create().show();
                }
            }

        });

    }

    /**
     * 初始化导航栏
     * */
    private void initTabItem() {
        for (int i = 0; i < vg_list_tab_item.length; i++) {
            vg_list_tab_item[i].setOnClickListener(tabClickListener);
            if (screenIndex == i) {
                vg_list_tab_item[0]
                        .setBackgroundResource(R.drawable.list_top_press);
            }
            ((ImageView) vg_list_tab_item[i]
                    .findViewById(R.id.iv_list_item_icon))
                    .setImageResource(list_item_icons[i]);
            ((TextView) vg_list_tab_item[i]
                    .findViewById(R.id.tv_list_item_text))
                    .setText(list_item_items[i]);
        }
    }

    //主屏幕左右滑动事件
    private FlingGalleryView.OnScrollToScreenListener scrollToScreenListener = new FlingGalleryView.OnScrollToScreenListener() {

        public void operation(int currentScreen, int screenCount) {
            vg_list_tab_item[screenIndex].setBackgroundResource(0);
            screenIndex = currentScreen;
            vg_list_tab_item[screenIndex]
                    .setBackgroundResource(R.drawable.list_top_press);
        }
    };

    //导航栏选项卡切换事件
    private View.OnClickListener tabClickListener = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_tab_item_music:
                    if (screenIndex == 0) {
                        return;
                    }
                    vg_list_tab_item[screenIndex].setBackgroundResource(0);
                    screenIndex = 0;
                    break;
                case R.id.list_tab_item_web:
                    if (screenIndex == 1) {
                        return;
                    }
                    vg_list_tab_item[screenIndex].setBackgroundResource(0);
                    screenIndex = 1;
                    break;
                case R.id.list_tab_item_download:
                    if (screenIndex == 2) {
                        return;
                    }
                    vg_list_tab_item[screenIndex].setBackgroundResource(0);
                    screenIndex = 2;
                    break;
                default:
                    break;
            }
            vg_list_tab_item[screenIndex]
                    .setBackgroundResource(R.drawable.list_top_press);
            fgv_list_main.setToScreen(screenIndex, true);
        }
    };

    private void refreshBackground(String path) {
        this.getWindow().setBackgroundDrawable(new BitmapDrawable(Common.getBackgroundByPath(path)));
    }

    /**
     * 用来接收时间总线发送过来
     * 的待处理时间消息，切换主
     * fragment为音乐列表的fragment
     *
     * @param event
     */
/*    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenLocalMusicView(CommonMessage event) {
        if (event.getMsg().equals(getResources().getString(R.string.play_flow))) {
            //播放悬浮标签歌曲
            playerMusicByItem(event.getObj(), MediaPlayerManager.PLAYERFLAG_ALL, null);
        }
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshWebListView(CommonMessage event) {
        if (event.getMsg().equals(Constants.MSG_SONG_LOAD_OVER)) {
            LogUtil.e("事件接收成功");
            //刷新网络列表
            mSongItemWebAdapter.notifyDataSetChanged();
        }
        if (event.getMsg().equals(Constants.MSG_SEND_CHANGE_BG)) {  //modi BUG
            LogUtil.e("MainActivity--事件接收成功");
            //刷新背景
            Object lObj = event.getObj();
            if (lObj instanceof String){
                File lFile = new File((String) lObj);
                if (lFile.exists()) {
                    //刷新背景
                    refreshBackground((String) lObj);
                }
            }else if (lObj instanceof Integer){
                this.getWindow().setBackgroundDrawableResource(SystemSetting.SKIN_RESOURCES[(int) lObj]);
            }
        }
    }

}
