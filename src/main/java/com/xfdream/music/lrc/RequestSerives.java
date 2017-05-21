package com.xfdream.music.lrc;

import com.xfdream.music.entity.KgRecSongOne;
import com.xfdream.music.entity.KgSongInfo;
import com.xfdream.music.entity.KgSongOne;
import com.xfdream.music.entity.LrcStrInfo;
import com.xfdream.music.entity.NetLrc;
import com.xfdream.music.entity.NetLrcInfo;
import com.xfdream.music.entity.NetworkLrc;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Modi on 2016/12/3.
 * 邮箱：1294432350@qq.com
 */

public interface RequestSerives {

    @GET("api/lyric/{songName}/{artist}")
    Call<NetworkLrc> getNetworkLrc(@Path("songName") String songName,
                                   @Path("artist") String artist);

    //keyword=歌曲名&duration=歌曲总时长(毫秒)
    @GET("search?ver=1&man=yes&client=pc")
    Call<NetLrcInfo> getNetLrc(@Query("keyword") String songName,
                               @Query("duration") int duration);


    @GET(" ")
    Call<File> getNetworkLrcFile();

    @Streaming //大文件时要加不然会OOM
    @GET("{sid}.lrc")
    Call<ResponseBody> downloadFile(@Path("sid") String sid);

    //http://lyrics.kugou.com/download?ver=1&client=pc&id=10515303&accesskey=3A20F6A1933DE370EBA0187297F5477D&fmt=lrc&charset=utf8
    @GET("download?ver=1&client=mobi&fmt=lrc&charset=utf8")
    Call<LrcStrInfo> getNetLrcStr(@Query("id") String id,
                                  @Query("accesskey") String accesskey);


    /*
    搜歌API：
    http://mobilecdn.kugou.com/api/v3/search/song?format=jsonp&keyword=%E4%BB%99%E5%89%91&page=1&pagesize=10&showtype=1&callback=kgJSONP238513750
    返回带歌曲列表json文件。
    利用json中的hash查歌曲文件地址：
    http://m.kugou.com/app/i/getSongInfo.php?hash=2b616f6ab9f8655210fd823b900085cc&cmd=playInfo
    返回的就是m4a文件的地址了。
    经过这两步，就可以利用酷狗搜歌，听歌，下歌。

    每日日歌曲推荐
    everyday_song_recommend?platform=android&appid=1100&mid=252521543232814882237569999152811328050&clientver=8352&key=5b3d9ff6f5f094c76154cdfa95f61421&clienttime=1481775715213&userid=0
    */
    @GET("api/v3/search/song?format=json&page=1&pagesize=10&showtype=1")
    Call<KgSongOne> getKgSongOne(@Query("keyword") String keyword);

    @GET("everyday_song_recommend?platform=android&appid=1100&mid=252521543232814882237569999152811328050&clientver=8352&key=5b3d9ff6f5f094c76154cdfa95f61421&clienttime=1481775715213&userid=0")
    Call<KgRecSongOne> getKgRecSongOne();

    @GET("app/i/getSongInfo.php?cmd=playInfo")
    Call<KgSongInfo> getKgSongInfo(@Query("hash") String hash);
}
