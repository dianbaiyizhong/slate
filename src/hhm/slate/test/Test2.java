package hhm.slate.test;

import hhm.slate.util.PathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.test.AndroidTestCase;

public class Test2 extends AndroidTestCase {

	public void testSave() {

		String path = new PathUtil().getDB_PATH();

		String oldpath = path + "slate.db";
		String newpath = path + "slate1.db";
		// copyFile(oldpath, newpath);
		File file = new File(path);
		// file.delete();
		String[] arr = file.list();

		for (int i = 0; i < arr.length; i++) {
			System.out.println(")))))))))" + arr[i]);
		}
	}

	public void copyFile(String oldPath, String newPath) {
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
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

}
