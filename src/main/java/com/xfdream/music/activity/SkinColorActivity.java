package com.xfdream.music.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xfdream.music.R;
import com.xfdream.music.entity.SkinMessage;
import com.xfdream.music.observable.ObserverManage;
import com.xfdream.music.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class SkinColorActivity extends Activity {
	private int colorPanelIndex = 0;

	/**
	 * 棰勮鍥捐儗鏅?
	 */
	private LinearLayout linearLayoutBG;

	/**
	 * 棰滆壊闈㈡澘甯冨眬
	 */
	private LinearLayout selectPanelLinearLayout;

	/**
	 * 棰滆壊闈㈡澘
	 */
	private ImageView imageviews[];
	/**
	 * 璁板綍褰撳墠闈㈡澘index瀵瑰簲鐨勯鑹瞚ndex
	 */
	private Map<Integer, Integer> colors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skincolor);
		init();
		//ActivityManager.getInstance().addActivity(this);
	}

	private void init() {
		colorPanelIndex = Constants.DEF_COLOR_INDEX;
		
		linearLayoutBG = (LinearLayout) findViewById(R.id.skin_previewbg);
		linearLayoutBG
				.setBackgroundColor(Constants.BLACK_GROUND[Constants.DEF_COLOR_INDEX]);

		selectPanelLinearLayout = (LinearLayout) findViewById(R.id.selectPanel);

		int length = Constants.BLACK_GROUND.length;
		imageviews = new ImageView[length];

		colors = new HashMap<Integer, Integer>();

		int index = 0;

		colors.put(index, Constants.DEF_COLOR_INDEX);

		View viewFirst = LayoutInflater.from(this).inflate(
				R.layout.skin_color_panel, null, false);
		imageviews[index] = (ImageView) viewFirst.findViewById(R.id.colorpanel);
		imageviews[index].setImageResource(R.drawable.lib_contact_fram);
		imageviews[index]
				.setBackgroundColor(Constants.BLACK_GROUND[Constants.DEF_COLOR_INDEX]);
		imageviews[index++]
				.setOnClickListener(new MyImageViewOnClickListener());
		selectPanelLinearLayout.addView(viewFirst);

		for (int i = 0; i < length; i++) {
			if (i == Constants.DEF_COLOR_INDEX)
				continue;

			colors.put(index, i);

			View view = LayoutInflater.from(this).inflate(
					R.layout.skin_color_panel, null, false);
			imageviews[index] = (ImageView) view.findViewById(R.id.colorpanel);
			imageviews[index].setBackgroundColor(Constants.BLACK_GROUND[i]);
			imageviews[index++]
					.setOnClickListener(new MyImageViewOnClickListener());

			selectPanelLinearLayout.addView(view);
		}
	}

	public void back(View v) {
		back();
	}

	private void back() {
		finish();
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			back();
		}
		return false;
	}

	private class MyImageViewOnClickListener implements OnClickListener {

		public void onClick(View arg0) {
			int length = Constants.BLACK_GROUND.length;
			for (int i = 0; i < length; i++) {
				if (imageviews[i].isPressed()) {
					imageviews[i].setImageResource(R.drawable.lib_contact_fram);
					int colorIndex = colors.get(i);
					colorPanelIndex = colorIndex;
					linearLayoutBG
							.setBackgroundColor(Constants.BLACK_GROUND[colorIndex]);
				} else {
					imageviews[i].setImageDrawable(new BitmapDrawable());
				}
			}
		}
	}

	public void colorConfirm(View v) {

		Constants.DEF_COLOR_INDEX = colorPanelIndex;
		//modi
		//DataUtil.save(this, Constants.DEF_COLOR_INDEX_KEY, colorPanelIndex);
		SkinMessage msg = new SkinMessage();
		msg.type = SkinMessage.COLOR;
		ObserverManage.getObserver().setMessage(msg);

	}
}
