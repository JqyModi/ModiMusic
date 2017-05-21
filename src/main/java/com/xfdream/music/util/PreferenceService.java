package com.xfdream.music.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

public class PreferenceService {
	private Context context;

	public PreferenceService(Context context) {
		this.context = context;
	}

	public void save(String Name, Integer currentId) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("Name", Name);
		editor.putInt("currentId", currentId);
		editor.commit();
	}

	public Map<String, String> getPreferences() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params.put("Name", preferences.getString("Name", ""));
		params.put("currentId",
				String.valueOf(preferences.getInt("currentId", 0)));
		return params;
	}

	public void save2(String mingzi, Integer seekvalue) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("mingzi", mingzi);
		editor.putInt("seekvalue", seekvalue);
		editor.commit();
	}

	public Map<String, String> getPreferences2() {
		Map<String, String> params2 = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params2.put("mingzi", preferences.getString("mingzi", ""));
		params2.put("seekvalue",
				String.valueOf(preferences.getInt("seekvalue", 5)));
		return params2;
	}

	public void save3(String time, Integer progress) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("time", time);
		editor.putInt("progress", progress);
		editor.commit();
	}

	public Map<String, String> getPreferences3() {
		Map<String, String> params3 = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params3.put("time", preferences.getString("time", "00:00"));
		params3.put("progress",
				String.valueOf(preferences.getInt("progress", 0)));
		return params3;
	}

	public void savename(String musicna, Integer musicid) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("musicna", musicna);
		editor.putInt("musicid", musicid);
		editor.commit();

	}

	public Map<String, String> getPreferences4() {
		Map<String, String> params4 = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params4.put("musicna", preferences.getString("musicna", "????sytle"));
		params4.put("musicid", String.valueOf(preferences.getInt("musicid", 0)));
		return params4;
	}

	// 0
	public void band0(String band0, Integer progress0) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("band0", band0);
		editor.putInt("progress0", progress0);
		editor.commit();

	}

	public Map<String, String> getPreferences00() {
		Map<String, String> params00 = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params00.put("band0", preferences.getString("band0", "0"));
		params00.put("progress0",
				String.valueOf(preferences.getInt("progress0", 2)));
		return params00;
	}

	// 1
	public void band1(String band1, Integer progress1) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("band1", band1);
		editor.putInt("progress1", progress1);
		editor.commit();

	}

	public Map<String, String> getPreferences01() {
		Map<String, String> params11 = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params11.put("band1", preferences.getString("band1", "1"));
		params11.put("progress1",
				String.valueOf(preferences.getInt("progress1", 2)));
		return params11;
	}

	// 2
	public void band2(String band2, Integer progress2) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("band2", band2);
		editor.putInt("progress2", progress2);
		editor.commit();

	}

	public Map<String, String> getPreferences02() {
		Map<String, String> params22 = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params22.put("band2", preferences.getString("band2", "2"));
		params22.put("progress2",
				String.valueOf(preferences.getInt("progress2", 2)));
		return params22;
	}

	// 3
	public void band3(String band3, Integer progress3) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("band3", band3);
		editor.putInt("progress3", progress3);
		editor.commit();

	}

	public Map<String, String> getPreferences03() {
		Map<String, String> params33 = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params33.put("band3", preferences.getString("band3", "3"));
		params33.put("progress3",
				String.valueOf(preferences.getInt("progress3", 2)));
		return params33;
	}

	// 4
	public void band4(String band4, Integer progress4) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("band4", band4);
		editor.putInt("progress4", progress4);
		editor.commit();

	}

	public Map<String, String> getPreferences04() {
		Map<String, String> params44 = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		params44.put("band4", preferences.getString("band4", "4"));
		params44.put("progress4",
				String.valueOf(preferences.getInt("progress4", 2)));
		return params44;
	}

	public void savesinger(String string, Integer singernum) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("singer", "singer");
		editor.putInt("singernum", singernum);
		editor.commit();

	}

	public Map<String, String> getPreferencessinger() {
		Map<String, String> paramss = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		paramss.put("singer", preferences.getString("singer", "singer"));
		paramss.put("singernum",
				String.valueOf(preferences.getInt("singernum", 12)));
		return paramss;
	}

	public void savealbum(String string, Integer albumnum) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("album", "album");
		editor.putInt("albumnum", albumnum);
		editor.commit();

	}

	public Map<String, String> getPreferencesalbum() {
		Map<String, String> paramsa = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		paramsa.put("album", preferences.getString("album", "album"));
		paramsa.put("albumnum",
				String.valueOf(preferences.getInt("albumnum", 12)));
		return paramsa;
	}

	public void background(String uri, int arg1) {
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("background", uri);
		editor.putInt("ui", 0);
		editor.commit();

	}

	public Map<String, String> getPreferencesback() {
		Map<String, String> paramsui = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("music",
				Context.MODE_PRIVATE);
		paramsui.put("background", preferences.getString("background", ""));
		paramsui.put("ui", String.valueOf(preferences.getInt("ui", 0)));
		return paramsui;
	}
}