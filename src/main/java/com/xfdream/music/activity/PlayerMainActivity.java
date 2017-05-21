package com.xfdream.music.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.xfdream.music.R;
import com.xfdream.music.adapter.PlaySongItemAdapter;
import com.xfdream.music.custom.FlingGalleryView;
import com.xfdream.music.custom.FlingGalleryView.OnCustomTouchListener;
import com.xfdream.music.custom.KeywordsFlow;
import com.xfdream.music.custom.XfDialog;
import com.xfdream.music.dao.SongDao;
import com.xfdream.music.data.SystemSetting;
import com.xfdream.music.entity.Sentence;
import com.xfdream.music.entity.Song;
import com.xfdream.music.lrc.LrcHandle;
import com.xfdream.music.lrc.WordView;
import com.xfdream.music.message.CommonMessage;
import com.xfdream.music.service.MediaPlayerManager;
import com.xfdream.music.service.MediaPlayerManager.ServiceConnectionListener;
import com.xfdream.music.util.Common;
import com.xfdream.music.util.Constants;
import com.xfdream.music.util.ImageUtil;
import com.xfdream.music.util.LogUtil;
import com.xfdream.music.util.MusicUtils;
import com.xfdream.music.util.StringHelper;

import net.robinx.lib.blurview.processor.NdkStackBlurProcessor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;
import java.util.Random;

import static com.xfdream.music.util.Constants.BG_CURRENT_INDEX;
import static com.xfdream.music.util.Constants.LRC_CURRENT_INDEX;
import static com.xfdream.music.util.Constants.LRC_WORD_DATA;
import static com.xfdream.music.util.Constants.MAX_WORLDS;
import static com.xfdream.music.util.Constants.MSG_SONG_PLAY_OVER;
import static com.xfdream.music.util.Constants.PICIDS;
import static com.xfdream.music.util.Constants.ShowFlowTag;
import static com.xfdream.music.util.Constants.ShowPlayLrc;


public class PlayerMainActivity extends BaseActivity {
    private RelativeLayout ll_player_voice;

    private SongDao mSongDao;
    private String[] mNames;

    private WordView mWordView;
    private List mTimeList;
    LrcHandle lrcHandler;

    //控制播放
    private ImageButton ibtn_player_voice;
    private ImageButton ibtn_player_list;
    private ImageButton ibtn_player_control_menu;
    private ImageButton ibtn_player_control_mode;
    private ImageButton ibtn_player_control_pre;
    private ImageButton ibtn_player_control_play;
    private ImageButton ibtn_player_control_next;

    //播放信息
    private TextView tv_player_playing_time;
    private TextView tv_player_playering_duration;
    private TextView tv_player_song_info;

    //调节音量
    private SeekBar sb_player_voice;
    //调节播放进度
    private SeekBar sb_player_playprogress;

    //专辑
    private ViewGroup player_main_album;
    private ImageView iv_player_ablum;
    private ImageView iv_player_ablum_reflection;

    private FlingGalleryView fgv_player_main;

    // 音量面板显示和隐藏动画
    private Animation showVoicePanelAnimation;
    private Animation hiddenVoicePanelAnimation;

    // 播放模式
    private String[] player_modeStr;

    //播放模式的Drawable Id
    private static final int[] MODE_DRAWABLE_ID = new int[]{
            R.drawable.player_btn_player_mode_circlelist,
            R.drawable.player_btn_player_mode_random,
            R.drawable.player_btn_player_mode_circleone,
            R.drawable.player_btn_player_mode_sequence
    };

    private Toast toastMsg;
    private MediaPlayerManager mediaPlayerManager;
    private MediaPlayerBroadcastReceiver mediaPlayerBroadcastReceiver;

    private AudioManager audioManager;// 获取系统音频对象

    private boolean isSeekDrag = false;//进度是否在拖动

    //歌词显示部分
    private ViewGroup player_main_lyric;
    private WordView tv_player_lyric_info;

    //------------------------------

    private ViewGroup player_main_flow_tag;
    //浮动标签
    private KeywordsFlow keywordsFlow;
    //
    boolean isshowkeyflower = true;

