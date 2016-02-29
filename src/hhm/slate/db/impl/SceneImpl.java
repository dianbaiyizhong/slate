package hhm.slate.db.impl;

import hhm.slate.db.DatabaseHelper;
import hhm.slate.db.IDBControl;
import hhm.slate.db.entity.Scene;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SceneImpl implements IDBControl {

	private SQLiteDatabase db;

	public SceneImpl(Context context) {
		DatabaseHelper database = new DatabaseHelper(context);
		this.db = database.getReadableDatabase();
	}

	public void save(Object obj) {

		Scene scene = new Scene();
		scene = (Scene) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("scene_name", scene.getScene_name());
		cValue.put("scene_number", scene.getScene_number());
		cValue.put("film_id", scene.getFilm_id());
		cValue.put("scene_pos", scene.getScene_pos());

		// 调用insert()方法插入数据
		db.insert("scene", null, cValue);

		// 插入之后，在另外的表插入一条数据

	}

	public void update(Object obj) {
		Scene scene = new Scene();
		scene = (Scene) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("scene_name", scene.getScene_name());
		cValue.put("scene_number", scene.getScene_number());
		cValue.put("scene_pos", scene.getScene_pos());

		// 实例化内容值 ContentValues values = new ContentValues();
		// 在values中添加内容
		// 修改条件
		String whereClause = "scene_id=?";
		// 修改添加参数
		String[] whereArgs = { String.valueOf(scene.getScene_id()) };
		// 修改
		db.update("scene", cValue, whereClause, whereArgs);
		db.close();
	}

	public List query() {

		return null;

	}

	public Scene queryById(int id) {

		Cursor cursor = db.rawQuery("select * from scene where scene_id = "
				+ id, null);
		Scene scene = new Scene();
		while (cursor.moveToNext()) {
			scene.setFilm_id(cursor.getInt(cursor.getColumnIndex("film_id")));
			scene.setScene_id(cursor.getInt(cursor.getColumnIndex("scene_id")));
			scene.setScene_name(cursor.getString(cursor
					.getColumnIndex("scene_name")));
			scene.setScene_number(cursor.getInt(cursor
					.getColumnIndex("scene_number")));
			scene.setScene_pos(cursor.getString(cursor
					.getColumnIndex("scene_pos")));
		}
		cursor.close();

		return scene;

	}

	public boolean isExist(String id) {

		Cursor cursor = db
				.rawQuery(
						"select count(*) as count from film,scene where film.[film_id] = scene.[film_id] and film.[film_id] ="
								+ id, null);

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

	public String[] queryPos(int id) {

		Cursor cursor = db.rawQuery(
				"select scene_pos from scene where scene_id = " + id, null);

		while (cursor.moveToNext()) {
			String result_pos = cursor.getString(cursor
					.getColumnIndex("scene_pos"));

			if (result_pos == null) {
				cursor.close();

				return null;

			} else {
				String[] pos = result_pos.split("#");
				cursor.close();

				return pos;

			}

		}
		cursor.close();

		return null;

	}

	public void updateAllDistance(Object obj) {
		Scene scene = new Scene();
		scene = (Scene) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();

		cValue.put("distance", scene.getDistance());

		// 实例化内容值 ContentValues values = new ContentValues();
		// 在values中添加内容
		// 修改条件
		String whereClause = "scene_id=?";
		// 修改添加参数
		String[] whereArgs = { String.valueOf(scene.getScene_id()) };
		// 修改
		db.update("scene", cValue, whereClause, whereArgs);
	}

	public List queryPos(String id) {

		List<Scene> list = new ArrayList<Scene>();

		Cursor cursor = db
				.rawQuery(
						"select scene_id,scene_pos from film,scene where film.[film_id] = scene.[film_id] and film.[film_id] ="
								+ id, null);

		while (cursor.moveToNext()) {

			Scene scene = new Scene();

			scene.setScene_id(cursor.getInt(cursor.getColumnIndex("scene_id")));

			scene.setScene_pos(cursor.getString(cursor
					.getColumnIndex("scene_pos")));
			list.add(scene);
		}
		cursor.close();

		return list;

	}

	public List query(String id) {

		List<Scene> list = new ArrayList<Scene>();

		Cursor cursor = db
				.rawQuery(
						"select * from film,scene where film.[film_id] = scene.[film_id] and film.[film_id] ="
								+ id, null);

		while (cursor.moveToNext()) {

			Scene scene = new Scene();

			scene.setFilm_id(cursor.getInt(cursor.getColumnIndex("film_id")));
			scene.setScene_id(cursor.getInt(cursor.getColumnIndex("scene_id")));
			scene.setScene_name(cursor.getString(cursor
					.getColumnIndex("scene_name")));
			scene.setScene_number(cursor.getInt(cursor
					.getColumnIndex("scene_number")));
			scene.setScene_pos(cursor.getString(cursor
					.getColumnIndex("scene_pos")));
			scene.setDistance(cursor.getString(cursor
					.getColumnIndex("distance")));
			list.add(scene);
		}
		cursor.close();

		return list;

	}

	public void delete(int id) {
		db.execSQL("PRAGMA foreign_keys=ON");

		// 删除条件
		String whereClause = "scene_id=?";
		// 删除条件参数
		String[] whereArgs = { String.valueOf(id) };
		// 执行删除
		db.delete("scene", whereClause, whereArgs);

	}

	public int getUncompCount(int id) {

		Cursor cursor = db
				.rawQuery(
						"select count( distinct(shot.[shot_id])) as count  from shot,take where shot.[shot_id] = take.[shot_id] and take.[is_available] = 1 and shot.[scene_id] ="
								+ id, null);

		int count = 0;
		while (cursor.moveToNext()) {

			count = cursor.getInt(cursor.getColumnIndex("count"));
		}
		cursor.close();

		return count;

	}

	/**
	 * 通过scene_id获取所有的次的数量
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public int getCountById(int id) {

		String sql = "select count(*) as count from shot where scene_id =" + id;
		Cursor cursor = db.rawQuery(sql, null);

		int count = 0;
		while (cursor.moveToNext()) {

			count = cursor.getInt(cursor.getColumnIndex("count"));
		}
		cursor.close();

		return count;

	}
}
