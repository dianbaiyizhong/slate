package hhm.slate.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public String getLocalTime() {

		return String.valueOf(System.currentTimeMillis());

	}

	public String getTime() {
		return sdf.format(new Date().getTime());
	}

	public String getTime(Timestamp time) {
		return sdf1.format(time);
	}

}