    showThread thread;

    //更新浮动标签handle
    Handler nameshandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    keywordsFlow.rubKeywords();
                    //设置浮动标签数据源
                    if (mNames.length > 0 && new SystemSetting(PlayerMainActivity.this,false).getBooleanValue(ShowFlowTag)) {
                        feedKeywordsFlow(keywordsFlow,
                                mNames);
                    }
                    keywordsFlow.removeAllViews();
                    Random random = new Random();
                    int ran = random.nextInt(10);
                    if (ran % 2 == 0) {
                        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
                    } else {
                        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
                    }
                    break;
                case 10:
                    break;
                case 20:
                    break;
            }

        }
    };

    Handler showLrcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    tv_player_lyric_info.invalidate();
                    break;
            }
        }
    };
    //private showLrcThread mShowLrcThread;
    private String mLrcPath;
    private String fmt;

    private UpdateLrcBrodcast updateLrcReceiver;

    private int currentTime = 0;
    private String mLocalPath;
    private Song mCurrentSong;
    private View rootView;
    private View mainRootView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;
    private int mState;
    private ListView mListViewRecent;
    private Toast toast;
    private View mRecentSongList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_main);

        EventBus.getDefault().register(this);

        setFullScreen(true);
        rootView = findViewById(R.id.rl_root);
        mainRootView = MainActivity.rootView;
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

        //register download listener

        IntentFilter lIntentFilter = new IntentFilter(getPackageName() + "update_lrc");
        updateLrcReceiver = new UpdateLrcBrodcast();
        registerReceiver(updateLrcReceiver, lIntentFilter);

        ll_player_voice = (RelativeLayout) this
                .findViewById(R.id.ll_player_voice);

        ibtn_player_voice = (ImageButton) this
                .findViewById(R.id.ibtn_player_voice);
        ibtn_player_list = (ImageButton) this
                .findViewById(R.id.ibtn_player_list);
        ibtn_player_control_menu = (ImageButton) this
                .findViewById(R.id.ibtn_player_control_menu);
        ibtn_player_control_mode = (ImageButton) this
                .findViewById(R.id.ibtn_player_control_mode);
        ibtn_player_control_pre = (ImageButton) this
                .findViewById(R.id.ibtn_player_control_pre);
        ibtn_player_control_play = (ImageButton) this
                .findViewById(R.id.ibtn_player_control_play);
        ibtn_player_control_next = (ImageButton) this
                .findViewById(R.id.ibtn_player_control_next);

        ibtn_player_voice.setOnClickListener(listener);
        ibtn_player_list.setOnClickListener(listener);
        ibtn_player_control_menu.setOnClickListener(listener);
        ibtn_player_control_mode.setOnClickListener(listener);
        ibtn_player_control_pre.setOnClickListener(listener);
        ibtn_player_control_play.setOnClickListener(listener);
        ibtn_player_control_next.setOnClickListener(listener);

        tv_player_playing_time = (TextView) this
                .findViewById(R.id.tv_player_playing_time);
        tv_player_playering_duration = (TextView) this
                .findViewById(R.id.tv_player_playering_duration);
        tv_player_song_info = (TextView) this
                .findViewById(R.id.tv_player_song_info);

        sb_player_voice = (SeekBar) this.findViewById(R.id.sb_player_voice);
        sb_player_playprogress = (SeekBar) this
                .findViewById(R.id.sb_player_playprogress);

        sb_player_voice.setOnSeekBarChangeListener(seekBarChangeListener);
        sb_player_playprogress
                .setOnSeekBarChangeListener(seekBarChangeListener);
        sb_player_playprogress.setMax(100);

        player_main_album = (ViewGroup) this
                .findViewById(R.id.player_main_album);
        iv_player_ablum = (ImageView) player_main_album
                .findViewById(R.id.iv_player_ablum);
        iv_player_ablum_reflection = (ImageView) player_main_album
                .findViewById(R.id.iv_player_ablum_reflection);
        setAlbum(R.drawable.default_album);

        fgv_player_main = (FlingGalleryView) this
                .findViewById(R.id.fgv_player_main);
        fgv_player_main.setOnCustomTouchListener(customTouchListener);

        showVoicePanelAnimation = AnimationUtils.loadAnimation(
                PlayerMainActivity.this, R.anim.push_up_in);
        hiddenVoicePanelAnimation = AnimationUtils.loadAnimation(
                PlayerMainActivity.this, R.anim.push_up_out);

        player_modeStr = getResources().getStringArray(R.array.player_mode);

        player_main_lyric = (ViewGroup) this.findViewById(R.id.player_main_lyric);
        /*tv_player_lyric_info=(TextView)player_main_lyric.findViewById(R.id.tv_player_lyric_info);
        tv_player_lyric_info.setText(getApplicationContext().getString(R.string.lrc_not_find));*/

        //注册播放器-广播接收器
        mediaPlayerBroadcastReceiver = new MediaPlayerBroadcastReceiver();
        registerReceiver(mediaPlayerBroadcastReceiver, new IntentFilter(MediaPlayerManager.BROADCASTRECEVIER_ACTON));
        //播放器管理
        if (mediaPlayerManager == null) {
            mediaPlayerManager = MainActivity.mediaPlayerManager;
            //mediaPlayerManager=new MediaPlayerManager(MainActivity.mContext);
            mediaPlayerManager.setConnectionListener(mConnectionListener);
            mediaPlayerManager.startAndBindService();
        }

        //tv_player_lyric_info = (WordView) player_main_lyric.findViewById(R.id.tv_player_lyric_info);
        //tv_player_lyric_info.setCurrentTime(currentTime);
        //lrc--modi --change lrc
        mLrcPath = "/sdcard/aa.lrc";

        lrcHandler = MainActivity.lrcHandler;

        /*int lSongId = mediaPlayerManager.getSongId();
        int lPlayerFlag = mediaPlayerManager.getPlayerFlag();
        if (lPlayerFlag == PLAYERFLAG_WEB){
            mCurrentSong = HttpUtils.querySongBySongId(lSongId);
        }else {
            mCurrentSong = new SongDao(PlayerMainActivity.this).searchById(lSongId);
        }
        String lLyricPath = null;
        if (mCurrentSong!=null){
            lLyricPath = mCurrentSong.getLyricPath();
        }
        lrcHandler = new LrcHandle();
        currentTime = mediaPlayerManager.getPlayerProgress();

        mLocalPath = Constants.LRC_DIRS_PATH + File.separator + StringHelper.getPingYin(mCurrentSong.getName()) + ".lrc";

        initLrchandler(mCurrentSong, lLyricPath);*/

        tv_player_lyric_info = (WordView) player_main_lyric.findViewById(R.id.tv_player_lyric_info);
        //设置是否显示播放界面歌词
        if (new SystemSetting(PlayerMainActivity.this,false).getBooleanValue(ShowPlayLrc)){
            tv_player_lyric_info.setLrcHandler(lrcHandler);
            tv_player_lyric_info.invalidate();
        }else {
            tv_player_lyric_info.setLrcHandler(null);
            tv_player_lyric_info.invalidate();
        }

        //tv_player_lyric_info.setText(getApplicationContext().getString(R.string.lrc_not_find));

        // 获取系统音乐音量
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 获取系统音乐当前音量
        int currentVolume = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        sb_player_voice.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sb_player_voice.setProgress(currentVolume);

        mSongDao = new SongDao(this);
        final List<String> lNames = mSongDao.searchByName();
        String[] lStrings = new String[0];
        mNames = lNames.toArray(lStrings);

        player_main_flow_tag = (ViewGroup) this.findViewById(R.id.player_main_flow_tag);
        keywordsFlow = (KeywordsFlow) (player_main_flow_tag
                .findViewById(R.id.frameLayout1));

        keywordsFlow.setOnItemClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView lTextView = (TextView) v;
                int id = mSongDao.searchSongIdByName(((TextView) v).getText().toString());
                mediaPlayerManager.player(id, MediaPlayerManager.PLAYERFLAG_ALL, null);
                //EventBus.getDefault().post(new CommonMessage(getResources().getString(R.string.play_flow),v));
                //更新面板
                ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_pause);
            }
        });
        //设置是否显示浮动标签
        keywordsFlow.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (thread == null) {
                        isshowkeyflower = true;
                        thread = new showThread();
                    }
                    thread.start();
                }else {
                    isshowkeyflower = false;
                    keywordsFlow.rubKeywords();
                }
            }
        });


        //modi 只有在获取焦点时才显示悬浮标签
        if (thread == null) {
            thread = new showThread();
        }
        thread.start();
        /*if (mShowLrcThread == null) {
            mShowLrcThread = new showLrcThread();
        }
        mShowLrcThread.start();*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void startRootAnimation() {
        rootView.setScaleX(0.3f);
        rootView.setScaleY(0.3f);
        rootView.setAlpha(0.5f);
        rootView.setPivotY(rootView.getY()+rootView.getWidth()/2);
        rootView.setPivotX(rootView.getY()+rootView.getWidth()/2);
        rootView.animate()
                .scaleX(1)
                .scaleY(1)
                .alpha(1.0f)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }
    private void startMainRootAnimation() {
        mainRootView.setScaleX(0.3f);
        mainRootView.setAlpha(0.5f);
        mainRootView.setPivotY(mainRootView.getY()+mainRootView.getWidth()/2);
        mainRootView.animate()
                .scaleX(1)
                .alpha(1.0f)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    private ServiceConnectionListener mConnectionListener = new ServiceConnectionListener() {
        @Override
        public void onServiceDisconnected() {
        }

        @Override
        public void onServiceConnected() {
            mediaPlayerManager.initPlayerMain_SongInfo();
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PlayerMain Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }

    /**
     * 播放器-广播接收器
     */
    private class MediaPlayerBroadcastReceiver extends BroadcastReceiver {
        private Bitmap mCachedArtwork;
        private Bitmap mDefaultArtWork;

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag = intent.getIntExtra("flag", -1);

            int lAlbum_id = intent.getIntExtra("album_id", 1);

            String lTitle = intent.getStringExtra("title");

            mDefaultArtWork = BitmapFactory.decodeResource(getResources(), R.drawable.zhangliangyin);
            mCachedArtwork = MusicUtils.getCachedArtwork(PlayerMainActivity.this, lAlbum_id, mDefaultArtWork);

            if (flag == MediaPlayerManager.FLAG_CHANGED) {
                if (!isSeekDrag) {
                    int currentPosition = intent.getIntExtra("currentPosition", 0);
                    int duration = intent.getIntExtra("duration", 0);
                    tv_player_playing_time.setText(Common.formatSecondTime(currentPosition));
                    tv_player_playering_duration.setText(Common.formatSecondTime(duration));
                    sb_player_playprogress.setProgress(currentPosition);

                    //progress change
                    currentTime = currentPosition;
                    sb_player_playprogress.setMax(duration);
                }
            } else if (flag == MediaPlayerManager.FLAG_PREPARE) {
                String albumPic = intent.getStringExtra("albumPic");
                tv_player_song_info.setText(lTitle);
				/*if(TextUtils.isEmpty(albumPic)){
					setAlbum(R.drawable.default_album);
				}else{
					Bitmap bitmap=BitmapFactory.decodeFile(albumPic);
					if(bitmap!=null){
						setAlbum(bitmap);
					}else{
						setAlbum(R.drawable.default_album);
					}
				}*/
                setAlbum(mCachedArtwork);
                int duration = intent.getIntExtra("duration", 0);
                int currentPosition = intent.getIntExtra("currentPosition", 0);
                tv_player_playing_time.setText(Common.formatSecondTime(currentPosition));
                tv_player_playering_duration.setText(Common.formatSecondTime(duration));
                sb_player_playprogress.setMax(duration);
                sb_player_playprogress.setProgress(currentPosition);
                sb_player_playprogress.setSecondaryProgress(0);

                //modi 刷新歌词
                EventBus.getDefault().post(new CommonMessage(MSG_SONG_PLAY_OVER,null));
            } else if (flag == MediaPlayerManager.FLAG_INIT) {//初始化播放信息
                int currentPosition = intent.getIntExtra("currentPosition", 0);
                int duration = intent.getIntExtra("duration", 0);
                int playerMode = intent.getIntExtra("playerMode", 0);
                int playerState = intent.getIntExtra("playerState", 0);

                currentTime = currentPosition;
                //initLrchandler(mCurrentSong, mCurrentSong.getLyricPath());

                tv_player_song_info.setText(lTitle);

                if (playerState == MediaPlayerManager.STATE_PLAYER || playerState == MediaPlayerManager.STATE_PREPARE) {//播放
                    ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_pause);
                } else {
                    ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_play);
                }

                ibtn_player_control_mode
                        .setBackgroundResource(MODE_DRAWABLE_ID[playerMode]);

                sb_player_playprogress.setMax(duration);
                sb_player_playprogress.setProgress(currentPosition);
                tv_player_playing_time.setText(Common.formatSecondTime(currentPosition));
                tv_player_playering_duration.setText(Common.formatSecondTime(duration));
                tv_player_song_info.setText(lTitle);
				/*String albumPic=intent.getStringExtra("albumPic");
				if(TextUtils.isEmpty(albumPic)){
					setAlbum(R.drawable.default_album);
				}else{
					Bitmap bitmap=BitmapFactory.decodeFile(albumPic);
					//判断SD图片是否存在
					if(bitmap!=null){
						setAlbum(bitmap);
					}else{
						setAlbum(R.drawable.default_album);
					}
				}*/
                setAlbum(mCachedArtwork);
            } else if (flag == MediaPlayerManager.FLAG_BUFFERING) {
                int percent = intent.getIntExtra("percent", 0);
                percent = (int) (sb_player_playprogress.getMax() / 100f) * percent;
                sb_player_playprogress.setSecondaryProgress(percent);
            } else if (flag == MediaPlayerManager.FLAG_LIST) {
                int state = mediaPlayerManager.getPlayerState();
                if (state == MediaPlayerManager.STATE_PLAYER || state == MediaPlayerManager.STATE_PREPARE) {//????
                    ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_pause);
                } else {
                    ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_play);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {

        IntentFilter lIntentFilter = new IntentFilter(getPackageName() + "update_lrc");
        unregisterReceiver(updateLrcReceiver);

/*        if (mediaPlayerManager != null){
            mediaPlayerManager.unbindService();
            unregisterReceiver(mediaPlayerBroadcastReceiver);
            //mediaPlayerManager = null;
        }*/
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    // 记录FlingGalleryView 上次Touch事件的Position
    private float lastX = 0;
    private float lastY = 0;
    private OnCustomTouchListener customTouchListener = new OnCustomTouchListener() {

        public void operation(MotionEvent event) {
            final int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                lastX = event.getX();
                lastY = event.getY();
            } else if (action == MotionEvent.ACTION_UP) {
                if (lastX == event.getX() && lastY == event.getY()) {
                    voicePanelAnimation();
                }
            }
        }

    };

    // 设置专辑封面-默认图片
    private void setAlbum(int rid) {
        iv_player_ablum.setImageResource(rid);
        iv_player_ablum_reflection.setImageBitmap(ImageUtil
                .createReflectionBitmapForSingle(BitmapFactory.decodeResource(
                        getResources(), rid)));
    }

    // 设置专辑封面-SD路径
    private void setAlbum(Bitmap bitmap) {
        iv_player_ablum.setImageBitmap(bitmap);
        iv_player_ablum_reflection.setImageBitmap(ImageUtil
                .createReflectionBitmapForSingle(bitmap));
    }

    // 音量面板显示和隐藏
    private void voicePanelAnimation() {
        if (ll_player_voice.getVisibility() == View.GONE) {
            ll_player_voice.startAnimation(showVoicePanelAnimation);
            ll_player_voice.setVisibility(View.VISIBLE);
        } else {
            ll_player_voice.startAnimation(hiddenVoicePanelAnimation);
            ll_player_voice.setVisibility(View.GONE);
        }
    }

    private OnClickListener listener = new OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_player_voice:
                    voicePanelAnimation();
                    break;
                case R.id.ibtn_player_list:
                    //overridePendingTransition(R.anim.act_in, R.anim.act_out);
                    startMainRootAnimation();
                    finish();
                    break;
                case R.id.ibtn_player_control_menu:
                    mRecentSongList = View.inflate(PlayerMainActivity.this, R.layout.play_song_list, null);
                    //设置高斯模糊背景
                    //RSGaussianBlurProcessor.getInstance(mActivity).process()
                    Bitmap bgBitmap = BitmapFactory.decodeResource(PlayerMainActivity.this.getResources(), PICIDS[BG_CURRENT_INDEX]);
                    //压缩图片
                    //Bitmap compressedBgBitmap = BlurUtils.compressBitmap(bgBitmap, 8);
                    Bitmap blurBgBitmap = NdkStackBlurProcessor.INSTANCE.process(bgBitmap, 25);
                    mRecentSongList.setBackgroundDrawable(new BitmapDrawable(blurBgBitmap));
                    //lInflate.setBackgroundResource(Constants.PICIDS[BG_CURRENT_INDEX]);
                    //lInflate.setBackgroundResource(Constants.PICIDS[new SystemSetting(PlayerMainActivity.this,false).getCurrentSkinResId()]);
                    mListViewRecent = (ListView) mRecentSongList.findViewById(R.id.lv_list_play_song_list);
                    List<String[]> data=new SongDao(PlayerMainActivity.this).searchByLately(mediaPlayerManager.getLatelyStr());
                    int[] playerInfo=new int[]{mediaPlayerManager.getSongId(),mediaPlayerManager.getPlayerState()};
                    mListViewRecent.setAdapter(new PlaySongItemAdapter(PlayerMainActivity.this, data,playerInfo));
                    mListViewRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            playerMusicByItem(view,MediaPlayerManager.PLAYERFLAG_LATELY,null);
                        }
                    });
                    XfDialog lXfRecentSongListDialog = new XfDialog.Builder(PlayerMainActivity.this)
                            .setTitle("当前正在播放：" + tv_player_song_info.getText())
                            //.setMessage(getResources().getString(R.string.pause_not_develop))
                            .setView(mRecentSongList)
                            .create();
                    ScaleAnimation lScaleAnimation = new ScaleAnimation(0, 1, 0, 1, 0, ScreenUtils.getScreenHeight(PlayerMainActivity.this));
                    Window lWindow = lXfRecentSongListDialog.getWindow();
                    lWindow.setWindowAnimations(R.style.animationRecentSongListDialog);
                    lXfRecentSongListDialog.show();
                    break;
                case R.id.ibtn_player_control_mode:
                    int player_mode = mediaPlayerManager.getPlayerMode();
                    if (player_mode == MediaPlayerManager.MODE_SEQUENCE) {
                        player_mode = MediaPlayerManager.MODE_CIRCLELIST;
                    } else {
                        player_mode++;
                    }
                    mediaPlayerManager.setPlayerMode(player_mode);
                    ibtn_player_control_mode
                            .setBackgroundResource(MODE_DRAWABLE_ID[player_mode]);
                    toastMsg = Common.showMessage(toastMsg,
                            PlayerMainActivity.this, player_modeStr[player_mode]);
                    break;
                case R.id.ibtn_player_control_pre:
                    ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_pause);
                    mediaPlayerManager.previousPlayer();
                    refreshPlayLrc();
                    break;
                case R.id.ibtn_player_control_play:
                    if (mediaPlayerManager.getPlayerState() == MediaPlayerManager.STATE_NULL) {
                        toastMsg = Common.showMessage(toastMsg, PlayerMainActivity.this, getResources().getString(R.string.please_add_song));
                        return;
                    }
                    //顺序列表播放结束
                    if (mediaPlayerManager.getPlayerState() == MediaPlayerManager.STATE_OVER) {
                        toastMsg = Common.showMessage(toastMsg, PlayerMainActivity.this, getResources().getString(R.string.list_sqe_play_finish));
                        return;
                    }

                    mediaPlayerManager.pauseOrPlayer();

                    final int state = mediaPlayerManager.getPlayerState();
                    if (state == MediaPlayerManager.STATE_PLAYER || state == MediaPlayerManager.STATE_PREPARE) {//播放
                        ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_pause);
                    } else if (state == MediaPlayerManager.STATE_PAUSE) {//暂停
                        ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_play);

                    }
                    break;
                case R.id.ibtn_player_control_next:
                    ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_pause);
                    mediaPlayerManager.nextPlayer();
                    refreshPlayLrc();
                    break;
                default:
                    break;
            }
        }
    };

    private void playerMusicByItem(View view,int flag,String condition){
        /*if(mediaPlayerManager.getPlayerFlag()==MediaPlayerManager.PLAYERFLAG_WEB){
            int[] playerInfo=new int[]{-1,-1};
            ((SongItemWebAdapter)lv_list_web.getAdapter()).setPlayerInfo(playerInfo);
        }*/
        int songId=Integer.valueOf(((PlaySongItemAdapter.ViewHolder)view.getTag()).tv_song_list_item_bottom.getTag().toString());
        if(songId==mediaPlayerManager.getSongId()){
            PlayerOrPause(view);
        }else {
            ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_mini_pause);
            mediaPlayerManager.player(songId,flag, condition);
            int[] playerInfo=new int[]{songId,mediaPlayerManager.getPlayerState()};
            ((PlaySongItemAdapter)mListViewRecent.getAdapter()).setPlayerInfo(playerInfo);
        }
    }
    /**
     * 播放或暂停歌曲
     * */
    private void PlayerOrPause(View v){
        if(mediaPlayerManager.getPlayerState()==MediaPlayerManager.STATE_NULL){
            toast=Common.showMessage(toast, PlayerMainActivity.this, "请先添加歌曲...");
            return;
        }
        if(v==null){
            //当前列表播放结束
            if(mediaPlayerManager.getPlayerState()==MediaPlayerManager.STATE_OVER){
                toast=Common.showMessage(toast, PlayerMainActivity.this, "当前列表已经播放完毕！");
                //ibtn_player_albumart.stopRotation();
                return;
            }
        }
        mediaPlayerManager.pauseOrPlayer();
        final int state=mediaPlayerManager.getPlayerState();
        LogUtil.e("播放状态："+state);
        int itemRsId=0;
        if(state==MediaPlayerManager.STATE_PLAYER||state==MediaPlayerManager.STATE_PREPARE){//播放
            ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_mini_pause);
            itemRsId=R.drawable.music_list_item_player;
            //modi 发送更新歌词界面消息
            EventBus.getDefault().post(new CommonMessage(MSG_SONG_PLAY_OVER,null));
        }else if(state==MediaPlayerManager.STATE_PAUSE){//暂停
            ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_mini_player);
            itemRsId=R.drawable.music_list_item_pause;
        }
        if(v==null){
            ((PlaySongItemAdapter)mListViewRecent.getAdapter()).setPlayerState(mediaPlayerManager.getPlayerState());
        }else{
            ((PlaySongItemAdapter.ViewHolder)v.getTag()).tv_song_list_item_number.setBackgroundResource(itemRsId);
        }
    }

    private void refreshPlayLrc() {
        MainActivity.mContext.initLrc();
        lrcHandler = MainActivity.lrcHandler;
        lrcHandler.setCurrentIndex(LRC_CURRENT_INDEX);
        showLrcHandler.sendEmptyMessageDelayed(2,500);
    }

    private OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (seekBar.getId() == R.id.sb_player_voice) {

            } else if (seekBar.getId() == R.id.sb_player_playprogress) {
                isSeekDrag = false;
                mediaPlayerManager.seekTo(seekBar.getProgress());
                //更新当前播放时间
                currentTime = mediaPlayerManager.getPlayerProgress();
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (seekBar.getId() == R.id.sb_player_playprogress) {
                isSeekDrag = true;
                tv_player_playing_time.setText(Common.formatSecondTime(seekBar.getProgress()));
            }
        }

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (seekBar.getId() == R.id.sb_player_voice) {
                // 设置音量
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            } else if (seekBar.getId() == R.id.sb_player_playprogress) {
                if (isSeekDrag) {
                    tv_player_playing_time.setText(Common.formatSecondTime(progress));
                }
            }

            currentTime = progress;

        }
    };

    /**
     * 随机获取浮动标签
     *
     * @param keywordsFlow
     * @param arr
     */
    private void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
        Random random = new Random();
        for (int i = 0; i < MAX_WORLDS; i++) {
            int ran = random.nextInt(arr.length);
            String tmp = arr[ran];
            keywordsFlow.feedKeyword(tmp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新状态
        int lPlayerState = mediaPlayerManager.getPlayerState();
        if (lPlayerState == MediaPlayerManager.STATE_PLAYER || lPlayerState == MediaPlayerManager.STATE_PREPARE) {//播放
            ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_pause);
            mState = MediaPlayerManager.STATE_PLAYER;
        } else if (lPlayerState == MediaPlayerManager.STATE_PAUSE) {//暂停
            ibtn_player_control_play.setBackgroundResource(R.drawable.player_btn_player_play);
            mState = MediaPlayerManager.STATE_PAUSE;
        }
        if (mediaPlayerManager!=null){
            tv_player_song_info.setText(mediaPlayerManager.getTitle());
        }
    }


    @Override
    public void onBackPressed() {
        startMainRootAnimation();
        //finish();
        super.onBackPressed();
    }

    class showThread extends Thread {
        @Override
        public void run() {
            while (isshowkeyflower) {
                try {
                    if (true) {
                        nameshandler.sendEmptyMessage(2);
                    }
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/*    class showLrcThread extends Thread {
        int i = 0;  //下标
        @Override
        public void run() {
            mState = mediaPlayerManager.getPlayerState();
            if (tv_player_lyric_info != null) {
                //i = Common.getCurrentIndex(currentTime);
                i = Common.getLrcIndex(currentTime,mediaPlayerManager.getPlayerDuration(),lrcHandler.getWords());
            }
            LogUtil.e("播放状态：" + mState);
            while (mState == MediaPlayerManager.STATE_PLAYER) {
                try {
                    List<Sentence> lWords = lrcHandler.getWords();
                    if (lWords != null&&lWords.size()>0) {
                        Sentence lSentence = lWords.get(i);
                        showLrcHandler.sendEmptyMessage(2);
                        long sleepTime = lSentence.getToTime() - lSentence.getFromTime();
                        LogUtil.e(sleepTime + "");
                        sleep(sleepTime);
                        //sleep(1000);
                        i++;
                        if (i == lWords.size() - 1) {
                            mediaPlayerManager.stop();
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }*/

    class UpdateLrcBrodcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.e("UpdateLrcBrodcast");
            String lFile = intent.getStringExtra("file");
            String path = Constants.LRC_DIRS_PATH + File.separator + StringHelper.getPingYin(lFile) + ".lrc";

            lrcHandler.readLRC(path);
        }
    }

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
            Sentence lSentence = LRC_WORD_DATA.get(currentIndex);
            int middle = (int) (lSentence.getToTime()-lSentence.getFromTime());
            lrcHandler.setCurrentIndex(currentIndex);
            tv_player_lyric_info.setLrcHandler(lrcHandler);
            //showLrcHandler.sendEmptyMessage(2);
            showLrcHandler.sendEmptyMessageAtTime(2,middle);
        }
        if (event.getMsg().equals(Constants.MSG_PLAYA_OVER)){
            //刷新歌词
            //initLrchandler(mCurrentSong, mCurrentSong.getLyricPath());
            tv_player_lyric_info.invalidate();
        }
        if (event.getMsg().equals(Constants.MSG_SONG_PLAY_OVER)){
            //刷新歌词
            refreshPlayLrc();
        }
        if (event.getMsg().equals(Constants.MSG_SEND_CHANGE_BG)) {
            LogUtil.e("PlayerMainActivity--事件接收成功");
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

    private void refreshBackground(String path) {
        this.getWindow().setBackgroundDrawable(new BitmapDrawable(Common.getBackgroundByPath(path)));
        if (mRecentSongList!=null){
            mRecentSongList.setBackground(new BitmapDrawable(Common.getBackgroundByPath(path)));
        }
    }
}