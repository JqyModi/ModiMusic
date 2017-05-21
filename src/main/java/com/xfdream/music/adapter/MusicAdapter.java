package com.xfdream.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xfdream.music.R;
import com.xfdream.music.entity.Song;
import com.xfdream.music.util.ToTime;

import java.util.List;

public class MusicAdapter extends BaseAdapter {
	ToTime time;
	private List<Song> listMusic;
	private Context context;

	public MusicAdapter(Context context, List<Song> listMusic) {
		this.context = context;
		this.listMusic = listMusic;
	}

	public void setListItem(List<Song> listMusic) {
		this.listMusic = listMusic;
	}

	@Override
	public int getCount() {
		return listMusic.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listMusic.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		time = new ToTime();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.music_item, null);
		}
		Song m = listMusic.get(position);
		// 音乐名
		TextView textMusicName = (TextView) convertView
				.findViewById(R.id.music_item_name);
		textMusicName.setText(m.getName());
/*
		textMusicName.setText(m.getName().subSequence(0,
				m.getName().length() - 4));
*/

		// 歌手
		TextView textMusicSinger = (TextView) convertView
				.findViewById(R.id.music_item_singer);
		textMusicSinger.setText(m.getArtist().getName());
		// 持续时间
		TextView textMusicTime = (TextView) convertView
				.findViewById(R.id.music_item_time);
		textMusicTime.setText(time.toTime((int) m.getDurationTime()));
		return convertView;
	}

	public Song getSongByIndex(int position){
		Song s = listMusic.get(position);
		return s;
	}
}
