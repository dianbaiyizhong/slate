package hhm.slate.db;

import hhm.slate.util.FileUtil;
import hhm.slate.util.PathUtil;

import java.io.IOException;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = new PathUtil().getDB_PATH();

	private static String DB_NAME = ConfigImpl.getNowDBName();

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	public DatabaseHelper(Context context) {

		super(context, ConfigImpl.getNowDBName(), null, 1);

		this.myContext = context;

	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {

			System.out.println("数据库已经存在");

		} else {

			this.getReadableDatabase();

			try {

				FileUtil fu = new FileUtil();
				fu.copyFile(myContext, DB_NAME, DB_PATH + "/" + DB_NAME);

				// 同时拷贝一份到用户目录
				String oldPath = new PathUtil().getDB_PATH() + DB_NAME;
				String newPath = new PathUtil().getUserDBPath() + DB_NAME;
				fu.copyFile(oldPath, newPath);

			} catch (IOException e) {
				System.out.println("Error copying database " + e.getMessage());

			} finally {
				this.close();

			}

		}

	}

	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {

			String myPath = DB_PATH + DB_NAME;

			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			System.out.println("does't exist yet " + e.getMessage());

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;

	}

	public void openDataBase() throws SQLException {

		// Open the database

		String myPath = DB_PATH + DB_NAME;

		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)

			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
