/**
 * Copyright (c) www.longdw.com
 */
package com.xfdream.music.entity;

/**
 * 姝岃瘝鍙ュ瓙锛屾槸涓€涓椂闂存埑鍜屼竴琛屾瓕璇嶇粍鎴愶紝濡傗€淸00.03.21.56]杩樿寰楄澶氬勾鍓嶇殑鏄ュぉ鈥?
 * */
public class LyricSentence {

	/** 姝岃鏂囨湰鐨勫紑濮嬫椂闂存埑杞崲涓烘绉掓暟鐨勫€硷紝濡俒00.01.02.34]涓?2340姣 */
	private long startTime = 0;

	/**涓€鍙ユ瓕璇嶇殑瀹炵幇*/
	private long duringTime = 0;

	/** 姣忎釜鏃堕棿鎴冲搴旂殑涓€琛屾瓕璇嶆枃鏈?濡傗€淸00.03.21.56]杩樿寰楄澶氬勾鍓嶇殑鏄ュぉ鈥濅腑鐨勨€滆繕璁板緱璁稿骞村墠鐨勬槬澶┾€?*/
	private String contentText = "";

	public LyricSentence(long time, String text) {
		startTime = time;
		contentText = text;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public long getDuringTime() {
		return duringTime;
	}

	public void setDuringTime(long duringTime) {
		this.duringTime = duringTime;
	}
}
