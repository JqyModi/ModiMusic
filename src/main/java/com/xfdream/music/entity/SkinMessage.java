package com.xfdream.music.entity;

/**
 * 鐨偆鐩稿叧鏁版嵁淇℃伅
 * 
 * @author Administrator
 * 
 */
public class SkinMessage {
	/**
	 * 棰滆壊
	 */
	public final static int COLOR = 0;
	/**
	 * 鐨偆鍥剧墖
	 */
	public final static int PIC = 1;
	/**
	 * 姝屾墜鍥剧墖
	 */
	public final static int ART = 2;

	private String url;
	private String path;
	private String parentPath;
	public int type;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

}
