package com.xfdream.music.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xfdream.music.R;
import com.xfdream.music.adapter.MusicAdapter;
import com.xfdream.music.dao.SongDao;
import com.xfdream.music.data.MusicNum;
import com.xfdream.music.entity.Album;
import com.xfdream.music.entity.Artist;
import com.xfdream.music.entity.Song;
import com.xfdream.music.service.MediaPlayerManager;
import com.xfdream.music.util.Common;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
	private static EditText edit_search;
	private ListView lv;
	private TextView resultsearch;
	static int[] musicnum = new int[3000];
	private Close close;
	String musicname;
	List<Song> list = new ArrayList<Song>();
	List<Song> newlist = new ArrayList<Song>();

	private MediaPlayerManager mediaPlayerManager;
	private SongDao mSongDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sousuo);

		//modi
		mediaPlayerManager=new MediaPlayerManager(this);
		mediaPlayerManager.setConnectionListener(mConnectionListener);
		mediaPlayerManager.startAndBindService();

		mSongDao = new SongDao(this);

		edit_search = (EditText) this.findViewById(R.id.edit_search);
		edit_search.addTextChangedListener(new TextWatcher_Enum());
		resultsearch = (TextView) this.findViewById(R.id.resultsearch);
		lv = (ListView) this.findViewById(R.id.musiclistevery);
		/*close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);*/
		//List<Song> listMusic = MusicList.getMusicData(getApplicationContext());
		List<Song> listMusic = mSongDao.searchAll();
		final MusicAdapter adapter = new MusicAdapter(SearchActivity.this, listMusic);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				if (edit_search.getText().toString().equals("")) {

					MusicNum.putplay(1);
					MusicNum.put_id(position);
					// / Intent intent = new
					// Intent(Search.this,MusicActivity.class);
					// startActivity(intent);

					Song s = adapter.getSongByIndex(position);
					int id = mSongDao.searchSongIdByName(s.getName());
					mediaPlayerManager.player(id, MediaPlayerManager.PLAYERFLAG_ALL, null);

					finish();

					/*Intent intent1 = new Intent(SearchActivity.this, MusicService.class);
					// intent1.putExtra("play",8);
					MusicNum.putplay(8);
					MusicNum.putisok(true);
					intent1.putExtra("_id", arg2);
					startService(intent1);*/

				} else {
					MusicNum.putplay(1);
					MusicNum.put_id(musicnum[position]);
					// Intent intent = new
					// Intent(Search.this,MusicActivity.class);
					// startActivity(intent);

					//Song s = adapter.getSongByIndex(position);
					TextView name = (TextView) v.findViewById(R.id.music_item_name);
					String nameStr = name.getText().toString();
					int id = mSongDao.searchSongIdByName(nameStr);
					mediaPlayerManager.player(id, MediaPlayerManager.PLAYERFLAG_ALL, null);

					finish();

					/*Intent intent1 = new Intent(SearchActivity.this, MusicService.class);
					// intent1.putExtra("play",8);
					MusicNum.putplay(8);
					MusicNum.putisok(true);
					intent1.putExtra("_id", musicnum[arg2]);
					startService(intent1);*/

				}
			}
		});
	}

	public static class MusicList2 {

		public static List<Song> getMusicData(Context context) {
			List<Song> musicList = new ArrayList<Song>();
			ContentResolver cr = context.getContentResolver();
			if (cr != null) {
				// 获取所有歌曲
				Cursor cursor = cr.query(
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
						null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
				if (null == cursor) {
					return null;
				}
				if (cursor.moveToFirst()) {
					int id = -1;
					int i = 0;
					do {
						// Log.i("id", String.valueOf(id));
						Song m = new Song();
						String title = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.TITLE));

						String singer = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.ARTIST));
						if ("<unknown>".equals(singer)) {
							singer = "未知艺术家";
						}
						String album = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.ALBUM));
						long size = cursor.getLong(cursor
								.getColumnIndex(MediaStore.Audio.Media.SIZE));
						long time = cursor
								.getLong(cursor
										.getColumnIndex(MediaStore.Audio.Media.DURATION));
						String url = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.DATA));
						String name = cursor
								.getString(cursor
										.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
						String sbr = name.substring(name.length() - 3,
								name.length());
						if (sbr.equals("mp3") && (time>=1000 && time <=900000)) {
							//m.setTitle(title);
							Artist lArtist = new Artist();
							lArtist.setName(singer);
							m.setArtist(lArtist);
							Album lAlbum = new Album();
							lAlbum.setName(album);
							m.setAlbum(lAlbum);
							m.setSize((int) size);
							m.setDurationTime((int) time);
							m.setFilePath(url);
							//m.setUrl(url);
							m.setName(title);
							id++;

							if (title
									.contains(edit_search.getText().toString())) {
								musicList.add(m);
								musicnum[i] = id;
								i++;
							}

						}
					} while (cursor.moveToNext());
					if (cursor != null) {
						cursor.close();
					}
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			return musicList;
		}

	}

	class TextWatcher_Enum implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			if (edit_search.getText().toString().equals("")) {

				/*List<Song> listMusic = MusicList
						.getMusicData(getApplicationContext());*/
				List<Song> listMusic = mSongDao.searchAll();
				MusicAdapter adapter = new MusicAdapter(SearchActivity.this, listMusic);
				lv.setAdapter(adapter);
				resultsearch.setText("");

			} else {
				List<Song> listMusic = MusicList2
						.getMusicData(getApplicationContext());
				//List<Song> listMusic = mSongDao.searchAll();
				MusicAdapter adapter2 = new MusicAdapter(SearchActivity.this, listMusic);
				lv.setAdapter(adapter2);
				resultsearch
						.setText(String.valueOf(listMusic.size()) + "条搜索结果");
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

		}

	}

	private MediaPlayerManager.ServiceConnectionListener mConnectionListener=new MediaPlayerManager.ServiceConnectionListener() {
		@Override
		public void onServiceDisconnected() {
		}
		@Override
		public void onServiceConnected() {
			mediaPlayerManager.initPlayerMain_SongInfo();
		}
	};

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}