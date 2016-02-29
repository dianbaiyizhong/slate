package hhm.slate.db.impl;

import hhm.slate.activity.storyboard.view.util.PosRecorder;
import hhm.slate.db.DatabaseHelper;
import hhm.slate.db.IDBControl;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Take;
import hhm.slate.util.TimeUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TakeImpl implements IDBControl {
	private SQLiteDatabase db;

	public TakeImpl(Context context) {
		DatabaseHelper database = new DatabaseHelper(context);
		this.db = database.getReadableDatabase();
	}

	public void save(Object obj) {

		Take take = (Take) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("take_number", take.getTake_number());

		cValue.put("take_time", new TimeUtil().getTime());

		cValue.put("shot_id", take.getShot_id());
		cValue.put("is_available", take.getIs_available());
		cValue.put("not_avaliable_season", take.getNot_avaliable_season());
		cValue.put("take_image", take.getTake_image());
		cValue.put("roll_name", take.getRoll_name());

		cValue.put("video_number", take.getVideo_number());
		cValue.put("audio_number", take.getAudio_number());

		// 调用insert()方法插入数据

		db.insert("take", null, cValue);
		Cursor cursor = db.rawQuery("select last_insert_rowid() from take",
				null);
		int new_id = 0;
		if (cursor.moveToFirst()) {
			new_id = cursor.getInt(0);
		}

		PosRecorder.setTake_pos_id(new_id);

		cursor.close();
		db.close();
	}

	public void update(Object obj) {

		Take take = new Take();
		take = (Take) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("audio_number", take.getAudio_number());
		cValue.put("video_number", take.getVideo_number());
		cValue.put("is_available", take.getIs_available());
		cValue.put("not_avaliable_season", take.getNot_avaliable_season());
		cValue.put("Roll_name", take.getRoll_name());
		cValue.put("is_available", take.getIs_available());

		// 实例化内容值 ContentValues values = new ContentValues();
		// 在values中添加内容
		// 修改条件
		String whereClause = "take_id=?";
		// 修改添加参数
		String[] whereArgs = { String.valueOf(take.getTake_id()) };
		// 修改
		db.update("take", cValue, whereClause, whereArgs);

	}

	public List query() {
		// TODO Auto-generated method stub
		return null;
	}

	public List query(String id) {

		List<Take> list = new ArrayList<Take>();

		Cursor cursor = db.rawQuery("select * from take where shot_id = " + id,
				null);

		while (cursor.moveToNext()) {

			Take take = new Take();
			take.setIs_available(cursor.getInt(cursor
					.getColumnIndex("is_available")));
			take.setNot_avaliable_season(cursor.getString(cursor
					.getColumnIndex("not_avaliable_season")));

			take.setTake_id(cursor.getInt(cursor.getColumnIndex("take_id")));
			take.setTake_number(cursor.getInt(cursor
					.getColumnIndex("take_number")));
			take.setTake_time(Timestamp.valueOf(cursor.getString(cursor
					.getColumnIndex("take_time"))));
			take.setTake_image(cursor.getString(cursor
					.getColumnIndex("take_image")));
			take.setAudio_number(cursor.getString(cursor
					.getColumnIndex("audio_number")));
			take.setVideo_number(cursor.getString(cursor
					.getColumnIndex("video_number")));
			take.setRoll_name(cursor.getString(cursor
					.getColumnIndex("roll_name")));
			list.add(take);
		}
		cursor.close();

		return list;

	}

	public int queryCountById(int id) {

		String sql = "select count(*) as count  from take   where  take.[shot_id] ="
				+ id;
		Cursor cursor = db.rawQuery(sql, null);

		int count = 0;
		while (cursor.moveToNext()) {

			count = cursor.getInt(cursor.getColumnIndex("count"));
		}
		cursor.close();

		return count;

	}

	public void delete(int id) {

		// 删除条件
		String whereClause = "take_id=?";
		// 删除条件参数
		String[] whereArgs = { String.valueOf(id) };
		// 执行删除
		db.delete("take", whereClause, whereArgs);

	}
}
