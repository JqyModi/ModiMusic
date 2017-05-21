package com.xfdream.music.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import com.xfdream.music.R;
import com.xfdream.music.entity.SkinMessage;
import com.xfdream.music.observable.ObserverManage;
import com.xfdream.music.util.Constants;
import com.xfdream.music.util.ImageUtil;

import java.io.InputStream;

public class GridViewAdapter extends BaseAdapter {
	//private MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
	private Context context;
	private int picIDS[] = Constants.PICIDS;
	private int picIndex = Constants.DEF_PIC_INDEX;
	private GridView gridView;

	public GridViewAdapter(Context context, GridView gridView) {
		this.context = context;
		this.gridView = gridView;
	}

	@Override
	public int getCount() {
		return picIDS.length;
	}

	@Override
	public Object getItem(int position) {
		return picIDS[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_theme_pic, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageView picImageView = holder.getpicImageView();

		ImageUtil.loadResourceImage(context, picImageView, picIDS[position],
				R.drawable.ic_playlist_recommend_icon_default);

		final ImageView selectImageView = holder.getselectImageView();
		if (position != Constants.DEF_PIC_INDEX) {
			selectImageView.setVisibility(View.INVISIBLE);
		} else {
			selectImageView.setVisibility(View.VISIBLE);
		}
		holder.getpicImageView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (position != Constants.DEF_PIC_INDEX) {
					selectImageView.setVisibility(View.VISIBLE);
					reshPICStatusUI(picIndex);
					picIndex = position;
					Constants.DEF_PIC_INDEX = picIndex;
					//modi
					/*DataUtil.save(context, Constants.DEF_PIC_INDEX_KEY,
							Constants.DEF_PIC_INDEX);*/
					SkinMessage msg = new SkinMessage();
					msg.type = SkinMessage.PIC;
					ObserverManage.getObserver().setMessage(msg);
				}
			}
		});

		return convertView;
	}

	protected void reshPICStatusUI(int wantedPosition) {
		int firstPosition = gridView.getFirstVisiblePosition();
		int wantedChild = wantedPosition - firstPosition;
		View view = gridView.getChildAt(wantedChild);
		if (wantedChild < 0 || wantedChild >= gridView.getChildCount()) {
			return;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null)
			return;
		holder.getselectImageView().setVisibility(View.INVISIBLE);
	}

	private class ViewHolder {
		private View view;

		private ImageView picImageView;

		private ImageView selectImageView;

		ViewHolder(View v) {
			this.view = v;
		}

		ImageView getpicImageView() {
			if (picImageView == null) {
				picImageView = (ImageView) view.findViewById(R.id.color_pic);
			}
			return picImageView;
		}

		ImageView getselectImageView() {
			if (selectImageView == null) {
				selectImageView = (ImageView) view
						.findViewById(R.id.secect_stats);
			}
			return selectImageView;
		}
	}

	public Bitmap readBitmap(Context context, int id) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 琛ㄧず16浣嶄綅鍥?565浠ｈ〃瀵瑰簲涓夊師鑹插崰鐨勪綅鏁?
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 璁剧疆鍥剧墖鍙互琚洖鏀?
		InputStream is = context.getResources().openRawResource(id);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, opt);
		} catch (Exception e) {
			e.printStackTrace();
			//logger.e(e.toString());
		}
		return bitmap;
	}
}
