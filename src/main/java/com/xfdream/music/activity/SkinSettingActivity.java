package com.xfdream.music.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.xfdream.music.R;
import com.xfdream.music.adapter.ImageAdapter;
import com.xfdream.music.data.SystemSetting;
import com.xfdream.music.message.CommonMessage;
import com.xfdream.music.util.Constants;
import com.xfdream.music.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import static com.xfdream.music.util.Constants.BG_CURRENT_INDEX;

public class SkinSettingActivity extends SettingActivity {
	private GridView gv_skin;
	private ImageAdapter adapter;
	private SystemSetting mSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skinsetting);

		//setImmersive(true);

		setFullScreen(true);
		
		resultCode=2;
		setBackButton();
		setTopTitle(getResources().getString(R.string.skinsetting_title));
		
		mSetting=new SystemSetting(this, true);
		
		adapter=new ImageAdapter(this,mSetting.getCurrentSkinId());
		gv_skin=(GridView)this.findViewById(R.id.gv_skin);
		gv_skin.setAdapter(adapter);
		gv_skin.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//GridView
				adapter.setCurrentId(position);
				//设置选中的图片作为当前背景
				SkinSettingActivity.this.getWindow().setBackgroundDrawableResource(SystemSetting.SKIN_RESOURCES[position]);
				//设置当前背景下标
				BG_CURRENT_INDEX = position;
				LogUtil.e("BG_CURRENT_INDEX = "+BG_CURRENT_INDEX);
				mSetting.setCurrentSkinResId(position);
				//modi  发送切换背景广播
				EventBus.getDefault().post(new CommonMessage(Constants.MSG_SEND_CHANGE_BG,BG_CURRENT_INDEX));
			}
		});
	}
}
