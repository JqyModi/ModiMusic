package com.xfdream.music.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Modi on 2016/12/14.
 * 邮箱：1294432350@qq.com
 */

public class KgSongInfo {

    /**
     * fileHead : 100
     * q : 17257
     * extra : {"320filesize":11191042,"sqfilesize":33479599,"sqhash":"09BE9E5C6515D5FC9BFE349A2FE5D218","128hash":"2B616F6AB9F8655210FD823B900085CC","320hash":"23F370FA1C067A6C51E091640648680C","128filesize":4485036}
     * fileSize : 4485036
     * hash : 2B616F6AB9F8655210FD823B900085CC
     * mvhash : 1217881290BAA14EFE930CE946367423
     * req_hash : 2B616F6AB9F8655210FD823B900085CC
     * imgUrl : http://singerimg.kugou.com/uploadpic/softhead/{size}/20160426/20160426113231414.jpg
     * url : http://fs.open.kugou.com/1607d1dce2242fa33affd5ce26297d81/5850235b/G003/M0A/1B/1D/o4YBAFS5KDqAZh0gAERvrKQpAZI992.mp3
     * bitRate : 128
     * songName : 春天里
     * intro :
     * topic_remark :
     * singerId : 2726
     * choricSinger : 汪峰
     * status : 1
     * stype : 11323
     * privilege : 8
     * singerName : 汪峰
     * ctype : 1009
     * fileName : 汪峰 - 春天里
     * topic_url :
     * errcode : 0
     * singerHead :
     * extName : mp3
     * error :
     * timeLength : 280
     */

    private int fileHead;
    private int q;
    /**
     * 320filesize : 11191042
     * sqfilesize : 33479599
     * sqhash : 09BE9E5C6515D5FC9BFE349A2FE5D218
     * 128hash : 2B616F6AB9F8655210FD823B900085CC
     * 320hash : 23F370FA1C067A6C51E091640648680C
     * 128filesize : 4485036
     */

    private ExtraBean extra;
    private int fileSize;
    private String hash;
    private String mvhash;
    private String req_hash;
    private String imgUrl;
    private String url;
    private int bitRate;
    private String songName;
    private String intro;
    private String topic_remark;
    private int singerId;
    private String choricSinger;
    private int status;
    private int stype;
    private int privilege;
    private String singerName;
    private int ctype;
    private String fileName;
    private String topic_url;
    private int errcode;
    private String singerHead;
    private String extName;
    private String error;
    private int timeLength;

    public int getFileHead() {
        return fileHead;
    }

    public void setFileHead(int fileHead) {
        this.fileHead = fileHead;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMvhash() {
        return mvhash;
    }

    public void setMvhash(String mvhash) {
        this.mvhash = mvhash;
    }

    public String getReq_hash() {
        return req_hash;
    }

    public void setReq_hash(String req_hash) {
        this.req_hash = req_hash;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTopic_remark() {
        return topic_remark;
    }

    public void setTopic_remark(String topic_remark) {
        this.topic_remark = topic_remark;
    }

    public int getSingerId() {
        return singerId;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public String getChoricSinger() {
        return choricSinger;
    }

    public void setChoricSinger(String choricSinger) {
        this.choricSinger = choricSinger;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getCtype() {
        return ctype;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTopic_url() {
        return topic_url;
    }

    public void setTopic_url(String topic_url) {
        this.topic_url = topic_url;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getSingerHead() {
        return singerHead;
    }

    public void setSingerHead(String singerHead) {
        this.singerHead = singerHead;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public static class ExtraBean {
        @SerializedName("320filesize")
        private int value320filesize;
        private int sqfilesize;
        private String sqhash;
        @SerializedName("128hash")
        private String value128hash;
        @SerializedName("320hash")
        private String value320hash;
        @SerializedName("128filesize")
        private int value128filesize;

        public int getValue320filesize() {
            return value320filesize;
        }

        public void setValue320filesize(int value320filesize) {
            this.value320filesize = value320filesize;
        }

        public int getSqfilesize() {
            return sqfilesize;
        }

        public void setSqfilesize(int sqfilesize) {
            this.sqfilesize = sqfilesize;
        }

        public String getSqhash() {
            return sqhash;
        }

        public void setSqhash(String sqhash) {
            this.sqhash = sqhash;
        }

        public String getValue128hash() {
            return value128hash;
        }

        public void setValue128hash(String value128hash) {
            this.value128hash = value128hash;
        }

        public String getValue320hash() {
            return value320hash;
        }

        public void setValue320hash(String value320hash) {
            this.value320hash = value320hash;
        }

        public int getValue128filesize() {
            return value128filesize;
        }

        public void setValue128filesize(int value128filesize) {
            this.value128filesize = value128filesize;
        }
    }
}
