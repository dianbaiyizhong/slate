package hhm.slate.db.impl;

import hhm.slate.db.DatabaseHelper;
import hhm.slate.db.IDBControl;
import hhm.slate.db.entity.Film;
import hhm.slate.db.entity.Scene;
import hhm.slate.db.entity.Shot;
import hhm.slate.db.entity.Staff;
import hhm.slate.db.entity.Take;
import hhm.slate.util.TimeUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FilmImpl implements IDBControl {
	private SQLiteDatabase db;

	public FilmImpl(Context context) {
		DatabaseHelper database = new DatabaseHelper(context);
		this.db = database.getReadableDatabase();
	}

	public void updateNullAllDistance(Object obj) {
		Scene scene = new Scene();
		scene = (Scene) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();

		cValue.put("distance", "");

		// 实例化内容值 ContentValues values = new ContentValues();
		// 在values中添加内容
		// 修改条件
		String whereClause = "film_id=?";
		// 修改添加参数
		String[] whereArgs = { String.valueOf(scene.getFilm_id()) };
		// 修改
		db.update("scene", cValue, whereClause, whereArgs);
	}

	public void save(Object obj) {

		Film film = new Film();
		film = (Film) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("film_name", film.getFilm_name());

		cValue.put("film_createtime", new TimeUtil().getTime());
		// 调用insert()方法插入数据
		db.insert("film", null, cValue);

		// 插入之后，在另外的表插入一条数据
		Cursor cursor = db.rawQuery("select last_insert_rowid() from film",
				null);
		int new_id = 0;
		if (cursor.moveToFirst()) {
			new_id = cursor.getInt(0);
		}

		ContentValues cValue1 = new ContentValues();
		cValue1.put("film_id", String.valueOf(new_id));

		cValue1.put("additional_camera", film.getStaff().getAdditional_camera());
		cValue1.put("director", film.getStaff().getDirector());
		cValue1.put("assistant_director", film.getStaff()
				.getAssistant_director());
		cValue1.put("lighting", film.getStaff().getLighting());
		cValue1.put("production", film.getStaff().getProduction());

		db.insert("staff", null, cValue1);

		cursor.close();

	}

	public void update(Object obj) {

		Film film = new Film();
		film = (Film) obj;

		// 实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("film_name", film.getFilm_name());

		// 实例化内容值 ContentValues values = new ContentValues();
		// 在values中添加内容
		// 修改条件
		String whereClause = "film_id=?";
		// 修改添加参数
		String[] whereArgs = { String.valueOf(film.getFilm_id()) };
		// 修改
		db.update("film", cValue, whereClause, whereArgs);

		ContentValues cValue1 = new ContentValues();
		cValue1.put("director", film.getStaff().getDirector());
		cValue1.put("assistant_director", film.getStaff()
				.getAssistant_director());
		cValue1.put("camera", film.getStaff().getCamera());
		cValue1.put("lighting", film.getStaff().getLighting());
		cValue1.put("production", film.getStaff().getProduction());

		String whereClause1 = "film_id=?";
		// 修改添加参数
		String[] whereArgs1 = { String.valueOf(film.getFilm_id()) };
		// 修改
		db.update("staff", cValue1, whereClause1, whereArgs1);
	}

	public List query() {

		List<Film> list = new ArrayList<Film>();

		Cursor cursor = db
				.rawQuery(
						"select * from film,staff where film.[film_id] = staff.[staff_id]",
						null);

		while (cursor.moveToNext()) {
			Film film = new Film();

			film.setFilm_createtime(Timestamp.valueOf(cursor.getString(cursor
					.getColumnIndex("film_createtime"))));
			film.setFilm_id(cursor.getInt(cursor.getColumnIndex("film_id")));
			film.setFilm_name(cursor.getString(cursor
					.getColumnIndex("film_name")));

			if (cursor.getString(cursor.getColumnIndex("film_wraptime")) == null) {
			} else {
				film.setFilm_wraptime(Timestamp.valueOf(cursor.getString(cursor
						.getColumnIndex("film_wraptime"))));
			}

			Staff staff = new Staff();
			staff.setDirector(cursor.getString(cursor
					.getColumnIndex("director")));
			film.setStaff(staff);
			list.add(film);
		}

		cursor.close();

		return list;

	}

	public int queryRecentSceneById(String id) {

		String sql = "select max(scene_number) as number from scene where film_id =   "
				+ id;
		Cursor cursor = db.rawQuery(sql, null);

		int value = 1;
		while (cursor.moveToNext()) {

			value = cursor.getInt(cursor.getColumnIndex("number")) + 1;

		}
		cursor.close();

		return value;

	}

	public Film queryArr() {

		List<Film> list = new ArrayList<Film>();

		Cursor cursor = db
				.rawQuery(
						"select * from film,staff where film.[film_id] = staff.[staff_id]",
						null);

		while (cursor.moveToNext()) {
			Film film = new Film();

			film.setFilm_createtime(Timestamp.valueOf(cursor.getString(cursor
					.getColumnIndex("film_createtime"))));
			film.setFilm_id(cursor.getInt(cursor.getColumnIndex("film_id")));
			film.setFilm_name(cursor.getString(cursor
					.getColumnIndex("film_name")));

			if (cursor.getString(cursor.getColumnIndex("film_wraptime")) == null) {
			} else {
				film.setFilm_wraptime(Timestamp.valueOf(cursor.getString(cursor
						.getColumnIndex("film_wraptime"))));
			}

			Staff staff = new Staff();
			staff.setDirector(cursor.getString(cursor
					.getColumnIndex("director")));
			film.setStaff(staff);
			list.add(film);
		}

		String arr_name[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr_name[i] = list.get(i).getFilm_name();
		}

		int arr_id[] = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr_id[i] = list.get(i).getFilm_id();
		}

		Film film = new Film();
		film.setFilmname(arr_name);
		film.setFilmid(arr_id);
		cursor.close();

		return film;

	}

	public List query(String id) {
		return null;
	}

	public Film queryByid(String id) {

		Cursor cursor = db
				.rawQuery(
						"select * from film,staff where film.[film_id] = staff.[film_id] and film.[film_id] ="
								+ id, null);

		Film film = new Film();

		while (cursor.moveToNext()) {

			film.setFilm_name(cursor.getString(cursor
					.getColumnIndex("film_name")));

			Staff staff = new Staff();
			staff.setDirector(cursor.getString(cursor
					.getColumnIndex("director")));
			staff.setAssistant_director(cursor.getString(cursor
					.getColumnIndex("assistant_director")));
			staff.setCamera(cursor.getString(cursor.getColumnIndex("camera")));
			staff.setLighting(cursor.getString(cursor
					.getColumnIndex("lighting")));
			staff.setProduction(cursor.getString(cursor
					.getColumnIndex("production")));
			film.setStaff(staff);
		}
		cursor.close();

		return film;

	}

	public Film queryByTask_id(String id) {

		Cursor cursor = db
				.rawQuery(
						"select * from film,scene,shot,take where  film.[film_id] = scene.[film_id] and scene.[scene_id] = shot.[scene_id] and shot.[shot_id] = take.[shot_id]  and take.[take_id] = "
								+ id, null);

		Film film = new Film();

		while (cursor.moveToNext()) {

			film.setFilm_name(cursor.getString(cursor
					.getColumnIndex("film_name")));

			Scene scene = new Scene();
			scene.setScene_name(cursor.getString(cursor
					.getColumnIndex("scene_name")));
			scene.setScene_number(cursor.getInt(cursor
					.getColumnIndex("scene_number")));

			film.setScene(scene);

			Shot shot = new Shot();
			shot.setShot_name(cursor.getString(cursor
					.getColumnIndex("shot_name")));
			shot.setShot_number(cursor.getInt(cursor
					.getColumnIndex("shot_number")));
			shot.setShots(cursor.getString(cursor.getColumnIndex("shots")));
			film.setShot(shot);

			Take take = new Take();
			take.setIs_available(cursor.getInt(cursor
					.getColumnIndex("is_available")));
			take.setNot_avaliable_season(cursor.getString(cursor
					.getColumnIndex("not_avaliable_season")));
			take.setTake_number(cursor.getInt(cursor
					.getColumnIndex("take_number")));
			take.setTake_time(Timestamp.valueOf(cursor.getString(cursor
					.getColumnIndex("take_time"))));

			take.setAudio_number(cursor.getString(cursor
					.getColumnIndex("audio_number")));
			take.setVideo_number(cursor.getString(cursor
					.getColumnIndex("video_number")));
			take.setRoll_name(cursor.getString(cursor
					.getColumnIndex("roll_name")));
			film.setTake(take);
		}
		cursor.close();

		return film;

	}

	public void delete(int id) {
		db.execSQL("PRAGMA foreign_keys=ON");
		// 删除条件
		String whereClause = "film_id=?";
		// 删除条件参数
		String[] whereArgs = { String.valueOf(id) };
		// 执行删除
		db.delete("film", whereClause, whereArgs);
		db.close();
	}

}
