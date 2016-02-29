package hhm.slate.util;

import hhm.slate.db.ConfigImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.BreakIterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;

public class FileUtil {

	public void OpenExcel(String filename, Activity activity) {
		Intent intent = new Intent("android.intent.action.VIEW");

		intent.addCategory("android.intent.category.DEFAULT");

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		File file = new File(new PathUtil().getExcelDefaultPath() + filename);

		Uri uri = Uri.fromFile(file);

		intent.setDataAndType(uri, "application/vnd.ms-excel");
		activity.startActivity(intent);

	}

	public void copyFile(Context myContext, String resName, String outFile)
			throws IOException {

		InputStream myInput = myContext.getAssets().open(resName);

		String outFileName = outFile;

		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];

		int length;

		while ((length = myInput.read(buffer)) > 0) {

			myOutput.write(buffer, 0, length);

		}

		myOutput.flush();

		myOutput.close();

		myInput.close();

	}

	public static String readToString(File file) {
		long filelength = file.length();
		byte[] filecontent = new byte[new Long(filelength).intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(filecontent);
	}

	public boolean copyFile(String oldPath, String newPath) {

		if (new File(newPath).exists()) {

			return false;
		} else {
			try {
				int bytesum = 0;
				int byteread = 0;
				File oldfile = new File(oldPath);
				if (oldfile.exists()) { // 文件存在时
					InputStream inStream = new FileInputStream(oldPath); // 读入原文件
					FileOutputStream fs = new FileOutputStream(newPath);
					byte[] buffer = new byte[1444];
					int length;
					while ((byteread = inStream.read(buffer)) != -1) {
						bytesum += byteread; // 字节数 文件大小
						fs.write(buffer, 0, byteread);
					}
					inStream.close();
				}
			} catch (Exception e) {
				System.out.println("复制单个文件操作出错");

				e.printStackTrace();
				return false;

			}
		}

		return true;

	}

	/**
	 * 同步数据库，保证两个地方的数据库一样
	 */
	public boolean synchronizeDB(String oldPath, String oldname) {
		PathUtil pu = new PathUtil();

		String newPath1 = pu.getDB_PATH() + oldname;
		String newPath2 = pu.getUserDBPath() + oldname;

		boolean a = copyFile(oldPath, newPath1);
		boolean b = copyFile(oldPath, newPath2);

		System.out.println(a + "__" + b);
		if (a && b) {
			return true;
		} else {
			System.out.println("已经存在同名文件");
			return false;
		}

	}

	public void delete(String filename) {
		PathUtil pu = new PathUtil();
		String Path1 = pu.getDB_PATH() + filename;
		String Path2 = pu.getUserDBPath() + filename;
		File file1 = new File(Path1);
		File file2 = new File(Path2);

		file1.delete();
		file2.delete();

	}

}
