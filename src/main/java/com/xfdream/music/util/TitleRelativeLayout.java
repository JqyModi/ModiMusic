package com.xfdream.music.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.xfdream.music.entity.SkinMessage;
import com.xfdream.music.observable.ObserverManage;

import java.util.Observable;
import java.util.Observer;

public class TitleRelativeLayout extends RelativeLayout implements Observer {

	public TitleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TitleRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TitleRelativeLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 鍒濆鍖?
	 * 
	 * @param context
	 */
	private void init(Context context) {
		ObserverManage.getObserver().addObserver(this);
		setBackgroundColor(Constants.BLACK_GROUND[Constants.DEF_COLOR_INDEX]);
	}

	@Override
	public void update(Observable arg0, Object data) {
		if (data instanceof SkinMessage) {
			SkinMessage msg = (SkinMessage) data;
			if (msg.type == SkinMessage.COLOR) {
				setBackgroundColor(Constants.BLACK_GROUND[Constants.DEF_COLOR_INDEX]);
			}
		}
	}
}
