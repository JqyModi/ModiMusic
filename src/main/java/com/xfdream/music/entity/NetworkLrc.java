package com.xfdream.music.entity;

import java.util.List;

/**
 * Created by Modi on 2016/12/4.
 * 邮箱：1294432350@qq.com
 */

public class NetworkLrc {

    /**
     * count : 3
     * code : 0
     * result : [{"aid":2287388,"artist_id":34073,"song":"撕夜","lrc":"http://s.gecimi.com/lrc/265/26594/2659478.lrc","sid":2659478},{"aid":2565155,"artist_id":34073,"song":"撕夜","lrc":"http://s.gecimi.com/lrc/305/30526/3052674.lrc","sid":3052674},{"aid":3062279,"artist_id":34073,"song":"撕夜","lrc":"http://s.gecimi.com/lrc/373/37313/3731367.lrc","sid":3731367}]
     */

    private int count;
    private int code;
    /**
     * aid : 2287388
     * artist_id : 34073
     * song : 撕夜
     * lrc : http://s.gecimi.com/lrc/265/26594/2659478.lrc
     * sid : 2659478
     */

    private List<ResultBean> result;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int aid;
        private int artist_id;
        private String song;
        private String lrc;
        private int sid;

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(int artist_id) {
            this.artist_id = artist_id;
        }

        public String getSong() {
            return song;
        }

        public void setSong(String song) {
            this.song = song;
        }

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }
    }
}
