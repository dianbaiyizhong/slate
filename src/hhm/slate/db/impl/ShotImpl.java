package hhm.slate.db.impl;

import hhm.slate.db.DatabaseHelper;
import hhm.slate.db.IDBControl;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Take;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ShotImpl implements IDBControl {

	private SQLiteDatabase db;

	public ShotImpl(Context context) {
		DatabaseHelper database = new DatabaseHelper(context);
		this.db = database.getReadableDatabase();
	}

	public Shot queryById(int id) {

		String sql = "select    *  from shot where   " + "shot_id =  " + id;
		Cursor cursor = db.rawQuery(sql, null);

		Shot shot = new Shot();
		while (cursor.moveToNext()) {

			shot.setShot_id(cursor.getInt(cursor.getColumnIndex("shot_id")));
			shot.setShot_name(cursor.getString(cursor
					.getColumnIndex("shot_name")));
			shot.setShot_number(cursor.getInt(cursor
					.getColumnIndex("shot_number")));
			shot.setShots(cursor.getString(cursor.getColumnIndex("shots")));
			shot.setShot_keyword(cursor.getString(cursor
					.getColumnIndex("shot_keyword")));
		}
		cursor.close();

		return shot;

	}

	/**
	 * 通过shot_id查询出最后一条 次 记录
	 * 
	 * @return
	 */
	public Shot queryRecentTakeById(int id) {

		String sql = "select    *  from scene,shot,take where scene.[scene_id] = shot.[scene_id] and shot.[shot_id] = take.[shot_id] and  "
				+ "shot.[shot_id] =  "
				+ id
				+ " ORDER BY take.[take_number] DESC  limit 0,1";
		Cursor cursor = db.rawQuery(sql, null);

		Shot shot = new Shot();
		while (cursor.moveToNext()) {

			shot.setShot_id(cursor.getInt(cursor.getColumnIndex("shot_id")));
			shot.setShot_name(cursor.getString(cursor
					.getColumnIndex("shot_name")));

			Take take = new Take();
			take.setTake_number(cursor.getInt(cursor
					.getColumnIndex("take_number")) + 1);
			shot.setShot_number(cursor.getInt(cursor
					.getColumnIndex("shot_number")));
			shot.setShots(cursor.getString(cursor.getColumnIndex("shots")));
			shot.setTake(take);

			Scene scene = new Scene();
			scene.setScene_number(cursor.getInt(cursor
					.getColumnIndex("scene_number")));
			scene.setScene_name(cursor.getString(cursor
					.getColumnIndex("scene_name")));
			shot.setScene(scene);
		}
		cursor.close();

		return shot;

	}
	
	

	public int queryRecentShotById(String id) {

		String sql = "select max(shot_number) as number from shot where scene_id =   "
				+ id;
		Cursor cursor = db.rawQuery(sql, null);

		int value = 1;
		while (cursor.moveToNext()) {

			value = cursor.getInt(cursor.getColumnIndex("number")) + 1;

		}
		cursor.close();

		return value;

	}

	public Shot querySceneById(int id) {

		String sql = "select * from shot,scene where shot.[scene_id] = scene.[scene_id] and shot.[shot_id]  ="
				+ id;
		Cursor cursor = db.rawQuery(sql, null);

		Shot shot = new Shot();
		while (cursor.moveToNext()) {

			shot.setShot_id(cursor.getInt(cursor.getColumnIndex("shot_id")));
			shot.setShot_name(cursor.getString(cursor
					.getColumnIndex("shot_name")));

			Take take = new Take();

			shot.setShot_number(cursor.getInt(cursor
					.getColumnIndex("shot_number")));
			shot.setShots(cursor.getString(cursor.getColumnIndex("shots")));
			shot.setTake(take);

			Scene scene = new Scene();
			scene.setScene_number(cursor.getInt(cursor
					.getColumnIndex("scene_number")));
			scene.setScene_name(cursor.getString(cursor
					.getColumnIndex("scene_name")));
			shot.setScene(scene);
		}
		cursor.close();

		return shot;

	}

	public void save(Object obj) {
		Shot shot = new Shot();
		shot = (Shot) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("scene_id", shot.getScene_id());
		cValue.put("shot_name", shot.getShot_name());
		cValue.put("shot_number", shot.getShot_number());
		cValue.put("shots", shot.getShots());
		cValue.put("shot_keyword", shot.getShot_keyword());
		// 调用insert()方法插入数据
		db.insert("shot", null, cValue);
	}

	public void save_Input(Object obj) {
		List<Shot> list = new ArrayList<Shot>();
		list = (List<Shot>) obj;

		// 获取scene最新插入的id
		Cursor cursor = db.rawQuery("select max(scene_id) from  scene", null);
		int new_id = 0;
		if (cursor.moveToFirst()) {
			new_id = cursor.getInt(0);
		}

		for (int i = 0; i < list.size(); i++) {
			// 实例化常量值
			ContentValues cValue = new ContentValues();
			cValue.put("scene_id", new_id);
			cValue.put("shot_name", list.get(i).getShot_name());
			cValue.put("shot_number", list.get(i).getShot_number());
			cValue.put("shots", list.get(i).getShots());

			// 调用insert()方法插入数据
			db.insert("shot", null, cValue);
		}
		cursor.close();

	}

	public void update(Object obj) {

		Shot shot = new Shot();
		shot = (Shot) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("shot_name", shot.getShot_name());
		cValue.put("shot_number", shot.getShot_number());
		cValue.put("shots", shot.getShots());

		cValue.put("shot_keyword", shot.getShot_keyword());

		// 实例化内容值 ContentValues values = new ContentValues();
		// 在values中添加内容
		// 修改条件
		String whereClause = "shot_id=?";
		// 修改添加参数
		String[] whereArgs = { String.valueOf(shot.getShot_id()) };
		// 修改
		db.update("shot", cValue, whereClause, whereArgs);

	}

	public List query() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 通过scene_id获取所有的分镜头
	 * 
	 * @param context
	 * @param id
	 * @return
	 */

	public List query(String id) {

		List<Shot> list = new ArrayList<Shot>();

		Cursor cursor = db.rawQuery(
				"select * from shot where scene_id = " + id, null);

		while (cursor.moveToNext()) {
			Shot shot = new Shot();
			shot.setShot_id(cursor.getInt(cursor.getColumnIndex("shot_id")));
			shot.setShot_name(cursor.getString(cursor
					.getColumnIndex("shot_name")));
			shot.setShot_number(cursor.getInt(cursor
					.getColumnIndex("shot_number")));
			shot.setShots(cursor.getString(cursor.getColumnIndex("shots")));
			shot.setShot_keyword(cursor.getString(cursor
					.getColumnIndex("shot_keyword")));
			list.add(shot);
		}
		cursor.close();

		return list;

	}

	public void delete(int id) {
		db.execSQL("PRAGMA foreign_keys=ON");

		// 删除条件
		String whereClause = "shot_id=?";
		// 删除条件参数
		String[] whereArgs = { String.valueOf(id) };
		// 执行删除
		db.delete("shot", whereClause, whereArgs);
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

	public boolean isExist(int id) {

		String sql = "select count(*) as count  from shot   where  shot.[shot_id] ="
				+ id;
		Cursor cursor = db.rawQuery(sql, null);

		int count = 0;
		while (cursor.moveToNext()) {

			count = cursor.getInt(cursor.getColumnIndex("count"));
		}
		cursor.close();

		if (count == 0) {
			return false;
		} else {
			return true;
		}

	}

}
