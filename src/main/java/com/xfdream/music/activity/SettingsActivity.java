package com.xfdream.music.activity;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.xfdream.music.R;
import com.xfdream.music.data.MusicNum;
import com.xfdream.music.data.SystemSetting;
import com.xfdream.music.entity.ImageBg;
import com.xfdream.music.message.CommonMessage;
import com.xfdream.music.service.FlowLrcService;
import com.xfdream.music.util.Constants;
import com.xfdream.music.util.LogUtil;
import com.xfdream.music.util.PreferenceService;
import com.xfdream.music.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.xfdream.music.activity.MainActivity.CONNECTIVITY_CHANGE_ACTION;
import static com.xfdream.music.util.Constants.AutoUpdate;
import static com.xfdream.music.util.Constants.BASE_DIRS_PATH;
import static com.xfdream.music.util.Constants.BG_CURRENT_PATH;
import static com.xfdream.music.util.Constants.LRC_DIRS_PATH;
import static com.xfdream.music.util.Constants.LongLight;
import static com.xfdream.music.util.Constants.MSG_SEND_SONG_TEXT;
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
import static com.xfdream.music.util.Constants.WifiNet;
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

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    private static File picFile;
    private static Uri photoUri;
    private static SettingsActivity mSettingsActivity;

    private static final int activity_result_camara_with_data = 1006;
    private static final int activity_result_cropimage_with_data = 1007;

    private static PreferenceService service;

    private static SystemSetting mSetting;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                //preference.setSummary(stringValue);
                if (preference.getKey().equals("pref_key_play_bg")) {
                    doPickPhotoAction();
                }

                if (preference instanceof SwitchPreference) {
                    SwitchPreference lSwitchPreference = (SwitchPreference) preference;
                    //网络运营商
                    /*if (lSwitchPreference.getKey().equals("pref_switch_wifi_net")) {
                        //ToastUtil.show(mSettingsActivity, "pref_switch_wifi_net");
                        wifiNet();
                    } else */
                    if (lSwitchPreference.getKey().equals("pref_switch_traffic_download_photo")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_traffic_download_photo");
                        trafficDownloadPhoto();
                    } else if (lSwitchPreference.getKey().equals("pref_switch_traffic_download_lrc")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_traffic_download_lrc");
                        trafficDownloadLrc();
                    } else if (lSwitchPreference.getKey().equals("pref_switch_traffic_listener_online")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_traffic_listener_online");
                        trafficListenerOnline();
                    }
                    //播放器显示
                    else if (lSwitchPreference.getKey().equals("pref_switch_show_flow_tag")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_show_flow_tag");
                        showFlowTag();
                    }
                    //歌词显示
                    /*else if (lSwitchPreference.getKey().equals("pref_switch_show_lrc")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_show_lrc");
                        showLrc();
                    } else */
                    if (lSwitchPreference.getKey().equals("pref_switch_show_lrc_play")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_show_lrc_play");
                        showPlayLrc();
                    } else if (lSwitchPreference.getKey().equals("pref_switch_show_lrc_desk")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_show_lrc_desk");
                        deskLrc();
                    }
                    /*else if (lSwitchPreference.getKey().equals("pref_switch_show_lrc_lock")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_show_lrc_lock");
                        lockLrc();
                    }*/
                    //其他
                    else if (lSwitchPreference.getKey().equals("pref_switch_try_listener")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_try_listener");
                        tryListenerMode();
                    }
                    /*else if (lSwitchPreference.getKey().equals("pref_switch_screen_long_light")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_screen_long_light");
                        screenLongLight();
                    }*/
                    else if (lSwitchPreference.getKey().equals("pref_switch_memory_exit")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_memory_exit");
                        memoryExit();
                    }
                    /*else if (lSwitchPreference.getKey().equals("pref_switch_shade")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_shade");
                        shade();
                    }*/
                    //更新
                    else if (lSwitchPreference.getKey().equals("pref_switch_check_update")) {
                        ToastUtil.show(mSettingsActivity, "pref_switch_check_update");
                        checkUpdate();
                    } else if (lSwitchPreference.getKey().equals("pref_key_update")) {
                        ToastUtil.show(mSettingsActivity, "pref_key_update");
                        update();
                    }
                }
            }
            return true;
        }
    };

    private static Preference.OnPreferenceClickListener sPreferenceClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            //网络运营商
            /*if (preference.getKey().equals("pref_switch_wifi_net")) {
                //ToastUtil.show(mSettingsActivity, "pref_switch_wifi_net");
                wifiNet();
            } else*/
            if (preference.getKey().equals("pref_switch_traffic_download_photo")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_traffic_download_photo");
                trafficDownloadPhoto();
            } else if (preference.getKey().equals("pref_switch_traffic_download_lrc")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_traffic_download_lrc");
                trafficDownloadLrc();
            } else if (preference.getKey().equals("pref_switch_traffic_listener_online")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_traffic_listener_online");
                trafficListenerOnline();
            }
            //播放器显示
            else if (preference.getKey().equals("pref_key_play_bg")) {
                doPickPhotoAction();
            } else if (preference.getKey().equals("pref_switch_show_flow_tag")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_show_flow_tag");
                showFlowTag();
            }
            //歌词显示
            /*else if (preference.getKey().equals("pref_switch_show_lrc")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_show_lrc");
                showLrc();
            } else */
            if (preference.getKey().equals("pref_switch_show_lrc_play")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_show_lrc_play");
                showPlayLrc();
            } else if (preference.getKey().equals("pref_switch_show_lrc_desk")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_show_lrc_desk");
                deskLrc();
            }
            /*else if (preference.getKey().equals("pref_switch_show_lrc_lock")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_show_lrc_lock");
                lockLrc();
            }*/
            //其他
            else if (preference.getKey().equals("pref_switch_try_listener")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_try_listener");
                tryListenerMode();
            }
            /*else if (preference.getKey().equals("pref_switch_screen_long_light")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_screen_long_light");
                screenLongLight();
            } */
            else if (preference.getKey().equals("pref_switch_memory_exit")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_memory_exit");
                memoryExit();
            }
            /*else if (preference.getKey().equals("pref_switch_shade")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_shade");
                shade();
            }*/
            //更新
            else if (preference.getKey().equals("pref_switch_check_update")) {
                ToastUtil.show(mSettingsActivity, "pref_switch_check_update");
                checkUpdate();
            } else if (preference.getKey().equals("pref_key_update")) {
                ToastUtil.show(mSettingsActivity, "pref_key_update");
                update();
            }
            return true;
        }
    };

    private static void trafficDownloadPhoto() {
        if (isTrafficPhoto) {
            isTrafficPhoto = false;
        } else {
            isTrafficPhoto = true;
        }
        mSetting.setValue(TrafficPhoto, isTrafficPhoto);
    }

    private static void trafficDownloadLrc() {
        if (isTrafficLrc) {
            isTrafficLrc = false;
        } else {
            isTrafficLrc = true;
        }
        mSetting.setValue(TrafficLrc, isTrafficLrc);
    }

    private static void trafficListenerOnline() {
        if (isTrafficOnline) {
            isTrafficOnline = false;
        } else {
            isTrafficOnline = true;
        }
        mSetting.setValue(TrafficOnline, isTrafficOnline);
    }

    private static void tryListenerMode() {
        if (isTryListener) {
            isTryListener = false;
        } else {
            isTryListener = true;
        }
        mSetting.setValue(TryListener, isTryListener);
    }

    private static void screenLongLight() {
        if (isLongLight) {
            isLongLight = false;
        } else {
            isLongLight = true;
        }
        mSetting.setValue(LongLight, isLongLight);
    }

    private static void memoryExit() {
        if (isMemoryExit) {
            isMemoryExit = false;
        } else {
            isMemoryExit = true;
        }
        mSetting.setValue(MemoryExit, isMemoryExit);
    }

    private static void shade() {
        if (isShade) {
            isShade = false;
        } else {
            isShade = true;
        }
        mSetting.setValue(Shade, isShade);
    }

    private static void checkUpdate() {
        if (isAutoUpdate) {
            isAutoUpdate = false;
        } else {
            isAutoUpdate = true;
        }
        mSetting.setValue(AutoUpdate, isAutoUpdate);
    }

    private static void update() {

    }

    private static void showPlayLrc() {
        if (isShowPlayLrc) {
            isShowPlayLrc = false;
        } else {
            isShowPlayLrc = true;
        }
        //菜单控制项状态
        mSetting.setValue(WifiNet, isWifiNet);
    }

    private static void showLrc() {
        if (isShowLrc) {
            isShowLrc = false;
        } else {
            isShowLrc = true;
        }
        mSetting.setValue(ShowLrc, isShowLrc);
    }

    private static void showFlowTag() {
        if (Constants.isShowFlowTag) {
            //Constants.MAX_WORLDS = MainActivity.mFlowTagCount;
            isShowFlowTag = false;
        } else {
            isShowFlowTag = true;
        }
        mSetting.setValue(ShowFlowTag, isShowFlowTag);
    }

    /**
     * 锁屏歌词
     */
    private static void lockLrc() {

    }

    /**
     * 桌面歌词
     */
    private static void deskLrc() {
        if (isShowDeskLrc) {
            isShowDeskLrc = false;
        } else {
            isShowDeskLrc = true;
        }
        mSetting.setValue(ShowDeskLrc, isShowDeskLrc);
    }

    /**
     * WiFi联网
     */
    private static void wifiNet() {
        mSettingsActivity.registerDateTransReceiver();
        if (isWifiNet) {
            isWifiNet = false;
        } else {
            isWifiNet = true;
        }
        mSetting.setValue(WifiNet, isWifiNet);
    }

    private void registerDateTransReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_CHANGE_ACTION);
        filter.setPriority(1000);
        registerReceiver(new NetChangeReceiver(), filter);
    }

    public class NetChangeReceiver extends BroadcastReceiver {
        public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, CONNECTIVITY_CHANGE_ACTION)) {
                int netType = getNetworkType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    ToastUtil.show(getApplicationContext(), "TYPE_WIFI");
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    ToastUtil.show(getApplicationContext(), "TYPE_MOBILE");
                } else {
                    ToastUtil.show(getApplicationContext(), "not connection");
                }
            }
        }
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    private int getNetworkType() {
        ConnectivityManager connectMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null) {
            return info.getType();
        } else {
            return -1;
        }
    }

    /**
     * 设置播放背景
     */
    private static void doPickPhotoAction() {
        final Context dialogContext = new ContextThemeWrapper(mSettingsActivity,
                android.R.style.Theme_Light);
        String cancel = mSettingsActivity.getString(R.string.cancel);
        String[] choices;
        choices = new String[2];
        choices[0] = mSettingsActivity.getString(R.string.title_defult_bg);
        choices[1] = mSettingsActivity.getString(R.string.title_photo);
        //choices[2] = mSettingsActivity.getString(R.string.title_picture);
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
                android.R.layout.simple_list_item_1, choices);
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                dialogContext);
        builder.setTitle(mSettingsActivity.getString(R.string.title_choice_pic));
        builder.setSingleChoiceItems(adapter, -1,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("ShowToast")
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                ImageBg.setback(null);
                                service.background("", 0);
                                Intent intent = new Intent(
                                        "com.cn.musicserviceplayer");
                                mSettingsActivity.sendBroadcast(intent);
                                break;
                            case 1:
                                String status = Environment
                                        .getExternalStorageState();
                                if (status.equals(Environment.MEDIA_MOUNTED)) {
                                    String lPath = doTakePhoto();
                                    LogUtil.e("BG_PATH = "+lPath);
                                    BG_CURRENT_PATH = lPath;
                                } else {
                                    Toast.makeText(dialogContext, "SD不存在", Toast.LENGTH_SHORT)
                                            .show();
                                }
                                break;
                           /* case 2:
                                String lPath = doCropPhoto();
                                LogUtil.e("BG_PATH = "+lPath);
                                Constants.BG_CURRENT_PATH = lPath;
                                break;*/
                        }
                    }
                });
        builder.setNegativeButton(cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private static void setCurrentBackgroung(String path) {
        //modi  切换背景图片
        //SettingsActivity.this.getWindow().setBackgroundDrawable();
        //根据路径获取图片
        File mFile=new File(path);
        //若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            mSettingsActivity.getWindow().setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
        //modi  发送切换背景广播
        EventBus.getDefault().post(new CommonMessage(Constants.MSG_SEND_CHANGE_BG,BG_CURRENT_PATH));
    }

    protected static String doTakePhoto() {
        try {
            File uploadFileDir = new File(BASE_DIRS_PATH,"/bg");
            //String fileName = "/backbg";
            //File f = new File(LRC_DIRS_PATH, fileName);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (!uploadFileDir.exists()) {
                uploadFileDir.mkdirs();
            }
            picFile = new File(uploadFileDir, "background.jpeg");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            photoUri = Uri.fromFile(picFile);
            Log.i("photoUri", String.valueOf(photoUri));
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            mSettingsActivity.startActivityForResult(cameraIntent,
                    activity_result_camara_with_data);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return picFile.getAbsolutePath();
    }

    protected static String doCropPhoto() {
        try {
           /* File pictureFileDir = new File(Environment
                    .getExternalStorageDirectory().toString(),
                    "/youyamusic/backbg");*/
            File pictureFileDir = new File(BASE_DIRS_PATH,"/bg");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            picFile = new File(pictureFileDir, "background.jpeg");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            photoUri = Uri.fromFile(picFile);
            final Intent intent = getCropImageIntent();
            mSettingsActivity.startActivityForResult(intent, activity_result_cropimage_with_data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picFile.getAbsolutePath();
    }

    public static Intent getCropImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 576);
        intent.putExtra("aspectY", 709);
        intent.putExtra("outputX", 1152);
        intent.putExtra("outputY", 1418);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case activity_result_camara_with_data:
                try {
                    cropImageUriByTakePhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setCurrentBackgroung(BG_CURRENT_PATH);
                break;
            case activity_result_cropimage_with_data:
                try {
                    if (data != null && resultCode != 0) {
                        service.background(photoUri.toString(), 0);
                        Uri uri = Uri.parse(photoUri.toString());
                        ContentResolver contentResolver = this.getContentResolver();
                        Bitmap bitmap2 = BitmapFactory.decodeStream(contentResolver
                                .openInputStream(uri));
                        ImageBg.setback(bitmap2);
                        MusicNum.putusbtn(18, true);
                        MusicNum.putusbtn(20, true);
                        Intent intent = new Intent("com.cn.musicserviceplayer");
                        sendBroadcast(intent);
                        //modi
                        setCurrentBackgroung(BG_CURRENT_PATH);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void cropImageUriByTakePhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 576);
        intent.putExtra("aspectY", 709);
        intent.putExtra("outputX", 1152);
        intent.putExtra("outputY", 1418);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, activity_result_cropimage_with_data);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            // Set the summary to reflect the new value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));

        } else if (preference instanceof RingtonePreference) {
            // For ringtone preferences, look up the correct display value
            // using RingtoneManager.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        } else {
            //
        }
    }

    private static void bindPreferenceClickEvent(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceClickListener(sPreferenceClickListener);

        // Trigger the listener immediately with the preference's
        // current value.

        //sPreferenceClickListener.onPreferenceClick(preference);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.perference_set_activity);

        super.onCreate(savedInstanceState);

        mSettingsActivity = this;

        service = new PreferenceService(this);

        setupActionBar();

        mSetting = new SystemSetting(SettingsActivity.this, true);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     * ???PreferenceFragment
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);

/*                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);*/
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValue(findPreference("example_text"));
            //bindPreferenceSummaryToValue(findPreference("example_list"));
            //bindPreferenceSummaryToValue(findPreference("about"));

            bindPreferenceClickEvent(findPreference("pref_key_play_bg"));

            /*//网络运营商
            bindPreferenceClickEvent(findPreference("pref_switch_wifi_net"));
            bindPreferenceClickEvent(findPreference("pref_switch_traffic_download_photo"));
            bindPreferenceClickEvent(findPreference("pref_switch_traffic_download_lrc"));
            bindPreferenceClickEvent(findPreference("pref_switch_traffic_listener_online"));
            //播放器显示
            bindPreferenceClickEvent(findPreference("pref_key_play_bg"));
            bindPreferenceClickEvent(findPreference("pref_switch_show_flow_tag"));
            //歌词显示
            bindPreferenceClickEvent(findPreference("pref_switch_show_lrc"));
            bindPreferenceClickEvent(findPreference("pref_switch_show_lrc_play"));
            bindPreferenceClickEvent(findPreference("pref_switch_show_lrc_desk"));
            bindPreferenceClickEvent(findPreference("pref_switch_show_lrc_lock"));
            //其他
            bindPreferenceClickEvent(findPreference("pref_switch_try_listener"));
            bindPreferenceClickEvent(findPreference("pref_switch_screen_long_light"));
            bindPreferenceClickEvent(findPreference("pref_switch_memory_exit"));
            bindPreferenceClickEvent(findPreference("pref_switch_shade"));
            //更新
            bindPreferenceClickEvent(findPreference("pref_key_update"));
            bindPreferenceClickEvent(findPreference("pref_switch_check_update"));*/


            //网络运营商
            //bindPreferenceSummaryToValue(findPreference("pref_switch_wifi_net"));
            bindPreferenceSummaryToValue(findPreference("pref_switch_traffic_download_photo"));
            bindPreferenceSummaryToValue(findPreference("pref_switch_traffic_download_lrc"));
            bindPreferenceSummaryToValue(findPreference("pref_switch_traffic_listener_online"));
            //播放器显示
            bindPreferenceSummaryToValue(findPreference("pref_key_play_bg"));
            bindPreferenceSummaryToValue(findPreference("pref_switch_show_flow_tag"));
            //歌词显示
            //bindPreferenceSummaryToValue(findPreference("pref_switch_show_lrc"));
            bindPreferenceSummaryToValue(findPreference("pref_switch_show_lrc_play"));
            bindPreferenceSummaryToValue(findPreference("pref_switch_show_lrc_desk"));
            //bindPreferenceSummaryToValue(findPreference("pref_switch_show_lrc_lock"));
            //其他
            bindPreferenceSummaryToValue(findPreference("pref_switch_try_listener"));
            //bindPreferenceSummaryToValue(findPreference("pref_switch_screen_long_light"));
            bindPreferenceSummaryToValue(findPreference("pref_switch_memory_exit"));
            //bindPreferenceSummaryToValue(findPreference("pref_switch_shade"));
            //更新
            bindPreferenceSummaryToValue(findPreference("pref_key_update"));
            bindPreferenceSummaryToValue(findPreference("pref_switch_check_update"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.

            //  modi --??
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
