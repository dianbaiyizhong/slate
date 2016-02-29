package hhm.slate.util;

import hhm.slate.common.Constants;
import android.os.Environment;

public class PathUtil {

	public String getStagePhotoPath() {

		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ Constants.StagePhotoPath;

	}

	public String getExcelDefaultPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ Constants.ExcelDefaultPath;
	}

	public String getDB_PATH() {
		return "/data/data/hhm.slate/databases/";
	}

	public String getUserDBPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ Constants.DBFilePath;
	}

}
