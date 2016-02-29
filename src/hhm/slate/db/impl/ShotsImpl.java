package hhm.slate.db.impl;

import hhm.slate.db.DatabaseHelper;
import hhm.slate.db.IDBControl;
import hhm.slate.db.entity.Shots;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ShotsImpl implements IDBControl {

	private SQLiteDatabase db;

	public ShotsImpl(Context context) {
		DatabaseHelper database = new DatabaseHelper(context);
		this.db = database.getReadableDatabase();
	}

	public void save(Object obj) {

		Shots shots = new Shots();
		shots = (Shots) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("shots_name", shots.getShots_name());

		// 调用insert()方法插入数据
		db.insert("shots", null, cValue);

	}

	public void update(Object obj) {
		// TODO Auto-generated method stub

	}

	public void delete(int id) {

		// 删除条件
		String whereClause = "shots_id=?";
		// 删除条件参数
		String[] whereArgs = { String.valueOf(id) };
		// 执行删除
		db.delete("shots", whereClause, whereArgs);

	}

	public List query(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List query() {

		List<Shots> list = new ArrayList<Shots>();

		Cursor cursor = db.rawQuery("select * from shots", null);

		while (cursor.moveToNext()) {

			Shots shots = new Shots();
			shots.setShots_id(cursor.getInt(cursor.getColumnIndex("shots_id")));
			shots.setShots_name(cursor.getString(cursor
					.getColumnIndex("shots_name")));

			list.add(shots);
		}
		cursor.close();

		return list;

	}

}
