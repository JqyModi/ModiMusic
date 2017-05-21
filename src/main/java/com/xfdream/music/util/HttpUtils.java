package com.xfdream.music.util;

import android.util.Base64;

import com.xfdream.music.entity.Album;
import com.xfdream.music.entity.Artist;
import com.xfdream.music.entity.KgRecSongOne;
import com.xfdream.music.entity.KgSongInfo;
import com.xfdream.music.entity.KgSongOne;
import com.xfdream.music.entity.LrcStrInfo;
import com.xfdream.music.entity.NetLrcInfo;
import com.xfdream.music.entity.Song;
import com.xfdream.music.lrc.RequestSerives;
import com.xfdream.music.message.CommonMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.xfdream.music.util.Constants.MSG_SONG_PLAY_OVER;

/**
 * Created by Modi on 2016/12/14.
 * 邮箱：1294432350@qq.com
 */
public class HttpUtils {

    private static List<Song> mSongs = new ArrayList<>();

    public static List<Song> getWebSongs(String keyword) {
        /*if (!keyword.equals("")){
            findKgSongOne(keyword);
        }else {
            findKgRecSongOne();
        }*/
        findKgRecSongOne();
        LogUtil.e(mSongs.toString());
        return mSongs;
    }

    //network download mp3
    /*
    搜歌API：
    http://mobilecdn.kugou.com/api/v3/search/song?format=jsonp&keyword=%E4%BB%99%E5%89%91&page=1&pagesize=10&showtype=1&callback=kgJSONP238513750
    返回带歌曲列表json文件。
    利用json中的hash查歌曲文件地址：
    http://m.kugou.com/app/i/getSongInfo.php?hash=2b616f6ab9f8655210fd823b900085cc&cmd=playInfo
    返回的就是m4a文件的地址了。
    经过这两步，就可以利用酷狗搜歌，听歌，下歌。

    酷狗每日歌曲推荐
	http://everydayrec.service.kugou.com/everyday_song_recommend?platform=android&appid=1100&mid=252521543232814882237569999152811328050&clientver=8352&key=5b3d9ff6f5f094c76154cdfa95f61421&clienttime=1481775715213&userid=0
    */

    private static void findKgSongOne(final String keyword) {
        String baseUrl = "http://mobilecdn.kugou.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RequestSerives requestSerives = retrofit.create(RequestSerives.class);//这里采用的是Java的动态代理模式
        Call<KgSongOne> call = requestSerives.getKgSongOne(keyword);//传入我们请求的键值对的值
        call.enqueue(new Callback<KgSongOne>() {
            @Override
            public void onResponse(Call<KgSongOne> call, Response<KgSongOne> response) {
                LogUtil.e("HttpUtils1---------------onResponse");
                KgSongOne lBody = response.body();
                if (response.isSuccess()) {
                    int lStatus = lBody.getStatus();
                    if (lStatus == 1) {
                        for (int i=0;i<lBody.getData().getInfo().size();i++){
                            KgSongOne.DataBean.InfoBean lInfoBean = lBody.getData().getInfo().get(i);
                            String lHash = lInfoBean.getHash();
                            String lAlbum_name = lInfoBean.getAlbum_name();
                            findKgSongInfo(lHash, lAlbum_name, i);
                        }
                        EventBus.getDefault().post(new CommonMessage(Constants.MSG_SONG_LOAD_OVER,null));
                    }
                }
            }


            @Override
            public void onFailure(Call<KgSongOne> call, Throwable t) {
                LogUtil.e("HttpUtils1---------------onFailure" + t.getMessage());
            }
        });
    }

