package com.xfdream.music.activity;

import android.os.Bundle;

import com.xfdream.music.R;

public class SystemSettingActivity extends SettingActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemsetting);
		
		setBackButton();
		setTopTitle(getResources().getString(R.string.systemsetting_title));
	}
}
