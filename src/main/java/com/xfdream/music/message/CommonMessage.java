package com.xfdream.music.message;

/**
 * Created by Modi on 2016/9/2.
 */

public class CommonMessage {
    private String msg = null;
    private Object obj = null;
    public CommonMessage(String msg, Object obj){
        this.msg = msg;
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
