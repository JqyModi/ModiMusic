package com.xfdream.music.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.xfdream.music.R;
import com.xfdream.music.adapter.GridViewAdapter;
import com.xfdream.music.async.AsyncTaskHandler;
import com.xfdream.music.data.SystemSetting;
import com.xfdream.music.util.Constants;
import com.xfdream.music.util.ImageUtil;
import com.xfdream.music.util.LoadRelativeLayout;
import com.xfdream.music.util.LogUtil;

import static com.xfdream.music.util.Constants.BG_CURRENT_INDEX;


public class SkinPicActivity extends Activity {

	private ImageButton deleteSkinImageButton;

	private LoadRelativeLayout loadRelativeLayout;

	private GridView gridView;

	private SystemSetting mSetting;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loadRelativeLayout.showLoadingView();
				break;
			case 1:
				loadRelativeLayout.showSuccessView();
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skinpic);
		init();
		loadData();
		//ActivityManager.getInstance().addActivity(this);
	}

	private void init() {
		deleteSkinImageButton = (ImageButton) findViewById(R.id.delete_skin);
		deleteSkinImageButton.setVisibility(View.INVISIBLE);

		gridView = (GridView) findViewById(R.id.grid);

		mSetting=new SystemSetting(this, true);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SkinPicActivity.this.getWindow().setBackgroundDrawableResource(Constants.PICIDS[position]);
				BG_CURRENT_INDEX = position;
				LogUtil.e("BG_CURRENT_INDEX = "+BG_CURRENT_INDEX);
				mSetting.setCurrentSkinResId(position);
			}
		});

		loadRelativeLayout = (LoadRelativeLayout) findViewById(R.id.loadRelativeLayout);

		loadRelativeLayout.init(this);
	}

	private void loadData() {
		new AsyncTaskHandler() {

			@Override
			protected void onPostExecute(Object result) {
				GridViewAdapter adapter = new GridViewAdapter(
						SkinPicActivity.this, gridView);
				gridView.setAdapter(adapter);

				handler.sendEmptyMessage(1);
			}

			@Override
			protected Object doInBackground() throws Exception {
				handler.sendEmptyMessage(0);
				loadPICData();
				return null;
			}
		}.execute();
	}

	private void loadPICData() {
		for (int i = 0; i < Constants.PICIDS.length; i++) {
			//modi---寰呭畬鍠?
			ImageUtil.readBitmap(this, Constants.PICIDS[i]);
		}
	}

	public void back(View v) {
		finish();
	}

	public void deleteSkin(View v) {
	}

	public void goColorDialog(View v) {
		Intent intent = new Intent(this, SkinColorActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}
}
