package com.xfdream.music.util;

import android.annotation.SuppressLint;

public class ToTime {
	@SuppressLint("DefaultLocale")
	public String toTime(int time) {
		time /= 1000;		
		int minute = time / 60;
		minute %= 60;
		int second = time % 60;
		@SuppressWarnings("unused")
		int hour = minute / 60;		
		return String.format("%02d:%02d", minute, second);
	}
}