    private static void findKgRecSongOne() {
        String baseUrl = "http://everydayrec.service.kugou.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RequestSerives requestSerives = retrofit.create(RequestSerives.class);//这里采用的是Java的动态代理模式
        Call<KgRecSongOne> call = requestSerives.getKgRecSongOne();//传入我们请求的键值对的值
        call.enqueue(new Callback<KgRecSongOne>() {
            @Override
            public void onResponse(Call<KgRecSongOne> call, Response<KgRecSongOne> response) {
                LogUtil.e("HttpUtils1---------------onResponse");
                KgRecSongOne lBody = response.body();
                if (response.isSuccess()) {
                    int lStatus = lBody.getStatus();
                    if (lStatus == 1) {
                        for (int i=0;i<lBody.getData().getSong_list().size();i++){
                            KgRecSongOne.DataBean.SongListBean lSongListBean = lBody.getData().getSong_list().get(i);
                            String lHash = lSongListBean.getHash();
                            String lAlbum_name = lSongListBean.getRemark();
                            findKgSongInfo(lHash, lAlbum_name,i);
                        }
                        EventBus.getDefault().post(new CommonMessage(Constants.MSG_SONG_LOAD_OVER,null));
                    }
                }
            }


            @Override
            public void onFailure(Call<KgRecSongOne> call, Throwable t) {
                LogUtil.e("HttpUtils1---------------onFailure" + t.getMessage());
            }
        });
    }

    private static void findKgSongInfo(String hash, final String album, final int songId) {

        mSongs = new ArrayList<>();

        String baseUrl = "http://m.kugou.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RequestSerives requestSerives = retrofit.create(RequestSerives.class);//这里采用的是Java的动态代理模式
        Call<KgSongInfo> call = requestSerives.getKgSongInfo(hash);//传入我们请求的键值对的值
        call.enqueue(new Callback<KgSongInfo>() {
            @Override
            public void onResponse(Call<KgSongInfo> call, Response<KgSongInfo> response) {
                LogUtil.e("HttpUtils2---------------onResponse");
                KgSongInfo lBody = response.body();
                if (response.isSuccess()) {
                    int lStatus = lBody.getStatus();
                    if (lStatus == 1) {
                        String lUrl = lBody.getUrl();
                        Song lSong = new Song();
                        /*<song id="1">
                        <name>倩女幽魂</name>
                        <artist>张国荣</artist>
                        <album>丰盛的一生之国语精选</album>
                        <displayName>倩女幽魂.mp3</displayName>
                        <mimeType>audio/mpeg</mimeType>
                        <netUrl>http://www.zihou.me/music/danqu/qiannvyouhun.mp3</netUrl>
                        <durationTime>217000</durationTime>
                        <size>3484569</size>
                        </song>*/
                        //lSong.setId(lBody.getSingerId());
                        lSong.setId(songId);
                        lSong.setName(lBody.getSongName());
                        Artist lArtist = new Artist();
                        lArtist.setName(lBody.getSingerName());
                        lSong.setArtist(lArtist);
                        Album lAlbum = new Album();
                        lAlbum.setName(album);
                        lSong.setAlbum(lAlbum);
                        lSong.setDisplayName(lBody.getFileName());
                        lSong.setMimeType("audio/mpeg");
                        lSong.setNetUrl(lBody.getUrl());
                        lSong.setDurationTime(lBody.getTimeLength()*1000);
                        lSong.setSize(lBody.getFileSize());
                        mSongs.add(lSong);
                    }
                }
            }

            @Override
            public void onFailure(Call<KgSongInfo> call, Throwable t) {
                LogUtil.e("HttpUtils2---------------onFailure" + t.getMessage());
            }
        });
    }

    public static Song querySongBySongId(int songId) {
        Song lSong = null;
        //List<Song> lWebSongData = Constants.WEB_SONG_DATA;
        for (int i=0;i<mSongs.size();i++){
            int lId = mSongs.get(i).getId();
            if (lId == songId){
                lSong = mSongs.get(i);
            }
        }
        return lSong;
    }

