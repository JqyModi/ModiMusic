package com.xfdream.music.activity;

import android.os.Bundle;

import com.xfdream.music.R;

public class AboutActivity extends SettingActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		setBackButton();
		setTopTitle(getResources().getString(R.string.about_title));
	}
}
