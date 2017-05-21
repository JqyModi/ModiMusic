package com.xfdream.music.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.xfdream.music.R;


public class LoadRelativeLayout extends RelativeLayout {

	/**
	 * 鍔犺浇瀹屾垚鍚庢樉绀虹殑椤甸潰
	 */
	private View contentView;
	/**
	 * 姝ｅ湪鍔犺浇椤甸潰
	 */
	private View loadingView;
	/**
	 * 鏃嬭浆鍔ㄧ敾
	 */
	private Animation rotateAnimation;
	private LoadingImageView loadingImageView;

	public LoadRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LoadRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoadRelativeLayout(Context context) {
		super(context);
	}

	/**
	 * 鍒濆鍖?
	 * 
	 * @param context
	 */
	public void init(Context context) {
		if (getChildCount() == 0) {
			return;
		}
		contentView = getChildAt(0);
		contentView.setVisibility(View.INVISIBLE);

		LayoutInflater inflater = LayoutInflater.from(context);
		LayoutParams params = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		loadingView = inflater.inflate(R.layout.view_loading, null, false);
		loadingImageView = (LoadingImageView) loadingView
				.findViewById(R.id.loadingImageView);
		loadingView.setVisibility(View.INVISIBLE);
		loadingView.setLayoutParams(params);
		rotateAnimation = AnimationUtils.loadAnimation(context,
				R.anim.anim_rotate);
		rotateAnimation.setInterpolator(new LinearInterpolator());// 鍖€閫?
		addView(loadingView);
	}

	/**
	 * 鏄剧ず姝ｅ湪鍔犺浇椤甸潰
	 */
	public void showLoadingView() {
		if (contentView == null)
			return;
		loadingImageView.clearAnimation();
		loadingImageView.startAnimation(rotateAnimation);
		contentView.setVisibility(View.INVISIBLE);
		loadingView.setVisibility(View.VISIBLE);
	}

	/**
	 * 鏄剧ず鍔犺浇鎴愬姛椤甸潰
	 */
	public void showSuccessView() {
		if (contentView == null)
			return;
		//鍋滄鍔ㄧ敾
		loadingImageView.clearAnimation();
		contentView.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.INVISIBLE);
	}

}
