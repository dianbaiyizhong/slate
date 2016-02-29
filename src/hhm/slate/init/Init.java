package hhm.slate.init;

import hhm.slate.common.Constants;
import hhm.slate.db.DatabaseHelper;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

public class Init {

	// 初始化数据库
	public void InitDB(Context content) {
		DatabaseHelper dbHelper = new DatabaseHelper(content);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 初始化，新建存放照片的文件夹
	public void InitImageFile() {
		String StagePhotoPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + Constants.StagePhotoPath;

		File file = new File(StagePhotoPath);

		if (!file.exists()) {
			file.mkdirs();
		} else {
			System.out.println(StagePhotoPath + "目录已经存在");
		}
	}

	public void InitExcelFile() {
		String Path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + Constants.ExcelDefaultPath;

		File file = new File(Path);

		if (!file.exists()) {
			file.mkdirs();
		} else {
			System.out.println(Path + "目录已经存在");
		}
	}

	public void InitUserDBFile(Context content) {
		String Path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + Constants.DBFilePath;

		File file = new File(Path);

		if (!file.exists()) {
			file.mkdirs();
		} else {
			System.out.println(Path + "目录已经存在");
		}

	}

}
