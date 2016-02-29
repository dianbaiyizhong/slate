package hhm.slate.util;

public class StringUtil {

	public String getName(String value) {

		try {
			String result = value.substring(0, value.indexOf("<"));
			return result;
		} catch (Exception e) {
			return value;

		}

	}

	public String getPhoneNum(String value) {
		try {
			String result = value.substring(value.indexOf("<") + 1,
					value.indexOf(">"));
			return result;
		} catch (Exception e) {
			return value;

		}
	}

}
