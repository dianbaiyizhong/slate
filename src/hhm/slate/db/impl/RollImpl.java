package hhm.slate.db.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import hhm.slate.db.DatabaseHelper;
import hhm.slate.db.IDBControl;
import hhm.slate.db.entity.Roll;

public class RollImpl implements IDBControl {

	private SQLiteDatabase db;

	public RollImpl(Context context) {
		DatabaseHelper database = new DatabaseHelper(context);
		this.db = database.getReadableDatabase();
	}

	public void save(Object obj) {

		Roll roll = new Roll();
		roll = (Roll) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("roll_name", roll.getRoll_name());

		// 调用insert()方法插入数据
		db.insert("roll", null, cValue);

	}

	public void update(Object obj) {
		// TODO Auto-generated method stub

	}

	public void delete(int id) {

		// 删除条件
		String whereClause = "roll_id=?";
		// 删除条件参数
		String[] whereArgs = { String.valueOf(id) };
		// 执行删除
		db.delete("roll", whereClause, whereArgs);

	}

	public List query() {

		List<Roll> list = new ArrayList<Roll>();

		Cursor cursor = db.rawQuery("select * from roll", null);

		while (cursor.moveToNext()) {
			Roll roll = new Roll();

			roll.setRoll_id(cursor.getInt(cursor.getColumnIndex("roll_id")));
			roll.setRoll_name(cursor.getString(cursor
					.getColumnIndex("roll_name")));

			list.add(roll);
		}
		cursor.close();

		return list;

	}

	public List query(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
