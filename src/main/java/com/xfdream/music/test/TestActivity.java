package com.xfdream.music.test;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.xfdream.music.R;
import com.xfdream.music.dao.DBData;
import com.xfdream.music.dao.DBHpler;

public class TestActivity extends Activity {
	private TextView tv_test_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testactivity);
	
		tv_test_content=(TextView)this.findViewById(R.id.tv_test_content);
		
		DBHpler dbHpler = new DBHpler(this);
		SQLiteDatabase db=dbHpler.getReadableDatabase();
		
		tv_test_content.append("================song====================");
		
		Cursor cr=db.rawQuery("select * from "+DBData.SONG_TABLENAME, null);
		while(cr.moveToNext()){
			tv_test_content.append("[");
			tv_test_content.append("id:"+cr.getInt(cr.getColumnIndex(DBData.SONG_ID))+",");
			tv_test_content.append("artistid:"+cr.getInt(cr.getColumnIndex(DBData.SONG_ARTISTID))+",");
			tv_test_content.append("albumid:"+cr.getInt(cr.getColumnIndex(DBData.SONG_ALBUMID)));
			tv_test_content.append("]");
		}
		cr.close();
		
		tv_test_content.append("================album====================");

		Cursor cr2=db.rawQuery("select * from "+DBData.ALBUM_TABLENAME, null);
		while(cr2.moveToNext()){
			tv_test_content.append("[");
			tv_test_content.append("id:"+cr2.getInt(cr2.getColumnIndex(DBData.ALBUM_ID))+",");
			tv_test_content.append("name:"+cr2.getString(cr2.getColumnIndex(DBData.ALBUM_NAME))+",");
			tv_test_content.append("]");
		}
		cr2.close();
		
		tv_test_content.append("================artist====================");
		
		Cursor cr3=db.rawQuery("select * from "+DBData.ARTIST_TABLENAME, null);
		while(cr3.moveToNext()){
			tv_test_content.append("[");
			tv_test_content.append("id:"+cr3.getInt(cr3.getColumnIndex(DBData.ARTIST_ID))+",");
			tv_test_content.append("name:"+cr3.getString(cr3.getColumnIndex(DBData.ARTIST_NAME))+",");
			tv_test_content.append("]");
		}
		
		cr3.close();
		db.close();
	}
	
	
}
