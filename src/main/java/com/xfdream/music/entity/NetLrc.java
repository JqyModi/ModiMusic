package com.xfdream.music.entity;

import java.util.List;

/**
 * Created by Modi on 2016/12/3.
 * 邮箱：1294432350@qq.com
 */

public class NetLrc {

/*  code:0
    count:3
    result:[,…]*/

    private int code;
    private int count;
    private List<LrcBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<LrcBean> getResult() {
        return result;
    }

    public void setResult(List<LrcBean> result) {
        this.result = result;
    }

    /*
    aid:2287388
    artist_id:34073
    lrc:"http://s.gecimi.com/lrc/265/26594/2659478.lrc"
    sid:2659478
    song:"撕夜"*/

    private class LrcBean{
        private String aid;
        private String artist_id;
        private String lrc;
        private String sid;
        private String song;

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(String artist_id) {
            this.artist_id = artist_id;
        }

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getSong() {
            return song;
        }

        public void setSong(String song) {
            this.song = song;
        }
    }
}
