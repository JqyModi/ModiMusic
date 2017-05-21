package com.xfdream.music.util;

/**
 * Created by Modi on 2016/12/4.
 * 邮箱：1294432350@qq.com
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;


import com.xfdream.music.lrc.HttpCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FileUtils {

    public static File createFile(Context context,String fileName){


        File file=null;
        String state = Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED)){

            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+fileName+".lrc");
        }else {
            file = new File(context.getCacheDir().getAbsolutePath()+"/"+fileName+".lrc");
        }

        Log.d("vivi","file "+file.getAbsolutePath());

        return file;

    }

    public static void writeFile2Disk(Response<ResponseBody> response, File file, HttpCallBack httpCallBack){
        long currentLength = 0;
        OutputStream os =null;
        InputStream is = response.body().byteStream();
        long totalLength =response.body().contentLength();
        try {
            os = new FileOutputStream(file);
            int len ;
            byte [] buff = new byte[1024];
            while((len=is.read(buff))!=-1){
                os.write(buff,0,len);
                currentLength+=len;
                LogUtil.e("当前进度:"+currentLength);
                httpCallBack.onLoading(currentLength,totalLength);
            }
            // httpCallBack.onLoading(currentLength,totalLength,true);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(os!=null){
                try {
                    os.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
