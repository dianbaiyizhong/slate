package hhm.slate.util;

import java.util.List;

import android.content.Context;
import android.widget.EditText;

public class VerifyUtil {

	public boolean verifyIsNull(Context content, EditText et) {

		if (et.getText() == null) {
			et.setError("请输入内容");
			return false;
		} else if (et.getText().toString().trim().equals("")) {
			et.setError("请输入内容");
			return false;
		}

		return true;
	}

	public boolean verifyIsNull(Context content, List<EditText> list) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getText() == null) {
				list.get(i).setError("请输入内容");
				return false;
			} else if (list.get(i).getText().toString().trim().equals("")) {
				list.get(i).setError("请输入内容");
				return false;
			}

		}

		return true;

	}

	public static boolean isValidFileName(String fileName) {
		if (fileName == null || fileName.length() > 255)
			return false;
		else
			return fileName
					.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
	}
}
