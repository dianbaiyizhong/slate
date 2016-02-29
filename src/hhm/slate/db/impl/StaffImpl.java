package hhm.slate.db.impl;

import hhm.slate.db.DatabaseHelper;
import hhm.slate.db.IDBControl;
import hhm.slate.db.entity.Staff;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StaffImpl implements IDBControl {
	private SQLiteDatabase db;

	public StaffImpl(Context context) {
		DatabaseHelper database = new DatabaseHelper(context);
		this.db = database.getReadableDatabase();
	}

	@Override
	public void save(Object obj) {

	}

	@Override
	public void update(Object obj) {

	}

	@Override
	public void delete(int id) {

	}

	@Override
	public List query() {
		return null;
	}

	@Override
	public List query(String id) {
		return null;
	}

	public Staff queryById(String id) {

		Cursor cursor = db.rawQuery(
				"select * from staff where film_id = " + id, null);

		Staff staff = new Staff();

		while (cursor.moveToNext()) {

			staff.setDirector(cursor.getString(cursor
					.getColumnIndex("director")));
			staff.setAssistant_director(cursor.getString(cursor
					.getColumnIndex("assistant_director")));
			staff.setCamera(cursor.getString(cursor.getColumnIndex("camera")));
			staff.setLighting(cursor.getString(cursor
					.getColumnIndex("lighting")));
			staff.setProduction(cursor.getString(cursor
					.getColumnIndex("production")));
		}
		cursor.close();

		return staff;

	}

}