    //network download lrc
    public static void netLoadLrcByDuration(final String name, int durationTime) {
        //http://lyrics.kugou.com/search?ver=1&man=yes&client=pc&keyword=歌曲名&duration=歌曲总时长(毫秒)&hash=歌曲Hash值(可有可无)
        String baseUrl = "http://lyrics.kugou.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RequestSerives requestSerives = retrofit.create(RequestSerives.class);//这里采用的是Java的动态代理模式
        Call<NetLrcInfo> call = requestSerives.getNetLrc(name, durationTime);//传入我们请求的键值对的值
        call.enqueue(new Callback<NetLrcInfo>() {
            @Override
            public void onResponse(Call<NetLrcInfo> call, Response<NetLrcInfo> response) {
                LogUtil.e("---------------onResponse");
                NetLrcInfo lBody = response.body();
                if (response.isSuccess()) {
                    int lStatus = lBody.getStatus();
                    if (lStatus == 200) {
                        List<NetLrcInfo.CandidatesBean> lCandidates = lBody.getCandidates();
                        NetLrcInfo.CandidatesBean lCandidatesBean = lCandidates.get(0);
                        String lId = lCandidatesBean.getId();
                        String lAccesskey = lCandidatesBean.getAccesskey();
                        LogUtil.e("lId = " + lId + "    lAccesskey = " + lAccesskey);
                        startDownload(lId, lAccesskey, name);
                    }
                }
            }

            @Override
            public void onFailure(Call<NetLrcInfo> call, Throwable t) {
                LogUtil.e("---------------onFailure" + t.getMessage());
            }
        });
    }

    public static void startDownload(String id, String accesskey, final String name) {
        //http://lyrics.kugou.com/download?ver=1&client=pc&id=5872571&accesskey=1031E3A989E497FDBA19532BFB06050B&fmt=lrc&charset=utf8

        //GET /download?ver=1&client=mobi&fmt=krc&charset=utf8&id=20691948&accesskey=C98D8ACFDCC7C0592180776A208BFC0F HTTP/1.1

        String baseUrl = "http://lyrics.kugou.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RequestSerives requestSerives = retrofit.create(RequestSerives.class);//这里采用的是Java的动态代理模式

        String charset = "utf8";
        Call<LrcStrInfo> call = requestSerives.getNetLrcStr(id, accesskey);//传入我们请求的键值对的值
        call.enqueue(new Callback<LrcStrInfo>() {
            @Override
            public void onResponse(Call<LrcStrInfo> call, Response<LrcStrInfo> response) {
                LogUtil.e("---------------onResponse");
                LrcStrInfo lBody = response.body();
                if (response.isSuccess()) {
                    int lStatus = lBody.getStatus();
                    if (lStatus == 200) {    //400
                        String lContent = lBody.getContent();// 编码后
                        //String  base64Token = Base64.encodeToString(token.getBytes(), Base64.DEFAULT);//  编码后
                        String lStrLrc = new String(Base64.decode(lContent.getBytes(), Base64.DEFAULT));
                        LogUtil.e("lStrLrc :" + lStrLrc);
                        String fileName = StringHelper.getPingYin(name) + ".lrc";
                        String lrcDirs = Constants.LRC_DIRS_PATH;

                        // 在内存中创建一个文件对象，注意：此时还没有在硬盘对应目录下创建实实在在的文件
                        File f = new File(lrcDirs, fileName);
                        if (f.exists()) {
                            // 文件已经存在，输出文件的相关信息
                            LogUtil.e(f.getAbsolutePath());
                            LogUtil.e(f.getName());
                            LogUtil.e(f.length() + "");
                        } else {
                            //  先创建文件所在的目录
                            f.getParentFile().mkdirs();
                            try {
                                // 创建新文件
                                f.createNewFile();
                                FileWriter lFileWriter = new FileWriter(f);
                                lFileWriter.write(lStrLrc);
                                lFileWriter.flush();
                                lFileWriter.close();

                                /*Intent lIntent = new Intent(getPackageName() + "update_lrc");
                                lIntent.putExtra("file", StringHelper.getPingYin(name));
                                sendBroadcast(lIntent);*/
                            } catch (IOException e) {
                                System.out.println("创建新文件时出现了错误。。。");
                                e.printStackTrace();
                            }
                        }
                        //modi 发送更新歌词界面消息
                        EventBus.getDefault().post(new CommonMessage(MSG_SONG_PLAY_OVER,null));
                    }
                }
            }

            @Override
            public void onFailure(Call<LrcStrInfo> call, Throwable t) {
                LogUtil.e("---------------onFailure" + t.getMessage());
            }
        });
    }
}
