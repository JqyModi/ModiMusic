package com.xfdream.music.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	public static void show(Context context,String content){
		
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	public static void show(Context context,String content,int showTime){
		
		Toast.makeText(context, content, showTime).show();
	}
}
