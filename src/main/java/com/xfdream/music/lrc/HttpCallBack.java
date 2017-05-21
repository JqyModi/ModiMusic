package com.xfdream.music.lrc;

import java.io.File;

/**
 * Created by Modi on 2016/12/4.
 * 邮箱：1294432350@qq.com
 */

public interface HttpCallBack {
    File onLoading(long current, long total);
}